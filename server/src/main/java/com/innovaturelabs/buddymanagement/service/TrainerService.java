package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TrainerForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TraineeTrainerView;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;

import java.util.List;

public interface TrainerService {
    Pager<UserListView> listTrainer(String search, Long employeeId, Integer status, Integer page, Integer limit);
    void trainerDelete(Integer userId)  throws BadRequestException;

    UserView fetchTrainer(Integer userId);

    UserView updateTrainer(Integer userId, TrainerForm form);

    UserView trainerRegister(TrainerForm form);

    List<String> fetchFirstName();

    List<TraineeTrainerView>  traineeTrainerList();
}
