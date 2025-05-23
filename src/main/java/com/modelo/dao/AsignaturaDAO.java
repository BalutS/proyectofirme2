package com.modelo.dao;

import com.modelo.Asignatura;
import java.util.List;

public interface AsignaturaDAO {
    void agregar(Asignatura asignatura);
    Asignatura obtenerPorNombre(String nombre);
    List<Asignatura> obtenerTodas();
    void actualizar(Asignatura asignatura);
    void eliminar(String nombre);
}
