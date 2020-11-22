package pl.sdacademy.projectbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.projectbackend.model.Comment;
import pl.sdacademy.projectbackend.service.CommentService;

@RestController
@RequestMapping("api/comment")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findCommentById(id));
    }

    @DeleteMapping("{id}")
    public void deleteCommentById(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(comment));
    }
}
