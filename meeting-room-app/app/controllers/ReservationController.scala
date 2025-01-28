package controllers

import models.{Reservation, Room}
import play.api.libs.json._
import play.api.mvc._
import services.{ReservationService, RoomService, UserService}
import utils.KafkaProducerUtil

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReservationController @Inject()(
                                       cc: ControllerComponents,
                                       reservationService: ReservationService,
                                       roomService: RoomService,
                                       userService: UserService
                                     )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  implicit val reservationFormat: OFormat[Reservation] = Json.format[Reservation]
  implicit val roomFormat: OFormat[Room] = Json.format[Room]

  def reserveRoom: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Reservation].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("error" -> "Invalid reservation data", "details" -> JsError.toJson(errors))))
      },
      reservation => {
        userService.getUserById(reservation.createdBy).flatMap {
          case Some(user) if user.role == "AdminStaff" =>
            reservationService.reserveRoom(reservation).map {
              case Some(savedReservation) =>
                val reservationData = Json.toJson(savedReservation).toString()
                KafkaProducerUtil.sendMessage("reservation-updated", savedReservation.id.toString, reservationData)
                Created(Json.toJson(savedReservation))
              case None =>
                Conflict(Json.obj("error" -> "Room is unavailable for the selected time"))
            }
          case Some(_) =>
            Future.successful(Forbidden(Json.obj("error" -> "Only Admin Staff can make reservations")))
          case None =>
            Future.successful(NotFound(Json.obj("error" -> "User not found")))
        }
      }
    )
  }

  def checkAvailability(startTime: String, endTime: String): Action[AnyContent] = Action.async {
    roomService.findAvailableRooms(startTime, endTime).map { availableRooms =>
      Ok(Json.toJson(availableRooms))
    }
  }

  def checkRoomAvailability(roomId: Int, startTime: String, endTime: String): Action[AnyContent] = Action.async {
    roomService.getRoomById(roomId).flatMap {
      case Some(_) =>
        reservationService.checkRoomAvailability(roomId, startTime, endTime).map { isAvailable =>
          if (isAvailable) {
            Ok(Json.obj("available" -> true))
          } else {
            Conflict(Json.obj("available" -> false, "message" -> "Room is already reserved for this time range."))
          }
        }
      case None =>
        Future.successful(NotFound(Json.obj("error" -> "Room not found")))
    }
  }
}
