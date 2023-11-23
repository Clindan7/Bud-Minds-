package com.innovaturelabs.buddymanagement.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Session {
       public enum Status {
        INACTIVE((byte) 0),
        CREATED((byte) 1),
        ONPROGRESS((byte) 2),
        COMPLETED((byte) 3);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training trainingId;

    @ManyToOne
    @JoinColumn(name="trainer_id")
    private User trainerId;

    @ManyToOne
    @JoinColumn(name="joiner_group_id")
    private JoinerGroup joinerGroupId;

    private byte status;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private String sessionDescription;

    private LocalDateTime sessionStart;

    private LocalDateTime sessionEnd;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Session() {
    }

    public Session(Training trainingId, User trainerId, JoinerGroup joinerGroupId, User createdBy, String sessionDescription, LocalDateTime sessionStart, LocalDateTime sessionEnd) {
        this.trainingId = trainingId;
        this.trainerId = trainerId;
        this.joinerGroupId = joinerGroupId;
        this.createdBy = createdBy;
        this.sessionDescription = sessionDescription;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.status=Status.CREATED.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Session(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Training getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Training trainingId) {
        this.trainingId = trainingId;
    }

    public User getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(User trainerId) {
        this.trainerId = trainerId;
    }

    public JoinerGroup getjoinerGroupId() {
        return joinerGroupId;
    }

    public void setjoinerGroupId(JoinerGroup joinerGroupId) {
        this.joinerGroupId = joinerGroupId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(LocalDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(LocalDateTime sessionEnd) {
        this.sessionEnd = sessionEnd;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", trainingId=" + trainingId +
                ", trainerId=" + trainerId +
                ", joinerGroupId=" + joinerGroupId +
                ", status=" + status +
                ", createdBy=" + createdBy +
                ", sessionDescription='" + sessionDescription + '\'' +
                ", sessionStart=" + sessionStart +
                ", sessionEnd=" + sessionEnd +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
