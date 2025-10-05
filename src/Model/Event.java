package Model;

import java.time.LocalDate;

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

	public Event(String name, String description, LocalDate startDate) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if (name.length() > 30) {
			throw new IllegalArgumentException("name must shorter than 30 charaters");
		}
		if (description != null) {
			throw new IllegalArgumentException("description must not be null");
		}
		if (description.length() >= 500) {
			throw new IllegalArgumentException("description must shorter than 500 charater");
		}

		this.name = name;
		this.description = description;
		this.startDate = startDate;
	}

	// TODO: überladen konstruktor für endDate

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
