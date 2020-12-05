package pl.sdacademy.projectbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.service.EventService;

import java.util.List;

@RestController
@RequestMapping("api/event/")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("add/{locationName}")
    public ResponseEntity<Event> addEvent(@RequestBody Event event, @PathVariable String locationName){
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addEvent(event, locationName));
    }

    @GetMapping("{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findEventById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEventById(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Event deleted");
    }

    @GetMapping("/name/{keyword}")
    public ResponseEntity<List<Event>> findEventByNameContaining(@PathVariable String keyword) {
        return ResponseEntity.ok(eventService.findEventByNameContaining(keyword));
    }

    @GetMapping("/login/{organizerLogin}")
    public ResponseEntity<List<Event>> findEventByOrganizerLogin(@PathVariable String organizerLogin) {
        return ResponseEntity.ok(eventService.findEventByOrganizer(organizerLogin));
    }

}
