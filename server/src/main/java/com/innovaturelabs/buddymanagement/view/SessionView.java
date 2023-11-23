package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Session;

import java.time.LocalDateTime;

public class SessionView {
    private final Integer sessionId;

    private final Integer trainingId;

    private final String trainingName;

    private final Integer trainerId;

    private final String trainerName;

    private final Integer joinerGroup;

    private final String joinerGroupName;

    private final Integer joinerBatch;
    private final byte status;

    private final Integer createdBy;

    private final String description;

    private final LocalDateTime sessionStart;

    private final LocalDateTime sessionEnd;

    private final Integer technologyId;
    private final String technologyName;

    public String getTechnologyName() {
        return technologyName;
    }

    public Integer getTechnologyId() {
        return technologyId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public Integer getJoinerGroup() {
        return joinerGroup;
    }

    public Integer getJoinerBatch() {
        return joinerBatch;
    }

    public byte getStatus() {
        return status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public String getDescription() {
        return description;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getJoinerGroupName() {
        return joinerGroupName;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }


    public SessionView(Session session){
        this.sessionId=session.getSessionId();
        this.trainingId=session.getTrainingId().getTrainingId();
        this.trainingName=session.getTrainingId().getTitle();
        this.trainerId=session.getTrainerId().getUserId();
        this.trainerName=session.getTrainerId().getFirstName().concat(" "+session.getTrainerId().getLastName());
        this.joinerGroup=session.getjoinerGroupId().getJoinerGroupId();
        this.joinerGroupName=session.getjoinerGroupId().getJoinerGroupName();
        this.joinerBatch=session.getjoinerGroupId().getJoinerBatch().getJoinerBatchId();
        this.description=session.getSessionDescription();
        this.sessionStart=session.getSessionStart();
        this.sessionEnd=session.getSessionEnd();
        this.createdBy=session.getCreatedBy().getUserId();
        this.status=session.getStatus();
        this.technologyId=session.getTrainingId().getTechnologyId().getTechnologyId();
        this.technologyName=session.getTrainingId().getTechnologyId().getTechnologyName();
    }
}