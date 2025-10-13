package view;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import model.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimelineView extends Pane {

    public TimelineView(List<Event> events) {
        int width = 800;
        int height = 400;
        int offset = 50;

        this.setStyle("-fx-background-color: white;");
        this.setPrefSize(width, height);

        if (events.isEmpty()) return;

        // === Zeitspanne bestimmen ===
        LocalDate min = events.stream().map(Event::getStartDate)
                .min(LocalDate::compareTo).orElse(LocalDate.now());
        LocalDate max = events.stream().map(Event::getEndDate)
                .max(LocalDate::compareTo).orElse(LocalDate.now());
        long totalDays = ChronoUnit.DAYS.between(min, max);
        if (totalDays == 0) totalDays = 1;

        double availableWidth = width - 2.0 * offset;
        double barHeight = 20.0;
        double verticalSpacing = 35.0;

        // === Achse zeichnen ===
        Line axis = new Line(offset, height - offset, width - offset, height - offset);
        axis.setStroke(Color.DARKGRAY);
        axis.setStrokeWidth(3);
        this.getChildren().add(axis);

        // === Events nach Startdatum sortieren ===
        List<Event> sorted = new ArrayList<>(events);
        sorted.sort(Comparator.comparing(Event::getStartDate));

        List<LocalDate> rowEndDates = new ArrayList<>();

        for (Event event : sorted) {
            LocalDate start = event.getStartDate();
            LocalDate end = event.getEndDate() != null ? event.getEndDate() : start;

            long startOffset = ChronoUnit.DAYS.between(min, start);
            long endOffset = ChronoUnit.DAYS.between(min, end);

            double startX = offset + (startOffset / (double) totalDays) * availableWidth;
            double endX = offset + (endOffset / (double) totalDays) * availableWidth;
            double barWidth = Math.max(5, endX - startX);

            // freie Zeile suchen
            int row = 0;
            for (; row < rowEndDates.size(); row++) {
                if (rowEndDates.get(row).isBefore(start)) {
                    rowEndDates.set(row, end);
                    break;
                }
            }
            if (row == rowEndDates.size()) rowEndDates.add(end);

            double y = height - offset - (row + 1) * verticalSpacing;

            Rectangle bar = new Rectangle(startX, y, barWidth, barHeight);
            bar.setArcWidth(8);
            bar.setArcHeight(8);
            bar.setFill(Color.DODGERBLUE);
            bar.setStroke(Color.DARKBLUE);
            bar.setStrokeWidth(1.2);
            this.getChildren().add(bar);

            Label nameLabel = new Label(event.getName());
            nameLabel.setLayoutX(startX + 5);
            nameLabel.setLayoutY(y - 18);
            nameLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
            this.getChildren().add(nameLabel);
        }

        // Jahresmarkierungen
        int years = max.getYear() - min.getYear();
        for (int i = 0; i <= years; i++) {
            int year = min.getYear() + i;
            double x = offset + (i / (double) years) * availableWidth;

            Line tick = new Line(x, height - offset - 5, x, height - offset + 5);
            tick.setStroke(Color.GRAY);
            this.getChildren().add(tick);

            Label label = new Label(String.valueOf(year));
            label.setLayoutX(x - 15);
            label.setLayoutY(height - offset + 10);
            label.setStyle("-fx-font-size: 11px;");
            this.getChildren().add(label);
        }
    }
}
