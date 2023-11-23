package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.form.ManagerForm;
import com.innovaturelabs.buddymanagement.form.UserAllocationForm;
import com.innovaturelabs.buddymanagement.service.ManagerService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserView;

import javax.validation.Valid;

import com.innovaturelabs.buddymanagement.view.UserListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Validated
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @GetMapping
    public Pager<UserListView> listManager(@RequestParam(name = "search", required = false) String search,
                                           @RequestParam(name = "employeeId", required = false) Long employeeId,
                                           @RequestParam(name = "status", required = false) Integer status,
                                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return managerService.listManager(search, employeeId, status, page, limit);
    }

    @DeleteMapping("/{userId}")
    public void managerDelete(@PathVariable("userId") Integer userId) {
        managerService.managerDelete(userId);
    }

    @GetMapping("/{userId}")
    public UserView fetchManager(@PathVariable("userId") Integer userId) {
        return managerService.fetchManager(userId);
    }

    @PutMapping("/{userId}")
    public UserView updateManager(@PathVariable("userId") Integer userId, @RequestBody @Valid ManagerForm form) {
        return managerService.updateManager(userId, form);
    }

    @PostMapping
    public UserView managerRegister(@Valid @RequestBody ManagerForm form) {
        return managerService.managerRegister(form);
    }

    @GetMapping("/firstName")
    public List<String> firstNameManager() {
        return managerService.fetchFirstName();
    }

    @PostMapping("/allocation-control")
    public void allocateManager(@RequestParam(name = "userRole") byte userRole,
                                @RequestParam(name = "allocationMode") byte allocationMode,
                                @RequestParam(name = "managerId") Integer managerId,
                                @Valid @RequestBody UserAllocationForm form) {
        managerService.allocateManagerControls(form.getUsers(), userRole, allocationMode, managerId);
    }


    @GetMapping("/resource-list")
    public Pager<User> listManagerResources(@RequestParam(name = "managerId", required = false) Integer managerId,
                                            @RequestParam(name = "userRole") byte userRole,
                                            @RequestParam(name = "search", required = false) String name,
                                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return managerService.listManagerResources(managerId, userRole, name, page, limit);
    }

}
