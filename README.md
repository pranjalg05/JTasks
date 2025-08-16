# JTasks
>A simple yet powerful Task Manager application built with Spring Boot, Vaadin, and MongoDB.
It allows users to manage tasks, track completion status, and organize tasks into collections.
----
## Features 

- User Authentication with Vaadin + Spring Security (session-based login)
- Task Collections (with a default Daily Collection)
- Task Managment
  - Add / View / Delete Tasks
  - Mark Tasks as Complete/Incomplete
- Progress Tracker per Collection
- Modern UI built with Vaadin
- MongoDB integration for data persistance

---
## Tech Stack
- Backend
  - Java 21 (Temurin)
  - Spring Boot 3.5.4
- Frontend
  - Vaadin 24 (Java Based UI Framework)
- Database
  - MongoDB
- Security
  - Spring Security + Vaadin LoginView (session-based auth)
 
---
##  Setup & Installation

### Prerequisites
- Java 21+
- Maven
- MongoDB running locally (default port `27017`)

### Steps

1. Clone the repository:
```bash
git clone https://github.com/yourusername/jtasks.git
cd jtasks
```

2. Configure MongoDB in application.properties or applications.yml:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/jtasks
spring.application.name=JTasks
```

3. Build and run the app:
```bash
mvn spring-boot:run
```

4. Open in Browser:
```arduino
http://localhost:8080
```
   
