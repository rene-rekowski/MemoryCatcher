package Controller;

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
		load();
	}

	/**
	 * Fügt ein neues Event hinzu.
	 */
	public void addEvent(String name, String description, LocalDate startDate) {
		String sql = "INSERT INTO events (user_id, name, description, start_date) VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {

			int userId = getUserId(); // Methode um die ID des Users aus der DB zu holen
			pstmt.setInt(1, userId);
			pstmt.setString(2, name);
			pstmt.setString(3, description);
			pstmt.setString(4, startDate.toString());
			pstmt.executeUpdate();

			Event e = new Event(name, description, startDate);
			events.add(e);
			user.addEvent(e);

		} catch (SQLException e) {
			System.err.println("Fehler beim Hinzufügen des Events: " + e.getMessage());
		}
	}
	
	public void addPerson(String name, String description) {
		Person newPerson = new Person(name, description);
		user.getPersons().add(newPerson);
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
		String sql = "SELECT name, description, start_date FROM events WHERE user_id = ?";

		try (Connection conn = DatabaseManager.getConnection(); var pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, getUserId());
			var rs = pstmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String description = rs.getString("description");
				LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
				Event e = new Event(name, description, startDate);
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
