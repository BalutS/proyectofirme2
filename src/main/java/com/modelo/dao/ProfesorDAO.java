package com.modelo.dao;

import com.modelo.Profesor;
import java.util.List;

public interface ProfesorDAO {
    void agregar(Profesor profesor);
    Profesor obtenerPorCodigo(int codigo);
    List<Profesor> obtenerTodos();
    void actualizar(Profesor profesor);
    void eliminar(int codigo);
}
