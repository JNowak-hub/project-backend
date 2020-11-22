package pl.sdacademy.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.projectbackend.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);
    List<User> findUserByFirstNameAndLastName(String firstName, String lastName);

}
