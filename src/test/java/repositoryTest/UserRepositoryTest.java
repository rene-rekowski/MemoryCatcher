package repositoryTest;

import database.DatabaseManager;
import database.repository.UserRepository;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    // ==========================================================
    // TEST: SAVE
    // ==========================================================
    @Test
    void testSaveInsertsUser() throws SQLException {
        User user = new User("Alice", LocalDate.of(2000, 1, 1));
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            userRepository.save(user);

            verify(mockStmt).setString(1, "Alice");
            verify(mockStmt).setString(2, "2000-01-01");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testSaveHandlesSQLException() throws SQLException {
        User user = new User("Bob", LocalDate.of(1995, 5, 20));
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("DB down"));

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            assertDoesNotThrow(() -> userRepository.save(user));
        }
    }

    // ==========================================================
    // TEST: DELETE
    // ==========================================================
    @Test
    void testDeleteRemovesUser() throws SQLException {
        User user = new User("Charlie", LocalDate.of(1990, 3, 3));
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            userRepository.delete(user);

            verify(mockStmt).setString(1, "Charlie");
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testDeleteHandlesSQLException() throws SQLException {
        User user = new User("Daniel", LocalDate.of(1991, 1, 1));
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("cannot delete"));

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            assertDoesNotThrow(() -> userRepository.delete(user));
        }
    }

    // ==========================================================
    // TEST: FIND ALL
    // ==========================================================
    @Test
    void testFindAllReturnsUsers() throws SQLException {
        Connection mockConn = mock(Connection.class);
        Statement mockStmt = mock(Statement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);

        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString("name")).thenReturn("Eve");
        when(mockRs.getString("birthday")).thenReturn("1999-09-09");

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<User> users = userRepository.findAll();

            assertEquals(1, users.size());
            assertEquals("Eve", users.get(0).getName());
            assertEquals(LocalDate.of(1999, 9, 9), users.get(0).getBirthday());
        }
    }

    @Test
    void testFindAllHandlesSQLException() throws SQLException {
        Connection mockConn = mock(Connection.class);
        when(mockConn.createStatement()).thenThrow(new SQLException("select failed"));

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<User> result = userRepository.findAll();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    // ==========================================================
    // TEST: FIND USER ID
    // ==========================================================
    @Test
    void testFindUserIdReturnsId() throws SQLException {
        User user = new User("Frank", LocalDate.of(1980, 8, 8));
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(42);

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            int id = userRepository.findUserId(user);
            assertEquals(42, id);
        }
    }

    @Test
    void testFindUserIdReturnsMinusOneWhenNotFound() throws SQLException {
        User user = new User("George", LocalDate.of(2001, 2, 2));
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // Kein Treffer

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            int id = userRepository.findUserId(user);
            assertEquals(-1, id);
        }
    }

    @Test
    void testFindUserIdHandlesSQLException() throws SQLException {
        User user = new User("Henry", LocalDate.of(1998, 6, 6));
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("broken query"));

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            int id = userRepository.findUserId(user);
            assertEquals(-1, id);
        }
    }
}
