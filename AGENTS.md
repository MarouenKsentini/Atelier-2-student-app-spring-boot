# AGENTS.md — project1

Spring Boot 4.1.1 + Java 17 (Maven wrapper). No DB, no CI/lint/format config.

## Commands (Windows: `mvnw.cmd`, Unix: `./mvnw`)

Run from the directory containing the `pom.xml` you intend to build (see Gotchas).

```bash
mvnw.cmd spring-boot:run          # dev server, default port 8080
mvnw.cmd test                     # all tests
mvnw.cmd -Dtest=Project1ApplicationTests test  # single test
```

## Layout

- Working app lives only under `project1/`: `controller/HomeController.java`, `model/Student.java` (Lombok `@Data`), `templates/index.html`. Root `src` is a bare `@SpringBootApplication` with no controller — `GET /` 404s there.
- `HomeController` (`GET /,/home`) does in-memory CRUD on an `ArrayList<Student>` seeded with 2 rows; IDs via `AtomicLong` starting at `3L`. No DB — state resets on restart.
- `index.html` is the only live view (`firstName`/`lastName` + `students`/`student`/`editMode` attributes). `message.html` and `templates/common/*` are empty, unreferenced — ignore them.
- Only test: `Project1ApplicationTests.contextLoads()` (context boot only, in both roots).

## Gotchas

- **Duplicate project — confirm working directory first.** Both root and `project1/` have their own `pom.xml`/`mvnw`/`src`/`target`. Never assume which `pom.xml` Maven used; do not edit/sync both.
- **Root vs nested `pom.xml` differ.** Nested adds `lombok` (required by `Student`) and only `webmvc-test`; root has `webmvc-test + thymeleaf-test` but no `lombok`. Lombok-dependent code only builds under `project1/`.
- **Port conflict escape hatch is commented out.** Nested `application.properties` has `#server.port=8082`; uncomment it if 8080 is occupied. Root config has no port key.
- **Verify with `test` only.** `.github/modernize/**` is upgrade-tooling telemetry, not CI. `HELP.md` is stock Initializr boilerplate.
