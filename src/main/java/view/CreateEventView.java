package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.EventController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Person;
import view.utils.AlertUtil;

/**
 * CreateEventView create a event and add it to the user
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class CreateEventView implements View {

	private final ViewManager viewManager;
	private final EventController eventController;

	public CreateEventView(ViewManager viewManager, EventController eventController) {
		this.eventController = eventController;
		this.viewManager = viewManager;
	}

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
		backButton.setOnAction(e -> viewManager.goBack());
		
		//TODO: sepation of the Pane
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

			try {
				eventController.addEvent(name, description, startDate, endDate, selectedPersons);
				AlertUtil.showInfo("Success", "Event successfully added!");
			} catch (IllegalArgumentException ex) {
				AlertUtil.showError("invaild Argument", ex.getMessage());
			}
			nameField.clear();
			descriptionArea.clear();
			selectedPersons.clear();
			selectedListView.getItems().clear();
			//TODO: clear Datepicker
		});

		VBox root = new VBox(10, titleLabel, nameField, startPicker, endPicker, descriptionArea, personComboBox,
				selectedListView, saveButton, backButton);

		return viewManager.createStandardScene(root);
	}
}