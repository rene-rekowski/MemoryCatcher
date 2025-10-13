package view;

import Controller.EventController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Event;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class TimelineView {

    private final EventController eventController;
    private final ViewManager viewManager; // Zum Öffnen von DetailViews

    public TimelineView(ViewManager viewManager, EventController eventController) {
        this.viewManager = viewManager;
        this.eventController = eventController;
    }

    public Scene createTimelineScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Event Timeline");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox timelineBox = new HBox(10);
        timelineBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(timelineBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        List<Event> events = eventController.getEvents();
        // Nach Startdatum sortieren
        events.sort(Comparator.comparing(Event::getStartDate));

        for (Event e : events) {
            VBox eventBox = new VBox(5);
            eventBox.setPadding(new Insets(10));
            eventBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: lightblue;");

            Label nameLabel = new Label(e.getName());
            Label dateLabel = new Label(e.getStartDate() + " - " + e.getEndDate());
            Label descLabel = new Label(e.getDescription());

            // Dauer-basiertes Rechteck (optional)
            long days = 1;
            if (e.getEndDate() != null && e.getStartDate() != null) {
                days = ChronoUnit.DAYS.between(e.getStartDate(), e.getEndDate());
                if (days <= 0) days = 1;
            }
            Rectangle durationRect = new Rectangle(days * 20, 20, Color.LIGHTGREEN);

            eventBox.getChildren().addAll(nameLabel, dateLabel, descLabel, durationRect);

            // Klick auf Event → DetailView
            eventBox.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                viewManager.showDetailEventView(e);
            });

            timelineBox.getChildren().add(eventBox);
        }

        root.getChildren().addAll(title, scrollPane);
        return new Scene(root, 800, 400);
    }
}
