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
