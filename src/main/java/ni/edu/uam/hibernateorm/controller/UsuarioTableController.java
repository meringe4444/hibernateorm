package ni.edu.uam.hibernateorm.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ni.edu.uam.hibernateorm.Main;
import ni.edu.uam.hibernateorm.model.Usuario;
import ni.edu.uam.hibernateorm.service.NavegationService;
import ni.edu.uam.hibernateorm.service.SesionService;
import ni.edu.uam.hibernateorm.service.UsuarioService;

import java.io.IOException;

public class UsuarioTableController {

    @FXML
    private TableView<Usuario> tableUsuario;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colApellido;
    @FXML
    private TableColumn<Usuario, String> colCorreo;
    @FXML
    private TableColumn<Usuario, String> colUsuario;
    @FXML
    private TableColumn<Usuario, Void> colAcciones;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Label lblUsuarioLogueado;
    @FXML
    private Label lblTotalUsuarios;

    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private FilteredList<Usuario> listaFiltrada;

    private final UsuarioService usuarioService = new UsuarioService();
    private final NavegationService navegationService = new NavegationService();

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNombre()));
        colApellido.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getApellido()));
        colCorreo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCorreo()));
        colUsuario.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUsuario()));

        configurarColumnaAcciones();

        if (lblUsuarioLogueado != null && SesionService.haySesionActiva()) {
            Usuario u = SesionService.getUsuarioAutenticado();
            String nombre = u.getNombre() != null ? u.getNombre() : u.getUsuario();
            String apellido = u.getApellido() != null ? " " + u.getApellido() : "";
            lblUsuarioLogueado.setText((nombre + apellido).trim());
        }

        listaFiltrada = new FilteredList<>(listaUsuarios, u -> true);
        tableUsuario.setItems(listaFiltrada);

        txtBuscar.textProperty().addListener((obs, oldV, nuevo) -> {
            String f = nuevo == null ? "" : nuevo.trim().toLowerCase();
            listaFiltrada.setPredicate(u -> f.isEmpty()
                    || contiene(u.getNombre(), f)
                    || contiene(u.getApellido(), f)
                    || contiene(u.getCorreo(), f)
                    || contiene(u.getUsuario(), f));
            actualizarContador();
        });

        cargarUsuarios();
    }

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Modificar");
            private final Button btnEliminar = new Button("Borrar");
            private final HBox box = new HBox(8, btnEditar, btnEliminar);

            {
                box.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color: #f1e1d0; -fx-border-color: #e1c5ac; -fx-border-radius: 14px; -fx-background-radius: 14px; -fx-text-fill: #945333; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4px 12px; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #f4e4dc; -fx-border-color: #e9c9bc; -fx-border-radius: 14px; -fx-background-radius: 14px; -fx-text-fill: #a14d38; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4px 12px; -fx-cursor: hand;");

                btnEditar.setOnAction(e -> ejecutarEdicion(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> ejecutarEliminacion(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private boolean contiene(String valor, String filtro) {
        return valor != null && valor.toLowerCase().contains(filtro);
    }

    public void cargarUsuarios() {
        try {
            listaUsuarios.setAll(usuarioService.findAll());
            actualizarContador();
        } catch (Exception e) {
            UtilController.error(e);
        }
    }

    private void actualizarContador() {
        if (lblTotalUsuarios != null && listaFiltrada != null) {
            int total = listaFiltrada.size();
            lblTotalUsuarios.setText(total == 1 ? "1 registro" : total + " registros");
        }
    }

    @FXML
    private void nuevoRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("registro.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ámbar - Registrar Usuario");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            UtilController.error(e);
        }
        cargarUsuarios();
    }

    private void ejecutarEdicion(Usuario seleccionado) {
        if (seleccionado == null) return;
        if (abrirModal(seleccionado)) {
            try {
                usuarioService.update(seleccionado);
                UtilController.info("Registro actualizado correctamente.");
            } catch (Exception e) {
                UtilController.error(e);
            }
            cargarUsuarios();
        }
    }

    private void ejecutarEliminacion(Usuario seleccionado) {
        if (seleccionado == null) return;
        String pregunta = "¿Está seguro de eliminar a " + seleccionado.getNombre() + " " + seleccionado.getApellido() + "?";
        if (UtilController.confirmar(pregunta)) {
            try {
                usuarioService.delete(seleccionado.getId());
                UtilController.info("Usuario eliminado correctamente.");
            } catch (Exception e) {
                UtilController.error(e);
            }
            cargarUsuarios();
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            SesionService.cerrarSesion();
            navegationService.cambiarVista("login.fxml", txtBuscar);
        } catch (Exception e) {
            UtilController.error(e);
        }
    }

    private boolean abrirModal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("usuario-modal.fxml"));
            Parent root = loader.load();
            UsuarioModalController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ámbar - Editar Perfil");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            return controller.isGuardado();
        } catch (IOException e) {
            UtilController.error(e);
            return false;
        }
    }
}