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
---

## Screenshots

### Registration and Login Page

<div style="display: flex"> 
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/102c3c13-182a-433b-8e37-7f432da69cea" />
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/98bf892c-680e-46c5-84bd-5e460653c859" />
</div>

### Dashboard 

<div style="display: flex">
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/45c94841-6925-4697-911a-53ef0394a76d" />
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/5d4145d7-e5a4-475b-9a26-4ce57658411c" />
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/d9f9058f-5695-49b1-8118-3ddd2ba21f6b" />
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/b2898110-82d2-434f-8101-92da65a4e383" />
</div>

### Profile View

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/e8b5697f-45c1-4eef-9c53-82339cb55e1d" />

### Task View 

<div style="display: flex">
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/57e1e273-0dd1-4e5d-a623-5cbf9c87a923" />
  <img width="500" height="300" alt="image" src="https://github.com/user-attachments/assets/dcc03639-bee8-4612-9ba4-2ea02747fe5e" />
</div>
---

## Future Improvements
- OAuth2 Login (Google/Github)
- Responsive Mobile UI
- Visual Tracking (Charts)
- Notification/ Remainders
---

## Contributing
Contributions are Welcome
- Fork the repo
- Create a feature branch
- Submit pull request 
