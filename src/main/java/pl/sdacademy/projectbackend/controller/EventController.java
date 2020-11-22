package pl.sdacademy.projectbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.service.EventService;

@RestController
@RequestMapping("api/event")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findEventById(id));
    }

    @DeleteMapping("{id}")
    public void deleteEventById(@PathVariable Long id) {
        eventService.deleteEventById(id);
    }
}
