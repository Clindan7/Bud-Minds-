package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Comment;

import java.time.LocalDateTime;

public class CommentView {

    private final Integer commentId;

    private final Integer sessionId;

    private final Integer userId;

    private final String description;

    private final byte status;

    private final byte userRole;

    private final LocalDateTime createDate;

    private final LocalDateTime updateDate;

    public Integer getCommentId() {
        return commentId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public byte getStatus() {
        return status;
    }

    public byte getUserRole() {
        return userRole;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public CommentView(Comment comment) {
        this.commentId = comment.getCommentId();
        this.sessionId = comment.getSession().getSessionId();
        this.userId = comment.getUser().getUserId();
        this.description = comment.getDescription();
        this.status = comment.getStatus();
        this.userRole = comment.getUserRole();
        this.createDate = comment.getCreateDate();
        this.updateDate = comment.getUpdateDate();
    }
}
