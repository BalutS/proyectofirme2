package com.modelo.dao;

import com.modelo.Estudiante;
import com.modelo.Curso; // Assuming Curso class exists in com.modelo
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EstudianteDAOImpl implements EstudianteDAO {

    private final String archivoCsv;
    private static final String CSV_HEADER = "codigo,nombre,edad,cedula,tipo,curso_grado,curso_grupo";

    public EstudianteDAOImpl(String archivoCsv) {
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
                System.err.println("Error al crear el archivo CSV: " + e.getMessage());
            }
        }
    }

    // Helper methods will be implemented next
    private Estudiante estudianteDesdeCsvLinea(String lineaCsv) {
        String[] campos = lineaCsv.split(",");
        if (campos.length < 7) { // Ensure enough fields
            // Or throw an IllegalArgumentException, or return null and handle upstream
            System.err.println("Línea CSV malformada, no hay suficientes campos: " + lineaCsv);
            return null; 
        }
        int codigo = Integer.parseInt(campos[0]);
        String nombre = campos[1];
        int edad = Integer.parseInt(campos[2]);
        String cedula = campos[3];
        String tipo = campos[4]; // Assuming tipo is a String for now
        int cursoGrado = Integer.parseInt(campos[5]);
        int cursoGrupo = Integer.parseInt(campos[6]);

        Estudiante estudiante = new Estudiante(codigo, nombre, edad, cedula, tipo);
        Curso curso = new Curso(); // Create a new Curso or get it from a CursoDAO
        curso.setGrado(cursoGrado);
        curso.setGrupo(cursoGrupo);
        estudiante.setCurso(curso);
        // Asignaturas are not handled here
        return estudiante;
    }

    private String estudianteACsvLinea(Estudiante estudiante) {
        Curso curso = estudiante.getCurso();
        return estudiante.getCodigo() + "," +
               estudiante.getNombre() + "," +
               estudiante.getEdad() + "," +
               estudiante.getCedula() + "," +
               estudiante.getTipo() + "," +
               (curso != null ? curso.getGrado() : "") + "," +
               (curso != null ? curso.getGrupo() : "");
    }

    private List<Estudiante> cargarEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.archivoCsv))) {
            String linea;
            br.readLine(); // Saltar cabecera
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Estudiante estudiante = estudianteDesdeCsvLinea(linea);
                    if (estudiante != null) {
                        estudiantes.add(estudiante);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar estudiantes desde CSV: " + e.getMessage());
        }
        return estudiantes;
    }

    private void guardarEstudiantes(List<Estudiante> estudiantes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv))) {
            pw.println(CSV_HEADER);
            for (Estudiante estudiante : estudiantes) {
                pw.println(estudianteACsvLinea(estudiante));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar estudiantes en CSV: " + e.getMessage());
        }
    }

    @Override
    public void agregar(Estudiante estudiante) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv, true))) {
            pw.println(estudianteACsvLinea(estudiante));
        } catch (IOException e) {
            System.err.println("Error al agregar estudiante al CSV: " + e.getMessage());
        }
    }

    @Override
    public Estudiante obtenerPorCodigo(int codigo) {
        List<Estudiante> estudiantes = cargarEstudiantes();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCodigo() == codigo) {
                return estudiante;
            }
        }
        return null;
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        return cargarEstudiantes();
    }

    @Override
    public void actualizar(Estudiante estudianteActualizado) {
        List<Estudiante> estudiantes = cargarEstudiantes();
        List<Estudiante> estudiantesActualizados = new ArrayList<>();
        boolean encontrado = false;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getCodigo() == estudianteActualizado.getCodigo()) {
                estudiantesActualizados.add(estudianteActualizado);
                encontrado = true;
            } else {
                estudiantesActualizados.add(estudiante);
            }
        }
        if (encontrado) {
            guardarEstudiantes(estudiantesActualizados);
        } else {
            System.err.println("No se encontró estudiante con código " + estudianteActualizado.getCodigo() + " para actualizar.");
        }
    }

    @Override
    public void eliminar(int codigo) {
        List<Estudiante> estudiantes = cargarEstudiantes();
        List<Estudiante> estudiantesRestantes = estudiantes.stream()
                                                     .filter(e -> e.getCodigo() != codigo)
                                                     .collect(Collectors.toList());
        if (estudiantes.size() == estudiantesRestantes.size()) {
            System.err.println("No se encontró estudiante con código " + codigo + " para eliminar.");
        }
        guardarEstudiantes(estudiantesRestantes);
    }
}
