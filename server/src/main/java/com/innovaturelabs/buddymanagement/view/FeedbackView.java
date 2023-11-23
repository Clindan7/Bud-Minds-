package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.UserFeedback;
import com.innovaturelabs.buddymanagement.json.Json;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FeedbackView {

    private final Integer feedbackId;

    private final Integer feedbackProvider;

    private final String feedbackProviderName;


    private final Integer feedbackReceiver;

    private final String feedbackReceiverName;

    private final byte feedbackType;

    private final LocalDate feedbackStart;

    private final LocalDate feedbackEnd;

    private final byte status;

    private final String comment;

    private final Integer adaptability;

    private final Integer analytical;

    private final Integer implementation;

    private final Integer jobKnowledge;

    private final Integer quality;

    private final Integer validation;

    private final Integer expressIdeas;
    private final Integer efficientChannelUse;

    private final Integer oral;

    private final Integer infoSharing;

    private final Integer written;

    private final Integer responsibility;

    private final Integer decision;

    private final  Integer innovation;
    private final Integer onTime;

    private final Integer excellence;

    private final Integer pleasantNature;

    private final Integer punctual;

    private final Integer hierarchy;

    private final Integer upgradation;

    private final Integer approachable;

    private final Integer constructive;

    private final Integer synergy;

    private final Integer respect;

    private final Integer selflessness;

    @Json.DateTimeFormat
    private final LocalDateTime createDate;
    @Json.DateTimeFormat
    private final LocalDateTime updateDate;

    public String getFeedbackProviderName() {
        return feedbackProviderName;
    }


    public String getFeedbackReceiverName() {
        return feedbackReceiverName;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public Integer getFeedbackProvider() {
        return feedbackProvider;
    }

    public Integer getFeedbackReceiver() {
        return feedbackReceiver;
    }

    public byte getFeedbackType() {
        return feedbackType;
    }

    public LocalDate getFeedbackStart() {
        return feedbackStart;
    }

    public LocalDate getFeedbackEnd() {
        return feedbackEnd;
    }

    public byte getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public Integer getAdaptability() {
        return adaptability;
    }

    public Integer getAnalytical() {
        return analytical;
    }

    public Integer getImplementation() {
        return implementation;
    }

    public Integer getJobKnowledge() {
        return jobKnowledge;
    }

    public Integer getQuality() {
        return quality;
    }

    public Integer getValidation() {
        return validation;
    }

    public Integer getExpressIdeas() {
        return expressIdeas;
    }

    public Integer getEfficientChannelUse() {
        return efficientChannelUse;
    }

    public Integer getOral() {
        return oral;
    }

    public Integer getInfoSharing() {
        return infoSharing;
    }

    public Integer getWritten() {
        return written;
    }

    public Integer getResponsibility() {
        return responsibility;
    }

    public Integer getDecision() {
        return decision;
    }

    public Integer getInnovation() {
        return innovation;
    }

    public Integer getOnTime() {
        return onTime;
    }

    public Integer getExcellence() {
        return excellence;
    }

    public Integer getPleasantNature() {
        return pleasantNature;
    }

    public Integer getPunctual() {
        return punctual;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public Integer getUpgradation() {
        return upgradation;
    }

    public Integer getApproachable() {
        return approachable;
    }

    public Integer getConstructive() {
        return constructive;
    }

    public Integer getSynergy() {
        return synergy;
    }

    public Integer getRespect() {
        return respect;
    }

    public Integer getSelflessness() {
        return selflessness;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public FeedbackView( UserFeedback userFeedback) {
        this.feedbackId = userFeedback.getFeedbackId();
        this.feedbackProvider = userFeedback.getFeedbackProvider().getUserId();
        this.feedbackProviderName = userFeedback.getFeedbackProvider().getFirstName().concat(""+userFeedback.getFeedbackProvider().getLastName());
        this.feedbackReceiver = userFeedback.getFeedbackReceiver().getUserId();
        this.feedbackReceiverName=userFeedback.getFeedbackReceiver().getFirstName().concat(""+userFeedback.getFeedbackReceiver().getLastName());
        this.feedbackType = userFeedback.getFeedbackType();
        this.feedbackStart = userFeedback.getFeedbackStart();
        this.feedbackEnd = userFeedback.getFeedbackEnd();
        this.status = UserFeedback.Status.ACTIVE.value;
        this.comment = userFeedback.getComment();
        this.adaptability = userFeedback.getAdaptability();
        this.analytical = userFeedback.getAnalytical();
        this.implementation = userFeedback.getImplementation();
        this.jobKnowledge = userFeedback.getJobKnowledge();
        this.quality = userFeedback.getQuality();
        this.validation = userFeedback.getValidation();
        this.expressIdeas = userFeedback.getExpressIdeas();
        this.efficientChannelUse = userFeedback.getEfficientChannelUse();
        this.oral = userFeedback.getOral();
        this.infoSharing = userFeedback.getInfoSharing();
        this.written = userFeedback.getWritten();
        this.responsibility = userFeedback.getResponsibility();
        this.decision = userFeedback.getDecision();
        this.innovation = userFeedback.getInnovation();
        this.onTime = userFeedback.getOnTime();
        this.excellence = userFeedback.getExcellence();
        this.pleasantNature = userFeedback.getPleasantNature();
        this.punctual = userFeedback.getPunctual();
        this.hierarchy = userFeedback.getHierarchy();
        this.upgradation = userFeedback.getUpgradation();
        this.approachable = userFeedback.getApproachable();
        this.constructive = userFeedback.getConstructive();
        this.synergy = userFeedback.getSynergy();
        this.respect = userFeedback.getRespect();
        this.selflessness = userFeedback.getSelflessness();
        this.createDate = userFeedback.getCreateDate();
        this.updateDate = userFeedback.getUpdateDate();
    }
}
