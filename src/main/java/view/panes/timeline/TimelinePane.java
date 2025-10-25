package view.panes.timeline;

import javafx.scene.layout.Pane;
import model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class TimelinePane extends Pane{
	
	public TimelinePane(List<Event> events) {
        int width = 800;
        int height = 400;
        double offset = 50.0;

        this.setStyle("-fx-background-color: white;");
        this.setPrefSize(width, height);

        if (events == null || events.isEmpty()) return;

        LocalDate min = events.stream().map(Event::getStartDate)
                .min(LocalDate::compareTo).orElse(LocalDate.now());
        LocalDate max = events.stream().map(Event::getEndDate)
                .max(LocalDate::compareTo).orElse(LocalDate.now());

        double availableWidth = width - 2.0 * offset;
        double verticalSpacing = 35.0;

        // Achse hinzufügen
        TimelineAxis axis = new TimelineAxis(min, max, width, height, offset);
        getChildren().add(axis);

        // Events sortieren
        List<Event> sorted = new ArrayList<>(events);
        sorted.sort(Comparator.comparing(Event::getStartDate));

        List<LocalDate> rowEndDates = new ArrayList<>();

        for (Event event : sorted) {
            LocalDate start = event.getStartDate();
            LocalDate end = event.getEndDate() != null ? event.getEndDate() : start;

            double startX = TimelineUtils.dateToPosition(start, min, max, availableWidth, offset);
            double endX = TimelineUtils.dateToPosition(end, min, max, availableWidth, offset);
            double barWidth = Math.max(5, endX - startX);

            // Zeilenlogik
            int row = 0;
            for (; row < rowEndDates.size(); row++) {
                if (rowEndDates.get(row).isBefore(start)) {
                    rowEndDates.set(row, end);
                    break;
                }
            }
            if (row == rowEndDates.size()) rowEndDates.add(end);

            double y = height - offset - (row + 1) * verticalSpacing;

            TimelineEventBar bar = new TimelineEventBar(event, startX, y, barWidth);
            getChildren().add(bar);
        }
    }

}
