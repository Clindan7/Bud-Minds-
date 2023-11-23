package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.form.SubTaskForm;
import com.innovaturelabs.buddymanagement.view.TaskView;

import java.util.List;

public interface SubTaskService {
    void subTaskAdd(SubTaskForm form);

    void allocateSubTaskControls(List<Integer> users, Integer taskId);

    TaskView updateSubTask(Integer taskId, SubTaskForm form);
}
