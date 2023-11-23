package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.TraineeTask;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDateTime;

public class TraineeTaskView {

    private final int traineeTaskId;
    private final String delayReason;
    private final Task taskId;

    private final User traineeId;

    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    private final byte status;

    public int getTraineeTaskId() {
        return traineeTaskId;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public Task getTaskId() {
        return taskId;
    }

    public User getTraineeId() {
        return traineeId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public byte getStatus() {
        return status;
    }

    public TraineeTaskView(TraineeTask traineeTask){
        this.traineeTaskId = traineeTask.getTraineeTaskId();
        this.taskId=traineeTask.getTaskId();
        this.traineeId=traineeTask.getTraineeId();
        this.delayReason=traineeTask.getDelayReason();
        this.status = traineeTask.getStatus();
        this.createDate = traineeTask.getCreateDate();
        this.updateDate = traineeTask.getUpdateDate();
    }
}
