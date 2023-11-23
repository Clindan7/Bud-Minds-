package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.TraineeTask;

public class UserScoreListView {

    private final Integer traineeTaskId;

    private final Integer mentorId;

    private final String mentorName;

    private final Integer traineeId;

    private final String traineeName;

    private final Long employeeId;

    private final Integer managerId;

    private final String managerName;

    private final Integer overallScore;

    private final byte status;

    private final Integer joinerGroupId;

    public Integer getTraineeTaskId() {
        return traineeTaskId;
    }

    public Integer getMentorId() {
        return mentorId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public byte getStatus() {
        return status;
    }

    public Integer getJoinerGroupId() {
        return joinerGroupId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public Integer getTraineeId() {
        return traineeId;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public String getManagerName() {
        return managerName;
    }

    public UserScoreListView(TraineeTask task,Integer score) {
        this.traineeTaskId = task.getTraineeTaskId();
        if(task.getTraineeId().getMentorId() !=null) {
            this.mentorId = task.getTraineeId().getMentorId().getUserId();
            this.mentorName = task.getTraineeId().getMentorId().getFirstName().concat(" " + task.getTraineeId().getMentorId().getLastName());
        }
        else {
            this.mentorId=null;
            this.mentorName=null;
        }
        this.traineeId = task.getTraineeId().getUserId();
        this.traineeName=task.getTraineeId().getFirstName().concat(" "+task.getTraineeId().getLastName());
        this.employeeId = task.getTraineeId().getEmployeeId();
        if(task.getTraineeId().getManagerId() !=null) {
            this.managerId = task.getTraineeId().getManagerId().getUserId();
            this.managerName = task.getTraineeId().getManagerId().getFirstName().concat(" " + task.getTraineeId().getManagerId().getLastName());
        }
        else {
            this.managerId=null;
            this.managerName=null;
        }
        this.overallScore = score;
        this.status = task.getStatus();
        this.joinerGroupId = task.getTraineeId().getJoinerGroup().getJoinerGroupId();
    }
}
