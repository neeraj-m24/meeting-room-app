package actors

import akka.actor.{Actor, Props}
import models.Reservation
import services.{EmailService, RoomService}

object ReleaseActor {
  def props(emailService: EmailService, roomService: RoomService): Props =
    Props(new ReleaseActor(emailService, roomService))
}

class ReleaseActor(emailService: EmailService, roomService: RoomService) extends Actor {
  override def receive: Receive = {
    case reservation: Reservation =>
      context.system.log.info(s"Checking occupancy for reservation ID: ${reservation.id}")
      if (!roomService.checkRoomOccupancy(reservation.roomId, reservation.startTime)) {
        context.system.log.info(s"Room ID ${reservation.roomId} is unoccupied, releasing it.")
        roomService.releaseRoom(reservation.roomId)
        emailService.sendReleaseNotification(reservation)
      } else {
        context.system.log.info(s"Room ID ${reservation.roomId} is occupied, no action needed.")
      }
  }
}
