# Meeting Room Reservation Module for Office Meeting Room Management System

This repository contains the **Meeting Room Reservation Module** for the **Office Meeting Room Management System**. It provides functionality for reserving meeting rooms, checking availability, and managing meeting schedules efficiently.

---

## Features

- **Meeting Room Reservation**: Enables users to book meeting rooms by specifying date, time, and room details.
- **Availability Check**: Validates room availability during the desired time slot.
- **Conflict Management**: Prevents overlapping bookings for the same room.
- **Admin and User Roles**: Differentiates functionalities based on user roles.

---

## Repository Structure

- **Reservation Service**: Core logic for creating, updating, and canceling reservations.
- **Availability Endpoint**: API for checking room availability based on `room_id`, `startTime`, and `endTime`.
- **Database Schema**:
  - **Room Table**: Stores room details.
  - **Reservation Table**: Stores meeting reservation details, including room, time, and user.

---

## Tools and Technologies Used

1. **Programming Language**: Scala
2. **Frameworks**: Play Framework
3. **Database**: MySQL
4. **API Testing**: Postman or curl
5. **Containerization**: Docker (for deployment)

---

## How to Set Up and Run

1. **Clone the Repository**:
   ```bash
   git clone ""
   cd ""
