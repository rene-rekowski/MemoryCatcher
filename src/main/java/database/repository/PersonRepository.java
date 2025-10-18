package database.repository;

import database.DatabaseManager;
import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public void save(Person person, int userId) {
        String sql = "INSERT INTO persons (user_id, name, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, person.getName());
            pstmt.setString(3, person.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Speichern der Person: " + e.getMessage());
        }
    }

    public List<Person> findByUser(int userId) {
        List<Person> persons = new ArrayList<>();
        String sql = "SELECT name, description FROM persons WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                persons.add(new Person(
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Personen: " + e.getMessage());
        }
        return persons;
    }

    public void delete(Person person, int userId) {
        String sql = "DELETE FROM persons WHERE user_id = ? AND name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, person.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen der Person: " + e.getMessage());
        }
    }
}
