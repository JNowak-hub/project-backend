package pl.sdacademy.projectbackend.service;

import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.EventNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.EventRepository;
import pl.sdacademy.projectbackend.utilities.SecurityContextUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private EventRepository eventRepository;
    private SecurityContextUtils securityContestUtils;

    public EventService(EventRepository eventRepository, SecurityContextUtils securityContestUtils) {
        this.securityContestUtils = securityContestUtils;
        this.eventRepository = eventRepository;
    }

    public Event addEvent(Event event) {
        event.setOrganizer(securityContestUtils.getCurrentUser());
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

    public List<Event> findEventByNameContaining(String name) {
        return eventRepository.findEventByNameContaining(name);
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
