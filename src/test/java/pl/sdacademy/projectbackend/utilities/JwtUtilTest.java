package pl.sdacademy.projectbackend.utilities;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ContextConfiguration
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.expiration.time}")
    private Long EXPIRATION_TIME;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setPassword("password");
        user.setLogin("login");
        user.setRole(Role.USER);
    }

    @Test
    @DisplayName("When generateToken then return token as String")
    public void test1() {
        //given when
        String token = jwtUtil.generateToken(user);
        //then
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("When extractExpiration then extract date.now() + 1200000 ms")
    void test2() {
        //given
        String token = jwtUtil.generateToken(user);
        //when
        Date expirationDate = jwtUtil.extractExpiration(token);
        //then
        assertThat(expirationDate).isCloseTo(new Date(System.currentTimeMillis() + EXPIRATION_TIME), 10000L);
    }

    @Test
    @DisplayName("When extractLogin then extract login from token")
    void test3() {
        //given
        String token = jwtUtil.generateToken(user);
        //when
        String login = jwtUtil.extractLogin(token);
        //then
        assertThat(login).isEqualTo(user.getLogin());
    }

    @Test
    @DisplayName("When validate token with correct token then throw ExpiredJwtException")
    void test4() {
        //given
        String correctToken = jwtUtil.generateToken(user);
        //when
        Boolean tokenIsValid = jwtUtil.validateToken(correctToken, user);
        //then
        assertThat(tokenIsValid).isTrue();
    }

    @Test
    @DisplayName("When validate token with correct token but bad user then return false")
    void test5() {
        //given
        User wrongUser = new User();
        wrongUser.setRole(Role.USER);
        wrongUser.setLogin("badUser");
        wrongUser.setPassword("password");
        String correctToken = jwtUtil.generateToken(user);
        //when
        Boolean tokenIsValid = jwtUtil.validateToken(correctToken, wrongUser);
        //then
        assertThat(tokenIsValid).isFalse();
    }

    @Test
    @DisplayName("When validate token with expired token then return false")
    void test6() {
        //given
        ReflectionTestUtils.setField(jwtUtil, "EXPIRATION_TIME", 0L);
        String expiredToken = jwtUtil.generateToken(user);
        ReflectionTestUtils.setField(jwtUtil, "EXPIRATION_TIME", EXPIRATION_TIME);
        //when
        ExpiredJwtException exception = assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(expiredToken, user));
        //then
        assertThat(exception.getMessage()).startsWith("JWT expired at");
    }
}
