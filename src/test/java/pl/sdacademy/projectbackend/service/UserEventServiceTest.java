package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.UserEventRepository;

@ExtendWith(MockitoExtension.class)
public class UserEventServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private EventService eventService;
    @Mock
    private UserEventRepository repository;
    @InjectMocks
    private UserEventService service;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setup(){

    }
}
