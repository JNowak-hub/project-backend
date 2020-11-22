package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.sdacademy.projectbackend.exceptions.UserAlreadyAssigned;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;
import pl.sdacademy.projectbackend.repository.UserEventRepository;
import pl.sdacademy.projectbackend.utilities.SecurityContestUtils;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEventServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private EventService eventService;
    @Mock
    private UserEventRepository repository;
    @Mock
    private SecurityContestUtils securityContestUtils;
    @InjectMocks
    private UserEventService service;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setup(){
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setLogin("login");
        testUser.setRole(Role.USER);

        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Event name");
        testEvent.setStartDate(LocalDateTime.of(2020, 12, 11, 16, 30));
        testEvent.setEndDate(LocalDateTime.of(2020, 12, 11, 21, 30));
        testEvent.setOrganizer(testUser);
        testEvent.setLocation("Poland");
    }

    @Test
    @DisplayName("When takePartInEvent then return UserEvent Entity and call repository save")
    public void test1(){
        //given
        UserEvent userEvent = new UserEvent(testUser, testEvent);
        when(repository.save(any())).thenReturn(userEvent);
        when(securityContestUtils.getCurrentUser()).thenReturn(testUser);
        when(eventService.findEventById(1L)).thenReturn(testEvent);
        when(repository.existsById(any())).thenReturn(false);
        //when
        UserEvent savedUserEvent = service.takePartInEvent(1L);
        //then
        assertThat(userEvent).isEqualTo(savedUserEvent);
    }

    @Test
    @DisplayName("When takePartInEvent then throw UserNotFound exception")
    public void test2(){
        //given
        when(securityContestUtils.getCurrentUser()).thenThrow(UserNotFound.class);
        when(eventService.findEventById(1L)).thenReturn(testEvent);
        //when then
        UserNotFound exception = assertThrows(UserNotFound.class, () -> service.takePartInEvent(1L));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("When takePartInEvent then throw UserAlreadyAssigned exception")
    public void test3(){
        //given
        when(securityContestUtils.getCurrentUser()).thenReturn(testUser);
        when(eventService.findEventById(1L)).thenReturn(testEvent);
        when(repository.existsById(any())).thenReturn(true);
        //when
        UserAlreadyAssigned exception = assertThrows(UserAlreadyAssigned.class, () -> service.takePartInEvent(1L));
        //then
        assertThat("User : " + testUser.getLogin() + " is already assigned to event: " + testEvent.getName()).isEqualTo(exception.getMessage());
        verify(repository, never()).save(any());
    }

}
