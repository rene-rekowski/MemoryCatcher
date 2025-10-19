package repositoryTest;

import database.DatabaseManager;
import database.repository.PersonRepository;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonRepositoryTest {

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepository();
    }

    // ==========================================================
    // TEST: SAVE PERSON (Erfolg)
    // ==========================================================
    @Test
    void testSavePersonSuccess() throws SQLException {
        Person person = new Person("Alice", "Best friend");
        int userId = 42;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            personRepository.save(person, userId);

            verify(mockStmt).setInt(1, userId);
            verify(mockStmt).setString(2, "Alice");
            verify(mockStmt).setString(3, "Best friend");
            verify(mockStmt).executeUpdate();
        }
    }

    // ==========================================================
    // TEST: SAVE PERSON (Fehlerfall)
    // ==========================================================
    @Test
    void testSavePersonSQLException() throws SQLException {
        Person person = new Person("Bob", "Colleague");
        int userId = 13;

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB write error"));

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            assertDoesNotThrow(() -> personRepository.save(person, userId));
        }
    }

    // ==========================================================
    // TEST: FIND BY USER (Erfolg)
    // ==========================================================
    @Test
    void testFindByUserSuccess() throws SQLException {
        int userId = 10;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString("name")).thenReturn("Charlie");
        when(mockRs.getString("description")).thenReturn("Old friend");

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Person> persons = personRepository.findByUser(userId);

            assertEquals(1, persons.size());
            assertEquals("Charlie", persons.get(0).getName());
            assertEquals("Old friend", persons.get(0).getDescription());
        }
    }

    // ==========================================================
    // TEST: FIND BY USER (Fehlerfall)
    // ==========================================================
    @Test
    void testFindByUserSQLException() throws SQLException {
        int userId = 5;

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Person> persons = personRepository.findByUser(userId);

            assertNotNull(persons);
            assertTrue(persons.isEmpty());
        }
    }

    // ==========================================================
    // TEST: DELETE PERSON (Erfolg)
    // ==========================================================
    @Test
    void testDeletePersonSuccess() throws SQLException {
        Person person = new Person("Diana", "Cousin");
        int userId = 2;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            personRepository.delete(person, userId);

            verify(mockStmt).setInt(1, userId);
            verify(mockStmt).setString(2, "Diana");
            verify(mockStmt).executeUpdate();
        }
    }

    // ==========================================================
    // TEST: DELETE PERSON (Fehlerfall)
    // ==========================================================
    @Test
    void testDeletePersonSQLException() throws SQLException {
        Person person = new Person("Eve", "Neighbor");
        int userId = 3;

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Cannot delete"));

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            assertDoesNotThrow(() -> personRepository.delete(person, userId));
        }
    }
}
