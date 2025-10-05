package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the application
 * @author rene-rekowski
 * @version 1.0
 */
public class User {
	/** the name of the user (must not be null or empty). */
	private String name;
	
	/** A list containing all Events by this user */
	private List<Event> events;
	
	public User(String name) {
		if(name == null || name.isBlank()) {
			throw new IllegalArgumentException("User name must not be null or empty");
		}
		this.name = name;
		this.events = new ArrayList<Event>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if(name == null || name.isBlank()) {
			throw new IllegalArgumentException("User name must not be null or empty");
		}
		this.name = name;
	}
	
	public List<Event> getEvents(){
		return events;
	}
	
	public void addEvent(Event newEvent) {
		if(newEvent == null) {
			throw new IllegalArgumentException("Event must not be Null");
		}
		this.events.add(newEvent);
	}
	
	public boolean removeEvent(Event event){
		if(event == null) {
			return false;
		}
		return this.events.remove(event);
	}
	
	@Override
	public String toString() {
		return "User{name='" + name +"'}";
	}

}
