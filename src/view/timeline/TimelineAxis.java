package view.timeline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.control.Label;

import java.time.LocalDate;

/**
 * 
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class TimelineAxis extends Pane {

	public TimelineAxis(LocalDate min, LocalDate max, double width, double height, double offset) {
		Line axis = new Line(offset, height - offset, width - offset, height - offset);
		axis.setStroke(Color.DARKGRAY);
		axis.setStrokeWidth(3);
		getChildren().add(axis);

		int years = max.getYear() - min.getYear();
		if (years == 0)
			years = 1; // vermeiden durch 0 Division

		double availableWidth = width - 2.0 * offset;

		for (int i = 0; i <= years; i++) {
			int year = min.getYear() + i;
			double x = offset + (i / (double) years) * availableWidth;

			Line tick = new Line(x, height - offset - 5, x, height - offset + 5);
			tick.setStroke(Color.GRAY);
			getChildren().add(tick);

			Label label = new Label(String.valueOf(year));
			label.setLayoutX(x - 15);
			label.setLayoutY(height - offset + 10);
			label.setStyle("-fx-font-size: 11px;");
			getChildren().add(label);
		}
	}
}
