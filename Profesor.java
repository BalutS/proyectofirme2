package com.modelo;

public class Profesor extends Persona {
    private Curso curso;

    public Profesor() {
    }

    public Profesor(Curso curso, String nombre, int edad, int cedula, int codigo, String tipo) {
        super(nombre, edad, cedula, codigo, tipo);
        this.curso = curso;
    }
    
    public void calificarEstudiante(int codEst, String asig, Calificacion cal){
        getCurso().buscarEstudiante(codEst).buscarAsignatura(asig).agregarCalificacion(cal);
    }
    
    public String listarEstudiantes(){
        return getCurso().listarEstudiantes();
    }

    @Override
    public String toString() {
        return super.toString() + getCurso().toString();
    }

    /**
     * @return the curso
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    
}

