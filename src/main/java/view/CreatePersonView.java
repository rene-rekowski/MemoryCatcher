package view;

import controller.EventController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/*
 * CreatePersonView can the user create Person
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class CreatePersonView {

	private final ViewManager viewManager;
	private final EventController eventController;

	public CreatePersonView(ViewManager viewManager, EventController eventController) {
		this.viewManager = viewManager;
		this.eventController = eventController;
	}

	public Scene createSccene() {
		Label titleLabel = new Label("creat a new Person");

		TextField nameField = new TextField();
		nameField.setPromptText("Person name");

		TextArea descriptionArea = new TextArea();
		descriptionArea.setPromptText("description");

		Button saveButton = new Button("Save Person");
		Button backButton = new Button("Back");

		saveButton.setOnAction(e -> {
			String name = nameField.getText().trim();
			String description = descriptionArea.getText().trim();

			if (name.isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Missing Name", "Please enter an event name.");
				return;
			}

			if (description.isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Missing Description", "Please enter a description.");
				return;
			}

			eventController.addPerson(name, description);
			nameField.clear();
			descriptionArea.clear();
		});

		backButton.setOnAction(e -> viewManager.showHomeView(eventController.getUser()));
		;

		VBox root = new VBox(20, titleLabel, nameField, descriptionArea, saveButton, backButton);
		return new Scene(root, 600, 400);

	}

	/**
	 * Zeigt eine einfache Alert-Nachricht an.
	 */
	private void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
