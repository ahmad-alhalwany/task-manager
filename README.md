# Task Manager API

Small Spring Boot 3.3 REST API with JWT auth, JPA, PostgreSQL, and an H2 profile for zero-setup local runs.

## Stack

| Piece | Role (Python mental model) |
|---|---|
| Spring Web | FastAPI routers + uvicorn |
| Spring Data JPA | SQLAlchemy ORM |
| Spring Security + JWT | Auth middleware / Depends |
| BCrypt | passlib bcrypt |
| PostgreSQL / H2 | Postgres / SQLite-in-memory |
| Maven | pip + build tool |

## Prerequisites

- JDK 17+
- Maven 3.9+ **or** use the included Maven Wrapper (`mvnw` / `mvnw.cmd`) — no global Maven install needed
- Optional: Docker + Docker Compose

### Windows PATH note

`setx` updates PATH for **new** terminals only. After adding Maven to PATH, **close the terminal and open a new one**.  
In the current session you can run:

```powershell
$env:Path = "$env:USERPROFILE\apache-maven-3.9.9\bin;" + $env:Path
```

Or skip PATH entirely and use the wrapper below.

## Quick start (H2 — no database install)

```bash
# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2

# Windows PowerShell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=h2"
```

Equivalent with a global Maven install:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

App listens on http://localhost:8080  
H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:taskmanager`, user `sa`, empty password)

## Run with PostgreSQL locally

1. Start Postgres (or use Docker Compose below).
2. Export env vars if needed, then:

```bash
mvn spring-boot:run
```

Defaults: `jdbc:postgresql://localhost:5432/taskmanager` / user `taskmanager` / password `taskmanager`.

## Docker Compose (app + Postgres)

```bash
docker compose up --build
```

## Build & test

```bash
# Windows
.\mvnw.cmd clean compile
.\mvnw.cmd test

# Linux / macOS
./mvnw clean compile
./mvnw test
```

## API overview

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | no | Create user, return JWT |
| POST | `/api/auth/login` | no | Login, return JWT |
| GET | `/api/tasks` | JWT | List my tasks |
| GET | `/api/tasks/{id}` | JWT | Get one task |
| POST | `/api/tasks` | JWT | Create task |
| PUT | `/api/tasks/{id}` | JWT | Update task |
| DELETE | `/api/tasks/{id}` | JWT | Delete task |

Task `status` values: `TODO`, `IN_PROGRESS`, `DONE`.

## Example curl requests

### Register

```bash
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"alice\",\"email\":\"alice@example.com\",\"password\":\"secret12\"}"
```

### Login

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"alice\",\"password\":\"secret12\"}"
```

Save the `token` from the response.

### Create a task

```bash
TOKEN="<paste-jwt-here>"

curl -s -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Learn Spring\",\"description\":\"Build a task API\",\"status\":\"TODO\"}"
```

### List tasks

```bash
curl -s http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN"
```

### Update a task

```bash
curl -s -X PUT http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Learn Spring\",\"description\":\"Done with CRUD\",\"status\":\"DONE\"}"
```

### Delete a task

```bash
curl -s -o /dev/null -w "%{http_code}" -X DELETE http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer $TOKEN"
```

## Project layout

```
src/main/java/com/taskmanager/
  TaskManagerApplication.java   # entry point
  controller/                   # HTTP routes
  service/                      # business logic
  repository/                   # JPA data access
  entity/                       # DB models
  dto/                          # request/response shapes
  security/                     # JWT filter + UserDetails
  config/                       # SecurityFilterChain
  exception/                    # global error mapping
src/main/resources/
  application.properties        # Postgres defaults
  application-h2.properties     # H2 profile overrides
```

## Configuration

| Property / env | Meaning |
|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | DB user |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | Base64-encoded HS256 secret (≥ 256 bits) |
| `JWT_EXPIRATION_MS` | Token lifetime (default 24h) |
| `SPRING_PROFILES_ACTIVE=h2` | Use in-memory H2 |
