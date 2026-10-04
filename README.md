# Atelier-2-student-app-spring-boot

Application web de gestion des étudiants — Spring Boot + Thymeleaf.
Atelier 2 : **Marouen Ksentini — MP DSIR-GLNT** (outil : opencode / Muse Spark).

## Fonctionnalités

- **Questionnaire** : ajouter un étudiant (N° inscription, nom, prénom, date de
  naissance, moyenne année précédente, niveau L1/L2/L3/M1/M2) — `GET /students/new`, `POST /students/add`
- **Accueil** : tableau de **tous** les étudiants — `GET /`, `/home`
- **Liste des étudiants** : tableau des **ajouts du formulaire uniquement** — `GET /students/list`
- **Modifier** : formulaire pré-rempli — `GET /students/edit/{id}`, `POST /students/update/{id}`
- **Supprimer** : avec **alerte de confirmation** navigateur — `GET /students/delete/{id}`
- Données en mémoire (`List` dans `EtudiantService`, IDs par `AtomicLong` dès `3L`) :
  2 étudiants de base (Marouen Ksentini L3, Ahmed Ksentini L2), perdues au redémarrage (pas de BD).

## Stack

- Java 17, Spring Boot 4.1.1 (Maven wrapper `mvnw.cmd`)
- `spring-boot-starter-thymeleaf`, `spring-boot-starter-webmvc`, devtools, Lombok
- Thymeleaf : `index.html` (accueil), `formulaire.html` (questionnaire),
  `liste.html` (ajouts), fragments `common/header`, `common/menu`, `common/footer`
- Bootstrap 5.3.3 **en local** (`static/css/`, `static/js/` — marche sans internet)

## Structure

```
src/main/java/com/example/project1/
  Project1Application.java
  controller/HomeController.java  # 7 routes, délègue au service
  model/Etudiant.java             # id, numeroInscription, nom, prenom, dateNaissance, moyenne, niveau, flag formulaire
  service/EtudiantService.java    # List + ajouter() / lister() / listerFormulaire() / findById() / supprimer()
src/main/resources/
  static/css/bootstrap.min.css + static/js/bootstrap.bundle.min.js
  templates/index.html | formulaire.html | liste.html | common/
  application.properties
ATELIER2-Marouen-Ksentini.md (+ .docx)  # rapport : code expliqué ligne par ligne
TOUTES-ACTIONS-PROJET.md                # toutes les actions + tout le code
```

## Lancer l'application

```powershell
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.18
.\mvnw.cmd spring-boot:run
```

Ouvrir `http://localhost:8080/`. Si le port est occupé (Apache EDB `httpd`) :
`net stop PEMHTTPD-x64` (admin) ou
`$env:SERVER_PORT="8082"; .\mvnw.cmd spring-boot:run` → `http://localhost:8082/`.

## Tester

```powershell
.\mvnw.cmd test   # contextLoads : Tests run: 1, Failures: 0, Errors: 0 + BUILD SUCCESS
```

Parcours manuel : `/` (2 de base) → Formulaire (ex. INS-301/Sara/M1) →
`/students/list` (que les ajouts) → Modifier → Supprimer (avec alerte).
