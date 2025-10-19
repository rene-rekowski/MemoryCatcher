package repositoryTest;

import database.DatabaseManager;
import database.repository.EventRepository;
import database.repository.UserRepository;
import model.Event;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventRepositoryTest {

    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository = new EventRepository();
    }

    // ==========================================================
    // TEST: SAVE
    // ==========================================================
    @Test
    void testSaveInsertsCorrectValues() throws SQLException {
        Event event = new Event("Party", "Birthday Party",
                LocalDate.of(2025, 10, 18), null, null);
        int userId = 1;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            eventRepository.save(event, userId);

            verify(mockStmt).setInt(1, userId);
            verify(mockStmt).setString(2, "Party");
            verify(mockStmt).setString(3, "Birthday Party");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testSaveHandlesSQLExceptionGracefully() throws SQLException {
        Event event = new Event("ErrorEvent", "Should fail",
                LocalDate.now(), null, null);

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB down"));

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            assertDoesNotThrow(() -> eventRepository.save(event, 1),
                    "save() sollte SQLException intern abfangen");
        }
    }

    // ==========================================================
    // TEST: DELETE
    // ==========================================================
    @Test
    void testDeleteRemovesCorrectEvent() throws SQLException {
        Event event = new Event("Party", "To be deleted", LocalDate.now(), null, null);
        int userId = 42;

        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            eventRepository.delete(event, userId);

            verify(mockStmt).setInt(1, userId);
            verify(mockStmt).setString(2, "Party");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteHandlesSQLExceptionGracefully() throws SQLException {
        Event event = new Event("ErrorEvent", "Should fail", LocalDate.now(), null, null);
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            assertDoesNotThrow(() -> eventRepository.delete(event, 1),
                    "delete() sollte SQLException intern abfangen");
        }
    }

    // ==========================================================
    // TEST: FIND BY USER ID
    // ==========================================================
    @Test
    void testFindByUserIdReturnsEventList() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString("name")).thenReturn("Party");
        when(mockRs.getString("description")).thenReturn("Birthday Party");
        when(mockRs.getString("start_date")).thenReturn("2025-10-18");
        when(mockRs.getString("end_date")).thenReturn(null);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Event> events = eventRepository.findByUserId(1);

            assertEquals(1, events.size());
            assertEquals("Party", events.get(0).getName());
            assertEquals("Birthday Party", events.get(0).getDescription());
        }
    }

    @Test
    void testFindByUserIdHandlesEmptyResultSet() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Event> result = eventRepository.findByUserId(99);
            assertTrue(result.isEmpty(), "Leere Liste erwartet bei keinem Treffer");
        }
    }

    //TODO: findUserbyID is maby not a good idea
    
}
