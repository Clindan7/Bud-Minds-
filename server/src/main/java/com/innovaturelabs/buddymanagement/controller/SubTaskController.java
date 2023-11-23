package com.innovaturelabs.buddymanagement.controller;


import com.innovaturelabs.buddymanagement.form.SubTaskAllocationForm;
import com.innovaturelabs.buddymanagement.form.SubTaskForm;
import com.innovaturelabs.buddymanagement.service.SubTaskService;
import com.innovaturelabs.buddymanagement.view.TaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/subTask")
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @PostMapping
    public void subtaskRegister(@Valid @RequestBody SubTaskForm form) {
        subTaskService.subTaskAdd(form);
    }

    @PutMapping("/{taskId}")
    public TaskView updateSubTask(@PathVariable("taskId") Integer taskId, @RequestBody @Valid SubTaskForm form) {
        return subTaskService.updateSubTask(taskId, form);
    }

    @PostMapping("/allocation-control")
    public void allocateSubTaskToGroup(@RequestParam(name = "taskId") Integer taskId,
                                       @Valid @RequestBody SubTaskAllocationForm form) {
        subTaskService.allocateSubTaskControls(form.getTrainees(), taskId);
    }

}
