package pl.sdacademy.projectbackend.exceptions;

public class UserAlreadyAssigned extends RuntimeException {
    public UserAlreadyAssigned(String message) {
        super(message);
    }
}
