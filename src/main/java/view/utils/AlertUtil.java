package view.utils;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility for simple Alert for the View
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class AlertUtil {

    private AlertUtil() {
    }

    public static void showInfo(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    public static void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
