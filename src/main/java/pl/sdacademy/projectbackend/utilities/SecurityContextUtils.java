package pl.sdacademy.projectbackend.utilities;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.User;

@Component
public class SecurityContextUtils {

    public boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public User getCurrentUser() {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new UserNotFound("User not logged");
        }
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
