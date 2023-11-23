package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.form.ScoreForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.ScoreView;
import com.innovaturelabs.buddymanagement.view.UserScoreListView;

public interface ScoreService{

    void scoreAdd(ScoreForm form);
    Pager<UserScoreListView> listTaskWithScore(Integer taskId, Integer mentorId, Integer traineeId, Integer range, Integer managerId, Integer page, Integer limit);

    ScoreView fetchScoreDetail(Integer traineeTaskId);
    void scoreUpdate(ScoreForm form,Integer scoreId);
}