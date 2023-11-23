package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TraineeForm;
import com.innovaturelabs.buddymanagement.repository.TraineeRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.service.TraineeService;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.util.*;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TraineeServiceImpl implements TraineeService {

    Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private MailTemplate mailTemplate;

    private static final String USER_NOT_FOUND = "user.not.found";
    private static final String MAIL_NOT_SEND = "mail.not.send";
    private static final String MAIL_NOT_SEND_LOG = "mail not send";

    @Override
    public Pager<UserListView> listTrainees(String search, Long employeeId, Integer s, Integer page, Integer limit) {
        Pager<UserListView> userPager;
        List<UserListView> userList;
        if (s != null && s == 4) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.status", null, "en"));
        }
        byte status = 4;
        if (s != null) {
            int i = s;
            status = (byte) i;
        }
        statusCheck(status);
        if (status != 4) {
            if (employeeId != null && search != null) {
                userList = StreamSupport.stream(traineeRepository
                                .findByEmployeeIdAndSearch(employeeId, search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, userList.size(), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);
            }
            if (employeeId != null) {
                if (employeeId < 0) {
                    throw new BadRequestException(languageUtil.getTranslatedText("positive.employeeId", null, "en"));

                }
                userList = StreamSupport.stream(traineeRepository
                                .findByEmployeeId(employeeId, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, userList.size(), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);


            }
            if (search != null) {
                userList = StreamSupport.stream(traineeRepository
                                .findBySearch(search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, userList.size(), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);

            }
            userList = StreamSupport.stream(traineeRepository
                            .findAllTrainees(status, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, traineeRepository.findFirstNamesStatus(status).size(), page);
            userPager.setResult(userList);
            return userPager;
        }

        return filterWithOutStatus(employeeId, search, page, limit);

    }

    private Pager<UserListView> filterWithOutStatus(Long employeeId, String search, Integer page, Integer limit) {
        Pager<UserListView> userPager;
        List<UserListView> userList;
        if (employeeId != null && search != null) {
            userList = StreamSupport.stream(traineeRepository
                            .findByEmployeeIdAndSearchParams(employeeId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, userList.size(), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        if (employeeId != null) {
            if (employeeId < 0) {
                throw new BadRequestException(languageUtil.getTranslatedText("positive.employeeId", null, "en"));

            }
            userList = StreamSupport.stream(traineeRepository
                            .findByEmployeeIdParams(employeeId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, userList.size(), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        if (search != null) {
            userList = StreamSupport.stream(traineeRepository
                            .findBySearchParams(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, userList.size(), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        userList = StreamSupport.stream(traineeRepository
                        .findAllTraineesInStatus(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(UserListView::new)
                .collect(Collectors.toList());
        userPager = new Pager<>(limit, traineeRepository.findFirstNames().size(), page);
        userPager.setResult(userList);
        return userPager;
    }

    public Pager<UserListView> checkIfUserList(List<UserListView> userList, Pager<UserListView> userPager) {
        if (!userList.isEmpty()) {
            return userPager;
        } else {
            log.error(USER_NOT_FOUND);
            throw new BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"));
        }
    }

    private void statusCheck(byte status) {
        if ((status != 4) && (status == 0 || (!userService.checkStatus(status)))) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.status", null, "en"));
        }
    }

    @Override
    @Transactional
    public UserView traineeRegister(TraineeForm form) {
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new BadRequestException(languageUtil.getTranslatedText("email.exists", null, "en"));
        }
        if (userRepository.findByEmployeeId(form.getEmployeeId()).isPresent()) {
            throw new BadRequestException(languageUtil.getTranslatedText("employee.id.exists", null, "en"));
        }
        if (form.getDepartment() == 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("department.required", null, "en"));
        }
        if (!userService.checkDepartment(form.getDepartment())) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.department", null, "en"));
        }
        try {
            String traineePassword = passwordUtil.generatePassword();
            User user = userRepository.save(new User(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getEmail(),
                    form.getEmployeeId(),
                    passwordEncoder.encode(traineePassword),
                    User.Role.TRAINEE.value,
                    form.getDepartment()
            ));
            String content = mailTemplate.emailContent(user.getFirstName(), user.getEmail(), traineePassword);
            String subject = "Here are your credentials to BudMinds";
            emailUtil.sendEmail(user.getEmail(), subject, content);
            return new UserView(user);
        } catch (MessagingException | MailSendException me) {
            log.error(MAIL_NOT_SEND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(MAIL_NOT_SEND, null, "en"), me);
        }
    }

    @Override
    public UserView updateTrainee(Integer userId, TraineeForm form) {

        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINEE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        Optional<User> optionalUser = userRepository.findByEmail(form.getEmail());
        if ((optionalUser.isPresent()) && (!optionalUser.get().getUserId().equals(userId))) {
            throw new BadRequestException(languageUtil.getTranslatedText("email.exists", null, "en"));
        }
        optionalUser = userRepository.findByEmployeeId(form.getEmployeeId());
        if ((optionalUser.isPresent()) && (!optionalUser.get().getUserId().equals(userId))) {
            throw new BadRequestException(languageUtil.getTranslatedText("employee.id.exists", null, "en"));
        }
        if (form.getDepartment() == 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("department.required", null, "en"));
        }
        if (!userService.checkDepartment(form.getDepartment())) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.department", null, "en"));
        }
        if (!form.getEmail().equalsIgnoreCase(user.getEmail())) {
            user.setStatus(User.Status.PENDING.value);
        }
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setEmployeeId(form.getEmployeeId());
        user.setDepartment(form.getDepartment());
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return new UserView(user);
    }

    @Override
    public UserView fetchTrainee(Integer userId) {
        return new UserView(userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINEE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"))));
    }

    @Override
    @Transactional
    public void traineeDelete(Integer userId) throws BadRequestException {
        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINEE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("user.already.deleted", null, "en"));
        }
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<String> fetchFirstName() {
        List<String> firstName;
        try {
            firstName = traineeRepository.findFirstNames();
            return firstName;
        } catch (BadRequestException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("list.fetch.failed", null, "en"));
        }
    }

}
