package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

	private static final String DB_URL = "jdbc:sqlite:memorycatcher.db";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}

	public static void initialize() {
		try (Connection conn = getConnection(); var stmt = conn.createStatement()) {

			String createUsers = "CREATE TABLE IF NOT EXISTS users (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "name TEXT NOT NULL UNIQUE," + "birthday TEXT)";

			String createEvents = "CREATE TABLE IF NOT EXISTS events (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "user_id INTEGER NOT NULL," + "name TEXT NOT NULL," + "description TEXT," + "start_date TEXT,"
					+ "FOREIGN KEY(user_id) REFERENCES users(id))";

			stmt.execute(createUsers);
			stmt.execute(createEvents);

			System.out.println("Database initialized");

		} catch (SQLException e) {
			System.err.println("Database Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
