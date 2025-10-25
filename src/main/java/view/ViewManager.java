package view;

import java.util.ArrayDeque;
import java.util.Deque;

import controller.EventController;
import controller.UserController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Event;
import model.User;
/**
 * Coordinate the between the views and handle the logic
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class ViewManager {

	private final Stage stage;
	private final UserController userController;
	private EventController eventController;
	
	private final Deque<Scene> sceneHistory = new ArrayDeque<>(); //history to go back on the preview scene
	
	private static final int SCENE_WIDTH = 800;
	private static final int SCENE_HEIGHT = 600;

	public ViewManager(Stage stage) {
		this.stage = stage;
		this.userController = new UserController();
		userController.load();
	}
	
	public void goBack() {
	    if (!sceneHistory.isEmpty()) {
	        Scene previous = sceneHistory.pop();
	        stage.setScene(previous);
	    } else {
	        System.out.println("Keine vorherige Scene vorhanden.");
	    }
	}
	
	private void setScene(Scene newScene) {
	    Scene current = stage.getScene();
	    //rember previewly scene
	    if (current != null) {
	        sceneHistory.push(current);
	    }
	    newScene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
	    stage.setScene(newScene);
	}
	
	public void showLoginView() {
		LogInView logInView = new LogInView(this, userController);
		setScene(logInView.createScene());
	}

	public void showCreateUserView() {
		CreateUserView createUserView = new CreateUserView(this, userController);
		setScene(createUserView.createScene());
	}

	public void showHomeView(User user) {
		eventController = new EventController(user);
		HomeView homeView = new HomeView(this, eventController, user);
		setScene(homeView.createScene());
	}

	public void showAddEventView() {
		CreateEventView createEventView = new CreateEventView(this, eventController);
		setScene(createEventView.createScene());
	}

	public void showUserDetailView(User user) {
		DetailUserView userDetailView = new DetailUserView(this, user);
		setScene(userDetailView.createScene());
	}

	public void showDetailEventView(Event event) {
		DetailEventView detailEventView = new DetailEventView(this, eventController, event);
		setScene(detailEventView.createScene());
	}

	public void showEditEventView(Event event) {
		EditEventView editEventView = new EditEventView(this, eventController, event);
		setScene(editEventView.createScene());
	}

	public void showCreatePerson() {
		CreatePersonView createPersonView = new CreatePersonView(this, eventController);
		setScene(createPersonView.createScene());
	}

	public void showTimelineView() {
		TimelineView timelineView = new TimelineView(this, eventController);
		setScene(timelineView.createScene());
	}

	public Stage getStage() {
		return stage;
	}
	
	public Scene createStandardScene(Parent root) {
	    Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
	    return scene;
	}

}