/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.modelo;

import java.util.ArrayList;

/**
 *
 * @author river
 */
public class Asignatura {
    private String nombre;
    private ArrayList<Calificacion> calificaciones;

    public Asignatura(String nombre) {
        this.nombre = nombre;
        calificaciones = new ArrayList<>();
    }
    
    public void agregarCalificacion(Calificacion cal){
        getCalificaciones().add(cal);
    }
    
    public String listarCalificaiones () {
        String lis = "";
        for (Calificacion calificacion : getCalificaciones()) {
            lis += calificacion.toString() + "\n";
        }
        return lis;
    }
    
    public float promedio(){
        float sum = 0;
        for (Calificacion calificacion : calificaciones) {
            sum += calificacion.getNota();
        }
        return sum/calificaciones.size();
    }

    @Override
    public String toString() {
        return "Asignatura{" + "nombre=" + nombre + ", calificaciones=" + promedio() + '}';
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
     * @return the calificaciones
     */
    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    /**
     * @param calificaciones the calificaciones to set
     */
    public void setCalificaciones(ArrayList<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }
}
