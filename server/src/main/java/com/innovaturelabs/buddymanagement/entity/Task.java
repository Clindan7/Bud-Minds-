package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Task {
    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    private String taskName;
    private String taskDescription;
    private LocalDate taskStart;
    private LocalDate taskEnd;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User taskCreator;

    private byte status;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "training_id", referencedColumnName = "training_id")
    private Training trainingId;

    private Integer taskParentId;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public Task() {
    }

    public Task(Integer taskId) {
        this.taskId = taskId;
    }

    public Task(String taskName, String taskDescription, LocalDate taskStart, LocalDate taskEnd, User taskCreator, Training trainingId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
        this.taskCreator = taskCreator;
        this.status = Task.Status.ACTIVE.value;
        this.trainingId = trainingId;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }
    public Task(String taskName, String taskDescription,Integer taskParentId, LocalDate taskStart, LocalDate taskEnd, User taskCreator, Training trainingId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskParentId=taskParentId;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
        this.taskCreator = taskCreator;
        this.status = Task.Status.ACTIVE.value;
        this.trainingId = trainingId;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }


    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDate getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(LocalDate taskStart) {
        this.taskStart = taskStart;
    }

    public LocalDate getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(LocalDate taskEnd) {
        this.taskEnd = taskEnd;
    }

    public User getTaskCreator() {
        return taskCreator;
    }

    public void setTaskCreator(User taskCreator) {
        this.taskCreator = taskCreator;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Training getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Training trainingId) {
        this.trainingId = trainingId;
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

    public Integer getTaskParentId() {
        return taskParentId;
    }

    public void setTaskParentId(Integer taskParentId) {
        this.taskParentId = taskParentId;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
