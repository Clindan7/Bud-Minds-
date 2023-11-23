package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.form.TaskAllocationForm;
import com.innovaturelabs.buddymanagement.form.TaskDelayForm;
import com.innovaturelabs.buddymanagement.form.TaskForm;
import com.innovaturelabs.buddymanagement.service.TaskService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TaskView;
import com.innovaturelabs.buddymanagement.view.TraineeTaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author clindan
 */
@RestController
@Validated
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public void taskRegister(@Valid @RequestBody TaskForm form) {
        taskService.taskAdd(form);

    }

    @GetMapping
    public Pager<TaskView> listTasks(@RequestParam(name = "search", required = false) String search,
                                     @RequestParam(name = "trainingId", required = false) Integer trainingId,
                                     @RequestParam(name = "taskId", required = false) Integer taskId,
                                     @RequestParam(name = "technologyId", required = false) Integer technologyId,
                                     @RequestParam(value = "taskMode") byte taskMode,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                     @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        if(taskMode==0) {
            return taskService.listTask(search, trainingId, taskId,technologyId, page, limit);
        }
        else {
            return taskService.listSubTask(search, trainingId, taskId, technologyId ,taskMode, page, limit);
        }
    }

    @PutMapping("/{taskId}")
    public TaskView updateTask(@PathVariable("taskId") Integer taskId, @RequestBody @Valid TaskForm form) {
        return taskService.updateTask(taskId, form);
    }

    @GetMapping("/{taskId}")
    public TaskView fetchTask(@PathVariable("taskId") Integer taskId) {
        return taskService.fetchTask(taskId);
    }

    @DeleteMapping("/{taskId}")
    public void taskDelete(@PathVariable("taskId") Integer taskId) {
        taskService.taskDelete(taskId);
    }

    @PostMapping("/allocation-control")
    public void allocateTaskToGroup(@RequestParam(name = "taskId") Task taskId,
                                    @Valid @RequestBody TaskAllocationForm form) {
        taskService.allocateTaskControls(form.getJoinerGroup(), taskId);
    }

    @PostMapping("/changeTaskStatus")
    public TraineeTaskView changeTaskStatus(@RequestParam(name = "traineeTaskId") Integer traineeTaskId, @Valid @RequestBody(required = false) TaskDelayForm form) {
        return taskService.changeTaskStatus(traineeTaskId, form);
    }

    @GetMapping("traineeTask/{traineeTaskId}")
    public TraineeTaskView fetchTraineeTask(@PathVariable("traineeTaskId") Integer traineeTaskId) {
        return taskService.fetchTraineeTask(traineeTaskId);
    }


    @GetMapping("/traineeTask-list")
    public Pager<TraineeTaskView> listTraineeTask(@RequestParam(name = "taskId", required = false) Integer taskId,
                                                  @RequestParam(name = "joinerGroupId", required = false) Integer joinerGroupId,
                                                  @RequestParam(name = "traineeId", required = false) Integer traineeId,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(name = "status", required = false) Byte status){
        return taskService.listTraineeTask(taskId, joinerGroupId,traineeId, page, limit, status);
    }

    @GetMapping("/taskList")
    public List<TaskView> taskListFilter() {
        return taskService.taskListFilter();
    }

    @GetMapping("/userGroupList/{taskId}")
    public List<TraineeTaskView> userGroupListFilter(@PathVariable("taskId") Integer taskId) {
        return taskService.userGroupListFilter(taskId);
    }
}
