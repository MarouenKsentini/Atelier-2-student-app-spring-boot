# Atelier 2 — Gestion des Étudiants (Spring Boot + Thymeleaf)

**Étudiant :** Marouen Ksentini — MP DSIR-GLNT
**Outil utilisé :** opencode / Muse Spark 1.3 Free

## 1. Structure du projet (où va chaque fichier)
```
src/main/java/com/example/project1/
  model/Etudiant.java            → les données (champs)
  service/EtudiantService.java   → la liste + ajouter/lister/…
  controller/HomeController.java → les routes (URLs)
src/main/resources/
  static/css/bootstrap.min.css   → style Bootstrap (local)
  static/js/bootstrap.bundle.min.js → JS Bootstrap (local)
  templates/index.html           → accueil = liste complète
  templates/formulaire.html      → questionnaire « Nouveau étudiant »
  templates/liste.html           → liste des ajouts du formulaire
  templates/common/header.html, menu.html, footer.html → morceaux partagés
```

## 2. Code 1 — `model/Etudiant.java`
**Où le mettre :** `src/main/java/com/example/project1/model/Etudiant.java`
```java
package com.example.project1.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant {

    private Long id;
    private String numeroInscription;
    private String nom;
    private String prenom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;

    private Double moyenneAnneePrecedente;
    private String niveauEtude;
    private boolean ajouteParFormulaire;
}
```
**Ligne par ligne — comment et pourquoi :**

| Ligne | Explication |
|---|---|
| `package …model;` | Déclare le dossier du fichier (obligatoire en Java). |
| `import LocalDate;` | Pour le champ date de naissance (année-mois-jour). |
| `import lombok…` | Lombok génère le code répétitif à notre place. |
| `import DateTimeFormat;` | Pour convertir le texte du formulaire en date. |
| `@Data` | Crée getters/setters/toString auto (ex. `getNom()`). |
| `@AllArgsConstructor` | Constructeur avec tous les champs (sert aux 2 exemples de base). |
| `@NoArgsConstructor` | Constructeur vide `new Etudiant()` — **obligatoire** car le formulaire en a besoin (`th:object`). |
| `private Long id;` | Numéro technique auto (1, 2, 3…), jamais tapé au clavier. |
| `numeroInscription / nom / prenom` | Textes saisis dans le questionnaire (ex. INS-301, Benali, Sara). |
| `@DateTimeFormat(…)` | Dit à Spring : le texte `2005-01-15` du `<input type="date">` devient une date. |
| `private Double moyenne…` | Nombre à virgule (ex. 15.75). |
| `private String niveauEtude;` | `String` et pas entier car valeurs `L1, L2, L3, M1, M2` (lettres + chiffre). |
| `ajouteParFormulaire` | `false` = les 2 de base, `true` = ajouté par le questionnaire (pour filtrer la liste). |

## 3. Code 2 — `service/EtudiantService.java`
**Où le mettre :** `src/main/java/com/example/project1/service/EtudiantService.java`
```java
@Service
public class EtudiantService {

    private final List<Etudiant> etudiants = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(3L);

    public EtudiantService() {
        etudiants.add(new Etudiant(1L, "INS-001", "Ksentini", "Marouen",
                LocalDate.of(2003, 5, 12), 14.5, "L3", false));
        etudiants.add(new Etudiant(2L, "INS-002", "Ksentini", "Ahmed",
                LocalDate.of(2004, 9, 3), 12.0, "L2", false));
    }

    public void ajouter(Etudiant e) {
        e.setId(nextId.getAndIncrement());
        e.setAjouteParFormulaire(true);
        etudiants.add(e);
    }

    public Collection<Etudiant> lister() {
        return etudiants;
    }

    public Collection<Etudiant> listerFormulaire() {
        return etudiants.stream()
                .filter(Etudiant::isAjouteParFormulaire)
                .toList();
    }

    public Etudiant findById(Long id) {
        return etudiants.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void supprimer(Long id) {
        etudiants.removeIf(e -> e.getId().equals(id));
    }
}
```
**Ligne par ligne — comment et pourquoi :**

| Ligne | Explication |
|---|---|
| `@Service` | Spring crée UN seul objet partagé et l'injecte dans le contrôleur. |
| `List<Etudiant> etudiants` | La collection demandée : tous les étudiants vivent ici (pas de base de données). |
| `AtomicLong nextId (3L)` | Compteur d'id sans doublon : prochain ajout = 3, puis 4, 5… |
| Constructeur + 2 `add` | 2 étudiants de base (Marouen L3, Ahmed L2, flag `false`). |
| `ajouter()` ligne 1 | Donne l'id auto à l'étudiant du formulaire. |
| `ajouter()` ligne 2 | Marque `true` = « vient du questionnaire ». |
| `ajouter()` ligne 3 | Range l'étudiant dans la liste. |
| `lister()` | Rend TOUTE la liste → page Accueil. |
| `listerFormulaire()` | `stream + filter` : ne garde que les `true` → page Liste. |
| `findById()` | Cherche par id (modification) ; `null` si absent. |
| `supprimer()` | `removeIf` : enlève celui dont l'id correspond. |

## 4. Code 3 — `controller/HomeController.java`
**Où le mettre :** `src/main/java/com/example/project1/controller/HomeController.java`
```java
public HomeController(EtudiantService etudiantService) { … } // injection

@GetMapping({"/", "/home"})           -> lister()            -> "index"
@GetMapping("/students/new")          -> new Etudiant()      -> "formulaire"
@PostMapping("/students/add")         -> ajouter()            -> redirect:/students/list
@GetMapping("/students/list")         -> listerFormulaire()  -> "liste"
@GetMapping("/students/edit/{id}")    -> findById + editMode -> "formulaire"
@PostMapping("/students/update/{id}") -> recopie 6 champs    -> redirect liste
@GetMapping("/students/delete/{id}")  -> supprimer()         -> redirect liste
```
**Ligne par ligne — comment et pourquoi :**

| Ligne | Explication |
|---|---|
| Constructeur | Reçoit le service (injection : le contrôleur ne stocke rien lui-même). |
| `GET /` | Page d'accueil : met `lister()` dans `students`, affiche `index`. |
| `GET /students/new` | Questionnaire vide (`new Etudiant()`), affiche `formulaire`. |
| `POST /students/add` | `@ModelAttribute` remplit l'objet depuis les champs → `ajouter()` → redirect vers la liste (anti-doublon au refresh). |
| `GET /students/list` | Met `listerFormulaire()` dans `students`, affiche `liste`. |
| `GET /students/edit/{id}` | `findById` + `editMode=true` → même questionnaire, pré-rempli. |
| `POST /students/update/{id}` | Recopie les 6 champs sur l'objet existant (l'id ne change pas). |
| `GET /students/delete/{id}` | `supprimer(id)` puis retour à la liste. |

## 5. Code 4 — `templates/formulaire.html` (le questionnaire)
**Où le mettre :** `src/main/resources/templates/formulaire.html`
**Lignes clés — comment et pourquoi :**

| Ligne | Explication |
|---|---|
| `<link th:href="@{/css/bootstrap.min.css}">` | Charge le style Bootstrap **local** (`static/css/`). |
| `th:object="${student}"` | Lie tout le formulaire à l'objet Java. |
| `th:field="*{nom}"` (etc.) | Lie chaque champ au champ Java du même nom (remplissage auto dans les 2 sens). |
| `<input type="date" …dateNaissance>` | Calendrier navigateur → converti par `@DateTimeFormat`. |
| `<select th:field="*{niveauEtude}">` + 5 `<option>` | Liste imposée L1/L2/L3/M1/M2 (pas de frappe libre). |
| `th:action="@{/students/add}"` | Envoi vers la route d'ajout (ou `update/{id}` en modification). |
| `th:replace` header/menu/footer | Inclut les 3 morceaux communs (pas de duplication). |
| `<script th:src="@{/js/…}">` | JS Bootstrap local avant `</body>`. |

## 6. Code 5 — `templates/index.html` et `liste.html` (les listes)
**Où les mettre :** `src/main/resources/templates/`
**Lignes clés — comment et pourquoi :**

| Ligne | Explication |
|---|---|
| `<table class="table table-striped table-bordered">` | Tableau Bootstrap rayé + bordures. |
| `th:if="${#lists.isEmpty(students)}"` | Message « Aucun étudiant à afficher » si liste vide. |
| `th:each="s : ${students}"` | Boucle : une ligne `<tr>` par étudiant de `lister()` (index) ou `listerFormulaire()` (liste). |
| `th:text="${s.nom}"` (etc.) | Affiche chaque colonne. |
| `@{/students/edit/{id}(id=${s.id})}` | Lien Modifier avec le bon id. |
| `onclick="return confirm('Supprimer cet étudiant ?');"` | **Alerte de confirmation** : le navigateur demande OK/Annuler avant de supprimer. |
| Compteur `(#lists.size)` | Nombre d'ajouts affiché sur `liste.html`. |

## 7. Code 6 — `templates/common/` + Bootstrap local
- `header.html` (`:: header`) : bandeau du site — modifié 1 fois au lieu de 3.
- `menu.html` (`:: menu`) : `<nav>` Accueil / Formulaire / Liste.
- `footer.html` (`:: footer`) : pied de page.
- `static/css/bootstrap.min.css` + `static/js/bootstrap.bundle.min.js` :
  téléchargés une fois par `Invoke-WebRequest`, marche **sans internet**.

## 8. Lancement et test
```powershell
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.18
.\mvnw.cmd test                 # BUILD SUCCESS
.\mvnw.cmd spring-boot:run      # http://localhost:8080/
# si port occupé : $env:SERVER_PORT="8082"; .\mvnw.cmd spring-boot:run
```
Parcours : `/` (Marouen + Ahmed) → Formulaire « Nouveau étudiant » (ex. Sara M1) →
`/students/list` (que les ajouts) → Modifier → Supprimer (avec alerte).
