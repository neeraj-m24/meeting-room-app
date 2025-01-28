package services

import akka.actor.ActorSystem
import models.Reservation


class EmailService(implicit system: ActorSystem) {
  def sendBookingConfirmation(reservation: Reservation): Unit = {
    system.log.info(s"Sending booking confirmation to ${reservation.employeeName} for reservation ID ${reservation.id}")
  }

  def sendRoomPreparationNotification(reservation: Reservation): Unit = {
    system.log.info(s"Sending room preparation notification for reservation ID ${reservation.id}")
  }

  def sendReminder(reservation: Reservation): Unit = {
    system.log.info(s"Sending reminder to ${reservation.employeeName} for reservation ID ${reservation.id}")
  }

  def sendReleaseNotification(reservation: Reservation): Unit = {
    system.log.info(s"Room ${reservation.roomId} was not used for reservation ID ${reservation.id}. Notification sent to admin staff.")
  }

}
