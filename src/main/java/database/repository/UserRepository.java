package database.repository;

import database.DatabaseManager;
import model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public void save(User user) {
        String sql = "INSERT INTO users (name, birthday) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getBirthday().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Speichern des Users: " + e.getMessage());
        }
    }

    public void delete(User user) {
        String sql = "DELETE FROM users WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen des Users: " + e.getMessage());
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT name, birthday FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                LocalDate birthday = LocalDate.parse(rs.getString("birthday"));
                users.add(new User(name, birthday));
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Benutzer: " + e.getMessage());
        }
        return users;
    }

    public int findUserId(User user) {
        String sql = "SELECT id FROM users WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Finden der User-ID: " + e.getMessage());
        }
        return -1;
    }
}
