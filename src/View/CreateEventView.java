package View;

import java.time.LocalDate;

import Controller.EventController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Die AddEventView ermöglicht es dem Benutzer, ein neues Event anzulegen.
 */
public class AddEventView {
	
	private final ViewManager viewManager;
	private final EventController eventController;
	

	/**
	 * Erstellt die View zum Hinzufügen eines Events.
	 * 
	 * @param eventController Controller zur Verwaltung der Events
	 * @param homeView        Referenz auf die HomeView (für Navigation zurück)
	 */
	public AddEventView(ViewManager viewManager,EventController eventController) {
		this.eventController = eventController;
		this.viewManager = viewManager;
	}

	/**
	 * Erstellt die Szene zum Hinzufügen eines Events.
	 * 
	 * @param primaryStage Die Hauptbühne der Anwendung
	 * @return Eine Scene mit den Eingabefeldern und Buttons
	 */
	public Scene createScene() {
		Label titleLabel = new Label("Add New Event");

		// Eingabefelder
		TextField nameField = new TextField();
		nameField.setPromptText("Event name");
		
		DatePicker startPicker = new DatePicker();

		TextArea descriptionArea = new TextArea();
		descriptionArea.setPromptText("Event description");
		descriptionArea.setWrapText(true);

		// Buttons
		Button saveButton = new Button("Save Event");
		Button backButton = new Button("Back");

		// Button-Aktionen
		saveButton.setOnAction(e -> {
			String name = nameField.getText().trim();
			String description = descriptionArea.getText().trim();
			LocalDate startDate = startPicker.getValue();

			if (name.isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Missing Name", "Please enter an event name.");
				return;
			}

			if (description.isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Missing Description", "Please enter a description.");
				return;
			}

			eventController.addEvent(name, description, startDate);
			showAlert(Alert.AlertType.INFORMATION, "Success", "Event successfully added!");
			nameField.clear();
			descriptionArea.clear();

		});

		backButton.setOnAction(e -> viewManager.showHomeView(eventController.getUser()));

		// Layout
		VBox root = new VBox(10, titleLabel, nameField, descriptionArea, saveButton, backButton);
		root.setPadding(new Insets(20));

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