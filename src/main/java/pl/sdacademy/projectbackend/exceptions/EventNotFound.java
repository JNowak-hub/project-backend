package pl.sdacademy.projectbackend.exceptions;

public class EventNotFound extends RuntimeException {
    public EventNotFound(String message) {
        super(message);
    }
}
