package view;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;
import Database.DatabaseManager;

/**
 * Einstiegspunkt der Anwendung.
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

