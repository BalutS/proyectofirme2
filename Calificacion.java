package com.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Calificacion {
    private String nombre;
    private float nota;
    private int periodo;
    private LocalDate fecha;

    public Calificacion(String nombre, float nota, int periodo, LocalDate fecha) {
        this.nombre = nombre;
        this.nota = nota;
        this.periodo = periodo;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return getNombre() + "{" + "nota:" + getNota() + ", periodo:" + getPeriodo() + ", fecha:" + getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + '}';
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the nota
     */
    public float getNota() {
        return nota;
    }

    /**
     * @param nota the nota to set
     */
    public void setNota(float nota) {
        this.nota = nota;
    }

    /**
     * @return the periodo
     */
    public int getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    /**
     * @return the fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    
    
    
    
}
