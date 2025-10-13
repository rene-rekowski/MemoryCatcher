package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.Person;
import model.User;

public class EventController {
	private final User user;
	private final ObservableList<Event> events;

	public EventController(User user) {
		this.user = user;
		this.events = FXCollections.observableArrayList(user.getEvents());
		loadPersons();
		load();
	}

	public void addEvent(String name, String description, LocalDate startDate, LocalDate endDate,  List<Person> persons) {
		String sql = "INSERT INTO events (user_id, name, description, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = DatabaseManager.getConnection()) {
	        conn.setAutoCommit(false);

	        // 1. Event speichern
	        try (var pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
	            pstmt.setInt(1, getUserId());
	            pstmt.setString(2, name);
	            pstmt.setString(3, description);
	            pstmt.setString(4, startDate != null ? startDate.toString() : null);
	            pstmt.setString(5, endDate != null ? endDate.toString() : null);
	            pstmt.executeUpdate();

	            // Event-ID holen
	            var rs = pstmt.getGeneratedKeys();
	            if (rs.next()) {
	                int eventId = rs.getInt(1);

	                // 2. Personen-IDs heraussuchen & Verknüpfung speichern
	                String insertLinkSQL = "INSERT INTO event_persons (event_id, person_id) VALUES (?, ?)";
	                try (var linkStmt = conn.prepareStatement(insertLinkSQL)) {
	                    for (Person p : persons) {
	                        int personId = getPersonIdByName(conn, p.getName());
	                        if (personId != -1) {
	                            linkStmt.setInt(1, eventId);
	                            linkStmt.setInt(2, personId);
	                            linkStmt.addBatch();
	                        }
	                    }
	                    linkStmt.executeBatch();
	                }
	            }
	        }

	        conn.commit();

	        // Event-Objekt auch im Speicher ergänzen
	        Event event = new Event(name, description, startDate, endDate, persons);
	        events.add(event);
	        user.addEvent(event);

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Hinzufügen des Events: " + e.getMessage());
	    }
	}
	
	private int getPersonIdByName(Connection conn, String name) throws SQLException {
	    String sql = "SELECT id FROM persons WHERE name = ? AND user_id = ?";
	    try (var pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, name);
	        pstmt.setInt(2, getUserId());
	        var rs = pstmt.executeQuery();
	        if (rs.next()) return rs.getInt("id");
	    }
	    return -1;
	}
	
	/**
	 * Fügt eine Person in die Datenbank ein und speichert sie im User.
	 */
	public void addPerson(String name, String description) {
	    String sql = "INSERT INTO persons (user_id, name, description) VALUES (?, ?, ?)";

	    try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, getUserId());
	        pstmt.setString(2, name);
	        pstmt.setString(3, description);
	        pstmt.executeUpdate();

	        Person newPerson = new Person(name, description);
	        user.getPersons().add(newPerson);

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Hinzufügen der Person: " + e.getMessage());
	    }
	}
	
	/**
	 * Lädt alle Personen des Users aus der Datenbank.
	 */
	public void loadPersons() {
	    user.getPersons().clear();

	    String sql = "SELECT name, description FROM persons WHERE user_id = ?";

	    try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, getUserId());
	        var rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            user.getPersons().add(new Person(name, description));
	        }

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Laden der Personen: " + e.getMessage());
	    }
	}


	private int getUserId() {
		String sql = "SELECT id FROM users WHERE name = ?";
		try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, user.getName());
			var rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}

		} catch (SQLException e) {
			System.err.println("Fehler beim Holen der User-ID: " + e.getMessage());
		}
		return -1; // nicht gefunden
	}



	/**
	 * delete one spezifik event
	 * 
	 * @param event event to delete
	 */
	public void deleteEvent(Event event) {
		String sql = "DELETE FROM events WHERE user_id = ? AND name = ?";
		try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, getUserId());
			pstmt.setString(2, event.getName());
			pstmt.executeUpdate();

			events.remove(event);
			user.getEvents().remove(event);

		} catch (SQLException e) {
			System.err.println("Fehler beim Löschen des Events: " + e.getMessage());
		}
	}

	public void deleteAll() {
		String sql = "DELETE FROM events WHERE user_id = ?";
		try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, getUserId());
			pstmt.executeUpdate();

			events.clear();
			user.clearEvents();

		} catch (SQLException e) {
			System.err.println("Fehler beim Löschen aller Events: " + e.getMessage());
		}
	}

	/**
	 * Lädt alle Events aus der CSV-Datei für diesen User.
	 */
	public void load() {
	    events.clear();
	    String sql = "SELECT name, description, start_date, end_date FROM events WHERE user_id = ?";

	    try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, getUserId());
	        var rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String name = rs.getString("name");
	            String description = rs.getString("description");
	            LocalDate startDate = LocalDate.parse(rs.getString("start_date"));

	            // end_date korrekt laden
	            LocalDate endDate = null;
	            String endStr = rs.getString("end_date");
	            if (endStr != null) {
	                endDate = LocalDate.parse(endStr);
	            }

	            Event e = new Event(name, description, startDate, endDate, null);
	            events.add(e);
	            user.addEvent(e);
	        }

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Laden der Events: " + e.getMessage());
	    }
	}


	/* setter and setter */
	
	/**
	 * Gibt den aktuell angemeldeten Benutzer zurück.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Gibt alle Events des Benutzers zurück.
	 */
	public ObservableList<Event> getEvents() {
		return events;
	}

}
