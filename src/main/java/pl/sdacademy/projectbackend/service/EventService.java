package pl.sdacademy.projectbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.sdacademy.projectbackend.exceptions.EventNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.EventRepository;

import java.util.List;
import java.util.Optional;

public class EventService {
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event addEvent(Event event) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        event.setOrganizer(loggedUser);
        return eventRepository.save(event);
    }

    public Event findEventById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new EventNotFound("No event with id " + id);
        }
        return optionalEvent.get();
    }

    public void deleteEventById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new EventNotFound("No event with id " + id);
        }
        eventRepository.deleteById(id);
    }
}
