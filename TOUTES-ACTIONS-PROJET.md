# Toutes les actions du projet + tout le code créé

## Partie 1 — Toutes les actions faites (dans l'ordre)

1. **Fix du lancement** : `mvn clean sprint-boot:run` → faute de frappe,
   corrigé en `spring-boot:run` (`spring-boot-maven-plugin`). Test `BUILD SUCCESS`.
2. **Port 8080 occupé** par `httpd` (service `PEMHTTPD-x64`, EDB) :
   solution `net stop PEMHTTPD-x64` (admin) ou `$env:SERVER_PORT="8082"`.
3. **Modèle `Etudiant` créé** : id, numeroInscription, nom, prenom,
   dateNaissance (`LocalDate`), moyenneAnneePrecedente (`Double`),
   niveauEtude (`String` : L1/L2/L3/M1/M2), ajouteParFormulaire (`boolean`).
   Ancien `Student.java` (firstName/lastName) supprimé.
4. **`EtudiantService` créé** (`@Service`, `List`) : `ajouter()` (id auto +
   flag `true`), `lister()` (tout), `listerFormulaire()` (filtre ajouts),
   `findById()`, `supprimer()`. 2 étudiants de base : Marouen Ksentini INS-001/L3,
   Ahmed Ksentini INS-002/L2.
5. **`HomeController` réécrit** : injecte le service, 7 routes
   (`/`, `/students/new`, `POST /students/add`, `/students/list`,
   `/students/edit/{id}`, `POST /students/update/{id}`, `/students/delete/{id}`).
6. **Pages** : `index.html` (accueil = tout, sans bouton d'ajout),
   `formulaire.html` (questionnaire « Nouveau étudiant », select L1–M2),
   `liste.html` (ajouts formulaire uniquement + compteur).
7. **Fichiers communs** : `common/header.html`, `menu.html`, `footer.html`
   inclus par `th:replace` dans les 3 pages.
8. **Bootstrap en local** : `static/css/bootstrap.min.css` +
   `static/js/bootstrap.bundle.min.js`, branchés par `th:href`/`th:src`.
9. **Alerte de confirmation** avant chaque suppression :
   `onclick="return confirm('Supprimer cet étudiant ?');"` sur les liens
   Supprimer de `index.html` et `liste.html`.

## Partie 2 — Tout le code créé

### `model/Etudiant.java`
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

### `service/EtudiantService.java`
```java
package com.example.project1.service;

import com.example.project1.model.Etudiant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

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

### `controller/HomeController.java` (routes)
```java
@GetMapping({"/", "/home"})           -> lister()            -> "index"
@GetMapping("/students/new")          -> new Etudiant()      -> "formulaire"
@PostMapping("/students/add")         -> ajouter()            -> redirect:/students/list
@GetMapping("/students/list")         -> listerFormulaire()  -> "liste"
@GetMapping("/students/edit/{id}")    -> findById + editMode -> "formulaire"
@PostMapping("/students/update/{id}") -> recopie 6 champs    -> redirect liste
@GetMapping("/students/delete/{id}")  -> supprimer()         -> redirect liste
```

### Templates + Bootstrap local
- `formulaire.html` : `th:object` + `th:field`, `type="date"`, `type="number"`,
  `<select>` L1–M2, `btn-primary`, header/menu/footer par `th:replace`.
- `index.html` : tableau complet (`th:each`), Supprimer avec confirmation :
```html
<a th:href="@{/students/delete/{id}(id=${s.id})}" onclick="return confirm('Supprimer cet étudiant ?');">Supprimer</a>
```
- `liste.html` : même tableau sur `listerFormulaire()` + compteur + bouton ajout.
- `common/` : `header.html`, `menu.html`, `footer.html` (fragments).
- `static/css/bootstrap.min.css` + `static/js/bootstrap.bundle.min.js`,
  branchés par `<link th:href="@{/css/bootstrap.min.css}">` et
  `<script th:src="@{/js/bootstrap.bundle.min.js}">`.
