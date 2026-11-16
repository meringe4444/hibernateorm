package ni.edu.uam.hibernateorm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ni.edu.uam.hibernateorm.model.Usuario;
import ni.edu.uam.hibernateorm.service.ArchivoService;

import java.io.File;

public class UsuarioModalController {

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
    private ImageView imageFoto;

    private final ArchivoService archivoService = new ArchivoService();

    private Usuario usuario;
    private boolean guardado = false;

    private String nombreOriginal, apellidoOriginal, correoOriginal, usuarioOriginal, passwordOriginal, fotoOriginal;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.nombreOriginal = usuario.getNombre();
        this.apellidoOriginal = usuario.getApellido();
        this.correoOriginal = usuario.getCorreo();
        this.usuarioOriginal = usuario.getUsuario();
        this.passwordOriginal = usuario.getPassword();
        this.fotoOriginal = usuario.getRutafoto();

        txtNombre.setText(nombreOriginal);
        txtApellido.setText(apellidoOriginal);
        txtCorreo.setText(correoOriginal);
        txtUsuario.setText(usuarioOriginal);
        txtPassword.setText(passwordOriginal);
        mostrarImagen(fotoOriginal);
    }

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    private void cambiarFoto(ActionEvent event) {
        if (usuario == null) return;
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Fotografía");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
        File archivo = fc.showOpenDialog(imageFoto.getScene().getWindow());
        if (archivo == null) return;

        try {
            String ruta = archivoService.guardarArchivo(archivo);
            usuario.setRutafoto(ruta);
            mostrarImagen(ruta);
        } catch (Exception e) {
            UtilController.error(e);
        }
    }

    private void mostrarImagen(String rutaFoto) {
        if (rutaFoto != null && !rutaFoto.isBlank()) {
            File archivo = new File(rutaFoto);
            if (archivo.exists()) {
                imageFoto.setImage(new Image(archivo.toURI().toString()));
            }
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (txtNombre.getText().isBlank() || txtApellido.getText().isBlank()
                || txtCorreo.getText().isBlank() || txtUsuario.getText().isBlank()
                || txtPassword.getText().isBlank()) {
            UtilController.advertencia("Todos los campos son obligatorios.");
            return;
        }

        usuario.setNombre(txtNombre.getText());
        usuario.setApellido(txtApellido.getText());
        usuario.setCorreo(txtCorreo.getText());
        usuario.setUsuario(txtUsuario.getText());
        usuario.setPassword(txtPassword.getText());

        guardado = true;
        UtilController.cerrarVentana(event);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        if (usuario != null) {
            usuario.setNombre(nombreOriginal);
            usuario.setApellido(apellidoOriginal);
            usuario.setCorreo(correoOriginal);
            usuario.setUsuario(usuarioOriginal);
            usuario.setPassword(passwordOriginal);
            usuario.setRutafoto(fotoOriginal);
        }
        guardado = false;
        UtilController.cerrarVentana(event);
    }
}
