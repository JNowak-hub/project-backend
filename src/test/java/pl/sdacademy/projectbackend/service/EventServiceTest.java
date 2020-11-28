package pl.sdacademy.projectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.EventRepository;
import pl.sdacademy.projectbackend.utilities.SecurityContextUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private SecurityContextUtils securityContextUtils;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private User testUser;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Event test name");
        testEvent.setStartDate(LocalDateTime.of(2020, 12, 11, 16, 30));
        testEvent.setEndDate(LocalDateTime.of(2020, 12, 11, 21, 30));
        testEvent.setOrganizer(testUser);
        testEvent.setLocation("Poland");

        testUser = new User();
        testUser.setRole(Role.USER);
        testUser.setLogin("login");
        testUser.setPassword("password");
    }

    @Test
    @DisplayName("When addEvent is called it then return Event")
    void test1() {
        // given
        when(securityContextUtils.getCurrentUser()).thenReturn(testUser);
        when(eventRepository.save(any())).thenReturn(testEvent);
        // when
        Event savedEvent = eventService.addEvent(testEvent);
        // then
        assertThat(testUser).isEqualTo(savedEvent.getOrganizer());
        assertThat(testEvent.getName()).isEqualTo(savedEvent.getName());
    }

    @Test
    @DisplayName("When addEvent is called it then return Event")
    void test2() {
        // given
        when(securityContextUtils.getCurrentUser()).thenThrow(UserNotFound.class);
        // when
        assertThrows(UserNotFound.class, () -> eventService.addEvent(testEvent));
        // then
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("When findEventById gets not null Optional then Event should be returned")
    void test3() {
        // given
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(testEvent));
        // when
        Event event = eventService.findEventById(1L);
        // then
        assertThat(event).isEqualTo(testEvent);
    }

    @Test
    @DisplayName("should delete Event")
    void test4() {
        // given
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(testEvent));
        // when
        // wywolujemy metode serwisu do usuwania eventu o zadanym id
        eventService.deleteEventById(1L);
        // then
        // oczekujemy, ze zostala wywolana metoda delete repozytorium z parametrem o wartosci id usuwanego eventu
        verify(eventRepository).delete(testEvent);
    }

    @Test
    @DisplayName("When findEventByNameContaining gets at least one record then returns list of Events")
    void test5() {
        List<Event> events1;
        // given
        when(eventRepository.findEventByNameContaining(eq("test"))).thenReturn(events1 = Arrays.asList(testEvent));
        // when
        List<Event> events2 = eventService.findEventByNameContaining("test");
        // then
        assertThat(events2).isEqualTo(events1);
    }

    @Test
    @DisplayName("when findEventByOrganizerLogin is called then return list of Events")
    void test6() {
        List<Event> events1;
        List<Event> events2;
        String login = "myLogin";
        // given
        when(eventRepository.findEventByOrganizerLogin(login)).thenReturn(events1 = Arrays.asList(testEvent));
        // when
        events2 = eventService.findEventByOrganizer(login);
        // then
        assertThat(events1).isEqualTo(events2);
    }
}
