package controller;

import database.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Kontrolliert die Benutzerverwaltung.
 *
 * @author rene-rekowski
 * @version 1.0
 */
public class UserController {

    private final ObservableList<User> users;
    private final UserRepository userRepo;

    public UserController() {
        this.users = FXCollections.observableArrayList();
        this.userRepo = new UserRepository();
        load();
    }

    public void addUser(String name, LocalDate birthday) {
        User user = new User(name, birthday);
        userRepo.save(user);
        users.add(user);
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
        users.remove(user);
    }

    public void load() {
        users.clear();
        List<User> loadedUsers = userRepo.findAll();
        users.addAll(loadedUsers);
    }

    public int getUserId(User user) {
        return userRepo.findUserId(user);
    }

    public ObservableList<User> getUsers() {
        return users;
    }
}
