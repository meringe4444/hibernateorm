package ni.edu.uam.hibernateorm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ni.edu.uam.hibernateorm.model.Usuario;
import ni.edu.uam.hibernateorm.service.ArchivoService;
import ni.edu.uam.hibernateorm.service.NavegationService;
import ni.edu.uam.hibernateorm.service.SesionService;
import ni.edu.uam.hibernateorm.service.UsuarioService;

import java.io.File;

public class RegistroController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmaPassword;
    @FXML
    private ImageView imageFoto;

    private String rutaFoto;

    private final ArchivoService archivoService = new ArchivoService();
    private final NavegationService navegationService = new NavegationService();
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void seleccionarFoto(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Fotografía");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        Stage stage = (Stage) imageFoto.getScene().getWindow();
        File archivo = fc.showOpenDialog(stage);

        if (archivo != null) {
            try {
                rutaFoto = archivoService.guardarArchivo(archivo);
                imageFoto.setImage(new Image(new File(rutaFoto).toURI().toString()));
            } catch (Exception e) {
                UtilController.error(e);
            }
        }
    }

    @FXML
    private void registrar(ActionEvent event) {
        if (!validarFormulario()) {
            return;
        }

        Usuario usuario = new Usuario(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtCorreo.getText().trim(),
                txtUsuario.getText().trim(),
                txtPassword.getText(),
                rutaFoto
        );

        try {
            usuarioService.save(usuario);
            UtilController.info("Usuario registrado correctamente.");
            regresar(event);
        } catch (Exception e) {
            UtilController.error(e);
        }
    }

    private boolean validarFormulario() {
        if (txtNombre.getText().isBlank() || txtApellido.getText().isBlank()
                || txtCorreo.getText().isBlank() || txtUsuario.getText().isBlank()
                || txtPassword.getText().isBlank() || txtConfirmaPassword.getText().isBlank()) {
            UtilController.advertencia("Todos los campos son obligatorios.");
            return false;
        }

        if (!txtPassword.getText().equals(txtConfirmaPassword.getText())) {
            UtilController.advertencia("Las contraseñas ingresadas no coinciden.");
            return false;
        }

        if (rutaFoto == null || rutaFoto.isBlank()) {
            UtilController.advertencia("Debe seleccionar una fotografía para el usuario.");
            return false;
        }

        return true;
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {
            if (SesionService.haySesionActiva()) {
                UtilController.cerrarVentana(event);
            } else {
                navegationService.cambiarVista("login.fxml", txtUsuario);
            }
        } catch (Exception e) {
            UtilController.error(e);
        }
    }
}
