package services

import models.Reservation
import repositories.{ReservationRepository, RoomRepository}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReservationService @Inject()(reservationRepository: ReservationRepository, roomRepository: RoomRepository)(implicit ec: ExecutionContext) {

  def isRoomAvailable(roomId: Int, startTime: String, endTime: String): Future[Boolean] = {
    reservationRepository.findAvailableRooms(startTime, endTime).map { availableRooms =>
      availableRooms.filter(room => room.id == roomId).nonEmpty
    }
  }

  def reserveRoom(reservation: Reservation): Future[Option[Reservation]] = {
    isRoomAvailable(reservation.roomId, reservation.startTime, reservation.endTime).flatMap { available =>
      if (available) {
        reservationRepository.createReservation(reservation).map { reservationId =>
          Some(reservation.copy(id = Some(reservationId)))
        }
      } else {
        Future.successful(None)
      }
    }
  }

  def checkRoomAvailability(roomId: Int, startTime: String, endTime: String): Future[Boolean] = {
    reservationRepository.checkRoomAvailability(roomId, startTime, endTime)
  }

}
