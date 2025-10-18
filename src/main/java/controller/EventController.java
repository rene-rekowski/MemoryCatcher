package controller;

import database.repository.EventRepository;
import database.repository.PersonRepository;
import database.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.Person;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventController {

    private final User user;
    private final ObservableList<Event> events;
    private final EventRepository eventRepo;
    private final PersonRepository personRepo;
    private final UserRepository userRepo;

    public EventController(User user) {
        this.user = user;
        this.events = FXCollections.observableArrayList();
        this.eventRepo = new EventRepository();
        this.personRepo = new PersonRepository();
        this.userRepo = new UserRepository();
        load();
        loadPersons();
    }

    public void addEvent(String name, String description, LocalDate start, LocalDate end, List<Person> persons) {
        Event e = new Event(name, description, start, end, persons);
        int userId = userRepo.findUserId(user);
        eventRepo.save(e, userId);
        events.add(e);
    }

    public void addPerson(String name, String description) {
        Person p = new Person(name, description);
        int userId = userRepo.findUserId(user);
        personRepo.save(p, userId);
        user.getPersons().add(p);
    }

    public void loadPersons() {
        int userId = userRepo.findUserId(user);
        user.getPersons().setAll(personRepo.findByUser(userId));
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public User getUser() {
        return user;
    }

    public void load() {
        events.clear();
        events.addAll(eventRepo.findByUser(user));
    }

    public void deleteEvent(Event event) {
        int userId = userRepo.findUserId(user);
        eventRepo.delete(event, userId);
        events.remove(event);
    }

}
