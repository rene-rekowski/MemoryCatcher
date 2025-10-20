package view;

import controller.EventController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Event;

/**
 * View zur Anzeige der Details eines Events.
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class DetailEventView {

	private final ViewManager viewManager;
	private final EventController eventController;
	private final Event event;

	public DetailEventView(ViewManager viewManager, EventController eventController, Event event) {
		this.viewManager = viewManager;
		this.eventController = eventController;
		this.event = event;
	}

	public Scene createScene() {
		Label title = new Label("Event Details");
		Label name = new Label("Name: " + event.getName());
		Label start = new Label("Start: " + event.getStartDate());
		Label end = new Label("End: " + event.getEndDate());
		Label description = new Label("Description: " + event.getDescription());

		Button editButton = new Button("Edit");
		Button deleteButton = new Button("Delete");
		Button backButton = new Button("Back");

		editButton.setOnAction(e -> viewManager.showEditEventView(event));
		deleteButton.setOnAction(e -> {
			eventController.deleteEvent(event);
			viewManager.goBack();
		});
		backButton.setOnAction(e -> viewManager.goBack());

		VBox root = new VBox(10, title, name, start, end, description, editButton, deleteButton, backButton);

		return new Scene(root, 600, 400);
	}
}
