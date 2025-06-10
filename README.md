# 🧠 TrackMyThink – Online Examination System

TrackMyThink is a full-stack Online Examination System built using **Spring Boot**, **Spring MVC**, **Spring Data JPA**, **MySQL**, and **Thymeleaf**. It enables administrators to create, publish, and manage exams, while students can register, attempt exams, and view their results—all through a user-friendly web interface.

---

## 📌 Features

### 👩‍💼 Admin Panel
- Admin login and secure authentication
- Create, update, and delete exams
- Add questions with multiple choices
- Publish/unpublish exams
- View student exam attempts and scores
- Role-based access control

### 👨‍🎓 Student Panel
- Student registration and login
- View upcoming and past exams
- Attempt exams (one attempt per exam)
- Countdown timer for exam duration
- Instant score display after submission
- Profile view and update

---

## 🧱 Tech Stack

| Layer             | Technology                         |
|------------------|-------------------------------------|
| Backend           | Java 21, Spring Boot 3, Spring MVC |
| Database          | MySQL                              |
| ORM               | Spring Data JPA, Hibernate          |
| Frontend (View)   | Thymeleaf, HTML5, Bootstrap         |
| Build Tool        | Maven                              |
| Server            | Apache Tomcat (Embedded)           |

---

## 🗃️ Database Schema

Includes the following MySQL tables:
- `admins`
- `exams`
- `questions`
- `student`
- `student_exam_attempts`

➡️ ER Diagram and DBML representation available 
![Image](https://github.com/user-attachments/assets/81c501de-0e34-4bfc-8e2f-ec14c47b4716)
![TrackMyThink UI Screenshot](https://github.com/user-attachments/assets/81c501de-0e34-4bfc-8e2f-ec14c47b4716)


---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/trackmythink-online-exam-system.git
cd trackmythink-online-exam-system
