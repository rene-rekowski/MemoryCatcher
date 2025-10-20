package view;

import controller.UserController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import model.User;

/**
 * Startbildschirm: zeigt alle User an und erlaubt das Login.
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class LogInView implements View{

	private final ViewManager viewManager;
	private final UserController userController;

	public LogInView(ViewManager viewManager, UserController userController) {
		this.viewManager = viewManager;
		this.userController = userController;
	}

	public Scene createScene() {
		Label title = new Label("MemoryCatcher");
		Button createUserButton = new Button("Create User");
		Button exitButton = new Button("Exit");
		ListView<User> userListView = new ListView<>(userController.getUsers());

		createUserButton.setOnAction(e -> viewManager.showCreateUserView());
		exitButton.setOnAction(e -> Platform.exit());

		userListView.setCellFactory(listView -> new ListCell<>() {
			@Override
			protected void updateItem(User user, boolean empty) {
				super.updateItem(user, empty);
				if (empty || user == null) {
					setGraphic(null);
				} else {
					Button loginButton = new Button(user.getName());
					loginButton.setMaxWidth(Double.MAX_VALUE);
					loginButton.setOnAction(e -> {
						viewManager.showHomeView(user);
					});
					setGraphic(loginButton);
				}
			}
		});

		VBox root = new VBox(10, title, userListView, createUserButton, exitButton);
		return viewManager.createStandardScene(root);
	}
}
