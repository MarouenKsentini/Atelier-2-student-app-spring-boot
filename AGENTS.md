# AGENTS.md — student-app-spring-boot

Spring Boot 4.1.1 + Java 17 (Maven wrapper). Single module, no DB, no CI/lint/format config.

## Commands (Windows: `mvnw.cmd`, Unix: `./mvnw`)

Run from repo root (the only `pom.xml`).

```bash
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.18   # required first: default env JDK is wrong
mvnw.cmd spring-boot:run          # dev server, default port 8080
mvnw.cmd test                     # all tests
mvnw.cmd -Dtest=Project1ApplicationTests test  # single test
```

Requires JDK 17 (`JAVA_HOME=C:\Program Files\Java\jdk-17.0.18`, must be set in each new shell). `pom.xml` sets `<java.version>17</java.version>` — do not bump without also changing the JDK.

## Routes (`HomeController`, no service layer — controller holds the list directly)

- `GET /,/home` — list + add form (`students` + empty `student` in model)
- `POST /students/add` — `@ModelAttribute` bind, id from `AtomicLong`, redirect `/`
- `GET /students/edit/{id}` — same `index` view in `editMode` (adds `student` + `editMode=true`); unknown id redirects `/`
- `POST /students/update/{id}` — copies `firstName`/`lastName` onto found entity, redirect `/`
- `GET /students/delete/{id}` — `removeIf`, redirect `/` (delete via GET by design here, not REST-pure)

Single-view pattern: `index.html` switches add/edit forms on `editMode`; `th:object`+`th:field` does form binding (needs Lombok no-arg ctor — `pom.xml` already wires the annotation processor, don't remove it).

## Layout

- Standard single-module: `src/main/java/com/example/project1/` (`Project1Application`, `controller/HomeController`, `model/Student` with Lombok `@Data`).
- `HomeController` (`GET /,/home`) does in-memory CRUD on an `ArrayList<Student>` seeded with 2 rows; IDs via `AtomicLong` starting at `3L`. No DB — state resets on restart.
- `index.html` is the only live view (`firstName`/`lastName` + `students`/`student`/`editMode` attributes). `message.html` and `templates/common/*` are empty, unreferenced — ignore them.
- Only test: `Project1ApplicationTests.contextLoads()` (context boot only).

## Gotchas

- **Port 8080 is often occupied** (Apache `httpd` on this machine). Either stop it (admin: `taskkill /PID <pid> /F`), uncomment `#server.port=8082` in `src/main/resources/application.properties`, or (no file change, verified) run with `$env:SERVER_PORT="8082"` before `mvnw.cmd spring-boot:run` and use `http://localhost:8082/`. A stale instance serving old code is the usual cause when edits seem ignored — check `netstat` listeners before restarting.
- **Verify with `test` only.** `.github/modernize/**` is upgrade-tooling telemetry, not CI. `HELP.md` is stock Initializr boilerplate.
- Smoke-test CRUD order: `GET /` (2 seeds) → `POST /students/add` ×2 (ids 3,4) → `GET /students/edit/3` (prefilled) → `POST /students/update/3` → `GET /students/delete/4`.
- Docs: `RAPPORT-TD-JEE.md`/`.docx` is the student report (body font 12); runtime screenshots belong in `screenshot/app-*.png` (code screenshots there use bare numbers — don't collide).
