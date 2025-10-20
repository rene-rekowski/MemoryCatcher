package view;

import controller.EventController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import view.utils.AlertUtil;

/*
 * CreatePersonView can the user create Person
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class CreatePersonView implements View{

	private final ViewManager viewManager;
	private final EventController eventController;

	public CreatePersonView(ViewManager viewManager, EventController eventController) {
		this.viewManager = viewManager;
		this.eventController = eventController;
	}

	public Scene createScene() {
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
				AlertUtil.showWarning( "Missing Name", "Please enter an event name.");
				return;
			}

			if (description.isEmpty()) {
				AlertUtil.showWarning("Missing Description", "Please enter a description.");
				return;
			}

			eventController.addPerson(name, description);
			nameField.clear();
			descriptionArea.clear();
		});

		backButton.setOnAction(e -> viewManager.goBack());
		;

		VBox root = new VBox(20, titleLabel, nameField, descriptionArea, saveButton, backButton);
		return viewManager.createStandardScene(root);

	}
}
