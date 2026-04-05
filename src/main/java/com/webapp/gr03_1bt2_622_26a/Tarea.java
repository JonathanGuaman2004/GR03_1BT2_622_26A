package com.webapp.gr03_1bt2_622_26a;

import jakarta.persistence.*;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String estado;

    // Constructor vacío requerido por Hibernate
    public Tarea() {}

    public Tarea(String titulo, String descripcion, String estado) {
        this.titulo    = titulo;
        this.descripcion = descripcion;
        this.estado    = estado;
    }

    // Getters y Setters
    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getTitulo()           { return titulo; }
    public void setTitulo(String t)     { this.titulo = t; }

    public String getDescripcion()      { return descripcion; }
    public void setDescripcion(String d){ this.descripcion = d; }

    public String getEstado()           { return estado; }
    public void setEstado(String e)     { this.estado = e; }

    @Override
    public String toString() {
        return "Tarea{id=" + id + ", titulo='" + titulo + "', estado='" + estado + "'}";
    }
}