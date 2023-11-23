package com.innovaturelabs.buddymanagement.controller;


import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.form.MentorForm;
import com.innovaturelabs.buddymanagement.form.UserAllocationForm;
import com.innovaturelabs.buddymanagement.service.MentorService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@Validated
@RequestMapping("/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @GetMapping
    public Pager<UserListView> listMentor(@RequestParam(name = "search", required = false) String search,
                                          @RequestParam(name = "employeeId", required = false) Long employeeId,
                                          @RequestParam(name = "status", required = false) Integer status,
                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return mentorService.listMentor(search, employeeId, status,page, limit);
    }

    @DeleteMapping("/{userId}")
    public void mentorDelete(@PathVariable("userId") Integer userId) {
        mentorService.mentorDelete(userId);
    }

    @GetMapping("/{userId}")
    public UserView fetchMentor(@PathVariable("userId") Integer userId) {
        return mentorService.fetchMentor(userId);
    }

    @PutMapping("/{userId}")
    public UserView updateMentor(@PathVariable("userId") Integer userId, @RequestBody @Valid MentorForm form) {
        return mentorService.updateMentor(userId, form);
    }

    @PostMapping
    public UserView mentorRegister(@Valid @RequestBody MentorForm form) {
        return mentorService.mentorRegister(form);
    }

    @GetMapping("/firstName")
    public List<String> firstNameMentor() {
        return mentorService.fetchFirstName();
    }

    @PostMapping("/allocation-control")
    public void allocateMentor(@RequestParam(name = "allocationMode") byte allocationMode,
                               @RequestParam(name = "mentorId") Integer mentorId,
                               @Valid @RequestBody UserAllocationForm form) {
        mentorService.allocateMentorControls(form.getUsers(), allocationMode, mentorId);
    }

    @GetMapping("/resource-list")
    public Pager<User> listMentorResources(@RequestParam(name = "mentorId", required = false) Integer mentorId,
                                          @RequestParam(name = "search", required = false) String name,
                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return mentorService.listMentorResources(mentorId,name,page,limit);
    }
}
