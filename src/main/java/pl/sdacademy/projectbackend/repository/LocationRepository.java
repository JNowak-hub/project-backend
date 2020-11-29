package pl.sdacademy.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.projectbackend.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
