package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Event of an user
 * 
 * @author rene-rekowski
 * @Vesion 1.0
 */
public class Event {
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private String description;
	// TODO: Gefühle und Personen wären gut
	private static List<Person> persons;
	
	/* Constucktor*/
	public Event(String name, String description, LocalDate startDate, LocalDate endDate, List<Person> persons) {
		validateeName(name);
		validateDescription(description);

		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		if (persons == null) {
			this.persons = persons;
		} else {
			this.persons = new ArrayList<Person>();
		}
	}

	/* getter and setter */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		validateeName(name);
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		validateDescription(description);
		this.description = description;
	}

	public List<Person> getPersons() {
		return new ArrayList<Person>(persons);
	}

	public void addPerson(Person person) {
		this.persons.add(person);
	}

	/* validate-methods */
	private void validateeName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if (name.length() > 30) {
			throw new IllegalArgumentException("name must shorter than 30 charaters");
		}
	}

	private void validateDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("description must not be null");
		}
		if (description.length() >= 500) {
			throw new IllegalArgumentException("description must shorter than 500 charater");
		}
	}

}
