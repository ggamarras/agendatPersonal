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

    // ✅ Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}