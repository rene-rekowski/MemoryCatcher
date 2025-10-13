package view;

import Controller.EventController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import model.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TimelineView {

    private final EventController eventController;
    private final ViewManager viewManager; // hinzugefügt

    public TimelineView( ViewManager viewManager, EventController eventController) {
        this.eventController = eventController;
        this.viewManager = viewManager;
    }

    public Scene createTimelineScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Event Timeline");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Timeline-Container
        HBox timelineBox = new HBox();
        timelineBox.setSpacing(10);
        timelineBox.setPadding(new Insets(20));
        timelineBox.setStyle("-fx-background-color: #f0f0f0;");

        // Berechne min/max Datum der Events
        List<Event> events = eventController.getEvents();
        if (events.isEmpty()) {
            timelineBox.getChildren().add(new Label("Keine Events vorhanden."));
        } else {
            LocalDate minDate = events.stream()
                    .map(Event::getStartDate)
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());

            LocalDate maxDate = events.stream()
                    .map(e -> e.getEndDate() != null ? e.getEndDate() : e.getStartDate())
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.now());

            long totalMonths = ChronoUnit.MONTHS.between(
                    minDate.withDayOfMonth(1),
                    maxDate.withDayOfMonth(1)
            ) + 1;

            double scale = 100; // 1 Monat = 100px

            // Zeitskala erstellen (Monate)
            HBox scaleBox = new HBox();
            scaleBox.setSpacing(0);
            for (int i = 0; i < totalMonths; i++) {
                LocalDate monthDate = minDate.plusMonths(i);
                Label monthLabel = new Label(monthDate.getMonth().name() + " " + monthDate.getYear());
                monthLabel.setPrefWidth(scale);
                monthLabel.setStyle("-fx-border-color: gray; -fx-border-width: 0 1 0 0; -fx-alignment: center;");
                scaleBox.getChildren().add(monthLabel);
            }

            VBox timelineContainer = new VBox(5, scaleBox, timelineBox);

            // Events positionieren
            for (Event e : events) {
                LocalDate start = e.getStartDate();
                LocalDate end = e.getEndDate() != null ? e.getEndDate() : start;

                long monthsFromStart = ChronoUnit.MONTHS.between(minDate.withDayOfMonth(1), start.withDayOfMonth(1));
                long eventDuration = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), end.withDayOfMonth(1)) + 1;

                VBox eventBox = new VBox(5);
                eventBox.setPadding(new Insets(5));
                eventBox.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                eventBox.setPrefWidth(eventDuration * scale);

                Label nameLabel = new Label(e.getName());
                Label dateLabel = new Label(start + " - " + end);
                eventBox.getChildren().addAll(nameLabel, dateLabel);

                // Abstand vor dem Event
                Region spacer = new Region();
                spacer.setPrefWidth(monthsFromStart * scale);
                timelineBox.getChildren().addAll(spacer, eventBox);
            }

            // ScrollPane für horizontales Scrollen
            ScrollPane scrollPane = new ScrollPane(timelineContainer);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(false);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);

            root.getChildren().addAll(title, scrollPane);
        }

        // Back-Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> viewManager.showHomeView(eventController.getUser()));
        root.getChildren().add(backButton);

        return new Scene(root, 900, 400);
    }
}
