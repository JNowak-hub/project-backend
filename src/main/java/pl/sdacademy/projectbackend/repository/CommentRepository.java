package pl.sdacademy.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.projectbackend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
