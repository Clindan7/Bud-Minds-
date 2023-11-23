package com.innovaturelabs.buddymanagement.entity;

import com.innovaturelabs.buddymanagement.form.ScoreForm;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Score {

    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1),
        EDITABLE((byte) 2);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public enum QualityOfWork {
        BAD((byte) 0),
        BELOW_AVERAGE((byte) 1),
        MODERATE((byte) 2),
        GOOD((byte) 3);
        public final byte value;

        private QualityOfWork(byte value) {
            this.value = value;
        }
    }

    public enum ExtraEffort {
        NO((byte) 0),
        DO_NOT_KNOW((byte) 1),
        YES((byte) 2);

        public final byte value;

        private ExtraEffort(byte value) {
            this.value = value;
        }
    }

    public enum EnthusiamToLearn {
        NOT_PASSIONATE((byte) 0),
        AVERAGE((byte) 1),
        PASSIONATE((byte) 2);

        public final byte value;

        private EnthusiamToLearn(byte value) {
            this.value = value;
        }
    }

    public enum Adaptability {
        NOT_IMPRESSIVE((byte) 0),
        MODERATE((byte) 1),
        IMPRESSIVE((byte) 2);

        public final byte value;

        private Adaptability(byte value) {
            this.value = value;
        }
    }

    public enum TeamWork {
        BAD((byte) 0),
        AVERAGE((byte) 1),
        GOOD((byte) 2);

        public final byte value;

        private TeamWork(byte value) {
            this.value = value;
        }
    }

    public enum CompletionTime {
        NOT_COMPLETED((byte) 0),
        DELAYED((byte) 1),
        ON_TIME((byte) 2);

        public final byte value;

        private CompletionTime(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scoreId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "trainee_task_id", referencedColumnName = "trainee_task_id")
    private TraineeTask traineeTaskId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "score_assigner", referencedColumnName = "user_id")
    private User scoreAssigner;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "score_assignee", referencedColumnName = "user_id")
    private User scoreAssignee;

    private byte completionTime;

    private byte qualityOfWork;

    private byte extraEffort;

    private byte adaptability;

    private byte teamWork;

    private byte enthusisamToLearn;

    private Integer overallScore;

    private byte status;

    private String comment;


    public Score(TraineeTask traineeTaskId, User scoreAssigner, User scoreAssignee, byte completionTime, ScoreForm form, Integer overallScore) {
        this.traineeTaskId = traineeTaskId;
        this.scoreAssigner = scoreAssigner;
        this.scoreAssignee = scoreAssignee;
        this.completionTime = completionTime;
        adaptability = form.getAdaptability();
        extraEffort = form.getExtraEffort();
        qualityOfWork = form.getQualityOfWork();
        enthusisamToLearn = form.getEnthusiamToLearn();
        teamWork = form.getTeamWork();
        this.overallScore = overallScore;
        this.comment=form.getComment();
        this.status = Score.Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public Score() {

    }

    public Score(Integer scoreId) {
        this.scoreId = scoreId;
    }

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }


    public TraineeTask getTraineeTaskId() {
        return traineeTaskId;
    }

    public void setTraineeTaskId(TraineeTask traineeTaskId) {
        this.traineeTaskId = traineeTaskId;
    }

    public User getScoreAssigner() {
        return scoreAssigner;
    }

    public void setScoreAssigner(User scoreAssigner) {
        this.scoreAssigner = scoreAssigner;
    }

    public User getScoreAssignee() {
        return scoreAssignee;
    }

    public void setScoreAssignee(User scoreAssignee) {
        this.scoreAssignee = scoreAssignee;
    }

    public byte getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(byte completionTime) {
        this.completionTime = completionTime;
    }

    public byte getQualityOfWork() {
        return qualityOfWork;
    }

    public void setQualityOfWork(byte qualityOfWork) {
        this.qualityOfWork = qualityOfWork;
    }

    public byte getExtraEffort() {
        return extraEffort;
    }

    public void setExtraEffort(byte extraEffort) {
        this.extraEffort = extraEffort;
    }

    public byte getAdaptability() {
        return adaptability;
    }

    public void setAdaptability(byte adaptability) {
        this.adaptability = adaptability;
    }

    public byte getTeamWork() {
        return teamWork;
    }

    public void setTeamWork(byte teamWork) {
        this.teamWork = teamWork;
    }

    public byte getEnthusisamToLearn() {
        return enthusisamToLearn;
    }

    public void setEnthusisamToLearn(byte enthusisamToLearn) {
        this.enthusisamToLearn = enthusisamToLearn;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}