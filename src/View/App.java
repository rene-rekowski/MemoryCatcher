package View;

import javafx.application.Application;
import javafx.stage.Stage;
import View.ViewManager;

/**
 * Einstiegspunkt der Anwendung.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MemoryCatcher");
        ViewManager viewManager = new ViewManager(primaryStage);
        viewManager.showLoginView();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

