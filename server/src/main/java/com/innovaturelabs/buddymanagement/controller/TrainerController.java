package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.TrainerForm;
import com.innovaturelabs.buddymanagement.service.TrainerService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author clindan
 */
@RestController
@RequestMapping("/trainers")
public class TrainerController {
    @Autowired
    private TrainerService trainerService;

    @GetMapping
    public Pager<UserListView> listTrainer(@RequestParam(name = "search", required = false) String search,
                                           @RequestParam(name = "employeeId", required = false) Long employeeId,
                                           @RequestParam(name = "status", required = false) Integer status,
                                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return trainerService.listTrainer(search, employeeId,status, page, limit);
    }

    @DeleteMapping("/{userId}")
    public void trainerDelete(@PathVariable("userId") Integer userId) {
        trainerService.trainerDelete(userId);
    }


    @GetMapping("/{userId}")
    public UserView fetchTrainer(@PathVariable("userId") Integer userId) {
        return trainerService.fetchTrainer(userId);
    }

    @PutMapping("/{userId}")
    public UserView updateTrainer(@PathVariable("userId") Integer userId, @RequestBody @Valid TrainerForm form) {
        return trainerService.updateTrainer(userId, form);
    }

    @PostMapping
    public UserView trainerRegister(@Valid @RequestBody TrainerForm form) {
        return trainerService.trainerRegister(form);
    }

    @GetMapping("/firstName")
    public List<String> firstNameTrainer() {
        return trainerService.fetchFirstName();
    }

}
