package pl.sdacademy.projectbackend.model;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private String content;

    public Comment() {}

    public Comment(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                '}';
    }
}
