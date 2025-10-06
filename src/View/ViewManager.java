package View;

import Controller.EventController;
import Controller.UserController;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * cordinate the between the views and handle the logic
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class ViewManager {
	private final Stage stage;
	private final UserController userController;
	private EventController eventController;
	
	private Scene loginInSecne;
	private Scene homeScene;
	
	public ViewManager(Stage stage) {
		this.stage = stage;
		this.userController = new UserController();
		userController.load();
	}
	
	public void showLogInView() {
		LogInView logInView = new LogInView(this, userController);
        loginScene = logInView.createScene();
        stage.setScene(loginScene);
	}
	
	public void showCreateUserView() {
        CreateUserView createUserView = new CreateUserView(this, userController);
        stage.setScene(createUserView.createScene());
    }
	
    public void showHomeView(User user) throws InvaildNameException, InvaildDescriptionException {
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
        UserDetailView userDetailView = new UserDetailView(this, user);
        stage.setScene(userDetailView.createScene());
    }
    
    public void exitToLogin() {
        stage.setScene(loginScene);
    }
    
    public Stage getStage() {
        return stage;
    }

}
