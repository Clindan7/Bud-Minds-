package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TraineeForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;

import java.util.List;

public interface TraineeService {

    Pager<UserListView> listTrainees(String search, Long employeeId, Integer status,Integer page, Integer limit);

    void traineeDelete(Integer userId) throws BadRequestException;

    UserView traineeRegister(TraineeForm form);

    List<String> fetchFirstName();

    UserView updateTrainee(Integer userId, TraineeForm form);

    UserView fetchTrainee(Integer userId);
}
