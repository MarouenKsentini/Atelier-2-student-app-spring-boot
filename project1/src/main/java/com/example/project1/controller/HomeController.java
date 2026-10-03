package com.example.project1.controller;

import com.example.project1.model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final List<Student> students = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(3L);
    public HomeController() {
        students.add(new Student(1L, "Marouen", "Ksentini"));
        students.add(new Student(2L, "Ahmed", "Ksentini"));
    }
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("students", students);
        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new Student());
        }
        return "index";
    }
    @PostMapping("/students/add")
    public String add(@ModelAttribute Student student) {
        student.setId(nextId.getAndIncrement());
        students.add(student);
        return "redirect:/";
    }
    @GetMapping("/students/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Student found = findById(id);
        if (found == null) {
            return "redirect:/";
        }
        model.addAttribute("students", students);
        model.addAttribute("student", found);
        model.addAttribute("editMode", true);
        return "index";
    }
    @PostMapping("/students/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Student student) {
        Student found = findById(id);
        if (found != null) {
            found.setFirstName(student.getFirstName());
            found.setLastName(student.getLastName());
        }
        return "redirect:/";
    }
    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable Long id) {
        students.removeIf(s -> s.getId().equals(id));
        return "redirect:/";
    }
    private Student findById(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
