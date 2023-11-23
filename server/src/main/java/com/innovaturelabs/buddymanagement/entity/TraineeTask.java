package com.innovaturelabs.buddymanagement.entity;



import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TraineeTask {
    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1),
        PENDING((byte) 2),
        COMPLETED((byte) 3),
        DELAYED((byte) 4);

        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer traineeTaskId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "trainee_id", referencedColumnName = "user_id")
    private User traineeId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Task taskId;
    private byte status;

    private String delayReason;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public TraineeTask() {
    }

    public TraineeTask(Integer traineeTaskId) {
        this.traineeTaskId = traineeTaskId;
    }

    public TraineeTask( User traineeId, Task taskId, String delayReason) {
        this.taskId = taskId;
        this.traineeId = traineeId;
        this.delayReason=delayReason;
        this.status = TraineeTask.Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Integer getTraineeTaskId() {
        return traineeTaskId;
    }

    public void setTraineeTaskId(Integer traineeTaskId) {
        this.traineeTaskId = traineeTaskId;
    }

    public User getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(User traineeId) {
        this.traineeId = traineeId;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
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

}
