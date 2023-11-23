package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.TraineeTask;
import com.innovaturelabs.buddymanagement.entity.Training;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.SubTaskForm;
import com.innovaturelabs.buddymanagement.repository.TaskRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeTaskRepository;
import com.innovaturelabs.buddymanagement.repository.TrainingRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.SubTaskService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.view.TaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubTaskServiceImpl implements SubTaskService {
    @Autowired
    private LanguageUtil languageUtil;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TraineeTaskRepository traineeTaskRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String TASK_NOT_FOUND = "task.not.found";

    private static final String START_DATE_INVALID = "start.date.invalid";

    private static final String END_DATE_INVALID = "end.date.invalid";

    @Override
    public void subTaskAdd(SubTaskForm form) {
        LocalDate dateNow = LocalDate.now();
        if (form.getParentTaskId() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("parentTaskId.required", null, "en"));
        }
        Task task = taskRepository.findByTaskIdAndStatus(form.getParentTaskId(), Task.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        Training training = trainingRepository.findByTrainingIdAndStatus(task.getTrainingId().getTrainingId(), Training.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText("training.not.found", null, "en")));
        checkingErrors(form,dateNow,task);
        User currentUser = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        taskRepository.save(new Task(
                form.getTaskName(),
                form.getTaskDescription(),
                form.getParentTaskId(),
                form.getTaskStart(),
                form.getTaskEnd(),
                currentUser,
                training
        ));
    }

    private void checkingErrors(SubTaskForm form, LocalDate dateNow, Task task) {
        if (form.getTaskStart() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.required", null, "en"));
        }
        if (form.getTaskEnd() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.required", null, "en"));
        }
        if (form.getTaskStart().isBefore(task.getTaskStart())) {
            throw new BadRequestException(languageUtil.getTranslatedText(START_DATE_INVALID, null, "en"));
        }
        if (form.getTaskEnd().isAfter(task.getTaskEnd())) {
            throw new BadRequestException(languageUtil.getTranslatedText(END_DATE_INVALID, null, "en"));
        }

        if (form.getTaskStart().isBefore(dateNow)) {
            throw new BadRequestException(languageUtil.getTranslatedText(START_DATE_INVALID, null, "en"));
        }
        if (form.getTaskEnd().isBefore(dateNow)) {
            throw new BadRequestException(languageUtil.getTranslatedText(END_DATE_INVALID, null, "en"));
        }

        if (form.getTaskStart().isAfter(form.getTaskEnd())) {
            throw new BadRequestException(languageUtil.getTranslatedText(END_DATE_INVALID, null, "en"));
        }
        if (task.getTaskParentId() != null) {
            throw new BadRequestException(languageUtil.getTranslatedText("taskParent.id.null", null, "en"));
        }
    }


    @Override
    public void allocateSubTaskControls(List<Integer> trainees, Integer task) {
        List<Integer> notFoundUsers = new ArrayList<>();
        List<Integer> alreadyAllocated = new ArrayList<>();
        List<Integer> differentParentId = new ArrayList<>();
        Task taskEntity = taskRepository.findBySubTaskId(task).orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));

        allocationBasicChecks(trainees);
        for (Integer traineeId : trainees) {
            TraineeTask traineeTaskParent = traineeTaskRepository.findByTaskId(taskEntity.getTaskParentId(), traineeId);
            if (traineeTaskParent != null) {
                User user = userRepository.findByUserId(traineeId);
                if (user == null) {
                    notFoundUsers.add(traineeId);
                } else {
                        if (traineeTaskRepository.findByTraineeIdAndTaskId(user.getUserId(), task) == null) {
                            TraineeTask traineeTask = new TraineeTask();
                            traineeTask.setTaskId(taskEntity);
                            traineeTask.setTraineeId(user);
                            LocalDateTime dt = LocalDateTime.now();
                            traineeTask.setCreateDate(dt);
                            traineeTask.setUpdateDate(dt);
                            traineeTask.setStatus(TraineeTask.Status.PENDING.value);
                            traineeTaskRepository.save(traineeTask);
                        } else {
                            alreadyAllocated.add(user.getUserId());
                        }
                }
            } else {
                differentParentId.add(traineeId);
            }
        }

        userListErrors(notFoundUsers, alreadyAllocated, differentParentId);
    }

    @Override
    public TaskView updateSubTask(Integer taskId, SubTaskForm form) {
        LocalDate dateNow = LocalDate.now();
        Task subTask = taskRepository.findBySubTaskIdAndStatus(taskId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));

        Task task = taskRepository.findByTaskIdAndStatus(subTask.getTaskParentId(), Task.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        Training training = trainingRepository.findByTrainingIdAndStatus(subTask.getTrainingId().getTrainingId(), Training.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText("training.not.found", null, "en")));
        checkingErrors(form,dateNow,task);
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        subTask.setTaskDescription(form.getTaskDescription());
        subTask.setTaskName(form.getTaskName());
        subTask.setTaskStart(form.getTaskStart());
        subTask.setTaskEnd(form.getTaskEnd());
        subTask.setUpdateDate(LocalDateTime.now());
        subTask.setTaskCreator(user);
        subTask.setTrainingId(training);
        taskRepository.save(subTask);
        return new TaskView(subTask, subTask.getTaskParentId());
    }


    private void allocationBasicChecks(List<Integer> trainees) {
        if (trainees.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText("trainees.not.selected", null, "en"));
        }
    }


    public void userListErrors(List<Integer> notFoundUsers, List<Integer> alreadyAllocated, List<Integer> differentParentId) {
        String errorMessage = "1943-";

        if (!notFoundUsers.isEmpty()) {
            errorMessage = errorMessage + "Trainee not found - User IDs:" + notFoundUsers + ". ";
        }
        if (!alreadyAllocated.isEmpty()) {
            errorMessage = errorMessage + "Users is already allocated - User IDs:" + alreadyAllocated + ". ";
        }
        if (!differentParentId.isEmpty()) {
            errorMessage = errorMessage + "ParentId of the subtask is not assigned to users - User IDs:" + differentParentId + ". ";
        }
        if (!errorMessage.equals("1943-")) {
            throw new BadRequestException(errorMessage);
        }
    }
}

