package view.timeline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import model.Event;

/**
 * 
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class TimelineEventBar extends Pane {

    public TimelineEventBar(Event event, double startX, double y, double width) {
        Rectangle bar = new Rectangle(startX, y, width, 20);
        bar.setArcWidth(8);
        bar.setArcHeight(8);
        bar.setFill(Color.DODGERBLUE);
        bar.setStroke(Color.DARKBLUE);
        bar.setStrokeWidth(1.2);

        Label label = new Label(event.getName());
        label.setLayoutX(startX + 5);
        label.setLayoutY(y - 18);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        getChildren().addAll(bar, label);

        // optional: Hover-Effekt
        bar.setOnMouseEntered(e -> bar.setFill(Color.LIGHTBLUE));
        bar.setOnMouseExited(e -> bar.setFill(Color.DODGERBLUE));
    }
}
