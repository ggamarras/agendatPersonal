package com.gustavo.vaultapp.service;

import com.gustavo.vaultapp.model.RequestEntry;
import com.gustavo.vaultapp.repo.RequestEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private final RequestEntryRepository repo;

    public RequestService(RequestEntryRepository repo) {
        this.repo = repo;
    }

    public RequestEntry create(RequestEntry entry) {
        return repo.save(entry);
    }

    public List<RequestEntry> findAll() {
        return repo.findAll();
    }

    public RequestEntry update(Long id, RequestEntry updated) {
        return repo.findById(id).map(e -> {
            e.setNombre(updated.getNombre());
            e.setApellido(updated.getApellido());
            e.setDireccion(updated.getDireccion());
            e.setTelefono(updated.getTelefono());
            return repo.save(e);
        }).orElseThrow();
    }

    public void approve(Long id) {
        repo.findById(id).ifPresent(e -> {
            e.setStatus(RequestEntry.Status.APROBADO);
            repo.save(e);
        });
    }

    public void reject(Long id) {
        repo.findById(id).ifPresent(e -> {
            e.setStatus(RequestEntry.Status.RECHAZADO);
            repo.save(e);
        });
    }
}