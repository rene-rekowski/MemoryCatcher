package View;

import Controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * View zum Erstellen eines neuen Benutzers.
 */
public class CreateUserView {

    private final ViewManager viewManager;
    private final UserController userController;

    public CreateUserView(ViewManager viewManager, UserController userController) {
        this.viewManager = viewManager;
        this.userController = userController;
    }

    public Scene createScene() {
        Label title = new Label("Create User");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        DatePicker birthdayPicker = new DatePicker();
        birthdayPicker.setPromptText("Birthday");

        Button createButton = new Button("Create");
        Button cancelButton = new Button("Cancel");

        createButton.setOnAction(e -> {
            String name = nameField.getText();
            LocalDate birthday = birthdayPicker.getValue();

            // Validierung
            if (name == null || name.isBlank()) {
                showError("Ungültiger Name", "Der Name darf nicht leer sein.");
                return;
            }
            if (name.length() > 30) {
                showError("Ungültiger Name", "Der Name darf maximal 30 Zeichen haben.");
                return;
            }
            if (birthday == null) {
                showError("Ungültiges Geburtsdatum", "Bitte wähle ein Geburtsdatum aus.");
                return;
            }

            // Benutzer anlegen und speichern
            userController.addUser(name, birthday);
            userController.save();

            // Zurück zur Login-Ansicht (aktualisiert die Liste dort)
            viewManager.showLoginView();
        });

        cancelButton.setOnAction(e -> viewManager.showLoginView());

        VBox root = new VBox(12, title, nameField, birthdayPicker, createButton, cancelButton);
        root.setPrefSize(600, 300);
        return new Scene(root);
    }

    private void showError(String header, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Fehler");
        a.setHeaderText(header);
        a.setContentText(message);
        a.showAndWait();
    }
}