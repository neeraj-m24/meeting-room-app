# Automated Notification Component for Office Space Management System

This repository includes the **Automated Notification Component** of the **Office Space Management System**. It manages the creation and distribution of notifications related to meeting room readiness, upcoming meeting reminders, and other system alerts.

---

## Key Features

- **Room Preparation Alerts**: Sends notifications to the RoomService team to prepare rooms before scheduled meetings.
- **Meeting Reminder Notifications**: Sends timely reminders to users about their upcoming meetings.
- **Event-Driven System**: Uses **Kafka** for real-time, efficient message publishing and consumption.
- **Scalable Microservice**: Built for scalability and fault tolerance, utilizing Akka Actors for distributed processing.

---

## Repository Structure

- **Notification Service**: Contains the core logic to process and send notification events.
- **Kafka Consumer**: Listens to events related to meetings and triggers appropriate notifications.
- **Configuration Files**: Includes setup files for Kafka, database connections, and notification templates.

---

## Tools and Technologies Used

1. **Programming Language**: Scala
2. **Framework**: Akka
3. **Message Broker**: Kafka
4. **Database**: MySQL (for storing notification logs and settings)

---
