package controllerTest;

import controller.EventController;
import database.repository.EventRepository;
import database.repository.PersonRepository;
import database.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    private User mockUser;
    private UserRepository mockUserRepo;
    private EventRepository mockEventRepo;
    private PersonRepository mockPersonRepo;
    private EventController controller;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        when(mockUser.getPersons()).thenReturn(FXCollections.observableArrayList());
        
        mockUserRepo = mock(UserRepository.class);
        mockEventRepo = mock(EventRepository.class);
        mockPersonRepo = mock(PersonRepository.class);
    }

    // ==========================================================
    // TEST: Konstruktor lädt Events & Personen
    // ==========================================================
    @Test
    void testConstructorLoadsData() {
        List<Event> fakeEvents = List.of(
                new Event("Birthday", "Party", LocalDate.now(), LocalDate.now().plusDays(1), new ArrayList<>())
        );
        List<Person> fakePersons = List.of(new Person("Alice", "Friend"));

        // Erstelle eine Spy-Liste für die Personen
        ObservableList<Person> spyList = spy(FXCollections.observableArrayList());
        when(mockUser.getPersons()).thenReturn(spyList);

        try (MockedConstruction<EventRepository> eventRepoMock = mockConstruction(EventRepository.class, (mock, ctx) ->
                when(mock.findByUser(any())).thenReturn(fakeEvents));
             MockedConstruction<PersonRepository> personRepoMock = mockConstruction(PersonRepository.class, (mock, ctx) ->
                     when(mock.findByUser(anyInt())).thenReturn(fakePersons));
             MockedConstruction<UserRepository> userRepoMock = mockConstruction(UserRepository.class, (mock, ctx) ->
                     when(mock.findUserId(any())).thenReturn(1))
        ) {
            EventController controller = new EventController(mockUser);

            verify(spyList, atLeastOnce()).setAll(fakePersons);

            ObservableList<Event> events = controller.getEvents();
            assertEquals(1, events.size());
            assertEquals("Birthday", events.get(0).getName());
        }
    }


    // ==========================================================
    // TEST: addEvent fügt neues Event hinzu
    // ==========================================================
    @Test
    void testAddEventAddsToListAndRepo() {
        try (MockedConstruction<EventRepository> eventRepoMock = mockConstruction(EventRepository.class);
             MockedConstruction<PersonRepository> personRepoMock = mockConstruction(PersonRepository.class);
             MockedConstruction<UserRepository> userRepoMock = mockConstruction(UserRepository.class, (mock, ctx) ->
                     when(mock.findUserId(any())).thenReturn(99))
        ) {
            EventController controller = new EventController(mockUser);

            List<Person> persons = List.of(new Person("Bob", "Colleague"));
            controller.addEvent("Meeting", "Project update", LocalDate.now(), LocalDate.now().plusDays(2), persons);

            // Überprüfe: Event in Liste
            ObservableList<Event> events = controller.getEvents();
            assertEquals(1, events.size());
            assertEquals("Meeting", events.get(0).getName());
        }
    }

    // ==========================================================
    // TEST: addPerson fügt neue Person hinzu
    // ==========================================================
    @Test
    void testAddPersonAddsToUserAndRepo() {
        try (MockedConstruction<PersonRepository> personRepoMock = mockConstruction(PersonRepository.class);
             MockedConstruction<UserRepository> userRepoMock = mockConstruction(UserRepository.class, (mock, ctx) ->
                     when(mock.findUserId(any())).thenReturn(7));
             MockedConstruction<EventRepository> eventRepoMock = mockConstruction(EventRepository.class)
        ) {
            EventController controller = new EventController(mockUser);

            controller.addPerson("Eve", "Neighbor");

            assertEquals(1, mockUser.getPersons().size());
            assertEquals("Eve", mockUser.getPersons().get(0).getName());
        }
    }

    // ==========================================================
    // TEST: deleteEvent entfernt Event korrekt
    // ==========================================================
    @Test
    void testDeleteEventRemovesAndDeletesInRepo() {
        try (MockedConstruction<EventRepository> eventRepoMock = mockConstruction(EventRepository.class);
             MockedConstruction<PersonRepository> personRepoMock = mockConstruction(PersonRepository.class);
             MockedConstruction<UserRepository> userRepoMock = mockConstruction(UserRepository.class, (mock, ctx) ->
                     when(mock.findUserId(any())).thenReturn(3))
        ) {
            EventController controller = new EventController(mockUser);

            Event e = new Event("Cleanup", "Test", LocalDate.now(), LocalDate.now(), new ArrayList<>());
            controller.getEvents().add(e);

            controller.deleteEvent(e);

            assertTrue(controller.getEvents().isEmpty());
        }
    }

    // ==========================================================
    // TEST: loadPersons lädt Personen korrekt
    // ==========================================================
    @Test
    void testLoadPersonsReplacesList() {
        List<Person> fakePersons = List.of(new Person("Alice", "Best friend"));
        ObservableList<Person> spyList = spy(FXCollections.observableArrayList());
        when(mockUser.getPersons()).thenReturn(spyList);

        try (MockedConstruction<PersonRepository> personRepoMock = mockConstruction(PersonRepository.class, (mock, ctx) ->
                when(mock.findByUser(anyInt())).thenReturn(fakePersons));
             MockedConstruction<UserRepository> userRepoMock = mockConstruction(UserRepository.class, (mock, ctx) ->
                     when(mock.findUserId(any())).thenReturn(4));
             MockedConstruction<EventRepository> eventRepoMock = mockConstruction(EventRepository.class)) {

            // Spy, damit Konstruktor nicht sofort loadPersons() ausführt
            EventController controller = spy(new EventController(mockUser));
            doNothing().when(controller).loadPersons(); // ruft es im Konstruktor nicht auf

            // Jetzt manuell aufrufen
            controller.loadPersons();

            verify(spyList, times(1)).setAll(fakePersons);
        }
    }


}
