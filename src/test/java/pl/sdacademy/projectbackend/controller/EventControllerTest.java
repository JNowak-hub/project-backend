package pl.sdacademy.projectbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sdacademy.projectbackend.configuration.CustomExceptionHandler;
import pl.sdacademy.projectbackend.exceptions.EventNotFound;
import pl.sdacademy.projectbackend.model.Comment;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;
import pl.sdacademy.projectbackend.service.EventService;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
public class EventControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event testEvent;
    private User testUser;
    private Comment testComment;
    private UserEvent userEvent;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
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
        testEvent.setStartDate(LocalDateTime.of(2020, 10, 20, 20, 00));
        testEvent.setEndDate(LocalDateTime.of(2020, 10, 21, 20, 00));

        userEvent = new UserEvent(testUser, testEvent);

        testEvent.setParticipants(Arrays.asList(userEvent));
        testEvent.setOrganizer(testUser);
        testEvent.setComments(Arrays.asList(testComment));
        testEvent.setLocation("Katowice");
        testEvent.setPrivateEvent(true);

    }

    @Test
    @DisplayName("When findEventById gets not null Optional of Event then Event should be returned")
    void getEventById() throws Exception {
        //given
        when(eventService.findEventById(1L)).thenReturn(testEvent);
        //when then
        mockMvc.perform(get("/api/event/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(testEvent.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(testEvent.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", is(testEvent.getLocation())));
    }

    @Test
    @DisplayName("When call findEventById should return status 404")
    public void deleteEventById() throws Exception {
        //given
        when(eventService.findEventById(1L)).thenThrow(EventNotFound.class);
        //when then
        mockMvc.perform(get("/api/event/1"))
                .andExpect(status().isNotFound());
    }
}
