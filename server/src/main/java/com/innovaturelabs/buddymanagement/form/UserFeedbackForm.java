package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class UserFeedbackForm {
    private static final String VALIDATION = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$";

    private Integer feedbackReceiver;

    private LocalDate feedbackStart;

    private LocalDate feedbackEnd;

    private byte feedbackType;

    @NotBlank(message = "{comment.required}")
    @Size(message = "{comment.size}", min = 1, max = 2000)
    @Pattern(message = "{invalid.comment}", regexp = VALIDATION)
    private String comment;

    @Min(value = 1,message = ("{invalid.adaptability}"))
    @Max(value = 10,message = ("{invalid.adaptability}"))
    private Integer adaptability;

    @Min(value = 1,message = ("{invalid.analytical}"))
    @Max(value = 10,message = ("{invalid.analytical}"))
    private Integer analytical;

    @Min(value = 1,message = ("{invalid.implementation}"))
    @Max(value = 10,message = ("{invalid.implementation}"))
    private Integer implementation;

    @Min(value = 1,message = ("{invalid.jobKnowledge}"))
    @Max(value = 10,message = ("{invalid.jobKnowledge}"))
    private Integer jobKnowledge;

    @Min(value = 1,message = ("{invalid.quality}"))
    @Max(value = 10,message = ("{invalid.quality}"))
    private Integer quality;

    @Min(value = 1,message = ("{invalid.user.validation}"))
    @Max(value = 10,message = ("{invalid.user.validation}"))
    private Integer userValidation;

    @Min(value = 1,message = ("{invalid.expressIdeas}"))
    @Max(value = 10,message = ("{invalid.expressIdeas}"))
    private Integer expressIdeas;

    @Min(value = 1,message = ("{invalid.efficientChannelUse}"))
    @Max(value = 10,message = ("{invalid.efficientChannelUse}"))
    private Integer efficientChannelUse;

    @Min(value = 1,message = ("{invalid.oral}"))
    @Max(value = 10,message = ("{invalid.oral}"))
    private Integer oral;

    @Min(value = 1,message = ("{invalid.infoSharing}"))
    @Max(value = 10,message = ("{invalid.infoSharing}"))
    private Integer infoSharing;

    @Min(value = 1,message = ("{invalid.written}"))
    @Max(value = 10,message = ("{invalid.written}"))
    private Integer written;

    @Min(value = 1,message = ("{invalid.responsibility}"))
    @Max(value = 10,message = ("{invalid.responsibility}"))
    private Integer responsibility;

    @Min(value = 1,message = ("{invalid.decision}"))
    @Max(value = 10,message = ("{invalid.decision}"))
    private Integer decision;

    @Min(value = 1,message = ("{invalid.innovation}"))
    @Max(value = 10,message = ("{invalid.innovation}"))
    private Integer innovation;

    @Min(value = 1,message = ("{invalid.onTime}"))
    @Max(value = 10,message = ("{invalid.onTime}"))
    private Integer onTime;

    @Min(value = 1,message = ("{invalid.excellence}"))
    @Max(value = 10,message = ("{invalid.excellence}"))
    private Integer excellence;

    @Min(value = 1,message = ("{invalid.pleasantNature}"))
    @Max(value = 10,message = ("{invalid.pleasantNature}"))
    private Integer pleasantNature;

    @Min(value = 1,message = ("{invalid.punctual}"))
    @Max(value = 10,message = ("{invalid.punctual}"))
    private Integer punctual;

    @Min(value = 1,message = ("{invalid.hierarchy}"))
    @Max(value = 10,message = ("{invalid.hierarchy}"))
    private Integer hierarchy;

    @Min(value = 1,message = ("{invalid.upgradation}"))
    @Max(value = 10,message = ("{invalid.upgradation}"))
    private Integer upgradation;

    @Min(value = 1,message = ("{invalid.approachable}"))
    @Max(value = 10,message = ("{invalid.approachable}"))
    private Integer approachable;

    @Min(value = 1,message = ("{invalid.constructive}"))
    @Max(value = 10,message = ("{invalid.constructive}"))
    private Integer constructive;

    @Min(value = 1,message = ("{invalid.synergy}"))
    @Max(value = 10,message = ("{invalid.synergy}"))
    private Integer synergy;
    //
    @Min(value = 1,message = ("{invalid.respect}"))
    @Max(value = 10,message = ("{invalid.respect}"))
    private Integer respect;

    @Min(value = 1,message = ("{invalid.selflessness}"))
    @Max(value = 10,message = ("{invalid.selflessness}"))
    private Integer selflessness;

    public Integer getFeedbackReceiver() {
        return feedbackReceiver;
    }

    public void setFeedbackReceiver(Integer feedbackReceiver) {
        this.feedbackReceiver = feedbackReceiver;
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
        return userValidation;
    }

    public void setValidation(Integer userValidation) {
        this.userValidation = userValidation;
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

    public void setFeedbackType(byte feedbackType) {
        this.feedbackType = feedbackType;
    }

    public byte getFeedbackType() {
        return feedbackType;
    }
}
