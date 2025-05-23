package com.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Principal extends JFrame {

    private JComboBox<String> comboRoles;
    private JButton btnIngresar;

    public Principal() {
        setTitle("Sistema de Gestión Académica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 150);
        setLocationRelativeTo(null); // Center the window
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel lblRol = new JLabel("Seleccione su Rol:");
        comboRoles = new JComboBox<>(new String[]{"ADMINISTRADOR", "DOCENTE", "ESTUDIANTE"});
        btnIngresar = new JButton("Ingresar");

        btnIngresar.addActionListener(this::accionIngresar);

        add(lblRol);
        add(comboRoles);
        add(btnIngresar);
    }

    private void accionIngresar(ActionEvent e) {
        String rolSeleccionado = (String) comboRoles.getSelectedItem();
        if (rolSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un rol.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ocultar ventana principal
        this.setVisible(false);

        SwingUtilities.invokeLater(() -> {
            switch (rolSeleccionado) {
                case "ADMINISTRADOR":
                    MenuAdmin menuAdmin = new MenuAdmin();
                    menuAdmin.setVisible(true);
                    break;
                case "DOCENTE":
                    MenuDocente menuDocente = new MenuDocente();
                    menuDocente.setVisible(true);
                    break;
                case "ESTUDIANTE":
                    MenuEstudiante menuEstudiante = new MenuEstudiante();
                    menuEstudiante.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Rol no reconocido.", "Error", JOptionPane.ERROR_MESSAGE);
                    // Volver a mostrar la ventana principal si hay error o se cierra el menú específico
                    this.setVisible(true); 
                    break;
            }
        });
         // Cuando el menú específico se cierre, podríamos querer que esta ventana principal se muestre de nuevo o que la aplicación termine.
        // Por ahora, la ventana principal se oculta y el menú específico toma el control.
        // Para que la aplicación termine cuando se cierre el menú específico, el DefaultCloseOperation de esos JFrames debe ser EXIT_ON_CLOSE.
        // Si queremos que esta ventana se vuelva a mostrar, los otros JFrames deberían usar DISPOSE_ON_CLOSE y agregar un WindowListener.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Principal principal = new Principal();
            principal.setVisible(true);
        });
    }
}
