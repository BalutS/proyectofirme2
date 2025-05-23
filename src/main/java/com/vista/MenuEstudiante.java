package com.vista;

import com.modelo.Asignatura;
import com.modelo.Calificacion;
import com.modelo.Colegio;
import com.modelo.Curso;
import com.modelo.Estudiante;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MenuEstudiante extends JFrame {

    private Colegio colegio;
    private Estudiante estudianteLogueado;

    // Componentes para identificación
    private JTextField txtCodigoEstudiante;
    private JButton btnCargarInfoEstudiante;
    private JPanel panelIdentificacion;
    private JPanel panelPrincipalEstudiante; // Se habilitará después de la identificación

    // Componentes para Tab "Mi Curso"
    private JTextArea areaInfoCursoEstudiante;

    // Componentes para Tab "Mis Asignaturas y Calificaciones"
    private JList<Asignatura> listaAsignaturasEstudiante;
    private DefaultListModel<Asignatura> listModelAsignaturas;
    private JTextArea areaCalificacionesPorAsignatura;

    // Componentes para Tab "Reporte Académico"
    private JTextArea areaReporteAcademico;
    private JButton btnGenerarReporteAcademico;

    public MenuEstudiante() {
        this.colegio = new Colegio("Mi Colegio"); // Nombre del colegio

        setTitle("Portal del Estudiante");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        // Panel de Identificación
        panelIdentificacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelIdentificacion.setBorder(BorderFactory.createTitledBorder("Identificación del Estudiante"));
        txtCodigoEstudiante = new JTextField(10);
        btnCargarInfoEstudiante = new JButton("Ver mi Información");
        panelIdentificacion.add(new JLabel("Ingrese su Código:"));
        panelIdentificacion.add(txtCodigoEstudiante);
        panelIdentificacion.add(btnCargarInfoEstudiante);

        add(panelIdentificacion, BorderLayout.NORTH);

        // Panel Principal (inicialmente no visible o deshabilitado)
        panelPrincipalEstudiante = new JPanel(new BorderLayout());
        panelPrincipalEstudiante.setVisible(false);
        add(panelPrincipalEstudiante, BorderLayout.CENTER);
        
        btnCargarInfoEstudiante.addActionListener(this::accionCargarInfoEstudiante);
    }

    private void inicializarPanelPrincipal() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Mi Curso
        JPanel panelMiCurso = new JPanel(new BorderLayout(10, 10));
        panelMiCurso.setBorder(new EmptyBorder(10,10,10,10));
        areaInfoCursoEstudiante = new JTextArea(5, 30);
        areaInfoCursoEstudiante.setEditable(false);
        panelMiCurso.add(new JScrollPane(areaInfoCursoEstudiante), BorderLayout.CENTER);
        tabbedPane.addTab("Mi Curso", panelMiCurso);

        // Tab 2: Mis Asignaturas y Calificaciones
        JPanel panelMisAsignaturas = new JPanel(new BorderLayout(10, 10));
        panelMisAsignaturas.setBorder(new EmptyBorder(10,10,10,10));
        
        listModelAsignaturas = new DefaultListModel<>();
        listaAsignaturasEstudiante = new JList<>(listModelAsignaturas);
        // Custom cell renderer to display asignatura.getNombre()
        listaAsignaturasEstudiante.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Asignatura) {
                    setText(((Asignatura) value).getNombre());
                }
                return this;
            }
        });

        listaAsignaturasEstudiante.addListSelectionListener(this::accionSeleccionarAsignatura);
        JScrollPane scrollListaAsignaturas = new JScrollPane(listaAsignaturasEstudiante);
        scrollListaAsignaturas.setPreferredSize(new Dimension(150, 0)); // Ancho preferido para la lista
        panelMisAsignaturas.add(scrollListaAsignaturas, BorderLayout.WEST);

        areaCalificacionesPorAsignatura = new JTextArea();
        areaCalificacionesPorAsignatura.setEditable(false);
        panelMisAsignaturas.add(new JScrollPane(areaCalificacionesPorAsignatura), BorderLayout.CENTER);
        tabbedPane.addTab("Mis Asignaturas y Calificaciones", panelMisAsignaturas);

        // Tab 3: Reporte Académico
        JPanel panelReporte = new JPanel(new BorderLayout(10,10));
        panelReporte.setBorder(new EmptyBorder(10,10,10,10));
        areaReporteAcademico = new JTextArea();
        areaReporteAcademico.setEditable(false);
        panelReporte.add(new JScrollPane(areaReporteAcademico), BorderLayout.CENTER);
        btnGenerarReporteAcademico = new JButton("Generar/Actualizar Reporte Académico");
        btnGenerarReporteAcademico.addActionListener(this::accionGenerarReporteAcademico);
        JPanel panelBotonReporte = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonReporte.add(btnGenerarReporteAcademico);
        panelReporte.add(panelBotonReporte, BorderLayout.SOUTH);
        tabbedPane.addTab("Reporte Académico", panelReporte);

        panelPrincipalEstudiante.add(tabbedPane, BorderLayout.CENTER);
        panelPrincipalEstudiante.setVisible(true);

        // Cargar datos iniciales
        cargarDatosMiCurso();
        cargarDatosMisAsignaturas();
        cargarReporteAcademico(); // Carga inicial del reporte
    }

    private void accionCargarInfoEstudiante(ActionEvent e) {
        String codigoStr = txtCodigoEstudiante.getText().trim();
        if (codigoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su código de estudiante.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int codigo = Integer.parseInt(codigoStr);
            // Usar el DAO para obtener el estudiante con sus datos completos (incluyendo curso y asignaturas si están resueltas por Colegio)
            this.estudianteLogueado = colegio.buscarEstudiante(codigo); 

            if (this.estudianteLogueado != null) {
                panelIdentificacion.setVisible(false);
                inicializarPanelPrincipal(); // Crear y mostrar el panel principal con tabs
                setTitle("Portal del Estudiante - " + estudianteLogueado.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "Estudiante con código " + codigo + " no encontrado.", "Error de Identificación", JOptionPane.ERROR_MESSAGE);
                panelPrincipalEstudiante.setVisible(false); // Asegurarse que el panel principal esté oculto si falla
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El código del estudiante debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosMiCurso() {
        if (estudianteLogueado == null) return;
        Curso curso = estudianteLogueado.getCurso();
        if (curso != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Información de tu Curso:\n\n");
            sb.append("Grado: ").append(curso.getGrado()).append("\n");
            sb.append("Grupo: ").append(curso.getGrupo()).append("\n");
            if (curso.getProfesor() != null) {
                sb.append("Profesor: ").append(curso.getProfesor().getNombre()).append("\n");
            } else {
                sb.append("Profesor: (No asignado aún)\n");
            }
            areaInfoCursoEstudiante.setText(sb.toString());
        } else {
            areaInfoCursoEstudiante.setText("No estás asignado a ningún curso actualmente.");
        }
    }

    private void cargarDatosMisAsignaturas() {
        if (estudianteLogueado == null) return;
        listModelAsignaturas.clear();
        // Las asignaturas del estudiante se cargan en memoria en Colegio.resolverRelaciones()
        // pero no se persisten directamente en el estudiante.csv.
        // Para este menú, asumimos que la lista estudianteLogueado.getAsignaturas() se llena
        // al cargar los datos del colegio o mediante la lógica de MenuAdmin (agregarAsignaturaAEstudiante).
        // Si no hay asignaturas directamente en el objeto Estudiante, debemos obtenerlas del curso
        // o de una lista general de asignaturas que el estudiante "debería" tener.
        // Por ahora, confiamos en que estudianteLogueado.getAsignaturas() tiene algo si se asignaron en MenuAdmin.
        
        // Si la lista de asignaturas del estudiante está vacía, podríamos popularla con todas las del colegio
        // o con las asignaturas asociadas al grado del curso del estudiante (requeriría más lógica).
        // Por simplicidad, si la lista está vacía, indicamos que no hay.
        List<Asignatura> asignaturasDelEstudiante = estudianteLogueado.getAsignaturas();
        if (asignaturasDelEstudiante != null && !asignaturasDelEstudiante.isEmpty()) {
            for (Asignatura asig : asignaturasDelEstudiante) {
                listModelAsignaturas.addElement(asig);
            }
        } else {
             // Si no tiene asignaturas directas, podríamos mostrar todas las del colegio como referencia
            areaCalificacionesPorAsignatura.setText("No tienes asignaturas registradas directamente.\nConsidera que las calificaciones se ven por asignatura general del colegio.");
            List<Asignatura> todasLasAsignaturas = colegio.getAsignaturas();
            if (todasLasAsignaturas != null) {
                for (Asignatura asig : todasLasAsignaturas) {
                    listModelAsignaturas.addElement(asig); // Mostrar todas para que pueda ver calificaciones
                }
            }
        }

        if (listModelAsignaturas.isEmpty()) {
             areaCalificacionesPorAsignatura.setText("No hay asignaturas disponibles en el sistema para mostrar.");
        } else {
             listaAsignaturasEstudiante.setSelectedIndex(0); // Seleccionar la primera para mostrar sus calificaciones
        }
    }

    private void accionSeleccionarAsignatura(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && listaAsignaturasEstudiante.getSelectedValue() != null) {
            mostrarCalificacionesDeAsignaturaSeleccionada();
        }
    }

    private void mostrarCalificacionesDeAsignaturaSeleccionada() {
        Asignatura asignaturaSeleccionadaEnLista = listaAsignaturasEstudiante.getSelectedValue();
        if (asignaturaSeleccionadaEnLista == null) {
            areaCalificacionesPorAsignatura.setText("Selecciona una asignatura para ver tus calificaciones.");
            return;
        }

        // Obtener la instancia completa de la asignatura desde el colegio para asegurar que tenemos todas las calificaciones
        Asignatura asignaturaCompleta = colegio.buscarAsignatura(asignaturaSeleccionadaEnLista.getNombre());

        if (asignaturaCompleta == null || asignaturaCompleta.getCalificaciones() == null || asignaturaCompleta.getCalificaciones().isEmpty()) {
            areaCalificacionesPorAsignatura.setText("No hay calificaciones registradas para " + asignaturaSeleccionadaEnLista.getNombre() + ".");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Calificaciones en ").append(asignaturaCompleta.getNombre()).append(":\n\n");
        boolean encontradasParaEstudiante = false;
        for (Calificacion cal : asignaturaCompleta.getCalificaciones()) {
            // IMPORTANTE: El modelo Calificacion actual no tiene un ID de estudiante.
            // Por lo tanto, no podemos filtrar las calificaciones *solo* para el estudianteLogueado.
            // Mostraremos todas las calificaciones de la asignatura.
            // Una mejora sería que Calificacion tuviera un `codigoEstudiante`.
            sb.append("Nombre: ").append(cal.getNombre()).append("\n");
            sb.append("Nota: ").append(cal.getNota()).append("\n");
            sb.append("Periodo: ").append(cal.getPeriodo()).append("\n");
            sb.append("Fecha: ").append(cal.getFecha() != null ? cal.getFecha().toString() : "N/A").append("\n");
            sb.append("----------------------------\n");
            encontradasParaEstudiante = true; // Asumimos que son relevantes si están en la asignatura
        }

        if (!encontradasParaEstudiante) {
             areaCalificacionesPorAsignatura.setText("No se encontraron calificaciones para ti en " + asignaturaCompleta.getNombre() + ".");
        } else {
            areaCalificacionesPorAsignatura.setText(sb.toString());
        }
    }
    
    private void accionGenerarReporteAcademico(ActionEvent e) {
        cargarReporteAcademico();
    }

    private void cargarReporteAcademico() {
        if (estudianteLogueado != null) {
            // Asumimos que el método `reporteAcademico()` en Estudiante existe y genera el String.
            // Si no existe, necesitamos construirlo aquí o en `Colegio.java`.
            // String reporte = estudianteLogueado.reporteAcademico(); 
            // Por ahora, usaremos el `colegio.reporteEstudiante()` que ya hicimos.
            String reporte = colegio.reporteEstudiante(estudianteLogueado.getCodigo());
            areaReporteAcademico.setText(reporte);
        } else {
            areaReporteAcademico.setText("No se ha cargado la información del estudiante.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuEstudiante menu = new MenuEstudiante();
            menu.setVisible(true);
        });
    }
}
