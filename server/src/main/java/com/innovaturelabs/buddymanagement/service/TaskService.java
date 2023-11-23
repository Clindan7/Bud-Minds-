package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.form.TaskDelayForm;
import com.innovaturelabs.buddymanagement.form.TaskForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TaskView;
import com.innovaturelabs.buddymanagement.view.TraineeTaskView;

import java.util.List;

public interface TaskService {

    void taskAdd(TaskForm form);

    Pager<TaskView> listTask(String search, Integer trainingId,Integer taskId,Integer technologyId, Integer page, Integer limit);

    Pager<TaskView> listSubTask(String search, Integer trainingId, Integer taskId, Integer technologyId,byte taskMode, Integer page, Integer limit);

    TaskView updateTask(Integer taskId, TaskForm form);

    TaskView fetchTask(Integer taskId);

    void taskDelete(Integer taskId);

    void allocateTaskControls(List<Integer> joinerGroup, Task taskId);

    TraineeTaskView changeTaskStatus(Integer traineeTaskId, TaskDelayForm form);

    TraineeTaskView fetchTraineeTask(Integer traineeTaskId);

    Pager<TraineeTaskView> listTraineeTask(Integer taskId, Integer joinerGroupId,Integer traineeId, Integer page, Integer limit, Byte status);

    List<TaskView> taskListFilter();

    List<TraineeTaskView> userGroupListFilter(Integer taskId);
}
