package view;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * start of the application
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
    	DatabaseManager.initialize();
    	
        primaryStage.setTitle("MemoryCatcher");
        ViewManager viewManager = new ViewManager(primaryStage);
        viewManager.showLoginView();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

