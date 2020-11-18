package pl.sdacademy.projectbackend.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 3, message = "Name can not be shorter than 3 characters")
    @Max(value = 13, message = "Name can not be longer than 13 characters")
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    @NotEmpty(message = "Last name can not be empty")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Column(nullable = false)
    @Min(value = 3, message = "Login can not be shorter than 3 characters")
    private String login;

    @Column(nullable = false)
    @NotNull(message = "Password can not be null")
    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password can not be blank")
    private String password;

    @Column(nullable = false)
    @Email(message = "Email must be valid")
    private String email;

    @ManyToMany
    List<Event> events;

    public User() {
    }

    public User(String firstName, String lastName, LocalDate birthDate, String login, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public List<Event> getEvents() {
        return events;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
