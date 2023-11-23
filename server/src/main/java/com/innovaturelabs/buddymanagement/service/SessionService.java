package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.form.SessionForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.SessionView;
import com.innovaturelabs.buddymanagement.view.TraineeTrainingView;

import java.util.List;

public interface SessionService {
    void sessionCreate(SessionForm sessionForm);

    Pager<SessionView> sessionList(Integer trainingId, Integer trainerId, Integer groupId,Integer technologyId, Integer page, Integer limit, Byte status);

    void sessionUpdate(SessionForm sessionForm, Integer sessionId);

    SessionView sesssionDetailView(Integer sessionId);

    void deleteSession(Integer sessionId);

    void sessionStatus(Integer sessionId, byte status);

    List<TraineeTrainingView>   traineeTrainingList();

    List<SessionView> groupList();
}
