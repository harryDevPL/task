## 🚀 About the project

This project provides a **Spring Boot 3** application exposing a simple **REST API** for managing persons and tracking asynchronous tasks related to data classification.

It uses:
- **Spring Boot 3.4**
- **Java 21**
- **Spring WebFlux**
- **Spring Data R2DBC (Postgres)**
- **Testcontainers** (for integration tests)

---

## ⚙️ Requirements
Before you start, ensure you have installed:

- **Java 21** or higher
- **Docker** (for Testcontainers support)
- **Gradle** (or use the project's `gradlew` wrapper)

---

## 🏁 How to run
1. Clone the repository.
2. Build the project.
3. Run the application (example: `./gradlew bootRun`). The app will start on: `http://localhost:8080`.

## 🔥 API usage

The application exposes the following main endpoints:

###Create person
URL: `POST` `/api/v1/persons`

#### Request body:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1990-01-01",
  "company": "Acme Inc.",
  "idNumber": "12345678901"
}
```
#### Response:
```json
{
  "taskId": "aec56d95-f096-4882-8b5a-f72b90a26d23"
}
```
The taskId is used to track the classification task asynchronously.

### Get Task by ID
URL: `GET` `/api/v1/tasks/{taskId}`

#### Response Example:
```json
{
  "taskId": "aec56d95-f096-4882-8b5a-f72b90a26d23",
  "status": "COMPLETED",
  "percentageDone": "100",
  "result": {
    "firstNameClassification": "HIGH",
    "lastNameClassification": "HIGH",
    "birthDateClassification": "HIGH",
    "companyClassification": "HIGH"
  }
}
```
If the task is still processing, fields may be null or incomplete.

### 🎯 Quick start example (curl)
- Create person:
```bash
  curl -X POST http://localhost:8080/api/v1/persons \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1990-01-01",
    "company": "Acme Inc.",
    "idNumber": "12345678901"
  }'
```
- Then fetch task:
```bash
  curl http://localhost:8080/api/v1/tasks/{taskId}
```