/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.LoginForm;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.view.LoginView;
import com.innovaturelabs.buddymanagement.view.UserView;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nirmal
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @GetMapping
    public UserView currentUser() {
        return userService.currentUser();
    }

    @PostMapping
    public LoginView login(@Valid @RequestBody LoginForm form, BindingResult errors) {
        if(errors.hasErrors()){
            throw new BadRequestException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        log.info("Login to the user: {}", form.getEmail());
        return userService.login(form, errors);
    }

    @PutMapping
    public LoginView refresh(@RequestBody String refreshToken) {
        return userService.refresh(refreshToken);
    }

}
