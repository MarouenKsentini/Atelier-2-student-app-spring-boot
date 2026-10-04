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
