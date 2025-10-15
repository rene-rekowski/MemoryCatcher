package database.repository;

import database.DatabaseManager;
import model.Event;
import model.Person;
import model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {

    public void save(Event event, int userId) {
        String sql = "INSERT INTO events (user_id, name, description, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, event.getName());
            pstmt.setString(3, event.getDescription());
            pstmt.setString(4, event.getStartDate() != null ? event.getStartDate().toString() : null);
            pstmt.setString(5, event.getEndDate() != null ? event.getEndDate().toString() : null);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Speichern des Events: " + e.getMessage());
        }
    }

    public void delete(Event event, int userId) {
        String sql = "DELETE FROM events WHERE user_id = ? AND name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, event.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen des Events: " + e.getMessage());
        }
    }

    public List<Event> findByUserId(int userId) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT name, description, start_date, end_date FROM events WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String desc = rs.getString("description");
                LocalDate start = LocalDate.parse(rs.getString("start_date"));
                String endStr = rs.getString("end_date");
                LocalDate end = endStr != null ? LocalDate.parse(endStr) : null;

                events.add(new Event(name, desc, start, end, null));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Events: " + e.getMessage());
        }
        return events;
    }
    
    public List<Event> findByUser(User user) {
        List<Event> events = new ArrayList<>();
        int userId = new UserRepository().findUserId(user);

        String sql = "SELECT name, description, start_date, end_date FROM events WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String desc = rs.getString("description");
                String startStr = rs.getString("start_date");
                String endStr = rs.getString("end_date");

                events.add(new Event(
                        name,
                        desc,
                        startStr != null ? LocalDate.parse(startStr) : null,
                        endStr != null ? LocalDate.parse(endStr) : null,
                        new ArrayList<>()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Events: " + e.getMessage());
        }

        return events;
    }
}
