package com.innovaturelabs.buddymanagement.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Attendance {

    public enum Status{
        ABSENT((byte)0),
        PRESENT((byte)1);
        public final byte value;

        private Status(byte value){
            this.value=value;

        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendanceId;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session sessionId;
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private User traineeId;

    private LocalDateTime timeIn;

    private LocalDateTime timeOut;

    private byte status;
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Session getSessionId() {
        return sessionId;
    }

    public void setSessionId(Session sessionId) {
        this.sessionId = sessionId;
    }

    public User getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(User traineeId) {
        this.traineeId = traineeId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
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

    public Attendance() {
    }

    public Attendance(Session sessionId, User traineeId) {
        this.sessionId = sessionId;
        this.traineeId = traineeId;
        LocalDateTime dt=LocalDateTime.now();
        this.timeIn = dt;
        this.timeOut=dt;
        this.status = Status.ABSENT.value;
        this.createDate = dt;
        this.updateDate = dt;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendance_id=" + attendanceId +
                ", sessionId=" + sessionId +
                ", traineeId=" + traineeId +
                ", timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                ", status=" + status +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
