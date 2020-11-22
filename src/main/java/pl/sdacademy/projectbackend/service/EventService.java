package pl.sdacademy.projectbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.EventNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
        eventRepository.delete(findEventById(id));
    }

    public List<Event> findEventsByName(String name) {
        return eventRepository.findEventsByName(name);
    }

    public List<Event> findEventByOrganizer(User user) {
        return eventRepository.findEventsByOrganizer(user);
    }

    /*public List<Event> findEventByKeywordDescription(String keyword) {
        return eventRepository
                .findAll()
                .stream()
                .filter(a -> a.getDescription().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<Event> findEventByPartialLocation(String partialLocation) {
        return eventRepository
                .findAll()
                .stream()
                .filter(a -> a.getLocation().contains(partialLocation))
                .collect(Collectors.toList());
    }

    public List<Event> findEventByDate(LocalDateTime date) {
        return eventRepository
                .findAll()
                .stream()
                .filter(a ->
                        a.getStartDate().isBefore(date) &&
                        a.getEndDate().isAfter(date))
                .collect(Collectors.toList());
    }*/
}
