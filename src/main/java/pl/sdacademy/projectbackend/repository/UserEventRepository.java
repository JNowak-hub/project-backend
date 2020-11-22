package pl.sdacademy.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;
import pl.sdacademy.projectbackend.model.userparty.UserEventId;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, UserEventId> {
}
