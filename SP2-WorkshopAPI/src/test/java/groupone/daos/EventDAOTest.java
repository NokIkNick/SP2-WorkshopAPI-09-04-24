// In EventDAOTest.java
package groupone.daos;

import groupone.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private EventDAO eventDAO;

    @BeforeEach
    public void setup() {
        eventDAO = EventDAO.getInstance(true);
    }

    @Test
    public void getAllReturnsAllEvents() {
        List<Event> events = eventDAO.getAll();
        assertFalse(!events.isEmpty());
    }

    @Test
    public void getByIdReturnsNullWhenEventDoesNotExist() {
        Event fetchedEvent = eventDAO.getById(1);
        assertNotNull(fetchedEvent);
    }
}