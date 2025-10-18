package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * reprensted a user that rember events and persons in that life
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class User {
	private final String name;
	private final LocalDate birthday;
	private final List<Event> events;
	private ObservableList<Person> persons ;

	/* Constructor */
	public User(String name, LocalDate birthday, List<Event> events, ObservableList<Person> persons) {
		validateName(name);
		if (birthday == null) {
			throw new IllegalArgumentException("Geburtsdatum darf nicht null sein.");
		}
		
		this.name = name;
		this.birthday = birthday;
		
		if (events == null) {
			this.events = new ArrayList<>();
		} else {
			this.events = events;
		}
		if (persons == null) {
			this.persons = FXCollections.observableArrayList();
		} else {
			this.persons = persons;
		}
	}

	public User(String name, LocalDate birthday) {
		this(name, birthday, null, null);
	}

	/* setter an getter */
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
	public ObservableList<Person> getPersons() {
		return this.persons;
	}

	/* validate */
	public void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("name must not empty");
		}
		if ( name.length() >= 30) {
			throw new IllegalArgumentException("name must not longer than 30 charackter");
		}
	}

	@Override
	public String toString() {
		return name + " (" + birthday + ")";
	}
}
