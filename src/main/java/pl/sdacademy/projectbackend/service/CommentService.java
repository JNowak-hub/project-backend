package pl.sdacademy.projectbackend.service;

import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.CommentNotFound;
import pl.sdacademy.projectbackend.model.Comment;
import pl.sdacademy.projectbackend.repository.CommentRepository;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(@Valid Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty()) {
            throw new CommentNotFound("Comment with id: " + id + " doesn't exists");
        }

        return optionalComment.get();
    }

    public void deleteComment(Long id) {
        Comment comment = findCommentById(id);
        commentRepository.delete(comment);
    }

}
