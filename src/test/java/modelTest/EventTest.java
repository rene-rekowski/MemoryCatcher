package modelTest;

import org.junit.jupiter.api.Test;

import model.Event;
import model.Person;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Person person1;
    private Person person2;
    private List<Person> persons;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setup() {
        person1 = new Person("Max", "Mustermann"); 
        person2 = new Person("Erika", "Musterfrau");
        persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        startDate = LocalDate.of(2025, 10, 20);
        endDate = LocalDate.of(2025, 10, 25);
    }

    @Test
    void testConstructorAndGetters() {
        Event event = new Event("Geburtstag", "Party mit Freunden", startDate, endDate, persons);

        assertEquals("Geburtstag", event.getName());
        assertEquals("Party mit Freunden", event.getDescription());
        assertEquals(startDate, event.getStartDate());
        assertEquals(endDate, event.getEndDate());
        assertEquals(2, event.getPersons().size());
        assertTrue(event.getPersons().contains(person1));
        assertTrue(event.getPersons().contains(person2));
    }

    @Test
    void testAddPerson() {
        Event event = new Event("Meeting", "Teammeeting", startDate, endDate, new ArrayList<>());
        event.addPerson(person1);

        List<Person> attendees = event.getPersons();
        assertEquals(1, attendees.size());
        assertTrue(attendees.contains(person1));
    }

    @Test
    void testNameValidation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event("", "Description", startDate, endDate, persons);
        });
        assertEquals("name must not be null or empty", exception.getMessage());
    }

    @Test
    void testDescriptionValidation() {
        String longDescription = "a".repeat(501);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event("Event", longDescription, startDate, endDate, persons);
        });
        assertEquals("description must shorter than 500 charater", exception.getMessage());
    }

    @Test
    void testDateValidation() {
        LocalDate invalidEnd = LocalDate.of(2025, 10, 15);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Event("Event", "Description", startDate, invalidEnd, persons);
        });
        assertEquals("endDate must happend after startDate", exception.getMessage());
    }
}
