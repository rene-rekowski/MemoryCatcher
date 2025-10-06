package Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.User;

public class EventController {
    private final User user;
    private final List<Event> events;

    public EventController(User user) {
        this.user = user;
        this.events = new ArrayList<>(user.getEvents());
    }

    /**
     * Fügt ein neues Event hinzu.
     */
    public void addEvent(String name, String description, LocalDate startDate){
        Event newEvent = new Event(name, description, startDate);
        events.add(newEvent);
        user.addEvent(newEvent);
    }

    /**
     * Gibt alle Events des Benutzers zurück.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Entfernt alle Events des Benutzers.
     */
    public void deletAll() {
        events.clear();
        user.clearEvents();
    }

    /**
     * Gibt den aktuell angemeldeten Benutzer zurück.
     */
    public User getUser() {
        return user;
    }
}
