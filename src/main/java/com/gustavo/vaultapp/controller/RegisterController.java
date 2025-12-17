package com.gustavo.vaultapp.controller;

import com.gustavo.vaultapp.model.RequestEntry;
import com.gustavo.vaultapp.service.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final RequestService service;

    public RegisterController(RequestService service) {
        this.service = service;
    }

    @GetMapping
    public String form(Model model) {
        model.addAttribute("entry", new RequestEntry());
        return "register";
    }

    @PostMapping
    public String submit(@ModelAttribute RequestEntry entry) {
        service.create(entry);
        return "redirect:/register?ok";
    }
}