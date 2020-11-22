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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        testEvent.setName("Event test name");
        testEvent.setStartDate(LocalDateTime.of(2020, 12, 11, 16, 30));
        testEvent.setEndDate(LocalDateTime.of(2020, 12, 11, 21, 30));
        testEvent.setOrganizer(testUser);
        testEvent.setLocation("Poland");
    }

    @Test
    @DisplayName("When findEventByNameContaining gets at least one record then returns list")
    void test1() {
        List<Event> eventss;
        // given
        when(eventRepository.findEventByNameContaining(eq("test"))).thenReturn(eventss = Arrays.asList(testEvent));
        // when
        List<Event> events = eventService.findEventByNameContaining("test");
        // then
        assertThat(events).isEqualTo(eventss);
    }

    @Test
    @DisplayName("When addEvent is called it should save Event")
    void test2() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("When findEventByIt gets not null Optional then Event should be returned")
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

}
