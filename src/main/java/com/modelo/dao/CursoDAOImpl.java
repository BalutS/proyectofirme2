package com.modelo.dao;

import com.modelo.Curso;
import com.modelo.Estudiante; // Required for managing student lists
import com.modelo.Profesor;   // Required for managing professor assignment

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CursoDAOImpl implements CursoDAO {

    private final String archivoCsv;
    private static final String CSV_HEADER = "grado,grupo,profesor_codigo,estudiante_codigos";

    public CursoDAOImpl(String archivoCsv) {
        this.archivoCsv = archivoCsv;
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        File archivo = new File(this.archivoCsv);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv))) {
                    pw.println(CSV_HEADER);
                }
            } catch (IOException e) {
                System.err.println("Error al crear el archivo CSV de cursos: " + e.getMessage());
            }
        }
    }

    private Curso cursoDesdeCsvLinea(String lineaCsv) {
        String[] campos = lineaCsv.split(",", -1); // Use -1 limit to include trailing empty strings
        if (campos.length < 4) {
            System.err.println("Línea CSV malformada (cursos), no hay suficientes campos: " + lineaCsv);
            return null;
        }
        int grado = Integer.parseInt(campos[0]);
        int grupo = Integer.parseInt(campos[1]);
        
        Curso curso = new Curso();
        curso.setGrado(grado);
        curso.setGrupo(grupo);

        // Profesor (solo código)
        if (campos[2] != null && !campos[2].isEmpty()) {
            Profesor profesor = new Profesor(); // Create a placeholder, actual object resolved later
            profesor.setCodigo(Integer.parseInt(campos[2]));
            curso.setProfesor(profesor);
        }

        // Estudiantes (lista de códigos)
        if (campos[3] != null && !campos[3].isEmpty()) {
            String[] codigosEstudianteStr = campos[3].split(";"); // Assuming student codes are semicolon-separated
            List<Estudiante> estudiantes = new ArrayList<>();
            for (String codigoStr : codigosEstudianteStr) {
                if (!codigoStr.trim().isEmpty()) {
                    Estudiante estudiante = new Estudiante(); // Placeholder
                    estudiante.setCodigo(Integer.parseInt(codigoStr.trim()));
                    estudiantes.add(estudiante);
                }
            }
            curso.setEstudiantes(estudiantes);
        } else {
            curso.setEstudiantes(new ArrayList<>()); // Initialize with empty list if no students
        }
        
        // Asignaturas no se manejan aquí directamente
        curso.setAsignaturas(new ArrayList<>());


        return curso;
    }

    private String cursoACsvLinea(Curso curso) {
        String profesorCodigo = (curso.getProfesor() != null) ? String.valueOf(curso.getProfesor().getCodigo()) : "";
        
        String estudianteCodigos = "";
        if (curso.getEstudiantes() != null && !curso.getEstudiantes().isEmpty()) {
            estudianteCodigos = curso.getEstudiantes().stream()
                                    .map(e -> String.valueOf(e.getCodigo()))
                                    .collect(Collectors.joining(";"));
        }

        return curso.getGrado() + "," +
               curso.getGrupo() + "," +
               profesorCodigo + "," +
               estudianteCodigos;
    }

    private List<Curso> cargarCursos() {
        List<Curso> cursos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.archivoCsv))) {
            String linea;
            br.readLine(); // Saltar cabecera
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Curso curso = cursoDesdeCsvLinea(linea);
                    if (curso != null) {
                        cursos.add(curso);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar cursos desde CSV: " + e.getMessage());
        }
        return cursos;
    }

    private void guardarCursos(List<Curso> cursos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv))) {
            pw.println(CSV_HEADER);
            for (Curso curso : cursos) {
                pw.println(cursoACsvLinea(curso));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar cursos en CSV: " + e.getMessage());
        }
    }

    @Override
    public void agregar(Curso curso) {
        List<Curso> cursos = cargarCursos();
        // Verificar si ya existe un curso con el mismo grado y grupo
        boolean existe = cursos.stream().anyMatch(c -> c.getGrado() == curso.getGrado() && c.getGrupo() == curso.getGrupo());
        if (existe) {
            System.err.println("Error: Ya existe un curso con grado " + curso.getGrado() + " y grupo " + curso.getGrupo());
            return; // O lanzar una excepción específica
        }
        // Si no existe, agregarlo
        cursos.add(curso);
        guardarCursos(cursos); // Guardar toda la lista, podría ser ineficiente para solo agregar
                               // Alternativa: usar FileWriter en modo append, pero más complejo para IDs únicos
    }

    @Override
    public Curso obtenerPorId(int grado, int grupo) {
        List<Curso> cursos = cargarCursos();
        for (Curso curso : cursos) {
            if (curso.getGrado() == grado && curso.getGrupo() == grupo) {
                return curso;
            }
        }
        return null;
    }

    @Override
    public List<Curso> obtenerTodos() {
        return cargarCursos();
    }

    @Override
    public void actualizar(Curso cursoActualizado) {
        List<Curso> cursos = cargarCursos();
        List<Curso> cursosActualizados = new ArrayList<>();
        boolean encontrado = false;
        for (Curso curso : cursos) {
            if (curso.getGrado() == cursoActualizado.getGrado() && curso.getGrupo() == cursoActualizado.getGrupo()) {
                cursosActualizados.add(cursoActualizado);
                encontrado = true;
            } else {
                cursosActualizados.add(curso);
            }
        }
        if (encontrado) {
            guardarCursos(cursosActualizados);
        } else {
            System.err.println("No se encontró curso con grado " + cursoActualizado.getGrado() + 
                               " y grupo " + cursoActualizado.getGrupo() + " para actualizar.");
        }
    }

    @Override
    public void eliminar(int grado, int grupo) {
        List<Curso> cursos = cargarCursos();
        List<Curso> cursosRestantes = cursos.stream()
                                           .filter(c -> !(c.getGrado() == grado && c.getGrupo() == grupo))
                                           .collect(Collectors.toList());
        if (cursos.size() == cursosRestantes.size()) {
            System.err.println("No se encontró curso con grado " + grado + " y grupo " + grupo + " para eliminar.");
        }
        guardarCursos(cursosRestantes);
    }

    @Override
    public void agregarEstudianteACurso(int codigoEstudiante, int gradoCurso, int grupoCurso) {
        Curso curso = obtenerPorId(gradoCurso, grupoCurso);
        if (curso == null) {
            System.err.println("Error: Curso con grado " + gradoCurso + " y grupo " + grupoCurso + " no encontrado.");
            return;
        }

        // Verificar si el estudiante ya está en el curso
        boolean estudianteYaExiste = curso.getEstudiantes().stream()
                                        .anyMatch(e -> e.getCodigo() == codigoEstudiante);
        if (estudianteYaExiste) {
            System.err.println("Error: Estudiante con código " + codigoEstudiante + " ya está en el curso.");
            return;
        }
        
        Estudiante estudianteNuevo = new Estudiante(); // Placeholder, solo necesitamos el código
        estudianteNuevo.setCodigo(codigoEstudiante);
        curso.getEstudiantes().add(estudianteNuevo);
        actualizar(curso);
    }

    @Override
    public void asignarProfesorACurso(int codigoProfesor, int gradoCurso, int grupoCurso) {
        Curso curso = obtenerPorId(gradoCurso, grupoCurso);
        if (curso == null) {
            System.err.println("Error: Curso con grado " + gradoCurso + " y grupo " + grupoCurso + " no encontrado.");
            return;
        }
        Profesor profesorAsignado = new Profesor(); // Placeholder
        profesorAsignado.setCodigo(codigoProfesor);
        curso.setProfesor(profesorAsignado);
        actualizar(curso);
    }
}
