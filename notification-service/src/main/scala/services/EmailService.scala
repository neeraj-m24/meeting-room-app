package services

import models.Reservation

import java.io.{BufferedWriter, FileWriter}

class EmailService {
  def sendBookingConfirmation(reservation: Reservation): Unit = {
    fileWriter(s"Sending booking confirmation to ${reservation.employeeName} for reservation ID ${reservation.id}")
  }

  def sendRoomPreparationNotification(reservation: Reservation): Unit = {
    fileWriter(s"Sending room preparation notification for reservation ID ${reservation.id}")
  }

  def sendReminder(reservation: Reservation): Unit = {
    fileWriter(s"Sending reminder to ${reservation.employeeName} for reservation ID ${reservation.id}")
  }

  def sendReleaseNotification(reservation: Reservation): Unit = {
    fileWriter(s"Room ${reservation.roomId} was not used for reservation ID ${reservation.id}. Notification sent to admin staff.")
  }
  
  private def fileWriter(msg: String): Unit = {
    val fileName = "src/main/scala/messages/meetingRoom/messages.txt"
    val bw = new BufferedWriter(new FileWriter(fileName, true))
    bw.write(msg)
    bw.newLine()
    bw.close()
  }
}
