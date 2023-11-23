package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    public enum Status{
        INACTIVE((byte)0),
        ACTIVE((byte)1);
        public final byte value;

        private Status(byte value){this.value=value;}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private  byte status;

    private String message;

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name="session_id")
    private Session sessionId;

    @ManyToOne
    @JoinColumn(name="task_id")
    private Task taskId;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public User getUserId() {
        return userId;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Session getSessionId() {
        return sessionId;
    }

    public void setSessionId(Session sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Notification() {
    }

    public Notification(User user, String message, User createdBy, Session session,Task task) {
        this.userId = user;
        this.status = Status.ACTIVE.value;
        this.message = message;
        LocalDateTime dt=LocalDateTime.now();
        this.createdBy=createdBy;
        this.sessionId=session;
        this.taskId=task;
        this.createDate=dt;
        this.updateDate=dt;
    }

    public Notification(Integer notificationId, User userId, byte status, String message, User createdBy, Session sessionId,Task taskId) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.status = status;
        this.message = message;
        this.createdBy = createdBy;
        this.sessionId = sessionId;
        this.taskId=taskId;
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }
}
