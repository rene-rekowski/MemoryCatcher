package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 	
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class DatabaseManager {

	private static final String DB_URL = "jdbc:sqlite:memorycatcher.db";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}

	public static void initialize() {
		try (Connection conn = getConnection(); var stmt = conn.createStatement()) {

			// Tabelle für Benutzer
            String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    birthday TEXT
                )
            """;

            // Tabelle für Events
            String createEvents = """
                CREATE TABLE IF NOT EXISTS events (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    start_date TEXT,
                    end_date TEXT,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                )
            """;

            // Tabelle für Personen (gehört zu einem Benutzer)
            String createPersons = """
                CREATE TABLE IF NOT EXISTS persons (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                )
            """;

            // Zwischentabelle: Verknüpfung Event ↔ Person (Many-to-Many)
            String createEventPerson = """
                CREATE TABLE IF NOT EXISTS event_persons (
                    event_id INTEGER NOT NULL,
                    person_id INTEGER NOT NULL,
                    PRIMARY KEY (event_id, person_id),
                    FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE,
                    FOREIGN KEY(person_id) REFERENCES persons(id) ON DELETE CASCADE
                )
            """;

            stmt.execute(createUsers);
            stmt.execute(createEvents);
            stmt.execute(createPersons);
            stmt.execute(createEventPerson);

			System.out.println("Database initialized");

		} catch (SQLException e) {
			System.err.println("Database Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
