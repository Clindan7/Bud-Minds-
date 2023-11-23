/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.*;
import com.innovaturelabs.buddymanagement.repository.UserRepository;

import static com.innovaturelabs.buddymanagement.security.AccessTokenUserDetailsService.PURPOSE_ACCESS_TOKEN;

import com.innovaturelabs.buddymanagement.security.config.SecurityConfig;
import com.innovaturelabs.buddymanagement.security.util.InvalidTokenException;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.security.util.TokenExpiredException;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator.Status;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator.Token;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.util.EmailUtil;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.view.LoginView;
import com.innovaturelabs.buddymanagement.view.UserView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

/**
 * @author nirmal
 */
@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String PURPOSE_REFRESH_TOKEN = "REFRESH_TOKEN";

    private static final String PURPOSE_EMAIL_TOKEN = "EMAIL_TOKEN";

    private static final String PURPOSE_INVALID_TOKEN = "INVALID_TOKEN";

    private static final String USER_NOT_FOUND = "user.not.found";

    private static final String INVALID_TOKEN = "invalid.token";

    private static final String TOKEN_EXPIRED = "token.expired";


    @Value("${reset.link}")
    private String resetLink;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public UserView add(UserForm form) {
        return new UserView(userRepository.save(new User(
                form.getFirstName(),
                form.getLastName(),
                form.getEmail(),
                form.getEmployeeId(),
                passwordEncoder.encode(form.getPassword())
        )));
    }

    @Override
    public UserView currentUser() {
        return new UserView(
                userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                        BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"))));
    }

    @Override
    public LoginView login(LoginForm form, Errors errors) {
        if (errors.hasErrors()) {
            log.error("Invalid credentials");
            throw badRequestException();
        }
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if ((user.getStatus() != User.Status.ACTIVE.value) && (user.getStatus() != User.Status.REGISTERED.value)) {
            throw new BadRequestException(languageUtil.getTranslatedText("user.not.active", null, "en"));
        }
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new BadRequestException(languageUtil.getTranslatedText("password.is.wrong", null, "en"));
        }
        String id = String.format("%010d", user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, securityConfig.getAccessTokenExpiry());
        Token refreshToken = tokenGenerator.create(PURPOSE_REFRESH_TOKEN, id + user.getPassword(), securityConfig.getRefreshTokenExpiry());
        return new LoginView(user, accessToken, refreshToken);
    }

    @Override
    public LoginView refresh(String refreshToken) throws BadRequestException {
        Status status;
        try {
            status = tokenGenerator.verify(PURPOSE_REFRESH_TOKEN, refreshToken);
        } catch (InvalidTokenException e) {
            log.error(PURPOSE_INVALID_TOKEN);
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_TOKEN, null, "en"), e);
        } catch (TokenExpiredException e) {
            log.error(TOKEN_EXPIRED);
            throw new BadRequestException(languageUtil.getTranslatedText("auth.token.expired", null, "en"), e);
        }
        int userId;
        try {
            userId = Integer.parseInt(status.data.substring(0, 10));
        } catch (NumberFormatException e) {
            log.error(PURPOSE_INVALID_TOKEN);
            throw new BadRequestException(INVALID_TOKEN, e);
        }
        String password = status.data.substring(10);

        User user = userRepository.findByUserIdAndPassword(userId, password).orElseThrow(UserServiceImpl::badRequestException);

        String id = String.format("%010d", user.getUserId());
        Token accessToken = tokenGenerator.create(PURPOSE_ACCESS_TOKEN, id, securityConfig.getAccessTokenExpiry());
        return new LoginView(
                user,
                new LoginView.TokenView(accessToken.value, accessToken.expiry),
                new LoginView.TokenView(refreshToken, status.expiry)
        );
    }

    private static BadRequestException badRequestException() {
        return new BadRequestException("1910-Invalid credentials");
    }

    @Override
    public List<UserView> list() {
        return StreamSupport.stream(userRepository.
                        findAll().spliterator(), false)
                .map(UserView::new)
                .collect(Collectors.toList());
    }


    @Override
    public UserView addUser(UserRegForm form) {
        User user = new User();
        LocalDateTime dt = LocalDateTime.now();
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmployeeId(form.getEmployeeId());
        user.setUserRole(form.getUserRole());
        user.setCreateDate(dt);
        user.setStatus(User.Status.ACTIVE.value);
        user.setUpdateDate(dt);
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        return new UserView(userRepository.save(user));
    }

    @Override
    public ResponseEntity<String> forgotPassword(ForgotPasswordForm form) {
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));

        String to = user.getEmail();
        Duration duration = Duration.ofHours(1);
        Token emailToken = tokenGenerator.create(PURPOSE_EMAIL_TOKEN, to, duration);
        String content = "Hello, "
                + "You have requested to reset your password. "
                + "Click the link below to change your password. "
                + "Ignore this email if you do remember your password, "
                + "or you have not made the request.";
        String subject = "Here's the link to reset your password";
        String resetPasswordLink = resetLink + emailToken.value;
        emailUtil.sendMail(to, subject, content, resetPasswordLink);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyPasswordToken(ResetPasswordForm form) {
        Status status;

        try {
            status = tokenGenerator.verify(PURPOSE_EMAIL_TOKEN, form.getEmailToken());
        } catch (InvalidTokenException e) {
            log.error("Invalid token");
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_TOKEN, null, "en"), e);
        } catch (TokenExpiredException e) {
            log.error("Token expired");
            throw new BadRequestException(languageUtil.getTranslatedText(TOKEN_EXPIRED, null, "en"), e);
        }
        User user = userRepository.findByEmail(status.data).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (passwordEncoder.matches(form.getNewPassword(), user.getPassword())) {
            log.error("Password is same");
            throw new BadRequestException(languageUtil.getTranslatedText("password.is.same", null, "en"));
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public UserView fetchProfile() {
        return new UserView(userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"))));
    }

    @Override
    @Transactional
    public UserView updateProfile(UserUpdateForm form) {
        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return new UserView(user);
    }

    @Override
    public ResponseEntity<String> verifyEmailToken(String emailToken) {
        try {
            tokenGenerator.verify(PURPOSE_EMAIL_TOKEN, emailToken);
        } catch (InvalidTokenException e) {
            log.error("Invalid token");
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_TOKEN, null, "en"), e);
        } catch (TokenExpiredException e) {
            log.error("Token expired");
            throw new BadRequestException(languageUtil.getTranslatedText(TOKEN_EXPIRED, null, "en"), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordForm form) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (!(passwordEncoder.matches(form.getCurrentPassword(), user.getPassword()))) {
            throw new BadRequestException(languageUtil.getTranslatedText("wrong.password", null, "en"));
        }
        if (form.getNewPassword().equals(form.getCurrentPassword())) {
            throw new BadRequestException(languageUtil.getTranslatedText("password.is.same", null, "en"));
        }
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        if(user.getStatus()==User.Status.REGISTERED.value){
            user.setStatus(User.Status.ACTIVE.value);
        }
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public boolean checkDepartment(byte department) {
        for (User.Department dept : User.Department.values()) {
            if (dept.value == department)
                return true;
        }
        return false;
    }

    @Override
    public boolean checkStatus(byte status) {
        for (User.Status status1 : User.Status.values()) {
            if (status1.value == status)
                return true;
        }
        return false;
    }
}
