package View;


import Controller.UserController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import Model.User;

/**
 * Startbildschirm: zeigt alle User an und erlaubt das Login.
 */
public class LogInView {

    private final ViewManager viewManager;
    private final UserController userController;

    public LogInView(ViewManager viewManager, UserController userController) {
        this.viewManager = viewManager;
        this.userController = userController;
    }

    public Scene createScene() {
        Label title = new Label("MemoryCatcher");
        Button createUserButton = new Button("Create User");
        Button exitButton = new Button("Exit");
        ListView<User> userListView = new ListView<>(userController.getUsers());

        createUserButton.setOnAction(e -> viewManager.showCreateUserView());
        exitButton.setOnAction(e -> Platform.exit());

        userListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setGraphic(null);
                } else {
                    Button loginButton = new Button(user.getName());
                    loginButton.setMaxWidth(Double.MAX_VALUE);
                    loginButton.setOnAction(e -> {
                        try {
                            viewManager.showHomeView(user);
                        } catch (InvaildNameException | InvaildDescriptionException ex) {
                            showError("Fehler beim Laden", ex.getMessage());
                        }
                    });
                    setGraphic(loginButton);
                }
            }
        });

        VBox root = new VBox(10, title, userListView, createUserButton, exitButton);
        return new Scene(root, 600, 300);
    }

    private void showError(String header, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
