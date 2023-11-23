package com.innovaturelabs.buddymanagement.entity;

import com.innovaturelabs.buddymanagement.form.UserFeedbackForm;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class UserFeedback {

    public enum Status {
        INACTIVE((byte) 0),
        ACTIVE((byte) 1),
        EDITABLE((byte) 2);
        public final byte value;

        private Status(byte value) {
            this.value = value;
        }
    }

    public enum feedbackType {
        MONTHLY((byte) 1),
        WEEKLY((byte) 2);

        public final byte value;

        private feedbackType(byte value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feedbackId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_provider", referencedColumnName = "user_id")
    private User feedbackProvider;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_receiver", referencedColumnName = "user_id")
    private User feedbackReceiver;

    private byte feedbackType;

    private LocalDate feedbackStart;
    private LocalDate feedbackEnd;
    private byte status;

    private String comment;

    private Integer adaptability;

    private Integer analytical;

    private Integer implementation;

    private Integer jobKnowledge;

    private Integer quality;

    private Integer validation;

    private Integer expressIdeas;

    private Integer efficientChannelUse;

    private Integer oral;

    private Integer infoSharing;

    private Integer written;

    private Integer responsibility;

    private Integer decision;

    private Integer innovation;

    private Integer onTime;

    private Integer excellence;

    private Integer pleasantNature;

    private Integer punctual;

    private Integer hierarchy;

    private Integer upgradation;

    private Integer approachable;

    private Integer constructive;

    private Integer synergy;

    private Integer respect;
    private Integer selflessness;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public UserFeedback(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public UserFeedback() {
    }
    public UserFeedback(User feedbackProvider,User feedbackReceiver, UserFeedbackForm form) {
        this.feedbackProvider = feedbackProvider;
        this.feedbackReceiver=feedbackReceiver;
        this.feedbackStart = form.getFeedbackStart();
        this.feedbackEnd = form.getFeedbackEnd();
        this.feedbackType = form.getFeedbackType();
        this.comment = form.getComment();
        this.adaptability = form.getAdaptability();
        this.analytical = form.getAnalytical();
        this.implementation = form.getImplementation();
        this.jobKnowledge = form.getJobKnowledge();
        this.quality = form.getQuality();
        this.validation = form.getValidation();
        this.expressIdeas = form.getExpressIdeas();
        this.efficientChannelUse = form.getEfficientChannelUse();
        this.oral = form.getOral();
        this.infoSharing = form.getInfoSharing();
        this.written = form.getWritten();
        this.responsibility = form.getResponsibility();
        this.decision = form.getDecision();
        this.innovation = form.getInnovation();
        this.onTime = form.getOnTime();
        this.excellence = form.getExcellence();
        this.approachable = form.getApproachable();
        this.punctual = form.getPunctual();
        this.hierarchy=form.getHierarchy();
        this.respect = form.getRespect();
        this.upgradation = form.getUpgradation();
        this.pleasantNature = form.getPleasantNature();
        this.constructive = form.getConstructive();
        this.synergy = form.getSynergy();
        this.selflessness = form.getSelflessness();
        this.status = UserFeedback.Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }

    public UserFeedback(User feedbackProvider,User feedbackReceiver,String comment,LocalDate feedbackStart,LocalDate feedbackEnd,byte feedbackType) {
        this.feedbackProvider = feedbackProvider;
        this.feedbackReceiver=feedbackReceiver;
        this.comment=comment;
        this.feedbackStart=feedbackStart;
        this.feedbackEnd=feedbackEnd;
        this.feedbackType=feedbackType;
        this.status = UserFeedback.Status.ACTIVE.value;
        LocalDateTime dt = LocalDateTime.now();
        this.createDate = dt;
        this.updateDate = dt;
    }


    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public User getFeedbackProvider() {
        return feedbackProvider;
    }

    public void setFeedbackProvider(User feedbackProvider) {
        this.feedbackProvider = feedbackProvider;
    }

    public User getFeedbackReceiver() {
        return feedbackReceiver;
    }

    public void setFeedbackReceiver(User feedbackReceiver) {
        this.feedbackReceiver = feedbackReceiver;
    }

    public byte getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(byte feedbackType) {
        this.feedbackType = feedbackType;
    }

    public LocalDate getFeedbackStart() {
        return feedbackStart;
    }

    public void setFeedbackStart(LocalDate feedbackStart) {
        this.feedbackStart = feedbackStart;
    }

    public LocalDate getFeedbackEnd() {
        return feedbackEnd;
    }

    public void setFeedbackEnd(LocalDate feedbackEnd) {
        this.feedbackEnd = feedbackEnd;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAdaptability() {
        return adaptability;
    }

    public void setAdaptability(Integer adaptability) {
        this.adaptability = adaptability;
    }

    public Integer getAnalytical() {
        return analytical;
    }

    public void setAnalytical(Integer analytical) {
        this.analytical = analytical;
    }

    public Integer getImplementation() {
        return implementation;
    }

    public void setImplementation(Integer implementation) {
        this.implementation = implementation;
    }

    public Integer getJobKnowledge() {
        return jobKnowledge;
    }

    public void setJobKnowledge(Integer jobKnowledge) {
        this.jobKnowledge = jobKnowledge;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getValidation() {
        return validation;
    }

    public void setValidation(Integer validation) {
        this.validation = validation;
    }

    public Integer getExpressIdeas() {
        return expressIdeas;
    }

    public void setExpressIdeas(Integer expressIdeas) {
        this.expressIdeas = expressIdeas;
    }

    public Integer getEfficientChannelUse() {
        return efficientChannelUse;
    }

    public void setEfficientChannelUse(Integer efficientChannelUse) {
        this.efficientChannelUse = efficientChannelUse;
    }

    public Integer getOral() {
        return oral;
    }

    public void setOral(Integer oral) {
        this.oral = oral;
    }

    public Integer getInfoSharing() {
        return infoSharing;
    }

    public void setInfoSharing(Integer infoSharing) {
        this.infoSharing = infoSharing;
    }

    public Integer getWritten() {
        return written;
    }

    public void setWritten(Integer written) {
        this.written = written;
    }

    public Integer getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(Integer responsibility) {
        this.responsibility = responsibility;
    }

    public Integer getDecision() {
        return decision;
    }

    public void setDecision(Integer decision) {
        this.decision = decision;
    }

    public Integer getInnovation() {
        return innovation;
    }

    public void setInnovation(Integer innovation) {
        this.innovation = innovation;
    }

    public Integer getOnTime() {
        return onTime;
    }

    public void setOnTime(Integer onTime) {
        this.onTime = onTime;
    }

    public Integer getExcellence() {
        return excellence;
    }

    public void setExcellence(Integer excellence) {
        this.excellence = excellence;
    }

    public Integer getPleasantNature() {
        return pleasantNature;
    }

    public void setPleasantNature(Integer pleasantNature) {
        this.pleasantNature = pleasantNature;
    }

    public Integer getPunctual() {
        return punctual;
    }

    public void setPunctual(Integer punctual) {
        this.punctual = punctual;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Integer getUpgradation() {
        return upgradation;
    }

    public void setUpgradation(Integer upgradation) {
        this.upgradation = upgradation;
    }

    public Integer getApproachable() {
        return approachable;
    }

    public void setApproachable(Integer approachable) {
        this.approachable = approachable;
    }

    public Integer getConstructive() {
        return constructive;
    }

    public void setConstructive(Integer constructive) {
        this.constructive = constructive;
    }

    public Integer getSynergy() {
        return synergy;
    }

    public void setSynergy(Integer synergy) {
        this.synergy = synergy;
    }

    public Integer getRespect() {
        return respect;
    }

    public void setRespect(Integer respect) {
        this.respect = respect;
    }

    public Integer getSelflessness() {
        return selflessness;
    }

    public void setSelflessness(Integer selflessness) {
        this.selflessness = selflessness;
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
