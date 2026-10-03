# AGENTS.md — student-app-spring-boot

Spring Boot 4.1.1 + Java 17 (Maven wrapper). Single module, no DB, no CI/lint/format config.

## Commands (Windows: `mvnw.cmd`, Unix: `./mvnw`)

Run from repo root (the only `pom.xml`).

```bash
mvnw.cmd spring-boot:run          # dev server, default port 8080
mvnw.cmd test                     # all tests
mvnw.cmd -Dtest=Project1ApplicationTests test  # single test
```

Requires JDK 17 (`JAVA_HOME=C:\Program Files\Java\jdk-17.0.18`). `pom.xml` sets `<java.version>17</java.version>` — do not bump without also changing the JDK.

## Layout

- Standard single-module: `src/main/java/com/example/project1/` (`Project1Application`, `controller/HomeController`, `model/Student` with Lombok `@Data`).
- `HomeController` (`GET /,/home`) does in-memory CRUD on an `ArrayList<Student>` seeded with 2 rows; IDs via `AtomicLong` starting at `3L`. No DB — state resets on restart.
- `index.html` is the only live view (`firstName`/`lastName` + `students`/`student`/`editMode` attributes). `message.html` and `templates/common/*` are empty, unreferenced — ignore them.
- Only test: `Project1ApplicationTests.contextLoads()` (context boot only).

## Gotchas

- **Port 8080 is often occupied** (Apache `httpd` on this machine). Either stop it (admin: `taskkill /PID <pid> /F`) or uncomment `#server.port=8082` in `src/main/resources/application.properties`.
- **Verify with `test` only.** `.github/modernize/**` is upgrade-tooling telemetry, not CI. `HELP.md` is stock Initializr boilerplate.
