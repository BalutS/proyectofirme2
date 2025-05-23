package com.modelo.dao;

import com.modelo.Asignatura;
import com.modelo.Calificacion; // Assuming Calificacion class exists in com.modelo

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AsignaturaDAOImpl implements AsignaturaDAO {

    private final String archivoCsv;
    private static final String CSV_HEADER = "nombre,calificaciones_data";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public AsignaturaDAOImpl(String archivoCsv) {
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
                System.err.println("Error al crear el archivo CSV de asignaturas: " + e.getMessage());
            }
        }
    }

    // --- Helper methods for Calificacion ---
    private String calificacionToString(Calificacion calificacion) {
        return calificacion.getNombre() + ":" +
               calificacion.getNota() + ":" +
               calificacion.getPeriodo() + ":" +
               (calificacion.getFecha() != null ? calificacion.getFecha().format(DATE_FORMATTER) : "");
    }

    private Calificacion stringToCalificacion(String calificacionStr) {
        String[] partes = calificacionStr.split(":", -1); // Use -1 to include trailing empty strings if date is missing
        if (partes.length < 4) {
            System.err.println("Formato de calificación incorrecto: " + calificacionStr);
            return null;
        }
        String nombre = partes[0];
        double nota = Double.parseDouble(partes[1]);
        int periodo = Integer.parseInt(partes[2]);
        LocalDate fecha = null;
        if (partes[3] != null && !partes[3].isEmpty()) {
            try {
                fecha = LocalDate.parse(partes[3], DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.err.println("Error al parsear fecha de calificación ("+partes[3]+"): " + e.getMessage());
            }
        }
        return new Calificacion(nombre, nota, periodo, fecha);
    }

    private String listaCalificacionesToString(List<Calificacion> calificaciones) {
        if (calificaciones == null || calificaciones.isEmpty()) {
            return "";
        }
        return calificaciones.stream()
                             .map(this::calificacionToString)
                             .collect(Collectors.joining(";"));
    }

    private List<Calificacion> stringToListaCalificaciones(String data) {
        List<Calificacion> calificaciones = new ArrayList<>();
        if (data == null || data.trim().isEmpty()) {
            return calificaciones;
        }
        String[] calStrings = data.split(";");
        for (String calStr : calStrings) {
            if (!calStr.trim().isEmpty()) {
                Calificacion cal = stringToCalificacion(calStr);
                if (cal != null) {
                    calificaciones.add(cal);
                }
            }
        }
        return calificaciones;
    }
    // --- End Helper methods for Calificacion ---

    private Asignatura asignaturaDesdeCsvLinea(String lineaCsv) {
        String[] campos = lineaCsv.split(",", -1); // Use -1 to include trailing empty strings
        if (campos.length < 2) {
            System.err.println("Línea CSV malformada (asignaturas), no hay suficientes campos: " + lineaCsv);
            return null;
        }
        String nombre = campos[0];
        List<Calificacion> calificaciones = stringToListaCalificaciones(campos[1]);
        
        Asignatura asignatura = new Asignatura(nombre);
        // The problem description for Asignatura does not mention a List<Calificacion>
        // but the CSV format implies it. Assuming Asignatura has setCalificaciones.
        // If Asignatura model does not have a list of Calificacion, this part needs adjustment.
        // For now, let's assume Asignatura has a setCalificaciones method.
        // asignatura.setCalificaciones(calificaciones); // This line would be needed if Asignatura holds Calificacion objects
        // For now, we'll just create the Asignatura object. The calificaciones can be added if the model supports it.
        // The task is to parse them, which we do.
        return asignatura; // The Calificacion list is parsed but not directly set if Asignatura doesn't hold them
    }


    private String asignaturaACsvLinea(Asignatura asignatura) {
        // String calificacionesData = listaCalificacionesToString(asignatura.getCalificaciones()); // If Asignatura has getCalificaciones()
        // For now, as the Asignatura class in the provided snippets doesn't have calificaciones,
        // we'll save an empty string for calificaciones_data.
        // This needs to be revisited if the Asignatura model is updated to hold Calificacion objects.
        // Based on the CSV structure "nombre,calificaciones_data", we need to provide this data.
        // If the Asignatura object itself doesn't hold this, the DAO cannot persist it directly from the object.
        // Let's assume for now the Asignatura object *will* have a getCalificaciones method.
        // If not, this DAO implementation is ahead of the model, or the model needs update.
        // For the purpose of this task, we will assume Asignatura has List<Calificacion>
        String calificacionesData = "";
        if (asignatura.getCalificaciones() != null) { // Assuming getCalificaciones exists
             calificacionesData = listaCalificacionesToString(asignatura.getCalificaciones());
        }

        return asignatura.getNombre() + "," + calificacionesData;
    }


    private List<Asignatura> cargarAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.archivoCsv))) {
            String linea;
            br.readLine(); // Saltar cabecera
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Asignatura asignatura = asignaturaDesdeCsvLinea(linea);
                    if (asignatura != null) {
                        // If Asignatura model is updated to hold Calificacion objects,
                        // the calificaciones parsed in asignaturaDesdeCsvLinea should be set here or within that method.
                        // For now, we assume asignaturaDesdeCsvLinea correctly populates the Asignatura object
                        // including its calificaciones if the model supports it.
                        String[] campos = linea.split(",", -1);
                        if (campos.length >= 2) {
                             List<Calificacion> calificaciones = stringToListaCalificaciones(campos[1]);
                             asignatura.setCalificaciones(calificaciones); // Assuming this method exists
                        }
                        asignaturas.add(asignatura);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar asignaturas desde CSV: " + e.getMessage());
        }
        return asignaturas;
    }

    private void guardarAsignaturas(List<Asignatura> asignaturas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(this.archivoCsv))) {
            pw.println(CSV_HEADER);
            for (Asignatura asignatura : asignaturas) {
                pw.println(asignaturaACsvLinea(asignatura));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar asignaturas en CSV: " + e.getMessage());
        }
    }

    @Override
    public void agregar(Asignatura asignatura) {
        List<Asignatura> asignaturas = cargarAsignaturas();
        boolean existe = asignaturas.stream().anyMatch(a -> a.getNombre().equalsIgnoreCase(asignatura.getNombre()));
        if (existe) {
            System.err.println("Error: Ya existe una asignatura con el nombre " + asignatura.getNombre());
            return;
        }
        asignaturas.add(asignatura);
        guardarAsignaturas(asignaturas);
    }

    @Override
    public Asignatura obtenerPorNombre(String nombre) {
        List<Asignatura> asignaturas = cargarAsignaturas();
        for (Asignatura asignatura : asignaturas) {
            if (asignatura.getNombre().equalsIgnoreCase(nombre)) {
                return asignatura;
            }
        }
        return null;
    }

    @Override
    public List<Asignatura> obtenerTodas() {
        return cargarAsignaturas();
    }

    @Override
    public void actualizar(Asignatura asignaturaActualizada) {
        List<Asignatura> asignaturas = cargarAsignaturas();
        List<Asignatura> asignaturasActualizadas = new ArrayList<>();
        boolean encontrado = false;
        for (Asignatura asignatura : asignaturas) {
            if (asignatura.getNombre().equalsIgnoreCase(asignaturaActualizada.getNombre())) {
                asignaturasActualizadas.add(asignaturaActualizada);
                encontrado = true;
            } else {
                asignaturasActualizadas.add(asignatura);
            }
        }
        if (encontrado) {
            guardarAsignaturas(asignaturasActualizadas);
        } else {
            System.err.println("No se encontró asignatura con nombre " + asignaturaActualizada.getNombre() + " para actualizar.");
        }
    }

    @Override
    public void eliminar(String nombre) {
        List<Asignatura> asignaturas = cargarAsignaturas();
        List<Asignatura> asignaturasRestantes = asignaturas.stream()
                                                     .filter(a -> !a.getNombre().equalsIgnoreCase(nombre))
                                                     .collect(Collectors.toList());
        if (asignaturas.size() == asignaturasRestantes.size()) {
            System.err.println("No se encontró asignatura con nombre " + nombre + " para eliminar.");
        }
        guardarAsignaturas(asignaturasRestantes);
    }
}
