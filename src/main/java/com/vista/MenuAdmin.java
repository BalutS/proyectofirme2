package com.vista;

import com.modelo.Colegio;
import com.modelo.Curso;
import com.modelo.Estudiante;
import com.modelo.Persona; // For casting in listar
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuAdmin extends JFrame {

    private Colegio colegio;

    // Campos de texto para Estudiante
    private JTextField txtCodigoEst, txtNombreEst, txtEdadEst, txtCedulaEst;
    private JTextField txtGradoCursoEst, txtGrupoCursoEst;
    private JComboBox<String> comboTipoEst;
    private JTextArea areaEstudiantes;

    // Botones para Estudiante
    private JButton btnAgregarEst, btnActualizarEst, btnEliminarEst, btnListarEst;
    private JButton btnBuscarEst;

    // --- Componentes para Profesores ---
    private JTextField txtCodigoProf, txtNombreProf, txtEdadProf, txtCedulaProf;
    private JComboBox<String> comboTipoProf;
    private JTextArea areaProfesores;
    private JButton btnAgregarProf, btnActualizarProf, btnEliminarProf, btnListarProf, btnBuscarProf;
    private JTextField txtGradoCursoProf, txtGrupoCursoProf; // Para asignar curso a profesor

    // --- Componentes para Cursos ---
    private JTextField txtGradoCurso, txtGrupoCurso;
    private JTextArea areaCursos;
    private JButton btnAgregarCurso, btnActualizarCurso, btnEliminarCurso, btnListarCursos, btnBuscarCurso;

    // --- Componentes para Asignaturas ---
    private JTextField txtNombreAsig;
    private JTextArea areaAsignaturas;
    private JButton btnAgregarAsig, btnActualizarAsig, btnEliminarAsig, btnListarAsig, btnBuscarAsig;
    // Para calificaciones (dentro de Asignaturas o en Asignaciones)
    private JTextField txtNombreCalificacion, txtNotaCalificacion, txtPeriodoCalificacion, txtFechaCalificacion;
    private JTextField txtCodigoEstCalificacion; // A qué estudiante pertenece la calificación
    private JButton btnAgregarCalificacionAAsignatura;


    // --- Componentes para Asignaciones ---
    // Asignar Profesor a Curso (ya declarados en Profesores, reutilizar o nuevos específicos)
    private JTextField txtProfCodigoAsig, txtCursoGradoAsigProf, txtCursoGrupoAsigProf;
    private JButton btnAsignarProfACurso, btnDesasignarProfDeCurso;

    // Asignar Estudiante a Curso (ya declarados en Estudiantes, reutilizar o nuevos específicos)
    private JTextField txtEstCodigoAsig, txtCursoGradoAsigEst, txtCursoGrupoAsigEst;
    private JButton btnAsignarEstACurso, btnDesasignarEstDeCurso;

    // Asignar Asignatura a Estudiante
    private JTextField txtEstCodigoAsigAsig, txtAsigNombreAsigEst;
    private JButton btnAsignarAsigAEst, btnDesasignarAsigDeEst;
    private JTextArea areaAsignaciones;


    // --- Componentes para Reportes ---
    private JTextField txtEstCodigoReporte;
    private JButton btnGenerarReporteEst;
    private JTextField txtCursoGradoReporte, txtCursoGrupoReporte;
    private JButton btnGenerarInfoCurso;
    private JButton btnListarCursosDetalladoReporte; // Renombrado para evitar confusión
    private JTextArea areaReportes;


    public MenuAdmin() {
        this.colegio = new Colegio("Mi Colegio"); // Initialize Colegio

        setTitle("Administración del Colegio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de Estudiantes
        JPanel panelEstudiantes = new JPanel();
        configurarPanelEstudiantes(panelEstudiantes);
        tabbedPane.addTab("Estudiantes", panelEstudiantes);

        // Panel de Profesores
        JPanel panelProfesores = new JPanel();
        configurarPanelProfesores(panelProfesores); // Implement this method
        tabbedPane.addTab("Profesores", panelProfesores);

        // Panel de Cursos
        JPanel panelCursos = new JPanel();
        configurarPanelCursos(panelCursos); // Implement this method
        tabbedPane.addTab("Cursos", panelCursos);
        
        // Panel de Asignaturas
        JPanel panelAsignaturas = new JPanel();
        configurarPanelAsignaturas(panelAsignaturas); // Implement this method
        tabbedPane.addTab("Asignaturas", panelAsignaturas);

        // Panel de Asignaciones
        JPanel panelAsignaciones = new JPanel();
        configurarPanelAsignaciones(panelAsignaciones); // Implement this method
        tabbedPane.addTab("Asignaciones", panelAsignaciones);

        // Panel de Reportes
        JPanel panelReportes = new JPanel();
        configurarPanelReportes(panelReportes); // Implement this method
        tabbedPane.addTab("Reportes", panelReportes);

        add(tabbedPane);
        refrescarTodasLasListas(); // Cargar datos iniciales en todas las áreas de texto
    }

    private void configurarPanelEstudiantes(JPanel panel) {
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de Entradas (Norte)
        JPanel panelEntradas = new JPanel();
        GroupLayout layout = new GroupLayout(panelEntradas);
        panelEntradas.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblCodigoEst = new JLabel("Código:");
        txtCodigoEst = new JTextField(10);
        JLabel lblNombreEst = new JLabel("Nombre:");
        txtNombreEst = new JTextField(20);
        JLabel lblEdadEst = new JLabel("Edad:");
        txtEdadEst = new JTextField(3);
        JLabel lblCedulaEst = new JLabel("Cédula:");
        txtCedulaEst = new JTextField(10);
        JLabel lblTipoEst = new JLabel("Tipo:");
        comboTipoEst = new JComboBox<>(new String[]{"Estudiante"}); // Solo "Estudiante" por ahora
        comboTipoEst.setEnabled(false); // No editable por ahora
        JLabel lblGradoCursoEst = new JLabel("Grado Curso:");
        txtGradoCursoEst = new JTextField(5);
        JLabel lblGrupoCursoEst = new JLabel("Grupo Curso:");
        txtGrupoCursoEst = new JTextField(5);

        // Layout de Entradas
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblCodigoEst)
                .addComponent(lblNombreEst)
                .addComponent(lblEdadEst)
                .addComponent(lblGradoCursoEst))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtCodigoEst)
                .addComponent(txtNombreEst)
                .addComponent(txtEdadEst)
                .addComponent(txtGradoCursoEst))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblCedulaEst)
                .addComponent(lblTipoEst)
                .addComponent(lblGrupoCursoEst))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtCedulaEst)
                .addComponent(comboTipoEst)
                .addComponent(txtGrupoCursoEst))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblCodigoEst)
                .addComponent(txtCodigoEst)
                .addComponent(lblCedulaEst)
                .addComponent(txtCedulaEst))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNombreEst)
                .addComponent(txtNombreEst)
                .addComponent(lblTipoEst)
                .addComponent(comboTipoEst))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEdadEst)
                .addComponent(txtEdadEst))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblGradoCursoEst)
                .addComponent(txtGradoCursoEst)
                .addComponent(lblGrupoCursoEst)
                .addComponent(txtGrupoCursoEst))
        );
        panel.add(panelEntradas, BorderLayout.NORTH);

        // Panel de Botones (Sur)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAgregarEst = new JButton("Agregar Estudiante");
        btnActualizarEst = new JButton("Actualizar Estudiante");
        btnEliminarEst = new JButton("Eliminar Estudiante");
        btnListarEst = new JButton("Listar Estudiantes");
        btnBuscarEst = new JButton("Buscar Estudiante (para Act/Elim)");

        panelBotones.add(btnAgregarEst);
        panelBotones.add(btnBuscarEst);
        panelBotones.add(btnActualizarEst);
        panelBotones.add(btnEliminarEst);
        panelBotones.add(btnListarEst);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Área de Texto (Centro)
        areaEstudiantes = new JTextArea(15, 50);
        areaEstudiantes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaEstudiantes);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action Listeners
        btnAgregarEst.addActionListener(this::accionAgregarEstudiante);
        btnListarEst.addActionListener(this::accionListarEstudiantes);
        btnActualizarEst.addActionListener(this::accionActualizarEstudiante);
        btnEliminarEst.addActionListener(this::accionEliminarEstudiante);
        btnBuscarEst.addActionListener(this::accionBuscarEstudiante);
    }

    private void configurarPanelProfesores(JPanel panel) {
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de Entradas (Norte)
        JPanel panelEntradas = new JPanel();
        GroupLayout layout = new GroupLayout(panelEntradas);
        panelEntradas.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblCodigo = new JLabel("Código:");
        txtCodigoProf = new JTextField(10);
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombreProf = new JTextField(20);
        JLabel lblEdad = new JLabel("Edad:");
        txtEdadProf = new JTextField(3);
        JLabel lblCedula = new JLabel("Cédula:");
        txtCedulaProf = new JTextField(10);
        JLabel lblTipo = new JLabel("Tipo:");
        comboTipoProf = new JComboBox<>(new String[]{"Docente"});
        comboTipoProf.setEnabled(false);
        JLabel lblGradoCurso = new JLabel("Grado Curso (Opcional):");
        txtGradoCursoProf = new JTextField(5);
        JLabel lblGrupoCurso = new JLabel("Grupo Curso (Opcional):");
        txtGrupoCursoProf = new JTextField(5);


        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblCodigo)
                .addComponent(lblNombre)
                .addComponent(lblEdad)
                .addComponent(lblGradoCurso))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtCodigoProf)
                .addComponent(txtNombreProf)
                .addComponent(txtEdadProf)
                .addComponent(txtGradoCursoProf))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblCedula)
                .addComponent(lblTipo)
                .addComponent(lblGrupoCurso))
            .addGroup(layout.createParallelGroup()
                .addComponent(txtCedulaProf)
                .addComponent(comboTipoProf)
                .addComponent(txtGrupoCursoProf))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblCodigo)
                .addComponent(txtCodigoProf)
                .addComponent(lblCedula)
                .addComponent(txtCedulaProf))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNombre)
                .addComponent(txtNombreProf)
                .addComponent(lblTipo)
                .addComponent(comboTipoProf))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEdad)
                .addComponent(txtEdadProf))
             .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblGradoCurso)
                .addComponent(txtGradoCursoProf)
                .addComponent(lblGrupoCurso)
                .addComponent(txtGrupoCursoProf))
        );
        panel.add(panelEntradas, BorderLayout.NORTH);

        // Panel de Botones (Sur)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAgregarProf = new JButton("Agregar Profesor");
        btnActualizarProf = new JButton("Actualizar Profesor");
        btnEliminarProf = new JButton("Eliminar Profesor");
        btnListarProf = new JButton("Listar Profesores");
        btnBuscarProf = new JButton("Buscar Profesor");

        panelBotones.add(btnAgregarProf);
        panelBotones.add(btnBuscarProf);
        panelBotones.add(btnActualizarProf);
        panelBotones.add(btnEliminarProf);
        panelBotones.add(btnListarProf);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Área de Texto (Centro)
        areaProfesores = new JTextArea(15, 50);
        areaProfesores.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaProfesores);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action Listeners
        btnAgregarProf.addActionListener(this::accionAgregarProfesor);
        btnListarProf.addActionListener(this::accionListarProfesores);
        btnBuscarProf.addActionListener(this::accionBuscarProfesor);
        btnActualizarProf.addActionListener(this::accionActualizarProfesor);
        btnEliminarProf.addActionListener(this::accionEliminarProfesor);
    }

    private void configurarPanelCursos(JPanel panel) {
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelEntradas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblGrado = new JLabel("Grado:");
        txtGradoCurso = new JTextField(5);
        JLabel lblGrupo = new JLabel("Grupo:");
        txtGrupoCurso = new JTextField(5);
        panelEntradas.add(lblGrado);
        panelEntradas.add(txtGradoCurso);
        panelEntradas.add(lblGrupo);
        panelEntradas.add(txtGrupoCurso);
        panel.add(panelEntradas, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregarCurso = new JButton("Agregar Curso");
        btnActualizarCurso = new JButton("Actualizar Curso"); // La actualización de profesor y estudiantes se hará en Asignaciones
        btnEliminarCurso = new JButton("Eliminar Curso");
        btnListarCursos = new JButton("Listar Cursos");
        btnBuscarCurso = new JButton("Buscar Curso");
        panelBotones.add(btnAgregarCurso);
        panelBotones.add(btnBuscarCurso);
        panelBotones.add(btnActualizarCurso);
        panelBotones.add(btnEliminarCurso);
        panelBotones.add(btnListarCursos);
        panel.add(panelBotones, BorderLayout.SOUTH);

        areaCursos = new JTextArea(15, 50);
        areaCursos.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaCursos);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnAgregarCurso.addActionListener(this::accionAgregarCurso);
        btnListarCursos.addActionListener(this::accionListarCursos);
        btnBuscarCurso.addActionListener(this::accionBuscarCurso);
        btnActualizarCurso.addActionListener(this::accionActualizarCurso);
        btnEliminarCurso.addActionListener(this::accionEliminarCurso);
    }

    private void configurarPanelAsignaturas(JPanel panel) {
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    
        // Panel Superior para Nombre de Asignatura y Calificaciones
        JPanel panelSuperior = new JPanel(new BorderLayout(5,5));
    
        // SubPanel para Nombre de Asignatura
        JPanel panelNombreAsignatura = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblNombreAsig = new JLabel("Nombre Asignatura:");
        txtNombreAsig = new JTextField(20);
        panelNombreAsignatura.add(lblNombreAsig);
        panelNombreAsignatura.add(txtNombreAsig);
        panelSuperior.add(panelNombreAsignatura, BorderLayout.NORTH);
    
        // SubPanel para Agregar Calificaciones
        JPanel panelCalificaciones = new JPanel();
        GroupLayout layoutCal = new GroupLayout(panelCalificaciones);
        panelCalificaciones.setLayout(layoutCal);
        layoutCal.setAutoCreateGaps(true);
        layoutCal.setAutoCreateContainerGaps(true);
        panelCalificaciones.setBorder(BorderFactory.createTitledBorder("Agregar Calificación a esta Asignatura"));
    
        JLabel lblNombreCalif = new JLabel("Nombre Calif:");
        txtNombreCalificacion = new JTextField(15);
        JLabel lblNotaCalif = new JLabel("Nota:");
        txtNotaCalificacion = new JTextField(4);
        JLabel lblPeriodoCalif = new JLabel("Periodo:");
        txtPeriodoCalificacion = new JTextField(3);
        JLabel lblFechaCalif = new JLabel("Fecha (YYYY-MM-DD):");
        txtFechaCalificacion = new JTextField(10);
        JLabel lblEstCodigoCalif = new JLabel("Cód. Estudiante:"); // Para saber a quién pertenece esta calificación en la asignatura
        txtCodigoEstCalificacion = new JTextField(5);
        btnAgregarCalificacionAAsignatura = new JButton("Agregar Calificación");
    
        layoutCal.setHorizontalGroup(layoutCal.createSequentialGroup()
            .addGroup(layoutCal.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblNombreCalif)
                .addComponent(lblNotaCalif)
                .addComponent(lblEstCodigoCalif))
            .addGroup(layoutCal.createParallelGroup()
                .addComponent(txtNombreCalificacion)
                .addComponent(txtNotaCalificacion)
                .addComponent(txtCodigoEstCalificacion))
            .addGroup(layoutCal.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lblPeriodoCalif)
                .addComponent(lblFechaCalif))
            .addGroup(layoutCal.createParallelGroup()
                .addComponent(txtPeriodoCalificacion)
                .addComponent(txtFechaCalificacion)
                .addComponent(btnAgregarCalificacionAAsignatura))
        );
        layoutCal.setVerticalGroup(layoutCal.createSequentialGroup()
            .addGroup(layoutCal.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNombreCalif)
                .addComponent(txtNombreCalificacion)
                .addComponent(lblPeriodoCalif)
                .addComponent(txtPeriodoCalificacion))
            .addGroup(layoutCal.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNotaCalif)
                .addComponent(txtNotaCalificacion)
                .addComponent(lblFechaCalif)
                .addComponent(txtFechaCalificacion))
            .addGroup(layoutCal.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEstCodigoCalif)
                .addComponent(txtCodigoEstCalificacion)
                .addComponent(btnAgregarCalificacionAAsignatura))
        );
        panelSuperior.add(panelCalificaciones, BorderLayout.CENTER);
        panel.add(panelSuperior, BorderLayout.NORTH);
    
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregarAsig = new JButton("Agregar Asignatura");
        btnActualizarAsig = new JButton("Actualizar Asignatura (Nombre)");
        btnEliminarAsig = new JButton("Eliminar Asignatura");
        btnListarAsig = new JButton("Listar Asignaturas");
        btnBuscarAsig = new JButton("Buscar Asignatura");
        panelBotones.add(btnAgregarAsig);
        panelBotones.add(btnBuscarAsig);
        panelBotones.add(btnActualizarAsig);
        panelBotones.add(btnEliminarAsig);
        panelBotones.add(btnListarAsig);
        panel.add(panelBotones, BorderLayout.SOUTH);
    
        areaAsignaturas = new JTextArea(10, 50);
        areaAsignaturas.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaAsignaturas);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        btnAgregarAsig.addActionListener(this::accionAgregarAsignatura);
        btnListarAsig.addActionListener(this::accionListarAsignaturas);
        btnBuscarAsig.addActionListener(this::accionBuscarAsignatura);
        btnActualizarAsig.addActionListener(this::accionActualizarAsignatura);
        btnEliminarAsig.addActionListener(this::accionEliminarAsignatura);
        btnAgregarCalificacionAAsignatura.addActionListener(this::accionAgregarCalificacionAAsignatura);
    }


    private void configurarPanelAsignaciones(JPanel panel) {
        panel.setLayout(new GridLayout(0, 1, 10, 10)); // 0 rows, 1 col, gaps
        panel.setBorder(new EmptyBorder(10,10,10,10));

        // Panel para Asignar Profesor a Curso
        JPanel panelProfCurso = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelProfCurso.setBorder(BorderFactory.createTitledBorder("Asignar/Desasignar Profesor a Curso"));
        txtProfCodigoAsig = new JTextField(5);
        txtCursoGradoAsigProf = new JTextField(5);
        txtCursoGrupoAsigProf = new JTextField(5);
        btnAsignarProfACurso = new JButton("Asignar Prof-Curso");
        btnDesasignarProfDeCurso = new JButton("Desasignar Prof-Curso");
        panelProfCurso.add(new JLabel("Cód. Profesor:")); panelProfCurso.add(txtProfCodigoAsig);
        panelProfCurso.add(new JLabel("Grado Curso:")); panelProfCurso.add(txtCursoGradoAsigProf);
        panelProfCurso.add(new JLabel("Grupo Curso:")); panelProfCurso.add(txtCursoGrupoAsigProf);
        panelProfCurso.add(btnAsignarProfACurso);
        panelProfCurso.add(btnDesasignarProfDeCurso);
        panel.add(panelProfCurso);

        // Panel para Asignar Estudiante a Curso
        JPanel panelEstCurso = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstCurso.setBorder(BorderFactory.createTitledBorder("Asignar/Desasignar Estudiante a Curso"));
        txtEstCodigoAsig = new JTextField(5);
        txtCursoGradoAsigEst = new JTextField(5);
        txtCursoGrupoAsigEst = new JTextField(5);
        btnAsignarEstACurso = new JButton("Asignar Est-Curso");
        btnDesasignarEstDeCurso = new JButton("Desasignar Est-Curso");
        panelEstCurso.add(new JLabel("Cód. Estudiante:")); panelEstCurso.add(txtEstCodigoAsig);
        panelEstCurso.add(new JLabel("Grado Curso:")); panelEstCurso.add(txtCursoGradoAsigEst);
        panelEstCurso.add(new JLabel("Grupo Curso:")); panelEstCurso.add(txtCursoGrupoAsigEst);
        panelEstCurso.add(btnAsignarEstACurso);
        panelEstCurso.add(btnDesasignarEstDeCurso);
        panel.add(panelEstCurso);
        
        // Panel para Asignar Asignatura a Estudiante
        JPanel panelEstAsig = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstAsig.setBorder(BorderFactory.createTitledBorder("Asignar/Desasignar Asignatura a Estudiante (en memoria)"));
        txtEstCodigoAsigAsig = new JTextField(5);
        txtAsigNombreAsigEst = new JTextField(15);
        btnAsignarAsigAEst = new JButton("Asignar Asig-Est");
        btnDesasignarAsigDeEst = new JButton("Desasignar Asig-Est");
        panelEstAsig.add(new JLabel("Cód. Estudiante:")); panelEstAsig.add(txtEstCodigoAsigAsig);
        panelEstAsig.add(new JLabel("Nombre Asignatura:")); panelEstAsig.add(txtAsigNombreAsigEst);
        panelEstAsig.add(btnAsignarAsigAEst);
        panelEstAsig.add(btnDesasignarAsigDeEst);
        panel.add(panelEstAsig);

        areaAsignaciones = new JTextArea(10, 50);
        areaAsignaciones.setEditable(false);
        JScrollPane scrollAsignaciones = new JScrollPane(areaAsignaciones);
        panel.add(scrollAsignaciones);


        btnAsignarProfACurso.addActionListener(this::accionAsignarProfesorACurso);
        btnDesasignarProfDeCurso.addActionListener(this::accionDesasignarProfesorDeCurso);
        btnAsignarEstACurso.addActionListener(this::accionAsignarEstudianteACurso);
        btnDesasignarEstDeCurso.addActionListener(this::accionDesasignarEstudianteDeCurso);
        btnAsignarAsigAEst.addActionListener(this::accionAsignarAsignaturaAEstudiante);
        btnDesasignarAsigDeEst.addActionListener(this::accionDesasignarAsignaturaDeEstudiante);
    }

    private void configurarPanelReportes(JPanel panel) {
        panel.setLayout(new BorderLayout(10,10));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JPanel panelControles = new JPanel(new GridLayout(0,1,5,5)); // Single column for report controls

        // Reporte Estudiante
        JPanel panelRepEst = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRepEst.setBorder(BorderFactory.createTitledBorder("Reporte de Estudiante"));
        txtEstCodigoReporte = new JTextField(5);
        btnGenerarReporteEst = new JButton("Generar Reporte Estudiante");
        panelRepEst.add(new JLabel("Cód. Estudiante:")); panelRepEst.add(txtEstCodigoReporte);
        panelRepEst.add(btnGenerarReporteEst);
        panelControles.add(panelRepEst);

        // Info Curso
        JPanel panelInfoCurso = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfoCurso.setBorder(BorderFactory.createTitledBorder("Información de Curso"));
        txtCursoGradoReporte = new JTextField(5);
        txtCursoGrupoReporte = new JTextField(5);
        btnGenerarInfoCurso = new JButton("Mostrar Info Curso");
        panelInfoCurso.add(new JLabel("Grado:")); panelInfoCurso.add(txtCursoGradoReporte);
        panelInfoCurso.add(new JLabel("Grupo:")); panelInfoCurso.add(txtCursoGrupoReporte);
        panelInfoCurso.add(btnGenerarInfoCurso);
        panelControles.add(panelInfoCurso);
        
        // Listar Todos los Cursos Detallado
        JPanel panelListCursos = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnListarCursosDetalladoReporte = new JButton("Listar Todos los Cursos (Detallado)");
        panelListCursos.add(btnListarCursosDetalladoReporte);
        panelControles.add(panelListCursos);

        panel.add(panelControles, BorderLayout.NORTH);

        areaReportes = new JTextArea(15, 50);
        areaReportes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaReportes);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnGenerarReporteEst.addActionListener(this::accionGenerarReporteEstudiante);
        btnGenerarInfoCurso.addActionListener(this::accionGenerarInfoCurso);
        btnListarCursosDetalladoReporte.addActionListener(e -> areaReportes.setText(colegio.listarTodosLosCursosParaGUI()));
    }


    private void limpiarCamposEstudiante() {
        txtCodigoEst.setText("");
        txtNombreEst.setText("");
        txtEdadEst.setText("");
        txtCedulaEst.setText("");
        txtGradoCursoEst.setText("");
        txtGrupoCursoEst.setText("");
        // comboTipoEst.setSelectedIndex(0); 
    }

    private void limpiarCamposProfesor() {
        txtCodigoProf.setText("");
        txtNombreProf.setText("");
        txtEdadProf.setText("");
        txtCedulaProf.setText("");
        txtGradoCursoProf.setText("");
        txtGrupoCursoProf.setText("");
    }

    private void limpiarCamposCurso() {
        txtGradoCurso.setText("");
        txtGrupoCurso.setText("");
    }
    
    private void limpiarCamposAsignatura() {
        txtNombreAsig.setText("");
        txtNombreCalificacion.setText("");
        txtNotaCalificacion.setText("");
        txtPeriodoCalificacion.setText("");
        txtFechaCalificacion.setText("");
        txtCodigoEstCalificacion.setText("");
    }

    private void refrescarTodasLasListas() {
        refrescarListaEstudiantes();
        refrescarListaProfesores();
        refrescarListaCursos();
        refrescarListaAsignaturas();
        // areaAsignaciones y areaReportes se actualizan bajo demanda
    }

    private void refrescarListaEstudiantes() {
        areaEstudiantes.setText(colegio.listarTodosLosEstudiantesParaGUI());
    }

    private void refrescarListaProfesores() {
        areaProfesores.setText(colegio.listarTodosLosProfesoresParaGUI());
    }
    
    private void refrescarListaCursos() {
        areaCursos.setText(colegio.listarTodosLosCursosParaGUI());
    }

    private void refrescarListaAsignaturas() {
        areaAsignaturas.setText(colegio.listarTodasLasAsignaturasParaGUI());
    }

    // --- Action Listeners para Estudiantes (ya implementados) ---
    private void accionAgregarEstudiante(ActionEvent e) {
        try {
            int codigo = Integer.parseInt(txtCodigoEst.getText());
            String nombre = txtNombreEst.getText();
            int edad = Integer.parseInt(txtEdadEst.getText());
            String cedula = txtCedulaEst.getText();
            String tipo = (String) comboTipoEst.getSelectedItem();

            if (nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Cédula no pueden estar vacíos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Estudiante nuevoEstudiante = new Estudiante(codigo, nombre, edad, cedula, tipo);
            
            if (colegio.buscarEstudiante(codigo) != null) {
                 JOptionPane.showMessageDialog(this, "Ya existe un estudiante con el código " + codigo, "Error al Agregar", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            colegio.agregarEstudiante(nuevoEstudiante);

            String gradoStr = txtGradoCursoEst.getText();
            String grupoStr = txtGrupoCursoEst.getText();
            if (!gradoStr.isEmpty() && !grupoStr.isEmpty()) {
                try {
                    int grado = Integer.parseInt(gradoStr);
                    int grupo = Integer.parseInt(grupoStr);
                    if (colegio.buscarCurso(grado, grupo) != null) {
                         colegio.agregarEstudianteACurso(codigo, grado, grupo); 
                    } else {
                        JOptionPane.showMessageDialog(this, "El curso " + grado + "-" + grupo + " no existe. Estudiante agregado sin curso.", "Advertencia Curso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Grado y Grupo del curso deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            JOptionPane.showMessageDialog(this, "Estudiante agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposEstudiante();
            refrescarListaEstudiantes();
            refrescarListaCursos(); // Curso puede haber cambiado

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código y Edad deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionListarEstudiantes(ActionEvent e) {
        refrescarListaEstudiantes();
    }
    
    private void accionBuscarEstudiante(ActionEvent e) {
        try {
            if (txtCodigoEst.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese un código de estudiante para buscar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int codigo = Integer.parseInt(txtCodigoEst.getText());
            Estudiante est = colegio.buscarEstudiante(codigo);
            if (est != null) {
                txtNombreEst.setText(est.getNombre());
                txtEdadEst.setText(String.valueOf(est.getEdad()));
                txtCedulaEst.setText(est.getCedula());
                comboTipoEst.setSelectedItem(est.getTipo());
                if (est.getCurso() != null) {
                    txtGradoCursoEst.setText(String.valueOf(est.getCurso().getGrado()));
                    txtGrupoCursoEst.setText(String.valueOf(est.getCurso().getGrupo()));
                } else {
                    txtGradoCursoEst.setText("");
                    txtGrupoCursoEst.setText("");
                }
                areaEstudiantes.setText("Estudiante encontrado: \n" + colegio.reporteEstudiante(codigo));
            } else {
                JOptionPane.showMessageDialog(this, "Estudiante con código " + codigo + " no encontrado.", "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
                limpiarCamposEstudiante(); 
                txtCodigoEst.setText(String.valueOf(codigo));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido para buscar.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizarEstudiante(ActionEvent e) {
        try {
            if (txtCodigoEst.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Busque un estudiante primero usando su código.", "Campo Código Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int codigo = Integer.parseInt(txtCodigoEst.getText());
            String nombre = txtNombreEst.getText();
            int edad = Integer.parseInt(txtEdadEst.getText());
            String cedula = txtCedulaEst.getText();
            String tipo = (String) comboTipoEst.getSelectedItem();

            if (nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Cédula no pueden estar vacíos para actualizar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Estudiante estExistente = colegio.buscarEstudiante(codigo);
            if (estExistente == null) {
                 JOptionPane.showMessageDialog(this, "No se encontró estudiante con código " + codigo + ". No se puede actualizar.", "Error Actualización", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            Estudiante estActualizado = new Estudiante(codigo, nombre, edad, cedula, tipo);
            
            String gradoStr = txtGradoCursoEst.getText();
            String grupoStr = txtGrupoCursoEst.getText();
            Curso cursoNuevoAsignado = null;

            if (!gradoStr.isEmpty() && !grupoStr.isEmpty()) { // Intento de asignar o cambiar curso
                try {
                    int grado = Integer.parseInt(gradoStr);
                    int grupo = Integer.parseInt(grupoStr);
                    cursoNuevoAsignado = colegio.buscarCurso(grado, grupo);
                    if (cursoNuevoAsignado == null) {
                        JOptionPane.showMessageDialog(this, "El curso " + grado + "-" + grupo + " no existe. El estudiante mantendrá su curso anterior o quedará sin curso si los campos se limpiaron.", "Advertencia Curso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(this, "Grado y Grupo del curso deben ser números. El estudiante mantendrá su curso anterior o quedará sin curso.", "Error Formato Curso", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Lógica de cambio de curso
            Curso cursoAnterior = estExistente.getCurso();
            if (cursoAnterior != null && (cursoNuevoAsignado == null || !cursoAnterior.equals(cursoNuevoAsignado))) {
                // Desasignar del curso anterior
                colegio.desasignarEstudianteDeCurso(codigo, cursoAnterior.getGrado(), cursoAnterior.getGrupo());
            }
            if (cursoNuevoAsignado != null && (cursoAnterior == null || !cursoAnterior.equals(cursoNuevoAsignado))) {
                // Asignar al nuevo curso
                colegio.agregarEstudianteACurso(codigo, cursoNuevoAsignado.getGrado(), cursoNuevoAsignado.getGrupo());
            }
            estActualizado.setCurso(cursoNuevoAsignado); // El estudiante que se pasa a DAO debe tener la referencia correcta al curso
            
            colegio.actualizarEstudiante(estActualizado); 
            JOptionPane.showMessageDialog(this, "Estudiante actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposEstudiante();
            refrescarListaEstudiantes();
            refrescarListaCursos(); // Los cursos pueden haber cambiado

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código y Edad deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionEliminarEstudiante(ActionEvent e) {
        try {
            if (txtCodigoEst.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese un código de estudiante para eliminar.", "Campo Código Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int codigo = Integer.parseInt(txtCodigoEst.getText());
            Estudiante estParaEliminar = colegio.buscarEstudiante(codigo);

            if (estParaEliminar == null) {
                JOptionPane.showMessageDialog(this, "Estudiante con código " + codigo + " no encontrado.", "Error Eliminación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea eliminar al estudiante: " + estParaEliminar.getNombre() + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                colegio.eliminarEstudiante(codigo); 
                JOptionPane.showMessageDialog(this, "Estudiante eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEstudiante();
                refrescarListaEstudiantes();
                refrescarListaCursos(); // El curso pudo cambiar
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido para eliminar.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Action Listeners para Profesores ---
    private void accionAgregarProfesor(ActionEvent e) {
        try {
            int codigo = Integer.parseInt(txtCodigoProf.getText());
            String nombre = txtNombreProf.getText();
            int edad = Integer.parseInt(txtEdadProf.getText());
            String cedula = txtCedulaProf.getText();
            String tipo = (String) comboTipoProf.getSelectedItem();

            if (nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Cédula no pueden estar vacíos.", "Error Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (colegio.buscarProfesor(codigo) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un profesor con el código " + codigo, "Error al Agregar", JOptionPane.ERROR_MESSAGE);
                return;
            }

            com.modelo.Profesor nuevoProfesor = new com.modelo.Profesor(codigo, nombre, edad, cedula, tipo);
            colegio.agregarProfesor(nuevoProfesor);
            
            String gradoStr = txtGradoCursoProf.getText();
            String grupoStr = txtGrupoCursoProf.getText();
            if (!gradoStr.isEmpty() && !grupoStr.isEmpty()) {
                try {
                    int grado = Integer.parseInt(gradoStr);
                    int grupo = Integer.parseInt(grupoStr);
                    if (colegio.buscarCurso(grado, grupo) != null) {
                        colegio.agregarCursoAProfesor(codigo, grado, grupo);
                    } else {
                         JOptionPane.showMessageDialog(this, "Curso " + grado + "-" + grupo + " no existe. Profesor agregado sin asignar a este curso.", "Advertencia Curso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(this, "Grado y Grupo del curso deben ser números.", "Error Formato Curso", JOptionPane.ERROR_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(this, "Profesor agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposProfesor();
            refrescarListaProfesores();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código y Edad deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar profesor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionListarProfesores(ActionEvent e) {
        refrescarListaProfesores();
    }

    private void accionBuscarProfesor(ActionEvent e) {
         try {
            if (txtCodigoProf.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese un código de profesor para buscar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int codigo = Integer.parseInt(txtCodigoProf.getText());
            com.modelo.Profesor prof = colegio.buscarProfesor(codigo);
            if (prof != null) {
                txtNombreProf.setText(prof.getNombre());
                txtEdadProf.setText(String.valueOf(prof.getEdad()));
                txtCedulaProf.setText(prof.getCedula());
                comboTipoProf.setSelectedItem(prof.getTipo());
                 if (prof.getCurso() != null) {
                    txtGradoCursoProf.setText(String.valueOf(prof.getCurso().getGrado()));
                    txtGrupoCursoProf.setText(String.valueOf(prof.getCurso().getGrupo()));
                } else {
                    txtGradoCursoProf.setText("");
                    txtGrupoCursoProf.setText("");
                }
                areaProfesores.setText("Profesor encontrado: \n" + prof.toString() + (prof.getCurso() != null ? " - Curso: " + prof.getCurso().getGrado() + "-" + prof.getCurso().getGrupo() : ""));
            } else {
                JOptionPane.showMessageDialog(this, "Profesor con código " + codigo + " no encontrado.", "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
                limpiarCamposProfesor();
                txtCodigoProf.setText(String.valueOf(codigo));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido para buscar.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizarProfesor(ActionEvent e) {
        try {
            if (txtCodigoProf.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Busque un profesor primero usando su código.", "Campo Código Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int codigo = Integer.parseInt(txtCodigoProf.getText());
            String nombre = txtNombreProf.getText();
            int edad = Integer.parseInt(txtEdadProf.getText());
            String cedula = txtCedulaProf.getText();
            String tipo = (String) comboTipoProf.getSelectedItem();

            if (nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Cédula no pueden estar vacíos.", "Error Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            com.modelo.Profesor profExistente = colegio.buscarProfesor(codigo);
            if (profExistente == null) {
                JOptionPane.showMessageDialog(this, "Profesor con código " + codigo + " no encontrado para actualizar.", "Error Actualización", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            com.modelo.Profesor profActualizado = new com.modelo.Profesor(codigo, nombre, edad, cedula, tipo);

            String gradoStr = txtGradoCursoProf.getText();
            String grupoStr = txtGrupoCursoProf.getText();
            Curso cursoNuevoAsignado = null;

            if (!gradoStr.isEmpty() && !grupoStr.isEmpty()) {
                try {
                    int grado = Integer.parseInt(gradoStr);
                    int grupo = Integer.parseInt(grupoStr);
                    cursoNuevoAsignado = colegio.buscarCurso(grado, grupo);
                     if (cursoNuevoAsignado == null) {
                        JOptionPane.showMessageDialog(this, "El curso " + grado + "-" + grupo + " no existe. El profesor mantendrá su curso anterior o quedará sin curso.", "Advertencia Curso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(this, "Grado y Grupo del curso deben ser números. Profesor mantendrá su curso anterior o quedará sin curso.", "Error Formato Curso", JOptionPane.ERROR_MESSAGE);
                }
            }

            Curso cursoAnterior = profExistente.getCurso();
            if (cursoAnterior != null && (cursoNuevoAsignado == null || !cursoAnterior.equals(cursoNuevoAsignado))) {
                colegio.desasignarProfesorDeCurso(codigo, cursoAnterior.getGrado(), cursoAnterior.getGrupo());
            }
            if (cursoNuevoAsignado != null && (cursoAnterior == null || !cursoAnterior.equals(cursoNuevoAsignado))) {
                colegio.agregarCursoAProfesor(codigo, cursoNuevoAsignado.getGrado(), cursoNuevoAsignado.getGrupo());
            }
            profActualizado.setCurso(cursoNuevoAsignado);

            colegio.actualizarProfesor(profActualizado);
            JOptionPane.showMessageDialog(this, "Profesor actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposProfesor();
            refrescarListaProfesores();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código y Edad deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar profesor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionEliminarProfesor(ActionEvent e) {
        try {
            if (txtCodigoProf.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese un código de profesor para eliminar.", "Campo Código Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int codigo = Integer.parseInt(txtCodigoProf.getText());
            com.modelo.Profesor prof = colegio.buscarProfesor(codigo);
            if (prof == null) {
                JOptionPane.showMessageDialog(this, "Profesor con código " + codigo + " no encontrado.", "Error Eliminación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al profesor: " + prof.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                colegio.eliminarProfesor(codigo);
                JOptionPane.showMessageDialog(this, "Profesor eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposProfesor();
                refrescarListaProfesores();
                refrescarListaCursos(); // El curso pudo cambiar
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un código numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar profesor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Action Listeners para Cursos ---
    private void accionAgregarCurso(ActionEvent e) {
        try {
            int grado = Integer.parseInt(txtGradoCurso.getText());
            int grupo = Integer.parseInt(txtGrupoCurso.getText());

            if (colegio.buscarCurso(grado, grupo) != null) {
                 JOptionPane.showMessageDialog(this, "Ya existe un curso con grado " + grado + " y grupo " + grupo, "Error al Agregar", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            Curso nuevoCurso = new Curso();
            nuevoCurso.setGrado(grado);
            nuevoCurso.setGrupo(grupo);
            colegio.agregarCurso(nuevoCurso);
            JOptionPane.showMessageDialog(this, "Curso agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposCurso();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grado y Grupo deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar curso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionListarCursos(ActionEvent e) {
        refrescarListaCursos();
    }

    private void accionBuscarCurso(ActionEvent e) {
        try {
            if (txtGradoCurso.getText().trim().isEmpty() || txtGrupoCurso.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese Grado y Grupo para buscar.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int grado = Integer.parseInt(txtGradoCurso.getText());
            int grupo = Integer.parseInt(txtGrupoCurso.getText());
            Curso curso = colegio.buscarCurso(grado, grupo);
            if (curso != null) {
                areaCursos.setText(colegio.infoCurso(grado, grupo));
            } else {
                JOptionPane.showMessageDialog(this, "Curso " + grado + "-" + grupo + " no encontrado.", "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grado y Grupo deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void accionActualizarCurso(ActionEvent e) {
        // La actualización de Cursos se limita a su grado y grupo. Profesor y Estudiantes se manejan en Asignaciones.
        // Opcionalmente, se podría permitir cambiar grado/grupo aquí, pero eso tiene implicaciones de ID.
        // Por ahora, este botón podría simplemente recargar la info del curso si se desea.
        // O, si se permite cambiar grado/grupo, se necesitaría una lógica más compleja para actualizar el ID.
        // Para este ejercicio, asumimos que Grado y Grupo son la ID y no se cambian.
        // Lo que se podría actualizar son las listas de estudiantes o el profesor asignado, pero eso se hace en "Asignaciones".
        // Por lo tanto, la funcionalidad directa de "Actualizar Curso" aquí es limitada.
        // Podríamos usarlo para refrescar la vista de un curso específico.
         try {
            if (txtGradoCurso.getText().trim().isEmpty() || txtGrupoCurso.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Busque un curso primero.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int grado = Integer.parseInt(txtGradoCurso.getText());
            int grupo = Integer.parseInt(txtGrupoCurso.getText());
            Curso cursoExistente = colegio.buscarCurso(grado, grupo);
            if (cursoExistente == null) {
                 JOptionPane.showMessageDialog(this, "Curso " + grado + "-" + grupo + " no encontrado. No se puede actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            // No hay campos directos de Curso para actualizar desde aquí aparte de su ID (Grado, Grupo)
            // La persistencia de los cambios en profesor y estudiantes se hace en sus respectivas asignaciones.
            // Este botón puede servir para volver a cargar y mostrar los detalles del curso.
            colegio.actualizarCurso(cursoExistente); // Esto en Colegio llama a cursoDAO.actualizar que guarda el estado actual.
            JOptionPane.showMessageDialog(this, "Curso " + grado + "-" + grupo + " 'actualizado' (estado persistido).\nLos cambios en profesor/estudiantes se hacen en 'Asignaciones'.", "Info", JOptionPane.INFORMATION_MESSAGE);
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grado y Grupo deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void accionEliminarCurso(ActionEvent e) {
         try {
            if (txtGradoCurso.getText().trim().isEmpty() || txtGrupoCurso.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Ingrese Grado y Grupo para eliminar.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int grado = Integer.parseInt(txtGradoCurso.getText());
            int grupo = Integer.parseInt(txtGrupoCurso.getText());
            Curso curso = colegio.buscarCurso(grado, grupo);
            if (curso == null) {
                JOptionPane.showMessageDialog(this, "Curso " + grado + "-" + grupo + " no encontrado.", "Error Eliminación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el curso " + grado + "-" + grupo + "?\nEsto desasignará al profesor y a los estudiantes de este curso.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                colegio.eliminarCurso(grado, grupo);
                JOptionPane.showMessageDialog(this, "Curso eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposCurso();
                refrescarListaCursos();
                refrescarListaEstudiantes(); // Estudiantes pudieron cambiar de curso
                refrescarListaProfesores(); // Profesores pudieron cambiar de curso
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grado y Grupo deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar curso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Action Listeners para Asignaturas ---
    private void accionAgregarAsignatura(ActionEvent e) {
        try {
            String nombre = txtNombreAsig.getText();
            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre de asignatura no puede estar vacío.", "Error Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (colegio.buscarAsignatura(nombre) != null) {
                 JOptionPane.showMessageDialog(this, "Ya existe una asignatura con el nombre " + nombre, "Error al Agregar", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            com.modelo.Asignatura nuevaAsignatura = new com.modelo.Asignatura(nombre);
            colegio.agregarAsignatura(nuevaAsignatura);
            JOptionPane.showMessageDialog(this, "Asignatura agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposAsignatura();
            refrescarListaAsignaturas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar asignatura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void accionListarAsignaturas(ActionEvent e) {
        refrescarListaAsignaturas();
    }

    private void accionBuscarAsignatura(ActionEvent e) {
        String nombre = txtNombreAsig.getText();
        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de asignatura para buscar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        com.modelo.Asignatura asig = colegio.buscarAsignatura(nombre);
        if (asig != null) {
            // Mostrar calificaciones de la asignatura encontrada
            StringBuilder sb = new StringBuilder();
            sb.append("Asignatura Encontrada: ").append(asig.getNombre()).append("\n");
            if (asig.getCalificaciones() != null && !asig.getCalificaciones().isEmpty()) {
                sb.append("Calificaciones Registradas:\n");
                for (com.modelo.Calificacion cal : asig.getCalificaciones()) {
                    sb.append("  - ").append(cal.getNombre()).append(": ").append(cal.getNota())
                      .append(" (P").append(cal.getPeriodo()).append(", F:").append(cal.getFecha()).append(")\n");
                }
            } else {
                sb.append("  (Sin calificaciones registradas para esta asignatura)\n");
            }
            areaAsignaturas.setText(sb.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Asignatura '" + nombre + "' no encontrada.", "Búsqueda Fallida", JOptionPane.WARNING_MESSAGE);
             areaAsignaturas.setText(""); // Limpiar si no se encuentra
        }
    }
    
    private void accionActualizarAsignatura(ActionEvent e) {
        // Solo se permite actualizar el nombre si la lógica de negocio lo permitiera (requeriría buscar por un ID o nombre antiguo).
        // Para este caso, como el nombre es el ID, no se puede "actualizar" el nombre directamente sin eliminar y re-agregar.
        // Lo que sí se actualiza son las calificaciones asociadas, a través de accionAgregarCalificacionAAsignatura.
        // Este botón puede servir para refrescar la vista de la asignatura.
        String nombreOriginal = txtNombreAsig.getText();
        if (nombreOriginal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Busque una asignatura primero.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        com.modelo.Asignatura asigExistente = colegio.buscarAsignatura(nombreOriginal);
        if (asigExistente == null) {
            JOptionPane.showMessageDialog(this, "Asignatura '" + nombreOriginal + "' no encontrada para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Si se quisiera permitir cambiar el nombre, se necesitaría un nuevo campo para "Nuevo Nombre"
        // y luego colegio.actualizarAsignatura(asigExistenteConNuevoNombre);
        // Por ahora, solo "actualizamos" su estado actual (útil si las calificaciones se modificaron y queremos persistir)
        colegio.actualizarAsignatura(asigExistente);
        JOptionPane.showMessageDialog(this, "Asignatura '" + nombreOriginal + "' 'actualizada' (estado persistido).\nLas calificaciones se agregan por separado.", "Info", JOptionPane.INFORMATION_MESSAGE);
        refrescarListaAsignaturas();
    }

    private void accionEliminarAsignatura(ActionEvent e) {
        try {
            String nombre = txtNombreAsig.getText();
            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre de asignatura para eliminar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            com.modelo.Asignatura asig = colegio.buscarAsignatura(nombre);
            if (asig == null) {
                JOptionPane.showMessageDialog(this, "Asignatura '" + nombre + "' no encontrada.", "Error Eliminación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la asignatura: " + nombre + "?\nEsto también eliminará todas sus calificaciones asociadas.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                colegio.eliminarAsignatura(nombre);
                JOptionPane.showMessageDialog(this, "Asignatura eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposAsignatura();
                refrescarListaAsignaturas();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar asignatura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void accionAgregarCalificacionAAsignatura(ActionEvent e) {
        try {
            String nombreAsig = txtNombreAsig.getText();
            if (nombreAsig.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero busque y seleccione una asignatura (su nombre debe estar en el campo 'Nombre Asignatura').", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Validar que el estudiante exista
            int codigoEst = Integer.parseInt(txtCodigoEstCalificacion.getText());
            if (colegio.buscarEstudiante(codigoEst) == null){
                 JOptionPane.showMessageDialog(this, "Estudiante con código " + codigoEst + " no existe. No se puede agregar calificación.", "Error Estudiante", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nombreCal = txtNombreCalificacion.getText();
            double nota = Double.parseDouble(txtNotaCalificacion.getText());
            int periodo = Integer.parseInt(txtPeriodoCalificacion.getText());
            String fechaStr = txtFechaCalificacion.getText();

            if (nombreCal.isEmpty() || fechaStr.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Nombre de calificación y fecha no pueden estar vacíos.", "Error Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            colegio.agregarCalificacion(nombreAsig, codigoEst, nombreCal, nota, periodo, fechaStr);
            JOptionPane.showMessageDialog(this, "Calificación agregada/actualizada en la asignatura.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Limpiar campos de calificación
            txtNombreCalificacion.setText("");
            txtNotaCalificacion.setText("");
            txtPeriodoCalificacion.setText("");
            txtFechaCalificacion.setText("");
            txtCodigoEstCalificacion.setText("");
            refrescarListaAsignaturas(); // Para ver el recuento de calificaciones actualizado
             // También refrescar el área de la asignatura actual si está siendo mostrada
            accionBuscarAsignatura(null); // Re-buscar para actualizar la vista de calificaciones
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota, Periodo, Cód. Estudiante deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar calificación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Action Listeners para Asignaciones ---
    private void accionAsignarProfesorACurso(ActionEvent e) {
        try {
            int profCodigo = Integer.parseInt(txtProfCodigoAsig.getText());
            int cursoGrado = Integer.parseInt(txtCursoGradoAsigProf.getText());
            int cursoGrupo = Integer.parseInt(txtCursoGrupoAsigProf.getText());

            colegio.agregarCursoAProfesor(profCodigo, cursoGrado, cursoGrupo);
            // Mensaje de éxito/error ya se maneja en Colegio.java
            areaAsignaciones.setText("Intento de asignación Profesor a Curso realizado.\n" + colegio.infoCurso(cursoGrado, cursoGrupo));
            refrescarListaProfesores();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser números.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error Asignación", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void accionDesasignarProfesorDeCurso(ActionEvent e) {
        try {
            int profCodigo = Integer.parseInt(txtProfCodigoAsig.getText());
            int cursoGrado = Integer.parseInt(txtCursoGradoAsigProf.getText());
            int cursoGrupo = Integer.parseInt(txtCursoGrupoAsigProf.getText());

            colegio.desasignarProfesorDeCurso(profCodigo, cursoGrado, cursoGrupo);
            areaAsignaciones.setText("Intento de desasignación Profesor de Curso realizado.\n" + colegio.infoCurso(cursoGrado, cursoGrupo));
            refrescarListaProfesores();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser números.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error Desasignación", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void accionAsignarEstudianteACurso(ActionEvent e) {
         try {
            int estCodigo = Integer.parseInt(txtEstCodigoAsig.getText());
            int cursoGrado = Integer.parseInt(txtCursoGradoAsigEst.getText());
            int cursoGrupo = Integer.parseInt(txtCursoGrupoAsigEst.getText());

            colegio.agregarEstudianteACurso(estCodigo, cursoGrado, cursoGrupo);
            areaAsignaciones.setText("Intento de asignación Estudiante a Curso realizado.\nEstudiante: " + colegio.reporteEstudiante(estCodigo) + "\nCurso: " + colegio.infoCurso(cursoGrado, cursoGrupo));
            refrescarListaEstudiantes();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser números.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error Asignación", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void accionDesasignarEstudianteDeCurso(ActionEvent e) {
        try {
            int estCodigo = Integer.parseInt(txtEstCodigoAsig.getText());
            int cursoGrado = Integer.parseInt(txtCursoGradoAsigEst.getText());
            int cursoGrupo = Integer.parseInt(txtCursoGrupoAsigEst.getText());

            colegio.desasignarEstudianteDeCurso(estCodigo, cursoGrado, cursoGrupo);
            areaAsignaciones.setText("Intento de desasignación Estudiante de Curso realizado.\nEstudiante: " + colegio.reporteEstudiante(estCodigo) + "\nCurso: " + colegio.infoCurso(cursoGrado, cursoGrupo));
            refrescarListaEstudiantes();
            refrescarListaCursos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser números.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error Desasignación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionAsignarAsignaturaAEstudiante(ActionEvent e) {
        try {
            int estCodigo = Integer.parseInt(txtEstCodigoAsigAsig.getText());
            String asigNombre = txtAsigNombreAsigEst.getText();
            if (asigNombre.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Nombre de asignatura no puede estar vacío.", "Error Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            colegio.agregarAsignaturaAEstudiante(estCodigo, asigNombre);
            areaAsignaciones.setText("Intento de asignación Asignatura a Estudiante realizado (en memoria).\n" + colegio.reporteEstudiante(estCodigo));
            // No hay lista que refrescar que muestre esta relación directamente, se ve en el reporte del estudiante.
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código Estudiante debe ser número.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error Asignación", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void accionDesasignarAsignaturaDeEstudiante(ActionEvent e) {
         try {
            int estCodigo = Integer.parseInt(txtEstCodigoAsigAsig.getText());
            String asigNombre = txtAsigNombreAsigEst.getText();
             if (asigNombre.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Nombre de asignatura no puede estar vacío.", "Error Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            colegio.desasignarAsignaturaDeEstudiante(estCodigo, asigNombre);
            areaAsignaciones.setText("Intento de desasignación Asignatura de Estudiante realizado (en memoria).\n" + colegio.reporteEstudiante(estCodigo));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código Estudiante debe ser número.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error Desasignación", JOptionPane.ERROR_MESSAGE);
        }
    }


    // --- Action Listeners para Reportes ---
    private void accionGenerarReporteEstudiante(ActionEvent e) {
        try {
            if (txtEstCodigoReporte.getText().trim().isEmpty()){
                 JOptionPane.showMessageDialog(this, "Ingrese Código Estudiante.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            int codigo = Integer.parseInt(txtEstCodigoReporte.getText());
            areaReportes.setText(colegio.reporteEstudiante(codigo));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código debe ser número.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionGenerarInfoCurso(ActionEvent e) {
        try {
             if (txtCursoGradoReporte.getText().trim().isEmpty() || txtCursoGrupoReporte.getText().trim().isEmpty()){
                 JOptionPane.showMessageDialog(this, "Ingrese Grado y Grupo del Curso.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            int grado = Integer.parseInt(txtCursoGradoReporte.getText());
            int grupo = Integer.parseInt(txtCursoGrupoReporte.getText());
            areaReportes.setText(colegio.infoCurso(grado, grupo));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grado y Grupo deben ser números.", "Error Formato", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuAdmin menu = new MenuAdmin();
            menu.setVisible(true);
        });
    }
}
