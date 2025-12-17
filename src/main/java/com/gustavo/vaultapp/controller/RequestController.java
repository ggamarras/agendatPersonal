package com.gustavo.vaultapp.controller;

import com.gustavo.vaultapp.model.RequestEntry;
import com.gustavo.vaultapp.service.RequestService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class RequestController {

    private final RequestService service;

    public RequestController(RequestService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model, Authentication auth) {
        model.addAttribute("entries", service.findAll());
        model.addAttribute("isAdmin",
                auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")));
        model.addAttribute("isAprobador",
                auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_APROBADOR")));
        return "requests";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        service.approve(id);
        return "redirect:/requests?approved";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        service.reject(id);
        return "redirect:/requests?rejected";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // ✅ Ahora compila porque RequestEntry tiene getId()
        RequestEntry entry = service.findAll().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow();
        model.addAttribute("entry", entry);
        return "register"; // reutilizamos la vista de registro como edición
    }

    @PostMapping("/{id}/edit")
    public String saveEdit(@PathVariable Long id, @ModelAttribute RequestEntry entry) {
        service.update(id, entry);
        return "redirect:/requests?updated";
    }
}