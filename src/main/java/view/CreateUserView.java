package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

import controller.UserController;

//TODO: vaildierung ist not esseiy
/**
 * View zum Erstellen eines neuen Benutzers.
 * 
 * @author rene-rekowski
 * @version 1.0
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
			try {
				userController.addUser(name, birthday);
				viewManager.showLoginView();
			} catch (IllegalArgumentException ex) {
				showError("Invalid input: " + ex.getMessage());
			}
		});

		cancelButton.setOnAction(e -> viewManager.goBack());

		VBox root = new VBox(12, title, nameField, birthdayPicker, createButton, cancelButton);
		root.setPrefSize(600, 300);
		return new Scene(root);
	}

	private void showError(String message) {
		Alert a = new Alert(Alert.AlertType.ERROR);
		a.setTitle("Fehler");
		a.setContentText(message);
		a.showAndWait();
	}
}