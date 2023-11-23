package com.innovaturelabs.buddymanagement.service;


import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TrainingForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TrainingView;

import java.util.List;


public interface TrainingService {
    Pager<TrainingView> listTrainings(String search, Integer technologyId, byte departmentId, Integer page, Integer limit);

    TrainingView trainingAdd(TrainingForm form);
    TrainingView fetchTraining(Integer trainingId);
    TrainingView updateTraining(Integer trainingId, TrainingForm form);
    void trainingDelete(Integer trainingId)  throws BadRequestException;
    List<TrainingView> upcomingTrainings();
}
