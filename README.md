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

| Layer             | Technology                             |
|------------------|-----------------------------------------|
| Backend           | Java 21, Spring Boot 3, Spring MVC     |
| ORM & Persistence | Spring Data JPA, Hibernate              |
| Database          | MySQL                                  |
| View Layer        | Thymeleaf, HTML5, Bootstrap            |
| Client-side       | JavaScript (form validation)           |
| Build Tool        | Maven                                  |
| Server            | Apache Tomcat (Embedded)               |
| Utilities         | Lombok, JavaMail API (Mail sending)    |

---

## 🗃️ Database Schema

Includes the following MySQL tables:
- `admins`
- `exams`
- `questions`
- `student`
- `student_exam_attempts`

➡️ ER Diagram and DBML representation:

![TrackMyThink DB Schema](https://github.com/user-attachments/assets/81c501de-0e34-4bfc-8e2f-ec14c47b4716)

---

## 📁 Project Folder Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ashi/
│   │           ├── controller/          # Spring MVC Controllers
│   │           ├── entity/              # JPA Entity classes
│   │           ├── repository/          # Spring Data JPA Repositories
│   │           ├── service/             # Business logic layer- Mail configuration
│   │           ├── bindings/                 # Data Transfer Objects (if used)
│   │           ├── dao/              # DB Operations 
│   │           └── TrackMyThinkApplication.java  # Main Spring Boot application
│   ├── resources/
│   │   ├── templates/                   # Thymeleaf HTML pages
│   │   ├── static/images                      #  images
│   │   ├── application.properties       # DB and mail configurations
│   │   └── .war file       # war file pull and directly run in docker container (optional)

```

## 🚀 Getting Started
Follow the steps below to set up and run the TrackMyThink – Online Examination System locally on your machine.

## ✅ Prerequisites
Make sure you have the following installed:

Java 21+

Maven 3.6+

MySQL 8.0+

Docker (optional for deployment)

IDE (IntelliJ, Eclipse, or VS Code)

📦 Clone the Repository

- git clone https://github.com/your-username/trackmythink-online-exam-system.git
- cd trackmythink-online-exam-system
## 🔁 Replace your-username with your actual GitHub username.

## 🗃️ Setup MySQL Database
Create a new database:
- CREATE DATABASE trackmythink;
- Configure DB connection in src/main/resources/application.properties:
```
spring.datasource.url=jdbc:mysql://localhost:3306/trackmythink
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true #Optional
```
## 📧 Email Configuration (Optional)
For sending emails (e.g., registration confirmation), add your SMTP settings:

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
## ⚙️ Build and Run the Application
Run with Maven:

- mvn clean install
- mvn spring-boot:run
Or run from your IDE via:

TrackMyThinkApplication.java
🌐 Access the Application
After startup, open your browser and go to:
http://localhost:8088

## 🐳 Docker Deployment (Optional)
### 📁 Step 1: Generate WAR File ( Or which is already in resources)

mvn clean package
### 🐳 Step 2: Dockerfile (Sample)
dockerfile

FROM tomcat:9-jdk17
COPY target/trackmythink.war /usr/local/tomcat/webapps/

### ▶️ Step 3: Build and Run Docker

docker build -t trackmythink .
docker run -p 8080:8080 trackmythink
Access: http://localhost:8080/trackmythink

## 👨‍💻 Author
Ashish Baipalli
GitHub Profile
LinkedIn Profile






