package com.modelo;

import com.modelo.dao.AsignaturaDAO;
import com.modelo.dao.AsignaturaDAOImpl;
import com.modelo.dao.CursoDAO;
import com.modelo.dao.CursoDAOImpl;
import com.modelo.dao.EstudianteDAO;
import com.modelo.dao.EstudianteDAOImpl;
import com.modelo.dao.ProfesorDAO;
import com.modelo.dao.ProfesorDAOImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


// Assuming other model classes (Persona, Estudiante, Profesor, Curso, Asignatura, Calificacion) exist in this package.

public class Colegio {
    private String nombre;
    private ArrayList<Persona> personas; // Combined list of Estudiantes and Profesores
    private ArrayList<Curso> cursos;
    private ArrayList<Asignatura> asignaturas;

    private EstudianteDAO estudianteDAO;
    private ProfesorDAO profesorDAO;
    private CursoDAO cursoDAO;
    private AsignaturaDAO asignaturaDAO;

    // File paths for CSVs - consider making these configurable
    private static final String ESTUDIANTES_CSV = "data/estudiantes.csv";
    private static final String PROFESORES_CSV = "data/profesores.csv";
    private static final String CURSOS_CSV = "data/cursos.csv";
    private static final String ASIGNATURAS_CSV = "data/asignaturas.csv";


    public Colegio(String nombre) {
        this.nombre = nombre;

        // Initialize DAOs
        this.estudianteDAO = new EstudianteDAOImpl(ESTUDIANTES_CSV);
        this.profesorDAO = new ProfesorDAOImpl(PROFESORES_CSV);
        this.cursoDAO = new CursoDAOImpl(CURSOS_CSV);
        this.asignaturaDAO = new AsignaturaDAOImpl(ASIGNATURAS_CSV);

        // Initialize in-memory lists
        this.personas = new ArrayList<>();
        this.cursos = new ArrayList<>();
        this.asignaturas = new ArrayList<>();
        
        cargarDatos();
    }

    private void cargarDatos() {
        // Load initial data
        this.personas.clear();
        List<Estudiante> estudiantes = estudianteDAO.obtenerTodos();
        if (estudiantes != null) {
            this.personas.addAll(estudiantes);
        }
        List<Profesor> profesores = profesorDAO.obtenerTodos();
        if (profesores != null) {
            this.personas.addAll(profesores);
        }

        List<Curso> cursosCargados = cursoDAO.obtenerTodos();
        this.cursos = (cursosCargados != null) ? new ArrayList<>(cursosCargados) : new ArrayList<>();
        
        List<Asignatura> asignaturasCargadas = asignaturaDAO.obtenerTodas();
        this.asignaturas = (asignaturasCargadas != null) ? new ArrayList<>(asignaturasCargadas) : new ArrayList<>();

        // Resolve Object Relationships
        resolverRelaciones();
    }

    private void resolverRelaciones() {
        // Link Estudiantes to Cursos
        for (Persona p : this.personas) {
            if (p instanceof Estudiante) {
                Estudiante est = (Estudiante) p;
                if (est.getCurso() != null && est.getCurso().getGrado() != 0 && est.getCurso().getGrupo() != 0) { // Curso data from DAO is minimal
                    Curso cursoCompleto = buscarCurso(est.getCurso().getGrado(), est.getCurso().getGrupo());
                    if (cursoCompleto != null) {
                        est.setCurso(cursoCompleto);
                    } else {
                        System.err.println("Advertencia: No se encontró el curso " + est.getCurso().getGrado() + "-" + est.getCurso().getGrupo() + " para el estudiante " + est.getCodigo());
                    }
                }
            }
        }

        // Link Cursos to Profesores and Estudiantes
        for (Curso curso : this.cursos) {
            // Link Curso to Profesor
            if (curso.getProfesor() != null && curso.getProfesor().getCodigo() != 0) { // Profesor data from DAO is minimal (just code)
                Profesor profesorCompleto = buscarProfesor(curso.getProfesor().getCodigo());
                if (profesorCompleto != null) {
                    curso.setProfesor(profesorCompleto);
                } else {
                     System.err.println("Advertencia: No se encontró el profesor con código " + curso.getProfesor().getCodigo() + " para el curso " + curso.getGrado() + "-" + curso.getGrupo());
                }
            }

            // Link Curso to Estudiantes
            if (curso.getEstudiantes() != null && !curso.getEstudiantes().isEmpty()) {
                List<Estudiante> estudiantesCompletos = new ArrayList<>();
                List<Estudiante> estudiantesOriginales = new ArrayList<>(curso.getEstudiantes()); // Avoid ConcurrentModification
                curso.getEstudiantes().clear(); // Clear the list of minimal student objects

                for (Estudiante estMinimo : estudiantesOriginales) { // estMinimo from DAO has only code
                    Estudiante estudianteCompleto = buscarEstudiante(estMinimo.getCodigo());
                    if (estudianteCompleto != null) {
                        estudiantesCompletos.add(estudianteCompleto);
                    } else {
                        System.err.println("Advertencia: No se encontró el estudiante con código " + estMinimo.getCodigo() + " para el curso " + curso.getGrado() + "-" + curso.getGrupo());
                    }
                }
                curso.setEstudiantes(estudiantesCompletos);
            }
        }
        
        // Asignaturas and Calificaciones are loaded by AsignaturaDAOImpl directly.
        // Estudiante.asignaturas list is not persisted by EstudianteDAOImpl, populated at runtime.
        // Initialize asignaturas list for each student
        for (Persona p : this.personas) {
            if (p instanceof Estudiante) {
                Estudiante est = (Estudiante) p;
                if (est.getAsignaturas() == null) { // Ensure it's initialized
                    est.setAsignaturas(new ArrayList<>());
                }
            }
        }
    }


    // --- Getters and Setters ---
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Persona> getPersonas() {
        return personas;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    // --- Basic methods that will be refactored ---

    public void agregarEstudiante(Estudiante est) {
        if (buscarEstudiante(est.getCodigo()) == null) {
            estudianteDAO.agregar(est);
            this.personas.add(est); // Add to in-memory list
        } else {
            System.out.println("Error: Estudiante con código " + est.getCodigo() + " ya existe.");
        }
    }

    public void agregarProfesor(Profesor prof) {
        if (buscarProfesor(prof.getCodigo()) == null) {
            profesorDAO.agregar(prof);
            this.personas.add(prof); // Add to in-memory list
        } else {
            System.out.println("Error: Profesor con código " + prof.getCodigo() + " ya existe.");
        }
    }

    public void agregarCurso(Curso curso) {
        if (buscarCurso(curso.getGrado(), curso.getGrupo()) == null) {
            cursoDAO.agregar(curso);
            this.cursos.add(curso); // Add to in-memory list
        } else {
            System.out.println("Error: Curso " + curso.getGrado() + "-" + curso.getGrupo() + " ya existe.");
        }
    }

    public void agregarAsignatura(Asignatura asig) {
        if (buscarAsignatura(asig.getNombre()) == null) {
            asignaturaDAO.agregar(asig);
            this.asignaturas.add(asig); // Add to in-memory list
        } else {
            System.out.println("Error: Asignatura con nombre " + asig.getNombre() + " ya existe.");
        }
    }

    // Search methods now primarily use in-memory lists
    public Estudiante buscarEstudiante(int codigo) {
        return this.personas.stream()
            .filter(p -> p instanceof Estudiante)
            .map(p -> (Estudiante) p)
            .filter(e -> e.getCodigo() == codigo)
            .findFirst()
            .orElse(null);
    }

    public void actualizarEstudiante(Estudiante estudianteActualizado) {
        Estudiante estExistente = buscarEstudiante(estudianteActualizado.getCodigo());
        if (estExistente != null) {
            // Actualizar la instancia en la lista de personas
            int index = this.personas.indexOf(estExistente);
            if (index != -1) {
                this.personas.set(index, estudianteActualizado);
            }
            // Actualizar curso del estudiante si cambió
            if (estudianteActualizado.getCurso() != null) {
                 Curso cursoNuevo = buscarCurso(estudianteActualizado.getCurso().getGrado(), estudianteActualizado.getCurso().getGrupo());
                 if (cursoNuevo != null) {
                    // Quitar de curso anterior si estaba en uno y es diferente al nuevo
                    if(estExistente.getCurso() != null && !estExistente.getCurso().equals(cursoNuevo)) {
                        estExistente.getCurso().getEstudiantes().remove(estExistente);
                        // Aquí se debería llamar a cursoDAO.actualizar(estExistente.getCurso()) si la lista de estudiantes del curso es persistida por CursoDAO de forma robusta
                    }
                    if (!cursoNuevo.getEstudiantes().contains(estudianteActualizado)) {
                         cursoNuevo.getEstudiantes().add(estudianteActualizado); // Asegurar que esté en la lista del nuevo curso
                         // Aquí se debería llamar a cursoDAO.actualizar(cursoNuevo)
                    }
                 }
                 estudianteActualizado.setCurso(cursoNuevo); // Asegurar que el estudiante tenga la referencia completa del curso
            } else if (estExistente.getCurso() != null) { // Se desasignó el curso
                estExistente.getCurso().getEstudiantes().remove(estExistente);
                 // Aquí se debería llamar a cursoDAO.actualizar(estExistente.getCurso())
            }

            estudianteDAO.actualizar(estudianteActualizado);
            System.out.println("Estudiante con código " + estudianteActualizado.getCodigo() + " actualizado.");
        } else {
            System.out.println("Error: Estudiante con código " + estudianteActualizado.getCodigo() + " no encontrado para actualizar.");
        }
    }

    public void eliminarEstudiante(int codigo) {
        Estudiante est = buscarEstudiante(codigo);
        if (est != null) {
            this.personas.remove(est);
            if (est.getCurso() != null) {
                Curso cursoDelEstudiante = buscarCurso(est.getCurso().getGrado(), est.getCurso().getGrupo());
                if(cursoDelEstudiante != null) {
                    cursoDelEstudiante.getEstudiantes().removeIf(e -> e.getCodigo() == codigo);
                    // Aquí se debería llamar a cursoDAO.actualizar(cursoDelEstudiante) para persistir la eliminación del estudiante de la lista del curso
                    System.out.println("Estudiante " + codigo + " eliminado de la lista en memoria del curso " + cursoDelEstudiante.getGrado() + "-" + cursoDelEstudiante.getGrupo());
                }
            }
            estudianteDAO.eliminar(codigo);
            System.out.println("Estudiante con código " + codigo + " eliminado del sistema.");
        } else {
            System.out.println("Error: Estudiante con código " + codigo + " no encontrado para eliminar.");
        }
    }

    public Profesor buscarProfesor(int codigo) {
        return this.personas.stream()
            .filter(p -> p instanceof Profesor)
            .map(p -> (Profesor) p)
            .filter(pr -> pr.getCodigo() == codigo)
            .findFirst()
            .orElse(null);
    }

    public void actualizarProfesor(Profesor profesorActualizado) {
        Profesor profExistente = buscarProfesor(profesorActualizado.getCodigo());
        if (profExistente != null) {
            int index = this.personas.indexOf(profExistente);
            if (index != -1) {
                this.personas.set(index, profesorActualizado);
            }
            // Si el profesor estaba asignado a un curso, y el curso nuevo es diferente o nulo, desasignar del viejo.
            if (profExistente.getCurso() != null && (profesorActualizado.getCurso() == null || !profesorActualizado.getCurso().equals(profExistente.getCurso()))) {
                 Curso cursoAnterior = buscarCurso(profExistente.getCurso().getGrado(), profExistente.getCurso().getGrupo());
                 if (cursoAnterior != null) {
                    cursoAnterior.setProfesor(null);
                    cursoDAO.actualizar(cursoAnterior); // Persistir que el curso ya no tiene este profesor
                 }
            }
            // Si se asigna un nuevo curso al profesor
            if (profesorActualizado.getCurso() != null) {
                 Curso cursoNuevo = buscarCurso(profesorActualizado.getCurso().getGrado(), profesorActualizado.getCurso().getGrupo());
                 if (cursoNuevo != null) {
                    cursoNuevo.setProfesor(profesorActualizado); // Asignar en memoria
                    cursoDAO.asignarProfesorACurso(profesorActualizado.getCodigo(), cursoNuevo.getGrado(), cursoNuevo.getGrupo()); // Persistir
                 }
            }
            profesorDAO.actualizar(profesorActualizado);
            System.out.println("Profesor con código " + profesorActualizado.getCodigo() + " actualizado.");
        } else {
            System.out.println("Error: Profesor con código " + profesorActualizado.getCodigo() + " no encontrado para actualizar.");
        }
    }

    public void eliminarProfesor(int codigo) {
        Profesor prof = buscarProfesor(codigo);
        if (prof != null) {
            this.personas.remove(prof);
            // Si el profesor estaba asignado a un curso, desasignarlo.
            if (prof.getCurso() != null) {
                Curso cursoAsignado = buscarCurso(prof.getCurso().getGrado(), prof.getCurso().getGrupo());
                if (cursoAsignado != null) {
                    cursoAsignado.setProfesor(null);
                    // Persistir el cambio en el curso (que ya no tiene este profesor)
                    cursoDAO.actualizar(cursoAsignado); // Asume que actualizar el curso guarda el estado del profesor (null)
                }
            }
            profesorDAO.eliminar(codigo);
            System.out.println("Profesor con código " + codigo + " eliminado.");
        } else {
            System.out.println("Error: Profesor con código " + codigo + " no encontrado para eliminar.");
        }
    }

    public Curso buscarCurso(int grado, int grupo) {
        return this.cursos.stream()
            .filter(c -> c.getGrado() == grado && c.getGrupo() == grupo)
            .findFirst()
            .orElse(null);
    }

    public void actualizarCurso(Curso cursoActualizado) {
        Curso cursoExistente = buscarCurso(cursoActualizado.getGrado(), cursoActualizado.getGrupo());
        if (cursoExistente != null) {
            int index = this.cursos.indexOf(cursoExistente);
            if (index != -1) {
                this.cursos.set(index, cursoActualizado); // Actualiza en memoria
            }
            // Las relaciones con profesor y estudiantes se manejan por separado o se asume que cursoActualizado ya las tiene.
            // CursoDAOImpl.actualizar persiste el profesor_codigo y estudiante_codigos.
            cursoDAO.actualizar(cursoActualizado);
            System.out.println("Curso " + cursoActualizado.getGrado() + "-" + cursoActualizado.getGrupo() + " actualizado.");
        } else {
            System.out.println("Error: Curso " + cursoActualizado.getGrado() + "-" + cursoActualizado.getGrupo() + " no encontrado para actualizar.");
        }
    }

    public void eliminarCurso(int grado, int grupo) {
        Curso cursoAEliminar = buscarCurso(grado, grupo);
        if (cursoAEliminar != null) {
            // Desvincular profesor del curso
            if (cursoAEliminar.getProfesor() != null) {
                Profesor profesor = buscarProfesor(cursoAEliminar.getProfesor().getCodigo());
                if (profesor != null) {
                    profesor.setCurso(null);
                    profesorDAO.actualizar(profesor); // Persistir profesor sin curso
                }
            }
            // Desvincular estudiantes del curso
            if (cursoAEliminar.getEstudiantes() != null) {
                for (Estudiante est : new ArrayList<>(cursoAEliminar.getEstudiantes())) { // Copia para evitar ConcurrentModificationException
                    Estudiante estudianteCompleto = buscarEstudiante(est.getCodigo());
                    if (estudianteCompleto != null) {
                        estudianteCompleto.setCurso(null);
                        estudianteDAO.actualizar(estudianteCompleto); // Persistir estudiante sin curso
                    }
                }
            }
            this.cursos.remove(cursoAEliminar); // Eliminar de la lista en memoria
            cursoDAO.eliminar(grado, grupo); // Eliminar del CSV
            System.out.println("Curso " + grado + "-" + grupo + " eliminado.");
        } else {
            System.out.println("Error: Curso " + grado + "-" + grupo + " no encontrado para eliminar.");
        }
    }

    public Asignatura buscarAsignatura(String nombre) {
        return this.asignaturas.stream()
            .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElse(null);
    }

    public void actualizarAsignatura(Asignatura asignaturaActualizada) {
        Asignatura asigExistente = buscarAsignatura(asignaturaActualizada.getNombre());
        if (asigExistente != null) {
            int index = this.asignaturas.indexOf(asigExistente);
            if (index != -1) {
                this.asignaturas.set(index, asignaturaActualizada);
            }
            // AsignaturaDAOImpl.actualizar persiste calificaciones.
            asignaturaDAO.actualizar(asignaturaActualizada);
            System.out.println("Asignatura '" + asignaturaActualizada.getNombre() + "' actualizada.");
        } else {
            System.out.println("Error: Asignatura '" + asignaturaActualizada.getNombre() + "' no encontrada para actualizar.");
        }
    }

    public void eliminarAsignatura(String nombre) {
        Asignatura asigAEliminar = buscarAsignatura(nombre);
        if (asigAEliminar != null) {
            this.asignaturas.remove(asigAEliminar);
            // Eliminar la asignatura de las listas de los estudiantes (en memoria)
            for (Persona p : this.personas) {
                if (p instanceof Estudiante) {
                    Estudiante est = (Estudiante) p;
                    est.getAsignaturas().removeIf(a -> a.getNombre().equalsIgnoreCase(nombre));
                    // Nota: La lista de asignaturas del estudiante no se persiste directamente por EstudianteDAOImpl.
                }
            }
            asignaturaDAO.eliminar(nombre);
            System.out.println("Asignatura '" + nombre + "' eliminada.");
        } else {
            System.out.println("Error: Asignatura '" + nombre + "' no encontrada para eliminar.");
        }
    }

    public void agregarCursoAProfesor(int codigoProfesor, int grado, int grupo) {
        Profesor profesor = buscarProfesor(codigoProfesor);
        Curso curso = buscarCurso(grado, grupo);

        if (profesor != null && curso != null) {
            if (profesor.getCurso() != null && !profesor.getCurso().equals(curso)) {
                 System.out.println("Error: Profesor ya está asignado al curso " + profesor.getCurso().getGrado() + "-" + profesor.getCurso().getGrupo() + ". Desasígnelo primero.");
                return;
            }
            if (curso.getProfesor() != null && !curso.getProfesor().equals(profesor)) {
                 System.out.println("Error: Curso " + curso.getGrado() + "-" + curso.getGrupo() + " ya tiene asignado al profesor " + curso.getProfesor().getCodigo() + ". Desasígnelo primero.");
                return;
            }
            
            profesor.setCurso(curso);
            curso.setProfesor(profesor);
            cursoDAO.asignarProfesorACurso(codigoProfesor, grado, grupo); 
            profesorDAO.actualizar(profesor); // Asegurar que el profesor también se guarde con la referencia al curso
            System.out.println("Profesor " + profesor.getNombre() + " asignado al curso " + curso.getGrado() + "-" + curso.getGrupo());
        } else {
            System.out.println("Error: Profesor o Curso no encontrado para la asignación.");
        }
    }

    public void agregarEstudianteACurso(int codigoEstudiante, int grado, int grupo) {
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        Curso curso = buscarCurso(grado, grupo);

        if (estudiante != null && curso != null) {
            if (estudiante.getCurso() != null && !estudiante.getCurso().equals(curso)) {
                 System.out.println("Error: Estudiante con código " + codigoEstudiante + " ya está asignado al curso " + estudiante.getCurso().getGrado() + "-" + estudiante.getCurso().getGrupo() + ". Desasígnelo primero.");
                return;
            }
             if (estudiante.getCurso() != null && estudiante.getCurso().equals(curso)) {
                 System.out.println("Advertencia: Estudiante con código " + codigoEstudiante + " ya está en el curso " + grado + "-" + grupo + ".");
                 return;
            }
            
            if (!curso.getEstudiantes().stream().anyMatch(e -> e.getCodigo() == estudiante.getCodigo())) {
                 curso.getEstudiantes().add(estudiante);
            }
            estudiante.setCurso(curso);
            cursoDAO.agregarEstudianteACurso(codigoEstudiante, grado, grupo);
            estudianteDAO.actualizar(estudiante); // Asegurar que el estudiante también se guarde con la referencia al curso
            System.out.println("Estudiante " + estudiante.getNombre() + " asignado al curso " + curso.getGrado() + "-" + curso.getGrupo());
        } else {
            System.out.println("Error: Estudiante o Curso no encontrado para la asignación.");
        }
    }
    
    public void desasignarProfesorDeCurso(int codigoProfesor, int grado, int grupo) {
        Profesor profesor = buscarProfesor(codigoProfesor);
        Curso curso = buscarCurso(grado, grupo);

        if (profesor != null && curso != null) {
            if (profesor.getCurso() != null && profesor.getCurso().equals(curso) &&
                curso.getProfesor() != null && curso.getProfesor().equals(profesor)) {
                
                profesor.setCurso(null);
                curso.setProfesor(null);
                
                // Persistir cambios
                // En CursoDAOImpl, asignarProfesorACurso con un código de profesor vacío/nulo podría ser una forma,
                // o una nueva función cursoDAO.desasignarProfesorDeCurso(grado, grupo)
                // Por ahora, actualizamos el curso con profesor nulo.
                cursoDAO.actualizar(curso); 
                profesorDAO.actualizar(profesor);
                System.out.println("Profesor " + profesor.getNombre() + " desasignado del curso " + curso.getGrado() + "-" + curso.getGrupo());
            } else {
                System.out.println("Error: El profesor no está asignado a este curso o viceversa.");
            }
        } else {
            System.out.println("Error: Profesor o Curso no encontrado para la desasignación.");
        }
    }

    public void desasignarEstudianteDeCurso(int codigoEstudiante, int grado, int grupo) {
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        Curso curso = buscarCurso(grado, grupo);

        if (estudiante != null && curso != null) {
            if (estudiante.getCurso() != null && estudiante.getCurso().equals(curso) &&
                curso.getEstudiantes().stream().anyMatch(e -> e.getCodigo() == codigoEstudiante)) {
                
                estudiante.setCurso(null);
                curso.getEstudiantes().removeIf(e -> e.getCodigo() == codigoEstudiante);
                
                // Persistir cambios
                // CursoDAOImpl.agregarEstudianteACurso podría necesitar una contraparte como removerEstudianteDeCurso
                // O actualizar el curso con la lista de estudiantes modificada.
                cursoDAO.actualizar(curso); 
                estudianteDAO.actualizar(estudiante);
                System.out.println("Estudiante " + estudiante.getNombre() + " desasignado del curso " + curso.getGrado() + "-" + curso.getGrupo());
            } else {
                System.out.println("Error: El estudiante no está asignado a este curso.");
            }
        } else {
            System.out.println("Error: Estudiante o Curso no encontrado para la desasignación.");
        }
    }


    public void agregarAsignaturaAEstudiante(int codigoEstudiante, String nombreAsignatura) {
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        Asignatura asignatura = buscarAsignatura(nombreAsignatura);

        if (estudiante != null && asignatura != null) {
            boolean alreadyHas = estudiante.getAsignaturas().stream()
                                    .anyMatch(a -> a.getNombre().equalsIgnoreCase(nombreAsignatura));
            if (!alreadyHas) {
                estudiante.getAsignaturas().add(asignatura); // Solo en memoria
                System.out.println("Asignatura " + nombreAsignatura + " asignada (en memoria) al estudiante " + estudiante.getNombre() + ".");
                // Nota: EstudianteDAOImpl no persiste la lista de asignaturas del estudiante.
            } else {
                System.out.println("El estudiante " + estudiante.getNombre() + " ya tiene la asignatura " + nombreAsignatura + ".");
            }
        } else {
            System.out.println("Error: Estudiante o Asignatura no encontrado para la asignación.");
        }
    }

    public void desasignarAsignaturaDeEstudiante(int codigoEstudiante, String nombreAsignatura) {
        Estudiante estudiante = buscarEstudiante(codigoEstudiante);
        Asignatura asignatura = buscarAsignatura(nombreAsignatura); // Para validar que la asignatura existe

        if (estudiante != null && asignatura != null) {
            boolean removed = estudiante.getAsignaturas().removeIf(a -> a.getNombre().equalsIgnoreCase(nombreAsignatura));
            if (removed) {
                System.out.println("Asignatura " + nombreAsignatura + " desasignada (en memoria) del estudiante " + estudiante.getNombre() + ".");
                 // Nota: EstudianteDAOImpl no persiste la lista de asignaturas del estudiante.
            } else {
                System.out.println("El estudiante " + estudiante.getNombre() + " no tenía la asignatura " + nombreAsignatura + ".");
            }
        } else {
            System.out.println("Error: Estudiante o Asignatura no encontrado para la desasignación.");
        }
    }
    
    public void agregarCalificacion(String nombreAsignatura, int codigoEstudiante, String nombreCalificacion, double nota, int periodo, String fechaStr) {
        Asignatura asignatura = buscarAsignatura(nombreAsignatura);
        Estudiante estudiante = buscarEstudiante(codigoEstudiante); // Se usa para validar que el estudiante existe

        if (asignatura == null) {
            System.out.println("Asignatura '" + nombreAsignatura + "' no encontrada.");
            return;
        }
        if (estudiante == null) { // Aunque la calificación se guarda en la asignatura, validamos que el estudiante exista.
            System.out.println("Estudiante con código " + codigoEstudiante + " no encontrado. No se puede registrar calificación sin un estudiante válido en el sistema.");
            return;
        }
        
        // Opcional: Validar si el estudiante tiene la asignatura asignada (en memoria)
        // if (!estudiante.getAsignaturas().stream().anyMatch(a -> a.getNombre().equalsIgnoreCase(nombreAsignatura))) {
        //     System.out.println("Advertencia: El estudiante " + estudiante.getNombre() + " no tiene la asignatura " + nombreAsignatura + " asignada (en memoria). La calificación se registrará en la asignatura de todas formas.");
        // }


        try {
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr); // Assuming yyyy-MM-dd
            Calificacion nuevaCalificacion = new Calificacion(nombreCalificacion, nota, periodo, fecha);
            
            if (asignatura.getCalificaciones() == null) {
                asignatura.setCalificaciones(new ArrayList<>());
            }
            asignatura.getCalificaciones().add(nuevaCalificacion);
            asignaturaDAO.actualizar(asignatura); 
            System.out.println("Calificación '" + nombreCalificacion + "' ("+nota+") agregada a la asignatura '" + nombreAsignatura + "'.");

        } catch (java.time.format.DateTimeParseException e) {
            System.err.println("Error al parsear la fecha '" + fechaStr + "'. Use el formato YYYY-MM-DD. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al agregar calificación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Reportes ---
    public String reporteEstudiante(int codigoEstudiante) {
        Estudiante est = buscarEstudiante(codigoEstudiante);
        if (est == null) {
            return "Estudiante con código " + codigoEstudiante + " no encontrado.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- Reporte del Estudiante ---\n");
        sb.append("Código: ").append(est.getCodigo()).append("\n");
        sb.append("Nombre: ").append(est.getNombre()).append("\n");
        sb.append("Edad: ").append(est.getEdad()).append("\n");
        sb.append("Cédula: ").append(est.getCedula()).append("\n");
        sb.append("Tipo: ").append(est.getTipo()).append("\n");

        if (est.getCurso() != null) {
            sb.append("Curso: ").append(est.getCurso().getGrado()).append("-").append(est.getCurso().getGrupo()).append("\n");
        } else {
            sb.append("Curso: No asignado\n");
        }
        sb.append("Asignaturas en Memoria (pueden no estar persistidas en EstudianteDAO):\n");
        if (est.getAsignaturas() != null && !est.getAsignaturas().isEmpty()) {
            for (Asignatura asig : est.getAsignaturas()) {
                sb.append("  - ").append(asig.getNombre()).append("\n");
                // Buscar calificaciones para esta asignatura
                Asignatura asignaturaCompleta = buscarAsignatura(asig.getNombre());
                if (asignaturaCompleta != null && asignaturaCompleta.getCalificaciones() != null && !asignaturaCompleta.getCalificaciones().isEmpty()){
                    sb.append("    Calificaciones en ").append(asignaturaCompleta.getNombre()).append(":\n");
                    for(Calificacion cal : asignaturaCompleta.getCalificaciones()){
                        // Idealmente, las calificaciones deberían filtrarse por estudiante si estuvieran asociadas directamente.
                        // Como están en la Asignatura, se listan todas las de la asignatura.
                        // Una mejora sería Calificacion(nombre, nota, periodo, fecha, codigoEstudiante)
                        sb.append("      * ").append(cal.getNombre()).append(": ").append(cal.getNota())
                          .append(" (P").append(cal.getPeriodo()).append(", F:").append(cal.getFecha()).append(")\n");
                    }
                } else {
                     sb.append("    (Sin calificaciones registradas en la asignatura ").append(asig.getNombre()).append(")\n");
                }
            }
        } else {
            sb.append("  (Sin asignaturas en memoria)\n");
        }
        return sb.toString();
    }

    public String infoCurso(int grado, int grupo) {
        Curso curso = buscarCurso(grado, grupo);
        if (curso == null) {
            return "Curso " + grado + "-" + grupo + " no encontrado.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- Información del Curso ---\n");
        sb.append("Grado: ").append(curso.getGrado()).append("\n");
        sb.append("Grupo: ").append(curso.getGrupo()).append("\n");

        if (curso.getProfesor() != null) {
            sb.append("Profesor: ").append(curso.getProfesor().getNombre()).append(" (Código: ").append(curso.getProfesor().getCodigo()).append(")\n");
        } else {
            sb.append("Profesor: No asignado\n");
        }
        sb.append("Estudiantes (" + curso.getEstudiantes().size() + "):\n");
        if (curso.getEstudiantes() != null && !curso.getEstudiantes().isEmpty()) {
            for (Estudiante est : curso.getEstudiantes()) {
                sb.append("  - ").append(est.getNombre()).append(" (Código: ").append(est.getCodigo()).append(")\n");
            }
        } else {
            sb.append("  (Sin estudiantes asignados)\n");
        }
        // Asignaturas del curso no están directamente en el modelo Curso, se infieren de los estudiantes o se definen a nivel de grado.
        // Para este reporte, no se listan asignaturas específicas del curso.
        return sb.toString();
    }


    @Override
    public String toString() {
        return "Colegio [nombre=" + nombre + ", personas=" + (personas != null ? personas.size() : 0) +
               ", cursos=" + (cursos != null ? cursos.size() : 0) +
               ", asignaturas=" + (asignaturas != null ? asignaturas.size() : 0) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colegio colegio = (Colegio) o;
        return Objects.equals(nombre, colegio.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    // --- Métodos de listado para GUI (retornan String para JTextArea) ---
    public String listarTodosLosEstudiantesParaGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Lista de Todos los Estudiantes ---\n");
        if (personas == null || personas.stream().noneMatch(p -> p instanceof Estudiante)) {
            sb.append("No hay estudiantes registrados.\n");
            return sb.toString();
        }
        personas.stream()
                .filter(p -> p instanceof Estudiante)
                .map(p -> (Estudiante) p)
                .forEach(e -> sb.append(e.toString())
                                .append(e.getCurso() != null ? " - Curso: " + e.getCurso().getGrado() + "-" + e.getCurso().getGrupo() : " - Sin curso")
                                .append("\n"));
        return sb.toString();
    }

    public String listarTodosLosProfesoresParaGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Lista de Todos los Profesores ---\n");
         if (personas == null || personas.stream().noneMatch(p -> p instanceof Profesor)) {
            sb.append("No hay profesores registrados.\n");
            return sb.toString();
        }
        personas.stream()
                .filter(p -> p instanceof Profesor)
                .map(p -> (Profesor) p)
                .forEach(pr -> sb.append(pr.toString())
                                 .append(pr.getCurso() != null ? " - Curso Asignado: " + pr.getCurso().getGrado() + "-" + pr.getCurso().getGrupo() : " - Sin curso asignado")
                                 .append("\n"));
        return sb.toString();
    }

    public String listarTodosLosCursosParaGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Lista de Todos los Cursos ---\n");
        if (cursos == null || cursos.isEmpty()) {
            sb.append("No hay cursos registrados.\n");
            return sb.toString();
        }
        for (Curso curso : cursos) {
            sb.append("Curso: ").append(curso.getGrado()).append("-").append(curso.getGrupo());
            if (curso.getProfesor() != null) {
                sb.append(" - Profesor: ").append(curso.getProfesor().getNombre());
            } else {
                sb.append(" - Profesor: (No asignado)");
            }
            sb.append(" - Estudiantes: ").append(curso.getEstudiantes() != null ? curso.getEstudiantes().size() : 0).append("\n");
            if (curso.getEstudiantes() != null && !curso.getEstudiantes().isEmpty()) {
                 curso.getEstudiantes().forEach(e -> sb.append("    - ").append(e.getNombre()).append(" (Cód: ").append(e.getCodigo()).append(")\n"));
            }
        }
        return sb.toString();
    }
    
    public String listarTodasLasAsignaturasParaGUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Lista de Todas las Asignaturas ---\n");
        if (asignaturas == null || asignaturas.isEmpty()) {
            sb.append("No hay asignaturas registradas.\n");
            return sb.toString();
        }
        asignaturas.forEach(a -> {
            sb.append("Asignatura: ").append(a.getNombre()).append("\n");
            if (a.getCalificaciones() != null && !a.getCalificaciones().isEmpty()) {
                sb.append("  Calificaciones Registradas: ").append(a.getCalificaciones().size()).append("\n");
                 a.getCalificaciones().forEach(cal -> sb.append("    - ").append(cal.getNombre()).append(": ").append(cal.getNota())
                                                     .append(" (P").append(cal.getPeriodo()).append(", F:").append(cal.getFecha()).append(")\n"));
            } else {
                sb.append("  (Sin calificaciones registradas)\n");
            }
        });
        return sb.toString();
    }
}
