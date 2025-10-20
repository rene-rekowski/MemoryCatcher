package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Event;

import java.time.LocalDate;

import controller.EventController;

/**
 * View zum Bearbeiten eines Events.
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class EditEventView {

	private final ViewManager viewManager;
	private final EventController eventController;
	private final Event event;

	public EditEventView(ViewManager viewManager, EventController eventController, Event event) {
		this.viewManager = viewManager;
		this.eventController = eventController;
		this.event = event;
	}

	public Scene createScene() {
		Label title = new Label("Edit Event");

		TextField nameField = new TextField(event.getName());
		DatePicker startDatePicker = new DatePicker(event.getStartDate());
		DatePicker endDatePicker = new DatePicker(event.getEndDate());
		TextArea descriptionArea = new TextArea(event.getDescription());

		Button saveButton = new Button("Save Changes");
		Button cancelButton = new Button("Cancel");

		saveButton.setOnAction(e -> {

			event.setName(nameField.getText());
			event.setStartDate(startDatePicker.getValue());
			event.setEndDate(endDatePicker.getValue());
			event.setDescription(descriptionArea.getText());
			viewManager.goBack();
		});

		cancelButton.setOnAction(e -> viewManager.goBack());

		VBox root = new VBox(10, title, nameField, startDatePicker, endDatePicker, descriptionArea, saveButton,
				cancelButton);
		return viewManager.createStandardScene(root);
	}

	private void showError(String msg) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}