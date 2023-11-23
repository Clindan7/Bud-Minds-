/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.*;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.view.UserView;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author nirmal
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public UserView add(@Valid @RequestBody UserForm form) {
        return userService.add(form);
    }

    @PostMapping
    public UserView regUser(@Valid @RequestBody UserRegForm form) {
        return userService.addUser(form);
    }

    @GetMapping
    public List<UserView> list(HttpServletRequest request) {
        return userService.list();
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody @Valid ForgotPasswordForm form) {
        userService.forgotPassword(form);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordForm form) {

        userService.verifyPasswordToken(form);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/token/verify")
    public ResponseEntity<String> verifyToken(@RequestBody String emailToken) {
        userService.verifyEmailToken(emailToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile")
    public UserView fetchProfile() {
        return userService.fetchProfile();
    }

    @PutMapping("/profile")
    public UserView updateProfile(@RequestBody @Valid UserUpdateForm form) {
        return userService.updateProfile(form);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordForm form) {
        return userService.changePassword(form);
    }
}


