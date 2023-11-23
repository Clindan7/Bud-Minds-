package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.TraineeTask;
import com.innovaturelabs.buddymanagement.entity.Training;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TaskForm;
import com.innovaturelabs.buddymanagement.repository.TaskRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeTaskRepository;
import com.innovaturelabs.buddymanagement.repository.TrainingRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.TaskService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TaskView;
import com.innovaturelabs.buddymanagement.view.TraineeTaskView;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeTaskRepository traineeTaskRepository;

    @Mock
    private LanguageUtil languageUtil;

    @Test
    void testTaskAdd_ValidData_ShouldSaveTask() {

        // Mocking input data
        TaskForm form = new TaskForm();
        form.setTrainingId(1); // Assuming valid training ID
        form.setTaskName("Sample Task");
        form.setTaskDescription("This is a sample task.");
        form.setTaskStart(LocalDate.now());
        form.setTaskEnd(LocalDate.now().plusDays(7));

        User user = new User("John", "Doe", "john.doe@example.com", 123456L, "password");
        user.setStatus(User.Status.ACTIVE.value);

        Training training = new Training();
        training.setTrainingId(1);
        training.setStatus(Training.Status.ACTIVE.value);
        training.setTrainingStartDate(LocalDate.now());
        training.setTrainingEndDate(LocalDate.now());

        // Mocking repository calls
        when(trainingRepository.findByTrainingIdAndStatus(eq(1), eq(Training.Status.ACTIVE.value))).thenReturn(Optional.of(training));
        when(userRepository.findByUserIdAndStatus(eq(SecurityUtil.getCurrentUserId()), eq(User.Status.ACTIVE.value))).thenReturn(user);

        // Perform the taskAdd operation
        taskService.taskAdd(form);

        // Verify that the task is saved with the correct parameters
        verify(taskRepository).save(argThat(task -> task.getTaskName().equals(form.getTaskName())
                && task.getTaskDescription().equals(form.getTaskDescription())
                && task.getTaskStart().equals(form.getTaskStart())
                && task.getTaskEnd().equals(form.getTaskEnd())
                && task.getTaskCreator().equals(user)
                && task.getTrainingId().equals(training)));

    }
        @Test
        void testUpdateTask() {
            // Mock the form data
            TaskForm form = new TaskForm();
            form.setTrainingId(1);
            form.setTaskName("New Task");
            form.setTaskDescription("Description of New Task");
            form.setTaskStart(LocalDate.now().plusDays(1));
            form.setTaskEnd(LocalDate.now().plusDays(5));

            // Mock the current date
            LocalDate dateNow = LocalDate.now();

            // Mock the training entity
            Training training = new Training();
            training.setTrainingId(1);
            training.setStatus(Training.Status.ACTIVE.value);
            training.setTrainingStartDate(LocalDate.now().minusDays(1));
            training.setTrainingEndDate(LocalDate.now().plusDays(10));

            // Mock the user entity
            User user = new User("John", "Doe", "john.doe@example.com", 12345L, "password");
            user.setStatus(User.Status.ACTIVE.value);

            // Mock the task entity
            Task task = new Task();
            task.setTaskId(1);
            task.setTaskName("Old Task");
            task.setTaskDescription("Description of Old Task");
            task.setTaskStart(LocalDate.now().plusDays(2));
            task.setTaskEnd(LocalDate.now().plusDays(6));
            task.setUpdateDate(LocalDateTime.now().minusDays(1));
            task.setStatus(Task.Status.ACTIVE.value);
            task.setTaskCreator(user);
            task.setTrainingId(training);

            // Mock the repository methods
            when(trainingRepository.findByTrainingIdAndStatus(eq(form.getTrainingId()), eq(Training.Status.ACTIVE.value)))
                    .thenReturn(Optional.of(training));
            when(userRepository.findByUserIdAndStatus(eq(SecurityUtil.getCurrentUserId()), eq(User.Status.ACTIVE.value)))
                    .thenReturn(user);
            when(taskRepository.findByTaskIdAndStatus(eq(task.getTaskId()), eq(Task.Status.ACTIVE.value)))
                    .thenReturn(Optional.of(task));
            when(taskRepository.save(any(Task.class))).thenReturn(task);

            // Call the updateTask method
            TaskView updatedTaskView = taskService.updateTask(task.getTaskId(), form);

            // Verify that the taskRepository.save method is called once with the updated task entity
            verify(taskRepository, times(1)).save(task);

            // Verify that the returned TaskView contains the updated task data
            assertEquals(form.getTaskName(), updatedTaskView.getTaskName());
            assertEquals(form.getTaskDescription(), updatedTaskView.getTaskDescription());
            assertEquals(form.getTaskStart(), updatedTaskView.getTaskStart());
            assertEquals(form.getTaskEnd(), updatedTaskView.getTaskEnd());
            // Add more assertions as needed for other properties of TaskView
        }

        // Add other test cases as needed for different scenarios.


    @Test
    void fetchTask() {
        Integer taskId = 5; // Replace with the desired taskId for testing

        Task expectedTask = new Task(5); // Replace with the expected Task object for taskId = 5


        // Mock the behavior of TaskRepository
        when(taskRepository.findByTaskIdAndStatus(taskId, Task.Status.ACTIVE.value))
                .thenReturn(Optional.of(expectedTask));

        // Mock the behavior of LanguageUtil
        when(languageUtil.getTranslatedText(any(), any(), any()))
                .thenReturn("Test message");
        doThrow(BadRequestException.class).when(taskRepository).
                findByTaskIdAndStatus(taskId, Task.Status.ACTIVE.value);
        assertThrows(BadRequestException.class, () -> taskService.fetchTask(taskId));
        verify(taskRepository).findByTaskIdAndStatus(taskId, Task.Status.ACTIVE.value);
    }



    @Test
     void testTaskDelete_Success() throws BadRequestException {
        // Arrange
        Integer taskId = 5;
        Task task = new Task();
        task.setStatus(Task.Status.ACTIVE.value);

        when(taskRepository.findByTaskId(taskId)).thenReturn(Optional.of(task));
        when(traineeTaskRepository.findByTask(taskId)).thenReturn(new ArrayList<>());

        // Act
        taskService.taskDelete(taskId);

        // Assert
        assertEquals(Task.Status.INACTIVE.value, task.getStatus());
        assertNotNull(task.getUpdateDate());
        verify(taskRepository, times(1)).findByTaskId(taskId);
        verify(traineeTaskRepository, times(1)).findByTask(taskId);
        verify(taskRepository, times(1)).save(task);
        verify(traineeTaskRepository, times(0)).save(any(TraineeTask.class));
        verify(languageUtil, times(0)).getTranslatedText(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyString());
    }

    @Test
    void testTaskDelete_TaskNotFound() {
        // Arrange
        Integer taskId = 10;

        when(taskRepository.findByTaskId(taskId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BadRequestException.class, () -> taskService.taskDelete(taskId));
        verify(taskRepository, times(1)).findByTaskId(taskId);
        verify(taskRepository, times(0)).save(any(Task.class));
        verify(traineeTaskRepository, times(0)).findByTask(taskId);
        verify(traineeTaskRepository, times(0)).save(any(TraineeTask.class));
        verify(languageUtil, times(1)).getTranslatedText(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyString());
    }

//    @Test
//    public void testTaskDelete_TaskAlreadyDeleted() {
//        // Arrange
//        Integer taskId = 7;
//        Task task = new Task();
//        task.setStatus(Task.Status.INACTIVE.value);
//
//        when(taskRepository.findByTaskId(taskId)).thenReturn(Optional.of(task));
//
//        // Act and Assert
//        BadRequestException exception = assertThrows(BadRequestException.class, () -> taskService.taskDelete(taskId));
//        assertEquals("task.already.deleted", exception.getMessage());
//        verify(taskRepository, times(1)).findByTaskId(taskId);
//        verify(taskRepository, times(0)).save(any(Task.class));
//        verify(traineeTaskRepository, times(0)).findByTask(taskId);
//        verify(traineeTaskRepository, times(0)).save(any(TraineeTask.class));
//        verify(languageUtil, times(1)).getTranslatedText(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyString());
//    }

    @Test
     void testTaskDelete_WithTraineeTasks() throws BadRequestException {
        // Arrange
        Integer taskId = 3;
        Task task = new Task();
        task.setStatus(Task.Status.ACTIVE.value);

        List<TraineeTask> traineeTasks = new ArrayList<>();
        traineeTasks.add(new TraineeTask());
        traineeTasks.add(new TraineeTask());

        when(taskRepository.findByTaskId(taskId)).thenReturn(Optional.of(task));
        when(traineeTaskRepository.findByTask(taskId)).thenReturn(traineeTasks);

        // Act
        taskService.taskDelete(taskId);

        // Assert
        assertEquals(Task.Status.INACTIVE.value, task.getStatus());
        assertNotNull(task.getUpdateDate());
        verify(taskRepository, times(1)).findByTaskId(taskId);
        verify(traineeTaskRepository, times(1)).findByTask(taskId);
        verify(taskRepository, times(1)).save(task);
        verify(traineeTaskRepository, times(2)).save(any(TraineeTask.class));
        verify(languageUtil, times(0)).getTranslatedText(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.anyString());
    }


    @Test
     void testUserGroupListFilter_Success() {
        // Arrange
        Task taskId = new Task(1);
        TraineeTask traineeTask1 = new TraineeTask();
        traineeTask1.setTraineeTaskId(1);
        traineeTask1.setTaskId(taskId);
        TraineeTask traineeTask2 = new TraineeTask();
        traineeTask2.setTraineeTaskId(2);
        traineeTask2.setTaskId(taskId);

        when(traineeTaskRepository.findByUserGroup(taskId.getTaskId())).thenReturn(Arrays.asList(traineeTask1, traineeTask2));

        // Act
        List<TraineeTaskView> result = taskService.userGroupListFilter(taskId.getTaskId());

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getTraineeTaskId());
        assertEquals(taskId.getTaskId(), result.get(0).getTaskId().getTaskId());
        assertEquals(2, result.get(1).getTraineeTaskId());
        assertEquals(taskId.getTaskId(), result.get(1).getTaskId().getTaskId());
        verify(traineeTaskRepository, times(1)).findByUserGroup(taskId.getTaskId());
    }

    @Test
     void testUserGroupListFilter_EmptyResult() {
        // Arrange
        Integer taskId = 2;

        when(traineeTaskRepository.findByUserGroup(taskId)).thenReturn(new ArrayList<>());

        // Act
        List<TraineeTaskView> result = taskService.userGroupListFilter(taskId);

        // Assert
        assertEquals(0, result.size());
        verify(traineeTaskRepository, times(1)).findByUserGroup(taskId);
    }







    @Test
     void testListSubTask_InvalidTaskMode() {
        // Arrange
        int taskMode = 2;

        // Act and Assert
        assertThrows(BadRequestException.class, () -> taskService.listSubTask(null, null, null, null, (byte) taskMode, 1, 10));
        verifyNoInteractions(taskRepository, traineeTaskRepository);
    }
















    }












