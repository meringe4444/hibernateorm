package ni.edu.uam.hibernateorm.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class UtilController {

    public static void info(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, "Información", mensaje);
    }

    public static void advertencia(String mensaje) {
        mostrar(Alert.AlertType.WARNING, "Validación", mensaje);
    }

    public static void error(Exception e) {
        Throwable causa = e.getCause() != null ? e.getCause() : e;
        String mensaje = causa.getMessage() != null ? causa.getMessage() : causa.toString();
        mostrar(Alert.AlertType.ERROR, "Error", mensaje);
    }

    public static boolean confirmar(String pregunta) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, pregunta, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        Optional<ButtonType> resp = alert.showAndWait();
        return resp.isPresent() && resp.get() == ButtonType.YES;
    }

    public static void cerrarVentana(ActionEvent event) {
        if (event.getSource() instanceof Node) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }

    private static void mostrar(Alert.AlertType type, String title, String mensaje) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
