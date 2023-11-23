package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.Training;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskView {
    private final int taskId;
    private final String taskName;
    private final String taskDescription;
    private final User taskCreator;
    @Json.DateFormat
    private final LocalDate taskStart;
    @Json.DateFormat
    private final LocalDate taskEnd;

    private final Training trainingId;
    private Integer parentTaskId;

    private  String parentTaskName;

    public String getParentTaskName() {
        return parentTaskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public User getTaskCreator() {
        return taskCreator;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public LocalDate getTaskStart() {
        return taskStart;
    }

    public LocalDate getTaskEnd() {
        return taskEnd;
    }

    public TaskView(Task task){
        this.taskId = task.getTaskId();
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.taskCreator = task.getTaskCreator();
        this.taskStart = task.getTaskStart();
        this.taskEnd = task.getTaskEnd();
        this.trainingId = task.getTrainingId();
        this.createDate = task.getCreateDate();
        this.updateDate = task.getUpdateDate();
        this.status = task.getStatus();
    }

    public TaskView(Task task,Integer parentTaskId){
        this.taskId = task.getTaskId();
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.taskCreator = task.getTaskCreator();
        this.taskStart = task.getTaskStart();
        this.taskEnd = task.getTaskEnd();
        this.trainingId = task.getTrainingId();
        this.createDate = task.getCreateDate();
        this.updateDate = task.getUpdateDate();
        this.status = task.getStatus();
        this.parentTaskId=parentTaskId;
    }


    public TaskView(Task task,Integer parentTaskId,String mainTask){
        this.taskId = task.getTaskId();
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.taskCreator = task.getTaskCreator();
        this.taskStart = task.getTaskStart();
        this.taskEnd = task.getTaskEnd();
        this.trainingId = task.getTrainingId();
        this.createDate = task.getCreateDate();
        this.updateDate = task.getUpdateDate();
        this.status = task.getStatus();
        this.parentTaskId=parentTaskId;
        this.parentTaskName=mainTask;

    }

    public Training getTrainingId() {
        return trainingId;
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

    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    private final byte status;
}
