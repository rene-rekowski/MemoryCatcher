package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.User;

/**
 * View mit Benutzerinformationen.
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class DetailUserView {

	private final ViewManager viewManager;
	private final User user;

	public DetailUserView(ViewManager viewManager, User user) {
		this.viewManager = viewManager;
		this.user = user;
	}

	public Scene createScene() {
		Label title = new Label("User Details");
		Label name = new Label("Name: " + user.getName());
		Label birthday = new Label("Birthday: " + user.getBirthday());

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> viewManager.goBack());

		VBox root = new VBox(10, title, name, birthday, backButton);
		return viewManager.createStandardScene(root);
	}
}
