package com.modelo;

import java.util.ArrayList;

public class Curso {
    private int grado;
    private int grupo;
    private ArrayList<Estudiante> estudiantes;
    private Profesor profesor;

    public Curso() {
    }

    public Curso(int grado, int grupo, ArrayList<Estudiante> estudiantes) {
        this.grado = grado;
        this.grupo = grupo;
        this.estudiantes = estudiantes;
        profesor = new Profesor();
    }
    
    public Estudiante buscarEstudiante(int codigo){
        Estudiante est = null;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCodigo() == codigo) {
                est = estudiante;
            }
        }
        return est;
    }
    
    public String listarEstudiantes () {
        String lis = "";
        for (Estudiante estudiante : estudiantes) {
            lis += estudiante.toString();
        }
        return lis;
    }
    
    public String infoCurso(){
        return toString() + "\n"
                + profesor.toString()
                + listarEstudiantes();
    }
    

    @Override
    public String toString() {
        return "curso: " + grado + " - " + grupo
                + "\n profesor";
    }

    /**
     * @return the grado
     */
    public int getGrado() {
        return grado;
    }

    /**
     * @param grado the grado to set
     */
    public void setGrado(int grado) {
        this.grado = grado;
    }

    /**
     * @return the grupo
     */
    public int getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    /**
     * @return the estudiantes
     */
    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    /**
     * @param estudiantes the estudiantes to set
     */
    public void setEstudiantes(ArrayList<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    /**
     * @return the profesor
     */
    public Profesor getProfesor() {
        return profesor;
    }

    /**
     * @param profesor the profesor to set
     */
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
    
    
}

