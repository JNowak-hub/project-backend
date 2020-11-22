package pl.sdacademy.projectbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.UserAlreadyAssigned;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;
import pl.sdacademy.projectbackend.repository.UserEventRepository;
import pl.sdacademy.projectbackend.utilities.SecurityContestUtils;

@Service
public class UserEventService {

    private UserService userService;
    private EventService eventService;
    private UserEventRepository repository;
    private SecurityContestUtils securityContestUtils;

    public UserEventService(UserService userService, EventService eventService, UserEventRepository repository, SecurityContestUtils securityContestUtils) {
        this.userService = userService;
        this.eventService = eventService;
        this.repository = repository;
        this.securityContestUtils = securityContestUtils;
    }

    public UserEvent takePartInEvent(Long eventId){

        Event event = eventService.findEventById(eventId);
        User currentUser = securityContestUtils.getCurrentUser();

        UserEvent userEvent = new UserEvent(currentUser, event);

        if(repository.existsById(userEvent.getId())){
            throw new UserAlreadyAssigned("User : " + currentUser.getLogin() + " is already assigned to event: " + event.getName());
        }

        return repository.save(userEvent);
    }
}
