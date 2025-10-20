package modelTest;


import org.junit.jupiter.api.Test;

import model.Person;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testConstructorWithName() {
        Person person = new Person("Max");
        assertEquals("Max", person.getName());
        assertNull(person.getDescription());
    }

    @Test
    void testConstructorWithNameAndDescription() {
        Person person = new Person("Erika", "Freundin");
        assertEquals("Erika", person.getName());
        assertEquals("Freundin", person.getDescription());
    }

    @Test
    void testSetters() {
        Person person = new Person("Max");
        person.setName("Erika");
        person.setDescription("Kollegin");

        assertEquals("Erika", person.getName());
        assertEquals("Kollegin", person.getDescription());
    }

    @Test
    void testToString() {
        Person person = new Person("Max");
        assertEquals("Max", person.toString());
    }
}
