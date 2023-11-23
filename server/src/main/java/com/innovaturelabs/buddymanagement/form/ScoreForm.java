package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.*;

public class ScoreForm {

    private Integer traineeTaskId;

    @NotNull(message = ("{taskValuation.quality.required}"))
    @Min(value = 0, message = ("{taskValuation.quality.invalid}"))
    @Max(value = 3, message = ("{taskValuation.quality.invalid}"))
    private Byte qualityOfWork;

    @NotNull(message = ("{taskValuation.extraEffort.required}"))
    @Min(value = 0, message = ("{taskValuation.extraEffort.invalid}"))
    @Max(value = 2, message = ("{taskValuation.extraEffort.invalid}"))
    private Byte extraEffort;

    @NotNull(message = ("{taskValuation.enthusiamToLearn.required}"))
    @Min(value = 0, message = ("{taskValuation.enthusiamToLearn.invalid}"))
    @Max(value = 2, message = ("{taskValuation.enthusiamToLearn.invalid}"))
    private Byte enthusiamToLearn;

    @NotNull(message = ("{taskValuation.adaptability.required}"))
    @Min(value = 0, message = ("{taskValuation.adaptability.invalid}"))
    @Max(value = 2, message = ("{taskValuation.adaptability.invalid}"))
    private Byte adaptability;

    @NotNull(message = ("{taskValuation.teamWork.required}"))
    @Min(value = 0, message = ("{taskValuation.teamWork.invalid}"))
    @Max(value = 2, message = ("{taskValuation.teamWork.invalid}"))
    private Byte teamWork;

    @Size(message = ("{taskValuation.comment.size}"), max = 2000)
    @Pattern(message = "{taskValuation.comment.invalid}", regexp = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$|(^$)")
    private String comment;

    public Integer getTraineeTaskId() {
        return traineeTaskId;
    }

    public Byte getQualityOfWork() {
        return qualityOfWork;
    }

    public Byte getExtraEffort() {
        return extraEffort;
    }

    public Byte getEnthusiamToLearn() {
        return enthusiamToLearn;
    }

    public Byte getAdaptability() {
        return adaptability;
    }

    public Byte getTeamWork() {
        return teamWork;
    }

    public String getComment() {
        return comment;
    }
}