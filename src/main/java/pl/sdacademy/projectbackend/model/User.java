package pl.sdacademy.projectbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;
import pl.sdacademy.projectbackend.oauth.facebook.model.AuthProvider;
import pl.sdacademy.projectbackend.validaiton.groups.StandardUserValidation;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 13, message = "Name must be between 3-13 characters")
    private String firstName;

    @Length(min = 3, message = "Last name must have at least 3 characters")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    @Length(min = 3, message = "login can not be shorter than 3 characters")
    @NotNull(message = "Login can not be null")
    private String login;

    @NotBlank(message = "Password can not be blank", groups = StandardUserValidation.class)
    @NotNull(message = "Password can not be null" , groups = StandardUserValidation.class)
    private String password;

    @Column(nullable = false)
    @Email(message = "Email must be valid")
    @NotEmpty
    @NotNull(message = "Email can not be null")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    List<UserEvent> events;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String imageUrl;

    public User() {
    }

    public User(String firstName, String lastName, LocalDate birthDate, String login, String password, String email, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public List<UserEvent> getEvents() {
        return events;
    }

    public void setEvents(List<UserEvent> events) {
        this.events = events;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
