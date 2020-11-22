package pl.sdacademy.projectbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sdacademy.projectbackend.configuration.CustomExceptionHandler;
import pl.sdacademy.projectbackend.model.Comment;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.EventRepository;
import pl.sdacademy.projectbackend.service.EventService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebAppConfiguration
public class EventControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private User testUser;
    private Comment testComment;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(eventService)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("test");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setUser(testUser);
        testComment.setContent("Comment description sample text");

        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Sample Event Name");
        testEvent.setDescription("Description about event, some random text from description");
        testEvent.setStartDate(LocalDateTime.of(2020,10,20,20,00));
        testEvent.setEndDate(LocalDateTime.of(2020,10,21,20,00));
        testEvent.setParticipants(Arrays.asList(testUser));
        testEvent.setOrganizer(testUser);
        testEvent.setComments(Arrays.asList(testComment));
        testEvent.setLocation("Katowice");
        testEvent.setPrivateEvent(true);
        }

    @Test
    @DisplayName("When findById gets not null Optional of Event then Event should be returned")
    void test1() {
        //given
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(testEvent));
        //when
        Event event = eventService.findEventById(1L);
        //then
        assertThat(event).isEqualTo(testEvent);

    }

}
