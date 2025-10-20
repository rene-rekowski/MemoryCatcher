package modelTest;


import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.Person;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorWithAllParameters() {
        Person person1 = new Person("Max");
        Person person2 = new Person("Erika");
        ObservableList<Person> persons = FXCollections.observableArrayList(person1, person2);

        Event event1 = new Event("Geburtstag", "Party", LocalDate.of(2025, 10, 20),
                LocalDate.of(2025, 10, 21), new ArrayList<>());
        List<Event> events = new ArrayList<>();
        events.add(event1);

        User user = new User("René", LocalDate.of(2000, 5, 15), events, persons);

        assertEquals("René", user.getName());
        assertEquals(LocalDate.of(2000, 5, 15), user.getBirthday());
        assertEquals(1, user.getEvents().size());
        assertEquals(2, user.getPersons().size());
    }

    @Test
    void testConstructorWithMinimalParameters() {
        User user = new User("René", LocalDate.of(2000, 5, 15));
        assertEquals("René", user.getName());
        assertEquals(LocalDate.of(2000, 5, 15), user.getBirthday());
        assertTrue(user.getEvents().isEmpty());
        assertTrue(user.getPersons().isEmpty());
    }

    @Test
    void testAddEventAndClearEvents() {
        User user = new User("René", LocalDate.of(2000, 5, 15));
        Event event = new Event("Meeting", "Teammeeting", LocalDate.of(2025, 10, 20),
                LocalDate.of(2025, 10, 21), new ArrayList<>());

        user.addEvent(event);
        assertEquals(1, user.getEvents().size());

        user.clearEvents();
        assertTrue(user.getEvents().isEmpty());
    }

    @Test
    void testNameValidation() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new User("", LocalDate.of(2000, 1, 1)));
        assertEquals("name must not empty", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new User("a".repeat(31), LocalDate.of(2000, 1, 1)));
        assertEquals("name must not longer than 30 charackter", exception2.getMessage());
    }

    @Test
    void testToString() {
        User user = new User("René", LocalDate.of(2000, 5, 15));
        assertEquals("René (2000-05-15)", user.toString());
    }
}
