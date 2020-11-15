package pl.sdacademy.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sdacademy.projectbackend.model.User;

public interface UserRepository extends JpaRepository <User, Long> {
}
