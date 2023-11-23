package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.TraineeForm;
import com.innovaturelabs.buddymanagement.service.TraineeService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author adwaith
 */
@RestController
@Validated
@RequestMapping("/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;


    @GetMapping
    public Pager<UserListView> listTrainees(@RequestParam(name = "search", required = false) String search,
                                            @RequestParam(name = "employeeId", required = false) Long employeeId,
                                            @RequestParam(name = "status", required = false) Integer status,
                                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return traineeService.listTrainees(search, employeeId, status,page, limit);
    }

    @PostMapping
    public UserView traineeRegister(@Valid @RequestBody TraineeForm form) {
        return traineeService.traineeRegister(form);
    }

    @DeleteMapping("/{userId}")
    public void traineeDelete(@PathVariable("userId") Integer userId) {
        traineeService.traineeDelete(userId);
    }

    @PutMapping("/{userId}")
    public UserView updateTrainee(@PathVariable("userId") Integer userId, @RequestBody @Valid TraineeForm form) {
        return traineeService.updateTrainee(userId, form);
    }

    @GetMapping("/{userId}")
    public UserView fetchTrainee(@PathVariable("userId") Integer userId) {
        return traineeService.fetchTrainee(userId);
    }

    @GetMapping("/firstName")
    public List<String> getTraineeFirstNames() {
        return traineeService.fetchFirstName();
    }

}
