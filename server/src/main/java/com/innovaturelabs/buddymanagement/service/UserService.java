/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.*;
import com.innovaturelabs.buddymanagement.form.ForgotPasswordForm;
import com.innovaturelabs.buddymanagement.view.LoginView;
import com.innovaturelabs.buddymanagement.view.UserView;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

/**
 * @author nirmal
 */
public interface UserService {

    UserView add(UserForm form);

    UserView currentUser();

    LoginView login(LoginForm form, Errors errors);

    LoginView refresh(String refreshToken) throws BadRequestException;

    List<UserView> list();

    public UserView addUser(UserRegForm form);

    public ResponseEntity<String> verifyPasswordToken(@Valid ResetPasswordForm form);

    ResponseEntity<String> forgotPassword(@Valid ForgotPasswordForm form);

    UserView fetchProfile();

    UserView updateProfile(UserUpdateForm form);

    ResponseEntity<String> verifyEmailToken(String emailToken);

    ResponseEntity<String> changePassword(ChangePasswordForm form);

    boolean checkDepartment(byte department);

    boolean checkStatus(byte status);
}
