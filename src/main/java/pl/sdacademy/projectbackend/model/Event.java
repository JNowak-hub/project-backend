package pl.sdacademy.projectbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.sdacademy.projectbackend.model.userparty.UserEvent;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event_table")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Event name can not be blank")
    @NotEmpty(message = "Event name can not be empty")
    @NotNull
    private String name;

    @Min(value = 20, message = "Description can not be shorter than 20 characters")
    private String description;

    @Column(nullable = false)
    @FutureOrPresent
    @NotNull
    private LocalDateTime startDate;

    @Column(nullable = false)
    @NotNull
    @Future
    private LocalDateTime endDate;
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserEvent> participants;

    @ManyToOne
    @NotNull
    private User organizer;

    @OneToMany
    private List<Comment> comments;

    @NotNull(message = "Location can not be null")
    @ManyToOne
    private Location location;

    @NotNull
    private Boolean privateEvent;

    public Event() {
    }

    public Event(@NotBlank(message = "Event name can not be blank") @NotEmpty(message = "Event name can not be empty") @NotNull String name, @Min(value = 20, message = "Description can not be shorter than 20 characters") String description, @FutureOrPresent @NotNull LocalDateTime startDate, @NotNull @Future LocalDateTime endDate, List<UserEvent> participants, @NotNull User organizer, List<Comment> comments, @NotNull(message = "Location can not be null") Location location, @NotNull Boolean privateEvent) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.organizer = organizer;
        this.comments = comments;
        this.location = location;
        this.privateEvent = privateEvent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @JsonIgnore
    public List<UserEvent> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserEvent> participants) {
        this.participants = participants;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(Boolean privateEvent) {
        this.privateEvent = privateEvent;
    }
}
