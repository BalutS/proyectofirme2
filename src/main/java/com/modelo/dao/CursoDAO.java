package com.modelo.dao;

import com.modelo.Curso;
import java.util.List;

public interface CursoDAO {
    void agregar(Curso curso);
    Curso obtenerPorId(int grado, int grupo);
    List<Curso> obtenerTodos();
    void actualizar(Curso curso);
    void eliminar(int grado, int grupo);
    void agregarEstudianteACurso(int codigoEstudiante, int gradoCurso, int grupoCurso);
    void asignarProfesorACurso(int codigoProfesor, int gradoCurso, int grupoCurso);
}
