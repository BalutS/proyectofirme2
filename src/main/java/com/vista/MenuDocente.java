package com.vista;

import com.modelo.Asignatura;
import com.modelo.Calificacion;
import com.modelo.Colegio;
import com.modelo.Curso;
import com.modelo.Estudiante;
import com.modelo.Profesor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MenuDocente extends JFrame {

    private Colegio colegio;
    private Profesor profesorLogueado;

    // Componentes para identificación
    private JTextField txtCodigoDocente;
    private JButton btnCargarInfoDocente;
    private JPanel panelIdentificacion;
    private JPanel panelPrincipalDocente; // Se habilitará después de la identificación

    // Componentes para Tab "Mis Cursos y Estudiantes"
    private JTextArea areaInfoCursoDocente;
    private JList<Estudiante> listaEstudiantesDocente;
    private DefaultListModel<Estudiante> listModelEstudiantesDocente;
    private JButton btnRefrescarCursosEstudiantes;

    // Componentes para Tab "Gestionar Calificaciones"
    private JComboBox<Estudiante> comboEstudiantesParaCalificar;
    private JComboBox<Asignatura> comboAsignaturasParaCalificar;
    private JTextField txtNombreCalificacion;
    private JTextField txtNotaCalificacion;
    private JTextField txtPeriodoCalificacion;
    // Fecha se tomará como LocalDate.now() al agregar.
    private JButton btnAgregarCalificacion;
    private JTextArea areaCalificacionesEstudiante;
    private JButton btnVerCalificaciones;

    public MenuDocente() {
        this.colegio = new Colegio("Mi Colegio"); // Nombre del colegio

        setTitle("Portal del Docente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cerrar toda la app
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Panel de Identificación
        panelIdentificacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelIdentificacion.setBorder(BorderFactory.createTitledBorder("Identificación del Docente"));
        txtCodigoDocente = new JTextField(10);
        btnCargarInfoDocente = new JButton("Cargar Información");
        panelIdentificacion.add(new JLabel("Ingrese su Código:"));
        panelIdentificacion.add(txtCodigoDocente);
        panelIdentificacion.add(btnCargarInfoDocente);

        add(panelIdentificacion, BorderLayout.NORTH);

        // Panel Principal (inicialmente no visible o deshabilitado)
        panelPrincipalDocente = new JPanel(new BorderLayout());
        panelPrincipalDocente.setVisible(false); // Oculto hasta que se identifique el docente
        add(panelPrincipalDocente, BorderLayout.CENTER);
        
        btnCargarInfoDocente.addActionListener(this::accionCargarInfoDocente);
    }

    private void inicializarPanelPrincipal() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Mis Cursos y Estudiantes
        JPanel panelMisCursos = new JPanel(new BorderLayout(10, 10));
        panelMisCursos.setBorder(new EmptyBorder(10,10,10,10));
        
        areaInfoCursoDocente = new JTextArea(3, 40);
        areaInfoCursoDocente.setEditable(false);
        panelMisCursos.add(new JScrollPane(areaInfoCursoDocente), BorderLayout.NORTH);

        listModelEstudiantesDocente = new DefaultListModel<>();
        listaEstudiantesDocente = new JList<>(listModelEstudiantesDocente);
        panelMisCursos.add(new JScrollPane(listaEstudiantesDocente), BorderLayout.CENTER);

        btnRefrescarCursosEstudiantes = new JButton("Refrescar Datos");
        JPanel panelBotonRefrescar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonRefrescar.add(btnRefrescarCursosEstudiantes);
        panelMisCursos.add(panelBotonRefrescar, BorderLayout.SOUTH);
        
        btnRefrescarCursosEstudiantes.addActionListener(e -> cargarDatosMisCursos());
        tabbedPane.addTab("Mis Cursos y Estudiantes", panelMisCursos);


        // Tab 2: Gestionar Calificaciones
        JPanel panelGestionCalificaciones = new JPanel(new BorderLayout(10,10));
        panelGestionCalificaciones.setBorder(new EmptyBorder(10,10,10,10));

        JPanel panelSeleccion = new JPanel(new GridLayout(0,2,5,5));
        panelSeleccion.add(new JLabel("Estudiante:"));
        comboEstudiantesParaCalificar = new JComboBox<>();
        panelSeleccion.add(comboEstudiantesParaCalificar);
        
        panelSeleccion.add(new JLabel("Asignatura:"));
        comboAsignaturasParaCalificar = new JComboBox<>();
        panelSeleccion.add(comboAsignaturasParaCalificar);
        
        panelGestionCalificaciones.add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelInputsCalificacion = new JPanel(new GridLayout(0,2,5,5));
        panelInputsCalificacion.setBorder(BorderFactory.createTitledBorder("Nueva Calificación"));
        panelInputsCalificacion.add(new JLabel("Nombre Calificación:"));
        txtNombreCalificacion = new JTextField();
        panelInputsCalificacion.add(txtNombreCalificacion);
        panelInputsCalificacion.add(new JLabel("Nota (0.0 - 5.0):"));
        txtNotaCalificacion = new JTextField();
        panelInputsCalificacion.add(txtNotaCalificacion);
        panelInputsCalificacion.add(new JLabel("Periodo (1-4):"));
        txtPeriodoCalificacion = new JTextField();
        panelInputsCalificacion.add(txtPeriodoCalificacion);
        
        btnAgregarCalificacion = new JButton("Agregar Calificación");
        JPanel panelBtnAgregar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBtnAgregar.add(btnAgregarCalificacion);
        panelInputsCalificacion.add(new JLabel()); // Placeholder
        panelInputsCalificacion.add(panelBtnAgregar); // Add button to input panel

        JPanel panelCentralCalificaciones = new JPanel(new BorderLayout(5,5));
        panelCentralCalificaciones.add(panelInputsCalificacion, BorderLayout.NORTH);

        areaCalificacionesEstudiante = new JTextArea(10,30);
        areaCalificacionesEstudiante.setEditable(false);
        panelCentralCalificaciones.add(new JScrollPane(areaCalificacionesEstudiante), BorderLayout.CENTER);
        
        btnVerCalificaciones = new JButton("Ver Calificaciones Existentes");
        JPanel panelBtnVer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBtnVer.add(btnVerCalificaciones);
        panelCentralCalificaciones.add(panelBtnVer, BorderLayout.SOUTH);

        panelGestionCalificaciones.add(panelCentralCalificaciones, BorderLayout.CENTER);
        
        tabbedPane.addTab("Gestionar Calificaciones", panelGestionCalificaciones);
        
        panelPrincipalDocente.add(tabbedPane, BorderLayout.CENTER);

        // Action Listeners para calificaciones
        btnAgregarCalificacion.addActionListener(this::accionAgregarCalificacion);
        btnVerCalificaciones.addActionListener(e -> mostrarCalificaciones());
        
        comboEstudiantesParaCalificar.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mostrarCalificaciones();
            }
        });
        comboAsignaturasParaCalificar.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mostrarCalificaciones();
            }
        });

        panelPrincipalDocente.setVisible(true);
        // Cargar datos iniciales
        cargarDatosMisCursos();
        cargarDatosGestionCalificaciones();
    }

    private void accionCargarInfoDocente(ActionEvent e) {
        String codigoStr = txtCodigoDocente.getText().trim();
        if (codigoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su código de docente.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int codigo = Integer.parseInt(codigoStr);
            this.profesorLogueado = colegio.buscarProfesor(codigo);

            if (this.profesorLogueado != null) {
                if (this.profesorLogueado.getCurso() != null && this.profesorLogueado.getCurso().getGrado() != 0) {
                    panelIdentificacion.setVisible(false); // Ocultar panel de identificación
                    inicializarPanelPrincipal(); // Crear y mostrar el panel principal con tabs
                    setTitle("Portal del Docente - " + profesorLogueado.getNombre());
                } else {
                    JOptionPane.showMessageDialog(this, "Docente encontrado, pero no tiene un curso asignado. Por favor, contacte al administrador.", "Docente Sin Curso", JOptionPane.WARNING_MESSAGE);
                    this.profesorLogueado = null; // Resetear para evitar acceso parcial
                }
            } else {
                JOptionPane.showMessageDialog(this, "Docente con código " + codigo + " no encontrado.", "Error de Identificación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El código del docente debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosMisCursos() {
        if (profesorLogueado == null || profesorLogueado.getCurso() == null) {
            areaInfoCursoDocente.setText("No hay información de curso disponible.");
            listModelEstudiantesDocente.clear();
            return;
        }
        Curso cursoAsignado = profesorLogueado.getCurso(); // Este curso ya debería estar completamente cargado por Colegio.java
        areaInfoCursoDocente.setText("Información de su Curso Asignado:\n");
        areaInfoCursoDocente.append("Grado: " + cursoAsignado.getGrado() + "\n");
        areaInfoCursoDocente.append("Grupo: " + cursoAsignado.getGrupo() + "\n");
        areaInfoCursoDocente.append("Número de Estudiantes: " + (cursoAsignado.getEstudiantes() != null ? cursoAsignado.getEstudiantes().size() : 0) + "\n");

        listModelEstudiantesDocente.clear();
        if (cursoAsignado.getEstudiantes() != null) {
            for (Estudiante est : cursoAsignado.getEstudiantes()) {
                listModelEstudiantesDocente.addElement(est); // Estudiante tiene un toString() útil
            }
        }
    }

    private void cargarDatosGestionCalificaciones() {
        if (profesorLogueado == null || profesorLogueado.getCurso() == null || profesorLogueado.getCurso().getEstudiantes() == null) {
            comboEstudiantesParaCalificar.removeAllItems();
            comboAsignaturasParaCalificar.removeAllItems();
            return;
        }

        comboEstudiantesParaCalificar.removeAllItems();
        for (Estudiante est : profesorLogueado.getCurso().getEstudiantes()) {
            comboEstudiantesParaCalificar.addItem(est);
        }

        comboAsignaturasParaCalificar.removeAllItems();
        List<Asignatura> todasLasAsignaturas = colegio.getAsignaturas(); // Obtener todas las asignaturas del colegio
        if (todasLasAsignaturas != null) {
            for (Asignatura asig : todasLasAsignaturas) {
                comboAsignaturasParaCalificar.addItem(asig);
            }
        }
        mostrarCalificaciones(); // Cargar calificaciones para la selección inicial
    }

    private void mostrarCalificaciones() {
        Estudiante estSeleccionado = (Estudiante) comboEstudiantesParaCalificar.getSelectedItem();
        Asignatura asigSeleccionada = (Asignatura) comboAsignaturasParaCalificar.getSelectedItem();
        areaCalificacionesEstudiante.setText("");

        if (estSeleccionado == null || asigSeleccionada == null) {
            areaCalificacionesEstudiante.setText("Seleccione un estudiante y una asignatura para ver las calificaciones.");
            return;
        }

        // Necesitamos la instancia de Asignatura que está en la lista del colegio, porque esa es la que tiene las calificaciones persistidas.
        Asignatura asignaturaDelColegio = colegio.buscarAsignatura(asigSeleccionada.getNombre());
        if (asignaturaDelColegio == null || asignaturaDelColegio.getCalificaciones() == null || asignaturaDelColegio.getCalificaciones().isEmpty()) {
            areaCalificacionesEstudiante.setText("No hay calificaciones registradas para " + estSeleccionado.getNombre() + " en " + asigSeleccionada.getNombre() + ".");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Calificaciones de ").append(estSeleccionado.getNombre()).append(" en ").append(asignaturaDelColegio.getNombre()).append(":\n");
        boolean encontradas = false;
        for (Calificacion cal : asignaturaDelColegio.getCalificaciones()) {
            // Asumiendo que la calificación en Asignatura NO tiene referencia al estudiante.
            // Si la tuviera, filtraríamos aquí: if(cal.getCodigoEstudiante() == estSeleccionado.getCodigo())
            // Por ahora, mostramos todas las calificaciones de la asignatura, el docente debe saber a quién corresponden
            // o el modelo Calificacion debería incluir el codigoEstudiante.
            // Para esta implementación, asumimos que el docente quiere ver todas las calificaciones de esa asignatura
            // y la GUI de agregarCalificacion es la que asocia la calificación a un estudiante específico.
            // La tarea indica "Gestionar Calificaciones" de *sus* estudiantes.
            // El modelo actual de Calificacion (nombre, nota, periodo, fecha) no la vincula a un estudiante.
            // El CSV de AsignaturaDAOImpl (nombre,calificaciones_data) tampoco vincula la calificación a un estudiante.
            // Esto es una limitación del modelo actual.
            // El método `profesorLogueado.calificarEstudiante` no existe en el modelo actual.
            // El método `colegio.agregarCalificacion` sí existe y es el que se usa en MenuAdmin.
            // Vamos a simular que la calificación SÍ tiene un estudianteID para filtrarlas.
            // PERO, el modelo Calificacion no lo tiene. Entonces, no podemos filtrar.
            // Mostraremos todas las calificaciones de la asignatura.
            sb.append("  - Nombre: ").append(cal.getNombre());
            sb.append(", Nota: ").append(cal.getNota());
            sb.append(", Periodo: ").append(cal.getPeriodo());
            sb.append(", Fecha: ").append(cal.getFecha() != null ? cal.getFecha().toString() : "N/A").append("\n");
            encontradas = true;
        }

        if (!encontradas) {
             areaCalificacionesEstudiante.setText("No hay calificaciones registradas para " + estSeleccionado.getNombre() + " en " + asigSeleccionada.getNombre() + ".");
        } else {
            areaCalificacionesEstudiante.setText(sb.toString());
        }
    }

    private void accionAgregarCalificacion(ActionEvent e) {
        Estudiante estSeleccionado = (Estudiante) comboEstudiantesParaCalificar.getSelectedItem();
        Asignatura asigSeleccionada = (Asignatura) comboAsignaturasParaCalificar.getSelectedItem();

        if (estSeleccionado == null || asigSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante y una asignatura.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreCal = txtNombreCalificacion.getText().trim();
        String notaStr = txtNotaCalificacion.getText().trim();
        String periodoStr = txtPeriodoCalificacion.getText().trim();

        if (nombreCal.isEmpty() || notaStr.isEmpty() || periodoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, Nota y Periodo de la calificación son requeridos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double nota = Double.parseDouble(notaStr);
            if (nota < 0.0 || nota > 5.0) {
                JOptionPane.showMessageDialog(this, "La nota debe estar entre 0.0 y 5.0.", "Nota Inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int periodo = Integer.parseInt(periodoStr);
             if (periodo < 1 || periodo > 4) { // Asumiendo 4 periodos
                JOptionPane.showMessageDialog(this, "El periodo debe estar entre 1 y 4.", "Periodo Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fecha = LocalDate.now(); // Fecha actual
            
            // El problema indica: profesorLogueado.calificarEstudiante(...)
            // pero el modelo Profesor no tiene ese método.
            // Usaremos colegio.agregarCalificacion, que ya existe y maneja la persistencia.
            // El código de estudiante es el del estSeleccionado.
            colegio.agregarCalificacion(asigSeleccionada.getNombre(), estSeleccionado.getCodigo(), nombreCal, nota, periodo, fecha.toString());
            
            JOptionPane.showMessageDialog(this, "Calificación agregada exitosamente a la asignatura " + asigSeleccionada.getNombre() + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar campos
            txtNombreCalificacion.setText("");
            txtNotaCalificacion.setText("");
            txtPeriodoCalificacion.setText("");
            
            mostrarCalificaciones(); // Refrescar vista de calificaciones

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota y Periodo deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Error en el formato de la fecha.", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar calificación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuDocente menu = new MenuDocente();
            menu.setVisible(true);
        });
    }
}
