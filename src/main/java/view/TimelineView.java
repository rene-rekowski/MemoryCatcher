package view;


import controller.EventController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.pane.timeline.TimelinePane;

/**
 * View show event in a timeline
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class TimelineView {
	
	private EventController eventController;
	private ViewManager viewManager;
	
	public TimelineView(ViewManager viewManager, EventController eventController) {
		this.viewManager = viewManager;
		this.eventController = eventController;
	}
	
	
	public Scene createScene() {
        TimelinePane timeline = new TimelinePane(eventController.getEvents());
        Button back = new Button("Back");
        back.setOnAction(e-> viewManager.goBack());

        VBox root = new VBox(timeline, back);
        return new Scene(root, 800, 450);
    }
}
