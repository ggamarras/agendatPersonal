package com.gustavo.vaultapp.model;

import jakarta.persistence.*;

@Entity
public class RequestEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDIENTE;

    public enum Status {
        PENDIENTE, APROBADO, RECHAZADO
    }

    // getters y setters
    // ...
}