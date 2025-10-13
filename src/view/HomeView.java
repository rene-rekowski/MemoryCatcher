package view;

import Controller.EventController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.User;

/**
 * Startseite nach Login eines Users.
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
        Label title = new Label("Hello " + currentUser.getName() +
                "! Birthday: " + currentUser.getBirthday());

        Button addEventButton = new Button("Add Event");
        Button showEventsButton = new Button("Show Events");
        Button createPerson = new Button("add Person");
        Button timelineButton = new Button("timeline");
        Button detailButton = new Button("User Details");
        Button deleteUserButton = new Button("Delete User");
        Button logoutButton = new Button("Logout");

        addEventButton.setOnAction(e -> viewManager.showAddEventView());
        showEventsButton.setOnAction(e -> viewManager.showShowView());
        createPerson.setOnAction(e-> viewManager.showCreatePerson());
        timelineButton.setOnAction(e-> viewManager.showTimelineView());
        detailButton.setOnAction(e -> viewManager.showUserDetailView(currentUser));
        logoutButton.setOnAction(e -> viewManager.exitToLogin());

        VBox root = new VBox(15, title, addEventButton, showEventsButton,createPerson, timelineButton,
                detailButton, deleteUserButton, logoutButton);

        return new Scene(root, 600, 400);
    }
}
