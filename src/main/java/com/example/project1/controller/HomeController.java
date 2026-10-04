package com.example.project1.controller;

import com.example.project1.model.Etudiant;
import com.example.project1.service.EtudiantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final EtudiantService etudiantService;

    public HomeController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    // HOME = liste complète des étudiants
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("students", etudiantService.lister());
        return "index";
    }

    // PAGE FORMULAIRE (ajout)
    @GetMapping("/students/new")
    public String newForm(Model model) {
        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new Etudiant());
        }
        return "formulaire";
    }

    // SUBMIT formulaire -> liste
    @PostMapping("/students/add")
    public String add(@ModelAttribute("student") Etudiant etudiant) {
        etudiantService.ajouter(etudiant);
        return "redirect:/students/list";
    }

    @GetMapping("/students/list")
    public String list(Model model) {
        model.addAttribute("students", etudiantService.listerFormulaire());
        return "liste";
    }

    @GetMapping("/students/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Etudiant found = etudiantService.findById(id);
        if (found == null) {
            return "redirect:/students/list";
        }
        model.addAttribute("student", found);
        model.addAttribute("editMode", true);
        return "formulaire";
    }

    @PostMapping("/students/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("student") Etudiant etudiant) {
        Etudiant found = etudiantService.findById(id);
        if (found != null) {
            found.setNumeroInscription(etudiant.getNumeroInscription());
            found.setNom(etudiant.getNom());
            found.setPrenom(etudiant.getPrenom());
            found.setDateNaissance(etudiant.getDateNaissance());
            found.setMoyenneAnneePrecedente(etudiant.getMoyenneAnneePrecedente());
            found.setNiveauEtude(etudiant.getNiveauEtude());
        }
        return "redirect:/students/list";
    }

    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return "redirect:/students/list";
    }
}
