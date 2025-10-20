package controllerTest;

import controller.UserController;
import database.repository.UserRepository;
import javafx.collections.ObservableList;
import model.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void testConstructorLoadsUsers() {
    	
        List<User> fakeUsers = List.of(
                new User("Alice", LocalDate.of(2000, 1, 1)),
                new User("Bob", LocalDate.of(1995, 5, 5))
        );

        try (MockedConstruction<UserRepository> mocked = mockConstruction(UserRepository.class, (mock, ctx) -> {
            when(mock.findAll()).thenReturn(fakeUsers);
        })) {
            UserController controller = new UserController();

            ObservableList<User> users = controller.getUsers();
            assertEquals(2, users.size());
            assertEquals("Alice", users.get(0).getName());
            assertEquals("Bob", users.get(1).getName());

            UserRepository repo = mocked.constructed().get(0);
            verify(repo, times(1)).findAll();
        }
    }

    @Test
    void testAddUserAddsToListAndSaves() {
        try (MockedConstruction<UserRepository> mocked = mockConstruction(UserRepository.class)) {
            UserController controller = new UserController();
            UserRepository repo = mocked.constructed().get(0);

            controller.getUsers().clear();
            controller.addUser("Charlie", LocalDate.of(1999, 9, 9));

            // Prüfen, ob der User in der Liste ist
            assertEquals(1, controller.getUsers().size());
            assertEquals("Charlie", controller.getUsers().get(0).getName());

            // Sicherstellen, dass save() aufgerufen wurde
            verify(repo).save(any(User.class));
        }
    }

    @Test
    void testDeleteUserRemovesFromListAndRepository() {
        try (MockedConstruction<UserRepository> mocked = mockConstruction(UserRepository.class)) {
            UserController controller = new UserController();
            UserRepository repo = mocked.constructed().get(0);

            User testUser = new User("Dave", LocalDate.of(1988, 8, 8));
            controller.getUsers().add(testUser);

            controller.deleteUser(testUser);

            assertTrue(controller.getUsers().isEmpty());

            verify(repo).delete(testUser);
        }
    }

    @Test
    void testLoadReplacesUserList() {
        List<User> fakeUsers = List.of(new User("Eve", LocalDate.of(2001, 2, 2)));
        try (MockedConstruction<UserRepository> mocked = mockConstruction(UserRepository.class, (mock, ctx) ->
                when(mock.findAll()).thenReturn(fakeUsers))) {

            UserController controller = new UserController();
            UserRepository repo = mocked.constructed().get(0);

            controller.getUsers().clear();
            controller.load();

            // Erwartung: Liste wurde durch neue Daten ersetzt
            assertEquals(1, controller.getUsers().size());
            assertEquals("Eve", controller.getUsers().get(0).getName());
            verify(repo, atLeastOnce()).findAll();
        }
    }

    @Test
    void testGetUserIdReturnsRepositoryResult() {
        User user = new User("Frank", LocalDate.of(1990, 1, 1));

        try (MockedConstruction<UserRepository> mocked = mockConstruction(UserRepository.class, (mock, ctx) ->
                when(mock.findUserId(user)).thenReturn(42))) {

            UserController controller = new UserController();
            UserRepository repo = mocked.constructed().get(0);

            int id = controller.getUserId(user);

            assertEquals(42, id);
            verify(repo).findUserId(user);
        }
    }
}
