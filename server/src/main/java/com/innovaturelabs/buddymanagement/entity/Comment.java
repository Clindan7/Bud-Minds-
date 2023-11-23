package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1);
        public final byte value;
        private Status(byte value) {
            this.value = value;
        }
    }


    public enum Role {
        TRAINER((byte) 3),
        TRAINEE((byte) 4);

        public final byte value;

        private Role(byte value) {
            this.value = value;
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "session")
    private Session session;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private String description;

    private byte status;

    private byte userRole;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getUserRole() {
        return userRole;
    }

    public void setUserRole(byte userRole) {
        this.userRole = userRole;
    }


    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Comment(Session session, User user, String description, byte userRole) {
        this.session = session;
        this.user = user;
        this.description = description;
        this.status = Status.ACTIVE.value;
        this.userRole=userRole;
        LocalDateTime dt=LocalDateTime.now();
        this.updateDate=dt;
        this.createDate=dt;
    }

    public Comment() {
    }
}
