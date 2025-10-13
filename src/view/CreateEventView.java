package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.EventController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Person;

/**
 * Die AddEventView ermöglicht es dem Benutzer, ein neues Event anzulegen.
 */
public class CreateEventView {
	
	private final ViewManager viewManager;
	private final EventController eventController;
	

	/**
	 * Erstellt die View zum Hinzufügen eines Events.
	 * 
	 * @param eventController Controller zur Verwaltung der Events
	 * @param homeView        Referenz auf die HomeView (für Navigation zurück)
	 */
	public CreateEventView(ViewManager viewManager,EventController eventController) {
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

		TextField nameField = new TextField();
		nameField.setPromptText("Event name");
		
		DatePicker startPicker = new DatePicker();
		DatePicker endPicker = new DatePicker();

		TextArea descriptionArea = new TextArea();
		descriptionArea.setPromptText("Event description");
		descriptionArea.setWrapText(true);

		Button saveButton = new Button("Save Event");
		Button backButton = new Button("Back");

		

		backButton.setOnAction(e -> viewManager.showHomeView(eventController.getUser()));
		
		List<Person> selectedPersons = new ArrayList<>();
		// ComboBox zur Auswahl von Personen
        ComboBox<Person> personComboBox = new ComboBox<>();
        personComboBox.getItems().addAll(eventController.getUser().getPersons());
        personComboBox.setPromptText("Select person to add");

        // ListView zur Anzeige der ausgewählten Personen
        ListView<Person> selectedListView = new ListView<>();
        selectedListView.setPlaceholder(new Label("No persons added yet"));

        // Wenn eine Person in der ComboBox ausgewählt wird → zur Liste hinzufügen
        personComboBox.setOnAction(e -> {
            Person selected = personComboBox.getValue();
            if (selected != null && !selectedPersons.contains(selected)) {
                selectedPersons.add(selected);
                selectedListView.getItems().add(selected);
            }
            // ComboBox zurücksetzen, damit man dieselbe Person nicht nochmal wählt
            personComboBox.getSelectionModel().clearSelection();
        });
		
        saveButton.setOnAction(e -> {
			String name = nameField.getText().trim();
			String description = descriptionArea.getText().trim();
			LocalDate startDate = startPicker.getValue();
			LocalDate endDate = endPicker.getValue();

			if (name.isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Missing Name", "Please enter an event name.");
				return;
			}

			eventController.addEvent(name, description, startDate, endDate, selectedPersons);
			showAlert(Alert.AlertType.INFORMATION, "Success", "Event successfully added!");
			nameField.clear();
		    descriptionArea.clear();
		    selectedPersons.clear();
		    selectedListView.getItems().clear();
		});
        
		// Layout
		VBox root = new VBox(10, titleLabel, nameField,startPicker, endPicker, descriptionArea, personComboBox, selectedListView, saveButton, backButton);
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