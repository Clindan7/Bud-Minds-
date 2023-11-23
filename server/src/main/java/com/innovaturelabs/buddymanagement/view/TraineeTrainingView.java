package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.Session;

public class TraineeTrainingView {

    private final Integer trainingId;

    private final String title;

    public Integer getTrainingId() {
        return trainingId;
    }

    public String getTitle() {
        return title;
    }

    public TraineeTrainingView(Session session) {
        this.trainingId = session.getTrainingId().getTrainingId();
        this.title = session.getTrainingId().getTitle();
    }
}
