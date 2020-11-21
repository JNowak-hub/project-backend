package pl.sdacademy.projectbackend.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class UserParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Long userId;

    @OneToMany
    private Long partyId;

    @NotNull
    private Boolean isConfirmedByAdministrator;

    @NotNull
    private Boolean isConfirmedByUser;

    public UserParty() {
    }

    public UserParty(Long id, Long userId, Long partyId, Boolean isConfirmedByAdministrator, Boolean isConfirmedByUser) {
        this.id = id;
        this.userId = userId;
        this.partyId = partyId;
        this.isConfirmedByAdministrator = isConfirmedByAdministrator;
        this.isConfirmedByUser = isConfirmedByUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
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
