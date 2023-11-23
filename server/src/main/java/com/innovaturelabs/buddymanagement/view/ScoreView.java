package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Score;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDateTime;

public class ScoreView {
    private final Integer scoreId;

    private final Integer traineeTaskId;

    private final Integer scoreAssigner;

    private  final String mentorName;

    private final Integer taskId;

    private final String taskName;

    private  final String traineeName;

    private final Integer scoreAssignee;

    private final byte completionTime;

    private final byte qualityOfWork;

    private final byte extraEffort;

    private final byte adaptability;

    private final byte teamWork;

    private final byte enthusisamToLearn;

    private final Integer overallScore;

    private final byte status;

    private final String comment;

    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;


    public Integer getScoreId() {
        return scoreId;
    }

    public Integer getTraineeTaskId() {
        return traineeTaskId;
    }


    public Integer getScoreAssigner() {
        return scoreAssigner;
    }

    public Integer getScoreAssignee() {
        return scoreAssignee;
    }

    public byte getCompletionTime() {
        return completionTime;
    }

    public byte getQualityOfWork() {
        return qualityOfWork;
    }

    public byte getExtraEffort() {
        return extraEffort;
    }

    public byte getAdaptability() {
        return adaptability;
    }

    public byte getTeamWork() {
        return teamWork;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public byte getEnthusisamToLearn() {
        return enthusisamToLearn;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public byte getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public String getMentorName() {
        return mentorName;
    }

    public ScoreView(Score score) {
        this.scoreId = score.getScoreId();
        this.traineeTaskId = score.getTraineeTaskId().getTraineeTaskId();
        this.taskId=score.getTraineeTaskId().getTaskId().getTaskId();
        this.taskName=score.getTraineeTaskId().getTaskId().getTaskName();
        this.scoreAssigner = score.getScoreAssigner().getUserId();
        this.mentorName = score.getScoreAssigner().getFirstName().concat(score.getScoreAssigner().getLastName());
        this.scoreAssignee=score.getScoreAssignee().getUserId();
        this.traineeName=score.getScoreAssignee().getFirstName().concat(score.getScoreAssignee().getLastName());
        this.completionTime = score.getCompletionTime();
        this.qualityOfWork = score.getQualityOfWork();
        this.extraEffort = score.getExtraEffort();
        this.adaptability = score.getAdaptability();
        this.teamWork = score.getTeamWork();
        this.enthusisamToLearn = score.getEnthusisamToLearn();
        this.overallScore = score.getOverallScore();
        this.status = score.getStatus();
        this.comment = score.getComment();
        this.createDate = score.getCreateDate();
        this.updateDate = score.getUpdateDate();
    }
}
