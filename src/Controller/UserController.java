package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserController {
	private final ObservableList<User> users;
	private static final String FILE_PATH = "user.csv";
	
	public UserController() {
		this.users = FXCollections.observableArrayList();
	}
	
	public void addUser(String name, LocalDate birthday) {
		users.add(new User(name, birthday));
		save();
	}
		
	public ObservableList<User> getUsers(){
		return users;
	}
	
	public void deletUser(User user) {
		users.remove(user);
		save();
	}
	
	/**
     * save all user in a csv-data.
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write(user.getName() + ";" + user.getBirthday().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Benutzer: " + e.getMessage());
        }
    }
    
    /**
     * load every user in a csv-data
     */
    public void load() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String name = parts[0];
                    LocalDate birthday = LocalDate.parse(parts[1]);
                    users.add(new User(name, birthday));
                }
            }
        } catch (IOException e) {
            System.out.println("Keine gespeicherten Benutzer gefunden (oder Datei leer).");
        }
    }
	

}
