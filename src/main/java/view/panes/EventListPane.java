package view.panes;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Event;

public class EventListPane extends VBox {

    private final ListView<Event> eventListView;
    private final Button detailButton;
    private final Button deleteButton;

    public interface EventActionHandler {
        void onDetail(Event event);
        void onDelete(Event event);
    }

    public EventListPane(ObservableList<Event> events, EventActionHandler handler) {
        eventListView = new ListView<>(events);
        eventListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setText(null);
                } else {
                    setText(event.getName() + " (" + event.getStartDate() + ")");
                }
            }
        });

        detailButton = new Button("Details");
        deleteButton = new Button("Löschen");

        detailButton.setOnAction(e -> {
            Event selected = eventListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handler.onDetail(selected);
            } else {
                showInfo("Bitte wähle ein Event aus.");
            }
        });

        deleteButton.setOnAction(e -> {
            Event selected = eventListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handler.onDelete(selected);
            } else {
                showInfo("Bitte wähle ein Event aus.");
            }
        });

        HBox buttonBar = new HBox(10, detailButton, deleteButton);
        this.getChildren().addAll(eventListView, buttonBar);
        this.setSpacing(10);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Event getSelectedEvent() {
        return eventListView.getSelectionModel().getSelectedItem();
    }
}
