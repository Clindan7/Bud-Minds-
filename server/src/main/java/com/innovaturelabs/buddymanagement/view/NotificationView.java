package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Notification;

import java.time.LocalDateTime;

public class NotificationView {

    private final Integer notificationId;

    private final Integer userId;
    private final Integer sessionId;

    private final String message;

    private final byte status;

    private final Integer createdBy;

    private final LocalDateTime createDate;


    public Integer getNotificationId() {
        return notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public String getMessage() {
        return message;
    }

    public byte getStatus() {
        return status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }


    public NotificationView(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.userId = notification.getUserId().getUserId();
        if (notification.getSessionId() != null)
            this.sessionId = notification.getSessionId().getSessionId();
        else
            this.sessionId = null;
        this.message = notification.getMessage();
        this.status = notification.getStatus();
        this.createdBy = notification.getCreatedBy().getUserId();
        this.createDate = notification.getCreateDate();
    }
}
