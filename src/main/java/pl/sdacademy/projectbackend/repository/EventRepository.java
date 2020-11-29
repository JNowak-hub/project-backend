package pl.sdacademy.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventByNameContaining(String name);
    List<Event> findEventByOrganizerLogin(String login);
}
