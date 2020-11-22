package pl.sdacademy.projectbackend.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SecurityContestUtilsTest {

    private User testUser;

    private SecurityContestUtils utils = new SecurityContestUtils();

    @BeforeEach
    void setUp(){
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setLogin("login");
        testUser.setRole(Role.USER);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(testUser.getRole().toString()));
        Authentication auth = new
                UsernamePasswordAuthenticationToken(testUser, testUser.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("When isUserLoggedIn then return true")
    public void test1(){
        //given when
        Boolean isLoggedIn = utils.isUserLoggedIn();
        //then
        assertThat(true).isEqualTo(isLoggedIn);
    }

    @Test
    @DisplayName("When isUserLoggedIn then return false")
    public void test2(){
        //given
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        //when
        Boolean isLoggedIn = utils.isUserLoggedIn();
        //then
        assertThat(false).isEqualTo(isLoggedIn);
    }

    @Test
    @DisplayName("When getCurrentUser then return User")
    public void test3(){
        //given when
        User currentUser = utils.getCurrentUser();
        //then
        assertThat(testUser).isEqualTo(currentUser);
    }

    @Test
    @DisplayName("When getCurrentUser then throw UserNotFoud exception")
    public void test4(){
        //given
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        //when
        UserNotFound exception = assertThrows(UserNotFound.class, () -> utils.getCurrentUser());
        //then
        assertThat("User not logged").isEqualTo(exception.getMessage());
    }
}
