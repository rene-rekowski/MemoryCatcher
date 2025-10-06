package View;

import Controller.EventController;
import Controller.UserController;
import Model.Event;
import Model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    private Scene loginScene;
    private Scene homeScene;

    public ViewManager(Stage stage) {
        this.stage = stage;
        this.userController = new UserController();
        userController.load();
    }

    public void showLoginView() {
        LogInView logInView = new LogInView(this, userController);
        loginScene = logInView.createScene();
        stage.setScene(loginScene);
    }

    public void showCreateUserView() {
        CreateUserView createUserView = new CreateUserView(this, userController);
        stage.setScene(createUserView.createScene());
    }

    public void showHomeView(User user) {
        eventController = new EventController(user);
        HomeView homeView = new HomeView(this, eventController, user);
        homeScene = homeView.createScene();
        stage.setScene(homeScene);
    }

    public void showAddEventView() {
        AddEventView addEventView = new AddEventView(this, eventController);
        stage.setScene(addEventView.createScene());
    }

    public void showShowView() {
        ShowView showView = new ShowView(this, eventController);
        stage.setScene(showView.createScene());
    }

    public void showUserDetailView(User user) {
        DetailUserView userDetailView = new DetailUserView(this, user);
        stage.setScene(userDetailView.createScene());
    }
    
    public void showDetailEventView(Event event) {
    	DetailEventView detailEventView = new DetailEventView(this, eventController, event);
    	stage.setScene(detailEventView.createScene());
    }
    
    public void showEditEventView(Event event) {
    	EditEventView editEventView = new EditEventView(this, eventController, event);
    	stage.setScene(editEventView.createScene());
    }

    public void exitToLogin() {
        stage.setScene(loginScene);
    }

    public Stage getStage() {
        return stage;
    }
}