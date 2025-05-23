package com.modelo.dao;

import com.modelo.Estudiante;
import java.util.List;

public interface EstudianteDAO {
    void agregar(Estudiante estudiante);
    Estudiante obtenerPorCodigo(int codigo);
    List<Estudiante> obtenerTodos();
    void actualizar(Estudiante estudiante);
    void eliminar(int codigo);
}
