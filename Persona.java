package com.modelo;

import java.util.ArrayList;

public class Persona {
    private String nombre;
    private int edad;
    private int cedula;
    private int codigo;
    private String tipo;

    public Persona() {
    }

    public Persona(String nombre, int edad, int cedula, int codigo, String tipo) {
        this.nombre = nombre;
        this.edad = edad;
        this.cedula = cedula;
        this.codigo = codigo;
        this.tipo = tipo;
    }
    

    @Override
    public String toString() {
        return "nombre: " + getNombre() 
                + ", edad: " + getEdad() 
                + ", cedula: " + getCedula() 
                + ", codigo: " + getCodigo() 
                + ", codigo: " + getTipo();
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
     * @return the edad
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @param edad the edad to set
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * @return the cedula
     */
    public int getCedula() {
        return cedula;
    }

    /**
     * @param cedula the cedula to set
     */
    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}

