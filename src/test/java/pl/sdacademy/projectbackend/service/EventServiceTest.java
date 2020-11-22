package pl.sdacademy.projectbackend.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.EventRepository;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private User testUser = new User();

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Event name");
        testEvent.setStartDate(LocalDateTime.of(2020, 12, 11, 16, 30));
        testEvent.setEndDate(LocalDateTime.of(2020, 12, 11, 21, 30));
        testEvent.setOrganizer(testUser);
        testEvent.setLocation("Poland");
    }

    @Test
    @DisplayName("When findEventsByName gets at least one record then returns list")
    void test1() {
        // given
        // when
        // then
    }
}
