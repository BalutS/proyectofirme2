package com.modelo.dao;

import com.modelo.Profesor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfesorDAOImpl implements ProfesorDAO {

    private final String archivoCsv;
    private static final String CSV_HEADER = "codigo,nombre,edad,cedula,tipo";

    public ProfesorDAOImpl(String archivoCsv) {
        this.archivoCsv = archivoCsv;
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        File archivo = new File(this.archivoCsv);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs(); // Create parent directories if they don't exist
                archivo.createNewFile();
                try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv))) {
                    pw.println(CSV_HEADER);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el archivo CSV de profesores: " + e.getMessage());
            }
        }
    }

    private Profesor profesorDesdeCsvLinea(String lineaCsv) {
        String[] campos = lineaCsv.split(",");
        if (campos.length < 5) {
            System.err.println("Línea CSV malformada (profesores), no hay suficientes campos: " + lineaCsv);
            return null;
        }
        int codigo = Integer.parseInt(campos[0]);
        String nombre = campos[1];
        int edad = Integer.parseInt(campos[2]);
        String cedula = campos[3];
        String tipo = campos[4];

        // El campo curso no se maneja aquí directamente
        return new Profesor(codigo, nombre, edad, cedula, tipo);
    }

    private String profesorACsvLinea(Profesor profesor) {
        return profesor.getCodigo() + "," +
               profesor.getNombre() + "," +
               profesor.getEdad() + "," +
               profesor.getCedula() + "," +
               profesor.getTipo();
    }

    private List<Profesor> cargarProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.archivoCsv))) {
            String linea;
            br.readLine(); // Saltar cabecera
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Profesor profesor = profesorDesdeCsvLinea(linea);
                    if (profesor != null) {
                        profesores.add(profesor);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar profesores desde CSV: " + e.getMessage());
        }
        return profesores;
    }

    private void guardarProfesores(List<Profesor> profesores) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv))) {
            pw.println(CSV_HEADER);
            for (Profesor profesor : profesores) {
                pw.println(profesorACsvLinea(profesor));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar profesores en CSV: " + e.getMessage());
        }
    }

    @Override
    public void agregar(Profesor profesor) {
        List<Profesor> profesores = cargarProfesores();
        // Verificar si ya existe un profesor con el mismo código
        boolean existe = profesores.stream().anyMatch(p -> p.getCodigo() == profesor.getCodigo());
        if (existe) {
            System.err.println("Error: Ya existe un profesor con el código " + profesor.getCodigo());
            return; // O lanzar una excepción específica
        }
        // Si no existe, agregarlo
        try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv, true))) {
            pw.println(profesorACsvLinea(profesor));
        } catch (IOException e) {
            System.err.println("Error al agregar profesor al CSV: " + e.getMessage());
        }
    }

    @Override
    public Profesor obtenerPorCodigo(int codigo) {
        List<Profesor> profesores = cargarProfesores();
        for (Profesor profesor : profesores) {
            if (profesor.getCodigo() == codigo) {
                return profesor;
            }
        }
        return null;
    }

    @Override
    public List<Profesor> obtenerTodos() {
        return cargarProfesores();
    }

    @Override
    public void actualizar(Profesor profesorActualizado) {
        List<Profesor> profesores = cargarProfesores();
        List<Profesor> profesoresActualizados = new ArrayList<>();
        boolean encontrado = false;
        for (Profesor profesor : profesores) {
            if (profesor.getCodigo() == profesorActualizado.getCodigo()) {
                profesoresActualizados.add(profesorActualizado);
                encontrado = true;
            } else {
                profesoresActualizados.add(profesor);
            }
        }
        if (encontrado) {
            guardarProfesores(profesoresActualizados);
        } else {
            System.err.println("No se encontró profesor con código " + profesorActualizado.getCodigo() + " para actualizar.");
        }
    }

    @Override
    public void eliminar(int codigo) {
        List<Profesor> profesores = cargarProfesores();
        List<Profesor> profesoresRestantes = profesores.stream()
                                                     .filter(p -> p.getCodigo() != codigo)
                                                     .collect(Collectors.toList());
        if (profesores.size() == profesoresRestantes.size()) {
            System.err.println("No se encontró profesor con código " + codigo + " para eliminar.");
             // Consider throwing an exception or returning a boolean status
        }
        guardarProfesores(profesoresRestantes);
    }
}
