package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Repräsentiert einen Benutzer mit Namen, Geburtstag und einer Liste von Events.
 */
public class User {
    private final String name;
    private final LocalDate birthday;
    private final List<Event> events;

    public User(String name, LocalDate birthday) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name darf nicht leer sein.");
        }
        if (birthday == null) {
            throw new IllegalArgumentException("Geburtsdatum darf nicht null sein.");
        }
        this.name = name;
        this.birthday = birthday;
        this.events = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void clearEvents() {
        events.clear();
    }

    @Override
    public String toString() {
        return name + " (" + birthday + ")";
    }
}

