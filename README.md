# student-app-spring-boot

Application web de gestion des étudiants — Spring Boot + Thymeleaf (CRUD en mémoire, sans base de données). Projet réalisé dans le cadre d'un TD Spring Boot / Maven / Thymeleaf.

## Fonctionnalités

- Afficher la liste des étudiants (`GET /`, `/home`)
- Ajouter un étudiant (`POST /students/add`)
- Modifier un étudiant (`GET /students/edit/{id}`, `POST /students/update/{id}`)
- Supprimer un étudiant (`GET /students/delete/{id}`)
- Données initiales : 2 étudiants ; nouveaux IDs générés par `AtomicLong` à partir de `3L` (données perdues au redémarrage — pas de BD).

## Stack

- Java 17, Spring Boot 4.1.1 (Maven wrapper `mvnw.cmd`)
- `spring-boot-starter-thymeleaf`, `spring-boot-starter-webmvc`, `spring-boot-devtools`, Lombok
- Templates Thymeleaf (`index.html` + fragments `common/header`, `common/menu`, `common/footer`), Bootstrap 5 via CDN

## Structure

Le code utile est dans `project1/` (le dossier racine seul ne contient qu'un `@SpringBootApplication` vide — `GET /` y répond 404) :

```
project1/
  src/main/java/com/example/project1/
    Project1Application.java
    controller/HomeController.java   # CRUD en mémoire (ArrayList + AtomicLong)
    model/Student.java               # id, firstName, lastName (Lombok @Data)
  src/main/resources/
    templates/index.html             # seule vue active (firstName/lastName, students/student/editMode)
    templates/common/                # fragments header / menu / footer
    application.properties           # spring.application.name=project1 (port 8080 par défaut)
```

## Lancer l'application

```powershell
Set-Location ".\project1"
.\mvnw.cmd spring-boot:run
```

Ouvrir `http://localhost:8080/` (ou `/home`). Si le port 8080 est occupé, décommenter `#server.port=8082` dans `project1/src/main/resources/application.properties`.

## Tester

```powershell
Set-Location ".\project1"
.\mvnw.cmd test                                  # tous les tests
.\mvnw.cmd -Dtest=Project1ApplicationTests test  # un seul test (contextLoads)
```
