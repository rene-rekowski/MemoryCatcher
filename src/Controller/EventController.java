package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventController {
    private final User user;
    private final ObservableList<Event> events;

    public EventController(User user) {
        this.user = user;
        this.events = FXCollections.observableArrayList(user.getEvents());
        load();
    }

    /**
     * Fügt ein neues Event hinzu.
     */
    public void addEvent(String name, String description, LocalDate startDate){
        Event newEvent = new Event(name, description, startDate);
        events.add(newEvent);
        user.addEvent(newEvent);
        save();
    }

    /**
     * Gibt alle Events des Benutzers zurück.
     */
    public ObservableList<Event> getEvents() {
        return events;
    }

    /**
     * Entfernt alle Events des Benutzers.
     */
    public void deletAll() {
        events.clear();
        user.clearEvents();
        save();
    }
    
    /**
     * delete one spezifik event
     * @param event event to delete
     */
    public void deleteEvent(Event event) {
    	events.remove(event);
    	save();
    }

    /**
     * Gibt den aktuell angemeldeten Benutzer zurück.
     */
    public User getUser() {
        return user;
    }
    
    private String getFileName() {
        // Datei: events_<username>.csv
        return "events_" + user.getName().replaceAll("\\s+", "_") + ".csv";
    }

    /**
     * Speichert alle Events des aktuellen Users in eine CSV-Datei.
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName()))) {
            for (Event event : events) {
                writer.write(event.getName() + ";" +
                             event.getDescription() + ";" +
                             event.getStartDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Events für " + user.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Lädt alle Events aus der CSV-Datei für diesen User.
     */
    public void load() {
        events.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFileName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String name = parts[0];
                    String description = parts[1];
                    LocalDate startDate = LocalDate.parse(parts[2]);
                    Event e = new Event(name, description, startDate);
                    events.add(e);
                    user.addEvent(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Keine gespeicherten Events für " + user.getName() + " gefunden (oder Datei leer).");
        }
    }
}
