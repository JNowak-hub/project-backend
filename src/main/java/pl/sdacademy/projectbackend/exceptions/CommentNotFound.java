package pl.sdacademy.projectbackend.exceptions;

public class CommentNotFound extends RuntimeException {
    public CommentNotFound(String message) {
        super(message);
    }
}
