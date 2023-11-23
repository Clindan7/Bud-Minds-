package com.innovaturelabs.buddymanagement.form;

import java.time.LocalDateTime;

import javax.validation.constraints.*;

public class SessionForm {
    
    @NotNull(message=("{training.id.required}"))
    private Integer trainingId;

    @Size(message = ("{session.description}"), max = 250)
    @Pattern(message = "{session.invalid.description}", regexp = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$|(^$)")
    private String description;

    @NotNull(message = ("{start.date.required}"))
    private LocalDateTime sessionStart;

    @NotNull(message = ("{end.date.required}"))
    private LocalDateTime sessionEnd;

    @NotNull(message=("{trainer.id.required}"))
    private Integer trainerId;


    private Integer groupId;

    public Integer getTrainingId() {
        return trainingId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSessionStart(LocalDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }

    public void setSessionEnd(LocalDateTime sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
