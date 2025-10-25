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
 * CreatePersonView create person for the user
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class CreatePersonView implements View {

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
			try {
				eventController.addPerson(name, description);
				nameField.clear();
				descriptionArea.clear();
			} catch (IllegalArgumentException ex) {
				AlertUtil.showError("invaild Argument", ex.getMessage());
			}
		});
		backButton.setOnAction(e -> viewManager.goBack());
		;

		VBox root = new VBox(20, titleLabel, nameField, descriptionArea, saveButton, backButton);
		return viewManager.createStandardScene(root);

	}
}
