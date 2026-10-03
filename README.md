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

```
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
.\mvnw.cmd spring-boot:run
```

Ouvrir `http://localhost:8080/` (ou `/home`). Si le port 8080 est occupé, décommenter `#server.port=8082` dans `src/main/resources/application.properties`.

## Tester

### 1. Tests automatiques (Maven)

```powershell
.\mvnw.cmd test                                  # tous les tests
.\mvnw.cmd -Dtest=Project1ApplicationTests test  # un seul test (contextLoads)
```

Le seul test existant (`src/test/.../Project1ApplicationTests.java`) vérifie que le contexte Spring démarre (`contextLoads`). Résultat attendu : `Tests run: 1, Failures: 0, Errors: 0` + `BUILD SUCCESS`.

### 2. Test manuel (CRUD dans le navigateur)

```powershell
.\mvnw.cmd spring-boot:run
```

1. Ouvrir `http://localhost:8080/` (ou `/home`) — le tableau affiche 2 lignes initiales : `1 / Marouen / Ksentini`, `2 / Ahmed / Ksentini`.
2. **Ajouter** : remplir prénom + nom → `Add` → `POST /students/add` → une nouvelle ligne apparaît (ID 3, 4, ...).
3. **Modifier** : cliquer `Modify` → le formulaire passe en mode `Modify student` (`GET /students/edit/{id}` avec `editMode=true`) → changer les champs → `Update` → `POST /students/update/{id}`.
4. **Supprimer** : cliquer `Delete` → `GET /students/delete/{id}` → la ligne disparaît.
5. Arrêter avec `Ctrl+C`. Les données sont en mémoire : tout est réinitialisé au redémarrage.
