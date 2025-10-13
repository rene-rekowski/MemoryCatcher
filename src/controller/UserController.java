package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

/**
 * controlled the user
 * 
 * @author rene-rekowski
 * @version 1.0
 */
public class UserController {
	private final ObservableList<User> users;
	private static final String FILE_PATH = "user.csv";
	
	public UserController() {
		this.users = FXCollections.observableArrayList();
	}
	
	public int getUserId(User user) {
	    String sql = "SELECT id FROM users WHERE name = ?";
	    try (Connection conn = DatabaseManager.getConnection();
	         var pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, user.getName());
	        var rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("id");
	        }

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Holen der User-ID: " + e.getMessage());
	    }
	    return -1; 
	}

	
	public void addUser(String name, LocalDate birthday) {
	    String sql = "INSERT INTO users (name, birthday) VALUES (?, ?)";

	    try (Connection conn = DatabaseManager.getConnection();
	         var pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, name);
	        pstmt.setString(2, birthday.toString());
	        pstmt.executeUpdate();

	        users.add(new User(name, birthday));

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Hinzufügen des Benutzers: " + e.getMessage());
	    }
	}
	
//	public void addUserCSV(String name, LocalDate birthday) {
//		users.add(new User(name, birthday));
//		save();
//	}
		
	public ObservableList<User> getUsers(){
		return users;
	}
	
	public void deleteUser(User user) {
	    String sql = "DELETE FROM users WHERE name = ?";
	    try (Connection conn = DatabaseManager.getConnection();
	         var pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, user.getName());
	        pstmt.executeUpdate();
	        users.remove(user);

	    } catch (SQLException e) {
	        System.err.println("Fehler beim Löschen des Benutzers: " + e.getMessage());
	    }
	}
//	public void deletUserCSV(User user) {
//		users.remove(user);
//		save();
//	}
	
	/**
     * save all user in a csv-data.
     */
//    public void save() {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
//            for (User user : users) {
//                writer.write(user.getName() + ";" + user.getBirthday().toString());
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            System.err.println("Fehler beim Speichern der Benutzer: " + e.getMessage());
//        }
//    }
    
    /**
     * load every user in a csv-data
     */
    public void load() {
        users.clear();
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
    }
//    
//    public void loadCSV() {
//        users.clear();
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(";");
//                if (parts.length == 2) {
//                    String name = parts[0];
//                    LocalDate birthday = LocalDate.parse(parts[1]);
//                    users.add(new User(name, birthday));
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Keine gespeicherten Benutzer gefunden (oder Datei leer).");
//        }
//    }
	

}
