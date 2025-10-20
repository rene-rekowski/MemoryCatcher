package view;

import controller.EventController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Event;
import model.User;
import view.pane.EventListPane;

/**
 * Startseite nach Login eines Users.
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class HomeView {

	private final ViewManager viewManager;
	private final EventController eventController;
	private final User currentUser;

	public HomeView(ViewManager viewManager, EventController eventController, User user) {
		this.viewManager = viewManager;
		this.eventController = eventController;
		this.currentUser = user;
	}

	public Scene createScene() {
		Label title = new Label("Hello " + currentUser.getName() + "! Birthday: " + currentUser.getBirthday());

		Button addEventButton = new Button("Add Event");
		Button createPerson = new Button("add Person");
		Button timelineButton = new Button("timeline");
		Button detailButton = new Button("User Details");
		Button deleteUserButton = new Button("Delete User");
		Button logoutButton = new Button("Logout");

		addEventButton.setOnAction(e -> viewManager.showAddEventView());
		createPerson.setOnAction(e -> viewManager.showCreatePerson());
		timelineButton.setOnAction(e -> viewManager.showTimelineView());
		detailButton.setOnAction(e -> viewManager.showUserDetailView(currentUser));
		logoutButton.setOnAction(e -> viewManager.goBack());

		EventListPane eventList = new EventListPane(eventController.getEvents(),
				new EventListPane.EventActionHandler() {
					@Override
					public void onDetail(Event event) {
						viewManager.showDetailEventView(event);
					}

					@Override
					public void onDelete(Event event) {
						eventController.deleteEvent(event);
					}
				});
	
		
		VBox buttonPane = new VBox(15, title, addEventButton, createPerson, timelineButton,
				detailButton,	deleteUserButton, logoutButton);
		BorderPane root = new BorderPane();
		root.setLeft(buttonPane);
		root.setRight(eventList);
		root.setBottom(logoutButton);
				
		return viewManager.createStandardScene(root);
	}
}
