package ni.edu.uam.hibernateorm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ni.edu.uam.hibernateorm.model.Usuario;
import ni.edu.uam.hibernateorm.service.NavegationService;
import ni.edu.uam.hibernateorm.service.SesionService;
import ni.edu.uam.hibernateorm.service.UsuarioService;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;

    private final NavegationService navegationService = new NavegationService();
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void iniciarSesion(ActionEvent actionEvent) {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();

        if (usuario.isBlank() || password.isBlank()) {
            UtilController.advertencia("Debe ingresar su usuario y contraseña.");
            return;
        }

        try {
            Usuario u = usuarioService.findByLogin(usuario, password).orElse(null);
            if (u != null) {
                SesionService.iniciarSesion(u);
                navegationService.cambiarVista("usuario-table.fxml", txtUsuario);
            } else {
                UtilController.advertencia("Usuario o contraseña incorrectos.");
            }
        } catch (Exception e) {
            UtilController.error(e);
        }
    }

    @FXML
    private void abrirRegistro(ActionEvent actionEvent) {
        try {
            navegationService.cambiarVista("registro.fxml", txtUsuario);
        } catch (Exception e) {
            UtilController.error(e);
        }
    }
}
