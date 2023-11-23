package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.*;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TaskDelayForm;
import com.innovaturelabs.buddymanagement.form.TaskForm;
import com.innovaturelabs.buddymanagement.repository.*;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.NotificationService;
import com.innovaturelabs.buddymanagement.service.TaskService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TaskView;

import com.innovaturelabs.buddymanagement.view.TraineeTaskView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskServiceImpl implements TaskService {
    Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);


    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private JoinerGroupRepository joinerGroupRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeTaskRepository traineeTaskRepository;

    private static final String TASK_NOT_FOUND = "task.not.found";

    private static final String START_DATE_INVALID = "start.date.invalid";

    private static final String END_DATE_INVALID = "end.date.invalid";

    private static final String USER_NOT_FOUND = "user.not.found";

    private static final String TRAINEE_TASK_NOT_FOUND = "trainee.task.id.not.found";


    @Override
    public void taskAdd(TaskForm form) {
        LocalDate dateNow = LocalDate.now();
        Training training = trainingRepository.findByTrainingIdAndStatus(form.getTrainingId(), Training.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText("training.not.found", null, "en")));
        basicCheckingErrors(form, dateNow, training);
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        taskRepository.save(new Task(
                form.getTaskName(),
                form.getTaskDescription(),
                form.getTaskStart(),
                form.getTaskEnd(),
                user,
                training
        ));
    }

    private void basicCheckingErrors(TaskForm form, LocalDate dateNow, Training training) {
        if (form.getTaskStart() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.required", null, "en"));
        }
        if (form.getTaskEnd() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.required", null, "en"));
        }
        if (training.getTrainingStartDate().isAfter(form.getTaskStart())) {
            throw new BadRequestException(languageUtil.getTranslatedText("training.startDate.invalid", null, "en"));
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
    }

    @Override
    public Pager<TaskView> listSubTask(String search, Integer trainingId, Integer taskId, Integer technologyId, byte taskMode, Integer page, Integer limit) {
        Pager<TaskView> taskPager;
        List<TaskView> taskList;
        if (taskMode != 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.task.mode", null, "en"));
        }
        if (trainingId != null && search != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingIdAndSearchAndSubTask(trainingId, search, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdAndSearchAndSubTaskCount(trainingId, search, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null && search != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdAndSearchAndSubTask(technologyId, search, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdAndSearchAndSubTaskCount(technologyId, search, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (trainingId != null && search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingIdAndSearch(trainingId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdAndSearchCount(trainingId, search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null && search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdAndSearchSub(technologyId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdAndSearchCountSub(technologyId, search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (taskId != null && search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findBySubTaskIdAndSearch(taskId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findBySubTaskIdAndSearchCount(taskId, search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (trainingId != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingIdAndSubTask(trainingId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdAndSubTaskCount(trainingId, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdAndSubTask(technologyId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdAndSubTaskCount(technologyId, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        return subTaskListFilter(trainingId, search, taskId, technologyId, page, limit);

    }

    private Pager<TaskView> subTaskListFilter(Integer trainingId, String search, Integer taskId, Integer technologyId, Integer page, Integer limit) {
        Pager<TaskView> taskPager;
        List<TaskView> taskList;
        if (trainingId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingId(trainingId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdCount(trainingId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findBySearch(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findBySearchCount(search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByFilterSubTaskId(taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findCountFilterSubTaskId(taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdSub(technologyId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdSub(technologyId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        taskList = StreamSupport.stream(taskRepository
                        .findAllSubTask(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(task -> new TaskView(task, task.getTaskParentId(), taskRepository.findByMainTask(task.getTaskParentId())))
                .collect(Collectors.toList());
        taskPager = new Pager<>(limit, taskRepository.findBySubTaskName().size(), page);
        taskPager.setResult(taskList);
        return taskPager;
    }

    @Override
    public Pager<TaskView> listTask(String search, Integer trainingId, Integer taskId, Integer technologyId, Integer page, Integer limit) {
        Pager<TaskView> taskPager;
        List<TaskView> taskList;
        if (trainingId != null && search != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingIdAndSearchAndTask(trainingId, search, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdAndSearchAndTaskCount(trainingId, search, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null && search != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdAndSearchAndTask(technologyId, search, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdAndSearchAndTaskCount(technologyId, search, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }

        if (trainingId != null && search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingIdAndSearch(trainingId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdAndSearchCount(trainingId, search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null && search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdAndSearch(technologyId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdAndSearchCount(technologyId, search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (taskId != null && search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTaskIdAndSearch(taskId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTaskIdAndSearchCount(taskId, search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (trainingId != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingIdAndTask(trainingId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdAndTaskCount(trainingId, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null && taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyIdAndTask(technologyId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyIdAndTaskCount(technologyId, taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        return taskListFilter(trainingId, search, taskId, technologyId, page, limit);

    }

    private Pager<TaskView> taskListFilter(Integer trainingId, String search, Integer taskId, Integer technologyId, Integer page, Integer limit) {
        Pager<TaskView> taskPager;
        List<TaskView> taskList;
        if (trainingId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTrainingId(trainingId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTrainingIdCount(trainingId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (search != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findBySearch(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findBySearchCount(search), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (taskId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByFilterTaskId(taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByFilterTaskId(taskId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        if (technologyId != null) {
            taskList = StreamSupport.stream(taskRepository
                            .findByTechnologyId(technologyId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(task -> new TaskView(task, task.getTaskParentId()))
                    .collect(Collectors.toList());
            taskPager = new Pager<>(limit, taskRepository.findByTechnologyId(technologyId), page);
            taskPager.setResult(taskList);
            return checkIfTrainingList(taskList, taskPager);
        }
        taskList = StreamSupport.stream(taskRepository
                        .findAllTask(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(task -> new TaskView(task, task.getTaskParentId()))
                .collect(Collectors.toList());
        taskPager = new Pager<>(limit, taskRepository.findByTaskName().size(), page);
        taskPager.setResult(taskList);
        return taskPager;
    }

    private Pager<TaskView> checkIfTrainingList(List<TaskView> taskList, Pager<TaskView> taskPager) {
        if (!taskList.isEmpty()) {
            return taskPager;
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en"));
        }
    }


    @Override
    public TaskView updateTask(Integer taskId, TaskForm form) {
        LocalDate dateNow = LocalDate.now();
        Training training = trainingRepository.findByTrainingIdAndStatus(form.getTrainingId(), Training.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText("training.not.found", null, "en")));
        basicCheckingErrors(form, dateNow, training);
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);

        Task task = taskRepository.findByTaskIdAndStatus(taskId, Task.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        task.setTaskDescription(form.getTaskDescription());
        task.setTaskName(form.getTaskName());
        task.setTaskStart(form.getTaskStart());
        task.setTaskEnd(form.getTaskEnd());
        task.setUpdateDate(LocalDateTime.now());
        task.setTaskCreator(user);
        task.setTrainingId(training);
        taskRepository.save(task);
        return new TaskView(task);
    }


    @Override
    public TaskView fetchTask(Integer taskId) {
        Task t = taskRepository.findByTaskIdAndStatus(taskId, Task.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        return new TaskView(t, t.getTaskParentId());
    }

    @Override
    @Transactional
    public void taskDelete(Integer taskId) throws BadRequestException {
        Task task = taskRepository.findByTaskId(taskId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        if (task.getStatus() == Task.Status.INACTIVE.value) {
            log.error("task already deleted");
            throw new BadRequestException(languageUtil.getTranslatedText("task.already.deleted", null, "en"));

        }
        List<TraineeTask> traineeTask = traineeTaskRepository.findByTask(taskId);
        for (TraineeTask t : traineeTask) {
            t.setStatus(TraineeTask.Status.INACTIVE.value);
            traineeTaskRepository.save(t);
        }
        task.setStatus(User.Status.INACTIVE.value);
        task.setUpdateDate(LocalDateTime.now());
        taskRepository.save(task);


    }


    @Override
    public void allocateTaskControls(List<Integer> joinerGroup, Task taskId) {
        List<Integer> notFoundGroups = new ArrayList<>();
        List<Integer> alreadyAllocated = new ArrayList<>();
        List<Integer> usersWithNoGroup = new ArrayList<>();
        List<Integer> usersWithNoMentor = new ArrayList<>();
        joinerGroup = joinerGroup.stream()
                .distinct()
                .collect(Collectors.toList());
        joinerGroup.removeAll(Collections.singletonList(null));
        allocationBasicChecks(joinerGroup, taskId);
        User currentUser = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        for (Integer groupId : joinerGroup) {
            joinerGroupRepository.findByJoinerGroupIdAndStatus(groupId, JoinerGroup.Status.ACTIVE.value)
                    .ifPresentOrElse(
                            group -> {
                                List<User> users = userRepository.findByGroup(groupId);
                                if (users.isEmpty()) {
                                    usersWithNoGroup.add(groupId);
                                } else {
                                    checkIfMentorIsNull(users, usersWithNoMentor);
                                    users.forEach(user -> {
                                        Task parentTask = taskRepository.findByTaskIdAndStatus(taskId.getTaskId(), Task.Status.ACTIVE.value)
                                                .orElseThrow(() -> new
                                                        BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
                                        checkIfParentIdNull(parentTask);
                                        if (traineeTaskRepository.findByTraineeIdAndTaskId(user.getUserId(), taskId.getTaskId()) == null) {
                                            TraineeTask task = new TraineeTask();
                                            task.setTaskId(taskId);
                                            task.setTraineeId(user);
                                            LocalDateTime dt = LocalDateTime.now();
                                            task.setCreateDate(dt);
                                            task.setUpdateDate(dt);
                                            task.setStatus(TraineeTask.Status.PENDING.value);
                                            traineeTaskRepository.save(task);
                                        } else {
                                            alreadyAllocated.add(user.getUserId());
                                        }
                                    });
                                    Notification notification = notificationService.checkIfNotificationSend(groupId, taskId);
                                    if (notification == null) {
                                        notificationService.createNotificationForTask(currentUser, groupId, taskId);
                                    }
                                }
                            },
                            () -> notFoundGroups.add(groupId)
                    );
        }
        userListErrors(notFoundGroups, alreadyAllocated, usersWithNoGroup, usersWithNoMentor);
    }

    private void checkIfMentorIsNull(List<User> users, List<Integer> usersWithNoMentor) {
        for (User user : users) {
            User userMentor = user.getMentorId();
            if (userMentor == null) {
                usersWithNoMentor.add(user.getUserId());
            }
        }
    }


    private void checkIfParentIdNull(Task parentTask) {
        if (parentTask.getTaskParentId() != null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en"));
        }
    }


    private void allocationBasicChecks(List<Integer> joinerGroup, Task taskId) {

        if (taskId == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en"));
        }
        if (joinerGroup.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText("joinerGroup.not.selected", null, "en"));
        }
    }


    public void userListErrors(List<Integer> notFoundGroups, List<Integer> alreadyAllocated, List<Integer> usersWithNoGroup, List<Integer> usersWithNoMentor) {
        String errorMessage = "1943-";

        if (!notFoundGroups.isEmpty()) {
            errorMessage = errorMessage + "Group not found - group IDs:" + notFoundGroups + ". ";
        }
        if (!alreadyAllocated.isEmpty()) {
            errorMessage = errorMessage + "Users is already allocated - User IDs:" + alreadyAllocated + ". ";
        }
        if (!usersWithNoGroup.isEmpty()) {
            errorMessage = errorMessage + "No users with specified group ID - User IDs:" + usersWithNoGroup + ". ";
        }
        if (!usersWithNoMentor.isEmpty()) {
            errorMessage = errorMessage + "Users with no mentor - User IDs:" + usersWithNoMentor + ". ";
        }
        if (!errorMessage.equals("1943-")) {
            throw new BadRequestException(errorMessage);
        }
    }


    @Override
    public TraineeTaskView changeTaskStatus(Integer traineeTaskId, TaskDelayForm form) {
        if (traineeTaskId == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"));
        }
        TraineeTask traineeTask = traineeTaskRepository.findById(traineeTaskId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en")));
        Optional<Task> task = taskRepository.findById(traineeTask.getTaskId().getTaskId());
        if (task.isPresent()) {
            Task simpleTask = task.get();
            User traineeId = traineeTask.getTraineeId();
            if (simpleTask.getTaskParentId() != null) {
                Optional<Task> parentTask = taskRepository.findById(simpleTask.getTaskParentId());
                ifParentTaskPresent(parentTask, simpleTask, traineeId, form);
            } else {
                if (simpleTask.getTaskEnd().isAfter(LocalDate.now())) {
                    traineeTask.setStatus(TraineeTask.Status.COMPLETED.value);
                    traineeTask.setUpdateDate(LocalDateTime.now());
                    traineeTaskRepository.save(traineeTask);
                } else if (!simpleTask.getTaskEnd().isAfter(LocalDate.now()) && form != null) {
                    traineeTask.setStatus(TraineeTask.Status.DELAYED.value);
                    traineeTask.setDelayReason(form.getDelayReason());
                    traineeTask.setUpdateDate(LocalDateTime.now());
                    traineeTaskRepository.save(traineeTask);
                } else {
                    throw new BadRequestException(languageUtil.getTranslatedText("task.is.delayed", null, "en"));
                }
            }
        }
        return new TraineeTaskView(traineeTask);
    }

    private void ifParentTaskPresent(Optional<Task> parentTask, Task simpleTask, User traineeId, TaskDelayForm form) {
        if (parentTask.isPresent()) {
            Task parent = parentTask.get();
            TraineeTask subTraineeTask = traineeTaskRepository.findByTaskIdAndTraineeIds(parent.getTaskId(), traineeId.getUserId())
                    .orElseThrow((() -> new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"))));

            if (simpleTask.getTaskEnd().isAfter(LocalDate.now())) {
                subTraineeTask.setStatus(TraineeTask.Status.COMPLETED.value);
                subTraineeTask.setUpdateDate(LocalDateTime.now());
                traineeTaskRepository.save(subTraineeTask);

            } else if (!simpleTask.getTaskEnd().isAfter(LocalDate.now()) && form != null) {
                subTraineeTask.setStatus(TraineeTask.Status.DELAYED.value);
                subTraineeTask.setDelayReason(form.getDelayReason());
                subTraineeTask.setUpdateDate(LocalDateTime.now());
                traineeTaskRepository.save(subTraineeTask);
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText("task.is.delayed", null, "en"));
            }
        }
    }


    @Override
    public TraineeTaskView fetchTraineeTask(Integer traineeTaskId) {
        User currentUser = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (currentUser.getUserRole() == User.Role.TRAINEE.value) {
            return new TraineeTaskView(traineeTaskRepository.findByTraineeTaskIdAndStatusOfTrainee(traineeTaskId, currentUser, TraineeTask.Status.PENDING.value).orElseThrow(() -> new
                    BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"))));
        }
        return new TraineeTaskView(traineeTaskRepository.findByTraineeTaskIdAndStatus(traineeTaskId, TraineeTask.Status.PENDING.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"))));
    }

    @Override
    public Pager<TraineeTaskView> listTraineeTask(Integer taskId, Integer joinerGroupId, Integer traineeId, Integer page, Integer limit, Byte status) {
        Pager<TraineeTaskView> traineeTaskPage;
        List<TraineeTaskView> traineeTaskList;
        User currentUser = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        List<Byte> statusList = new ArrayList<>();
        if (status != null) {
            statusList.add(status);
        } else {
            statusList.addAll(List.of(TraineeTask.Status.PENDING.value, TraineeTask.Status.COMPLETED.value,
                    TraineeTask.Status.DELAYED.value));
        }
        if (currentUser.getUserRole() == User.Role.TRAINEE.value) {
            checkGroupAndTask(joinerGroupId, taskId, traineeId);
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository.findByTaskOfTrainee(currentUser.getUserId(), statusList, PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTaskOfTraineeCount(currentUser.getUserId(), statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return traineeTaskPage;
        }
        if (taskId != null && joinerGroupId != null && traineeId != null) {
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository
                            .findByTaskIdAndJoinerGroupIdAndTraineeId(taskId, joinerGroupId, traineeId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTaskIdAndJoinerGroupIdAndTraineeIdCount(taskId, joinerGroupId, traineeId, statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return checkIfList(traineeTaskList, traineeTaskPage);
        }
        if (taskId != null && joinerGroupId != null) {
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository
                            .findByTaskIdAndJoinerGroupId(taskId, joinerGroupId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTaskIdAndJoinerGroupIdCount(taskId, joinerGroupId, statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return checkIfList(traineeTaskList, traineeTaskPage);
        }
        if (taskId != null && traineeId != null) {
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository
                            .findByTaskIdAndTraineeId(taskId, traineeId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTaskIdAndTraineeIdCount(taskId, traineeId, statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return checkIfList(traineeTaskList, traineeTaskPage);
        }
        if (joinerGroupId != null && traineeId != null) {
            User trainee = userRepository.findByUserId(traineeId);
            if (trainee.getJoinerGroup().getJoinerGroupId().equals(joinerGroupId)) {
                traineeTaskList = StreamSupport.stream(
                        traineeTaskRepository
                                .findByTraineeIdAndGroup(traineeId, statusList, PageRequest.of(page - 1, limit))
                                .spliterator(),
                        false).map(TraineeTaskView::new).collect(Collectors.toList());
                traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTraineeIdAndGroupCount(traineeId, statusList), page);
                traineeTaskPage.setResult(traineeTaskList);
                return checkIfList(traineeTaskList, traineeTaskPage);
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"));
            }
        }
        return traineeTaskListFilter(taskId, joinerGroupId, traineeId, statusList, page, limit);

    }


    private Pager<TraineeTaskView> traineeTaskListFilter(Integer taskId, Integer joinerGroupId, Integer traineeId, List<Byte> statusList, Integer page, Integer limit) {
        Pager<TraineeTaskView> traineeTaskPage;
        List<TraineeTaskView> traineeTaskList;
        User currentUser = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (joinerGroupId != null) {
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository.findByJoinerGroupId(joinerGroupId, statusList, PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByJoinerGroupIdCount(joinerGroupId, statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return checkIfList(traineeTaskList, traineeTaskPage);
        }
        if (taskId != null) {
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository.findByTaskAndStatus(taskId, statusList, PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTaskAndStatusCount(taskId, statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return checkIfList(traineeTaskList, traineeTaskPage);
        }
        if (traineeId != null) {
            traineeTaskList = StreamSupport.stream(
                    traineeTaskRepository.findByTraineeId(traineeId, statusList, PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(TraineeTaskView::new).collect(Collectors.toList());
            traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findByTraineeIdCount(traineeId, statusList), page);
            traineeTaskPage.setResult(traineeTaskList);
            return checkIfList(traineeTaskList, traineeTaskPage);
        }
        if (currentUser.getUserRole() == User.Role.TRAINEE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"));
        }
        traineeTaskList = StreamSupport
                .stream(traineeTaskRepository.findAllByStatusInOrderByCreateDateDesc(statusList, PageRequest.of(page - 1, limit)).spliterator(),
                        false)
                .map(TraineeTaskView::new).collect(Collectors.toList());
        traineeTaskPage = new Pager<>(limit, traineeTaskRepository.findCountAllStatus(statusList), page);
        traineeTaskPage.setResult(traineeTaskList);
        return traineeTaskPage;
    }

    private Pager<TraineeTaskView> checkIfList(List<TraineeTaskView> taskList, Pager<TraineeTaskView> taskPager) {
        if (!taskList.isEmpty()) {
            return taskPager;
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"));
        }
    }

    private void checkGroupAndTask(Integer joinerGroupId, Integer taskId, Integer traineeId) {
        if (joinerGroupId != null || taskId != null || traineeId != null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"));
        }
    }


    @Override
    public List<TaskView> taskListFilter() {
        List<TaskView> taskViewList;

        taskViewList = StreamSupport.stream(taskRepository
                        .findByTaskNameFilter().spliterator(), false)
                .map(TaskView::new)
                .collect(Collectors.toList());
        return taskViewList;
    }

    @Override
    public List<TraineeTaskView> userGroupListFilter(Integer taskId) {
        List<TraineeTaskView> traineeTaskViewList;

        traineeTaskViewList = StreamSupport.stream(traineeTaskRepository
                        .findByUserGroup(taskId).spliterator(), false)
                .map(TraineeTaskView::new)
                .collect(Collectors.toList());
        return traineeTaskViewList;
    }


}
