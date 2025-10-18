package view;

import controller.EventController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Event;

/**
 * View zum Anzeigen aller Events.
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class ShowView {

	private final ViewManager viewManager;
	private final EventController eventController;

	public ShowView(ViewManager viewManager, EventController eventController) {
		this.viewManager = viewManager;
		this.eventController = eventController;
	}

	public Scene createScene() {
		Label title = new Label("All Events");

		ListView<Event> eventList = new ListView<>(eventController.getEvents());
		eventList.setCellFactory(list -> new ListCell<>() {
			@Override
			protected void updateItem(Event event, boolean empty) {
				super.updateItem(event, empty);
				if (empty || event == null) {
					setText(null);
				} else {
					setText(event.getName() + " (" + event.getStartDate() + ")");
				}
			}
		});

		Button detailButton = new Button("Details");
		Button backButton = new Button("Back");

		detailButton.setOnAction(e -> {
			Event selected = eventList.getSelectionModel().getSelectedItem();
			if (selected != null) {
				viewManager.showDetailEventView(selected);
			} else {
				showInfo("Bitte wähle ein Event aus.");
			}
		});

		backButton.setOnAction(e -> viewManager.showHomeView(eventController.getUser()));

		VBox root = new VBox(10, title, eventList, detailButton, backButton);
		return new Scene(root, 600, 400);
	}

	private void showInfo(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(message);
		alert.showAndWait();
	}
}