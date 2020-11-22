package pl.sdacademy.projectbackend.model.userparty;

import pl.sdacademy.projectbackend.model.Event;
import pl.sdacademy.projectbackend.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class UserEvent {


    @EmbeddedId
    private UserEventId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    private Event event;

    @NotNull
    private Boolean isConfirmedByAdministrator;

    @NotNull
    private Boolean isConfirmedByUser;

    public UserEvent() {
    }

    public UserEvent(User user, Event event) {
        this.id = new UserEventId(user.getId(),event.getId());
        this.user = user;
        this.event = event;
    }



    public Boolean getConfirmedByAdministrator() {
        return isConfirmedByAdministrator;
    }

    public void setConfirmedByAdministrator(Boolean confirmedByAdministrator) {
        isConfirmedByAdministrator = confirmedByAdministrator;
    }

    public Boolean getConfirmedByUser() {
        return isConfirmedByUser;
    }

    public void setConfirmedByUser(Boolean confirmedByUser) {
        isConfirmedByUser = confirmedByUser;
    }
}
