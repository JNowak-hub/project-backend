package pl.sdacademy.projectbackend.service;

import com.google.maps.errors.ApiException;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.BadRequestException;
import pl.sdacademy.projectbackend.exceptions.EventNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.Location;
import pl.sdacademy.projectbackend.repository.EventRepository;
import pl.sdacademy.projectbackend.utilities.SecurityContextUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private EventRepository eventRepository;
    private SecurityContextUtils securityContextUtils;
    private GoogleMapsApi googleMapsApi;

    public EventService(EventRepository eventRepository, SecurityContextUtils securityContextUtils, GoogleMapsApi googleMapsApi) {
        this.eventRepository = eventRepository;
        this.securityContextUtils = securityContextUtils;
        this.googleMapsApi = googleMapsApi;
    }

    public Event addEvent(Event event, String locationName) {
        addLocationToEventByGoogleApi(event, locationName);
        event.setOrganizer(securityContextUtils.getCurrentUser());
        return eventRepository.save(event);
    }

    private void addLocationToEventByGoogleApi(Event event, String locationName) {
        try {
            event.setLocation(googleMapsApi.saveLocationFromGoogleMapsApi(locationName));
        } catch (InterruptedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ApiException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
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

    public List<Event> findEventByOrganizer(String login) {
        return eventRepository.findEventByOrganizerLogin(login);
    }

    public List<Event> findEventByStartDate(LocalDateTime startDate) {
        return eventRepository.findEventByStartDate(startDate);
    }

    public List<Event> findEventByEndDate(LocalDateTime endDate) {
        return eventRepository.findEventByEndDate(endDate);
    }

    public List<Event> findEventByLocation(Location location) {
        return eventRepository.findEventByLocation(location);
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
