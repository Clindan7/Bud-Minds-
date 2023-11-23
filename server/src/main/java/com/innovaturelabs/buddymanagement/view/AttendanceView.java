package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Attendance;

import java.time.LocalDateTime;

public class AttendanceView {

    private final Integer attendanceId;

    private final Integer sessionId;

    private final Integer traineeId;

    private final LocalDateTime timeIn;

    private final byte status;

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public Integer getTraineeId() {
        return traineeId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public byte getStatus() {
        return status;
    }

    public AttendanceView(Attendance attendance) {
        this.attendanceId = attendance.getAttendanceId();
        this.sessionId = attendance.getSessionId().getSessionId();
        this.traineeId = attendance.getTraineeId().getUserId();
        this.timeIn = attendance.getTimeIn();
        this.status = attendance.getStatus();
    }
}
