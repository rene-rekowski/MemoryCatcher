package repositoryTest;


import database.DatabaseManager;
import database.repository.EventRepository;
import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventRepositoryTest {

    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository = new EventRepository();
    }

    @Test
    void testSave() throws SQLException {
        Event event = new Event("Party", "Birthday Party", LocalDate.of(2025, 10, 18), null, null);
        int userId = 1;

        // Mock für Connection und PreparedStatement
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        // Mocken der statischen Methode getConnection
        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            eventRepository.save(event, userId);

            // Überprüfen, ob executeUpdate aufgerufen wurde
            verify(mockStmt, times(1)).executeUpdate();
            verify(mockStmt).setInt(1, userId);
            verify(mockStmt).setString(2, "Party");
        }
    }

    @Test
    void testFindByUserId() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false); // einmal true, dann false
        when(mockRs.getString("name")).thenReturn("Party");
        when(mockRs.getString("description")).thenReturn("Birthday Party");
        when(mockRs.getString("start_date")).thenReturn("2025-10-18");
        when(mockRs.getString("end_date")).thenReturn(null);

        try (MockedStatic<DatabaseManager> mockedDb = mockStatic(DatabaseManager.class)) {
            mockedDb.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Event> events = eventRepository.findByUserId(1);

            assertEquals(1, events.size());
            assertEquals("Party", events.get(0).getName());
        }
    }
}
