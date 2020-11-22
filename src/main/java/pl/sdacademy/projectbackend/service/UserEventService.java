package pl.sdacademy.projectbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;
import pl.sdacademy.projectbackend.repository.UserEventRepository;

@Service
public class UserEventService {

    private UserService userService;
    private EventService eventService;
    private UserEventRepository repository;

    public UserEventService(UserService userService, EventService eventService, UserEventRepository repository) {
        this.userService = userService;
        this.eventService = eventService;
        this.repository = repository;
    }

    public UserEvent takePartInEvent(Long eventId){

        Event event = eventService.findEventById(eventId);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(currentUser == null){
            throw new UserNotFound("Logg in before take part in event");
        }

        return repository.save(new UserEvent(currentUser, event));
    }
}
