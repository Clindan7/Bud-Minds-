package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TrainerForm;
import com.innovaturelabs.buddymanagement.repository.TrainerRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.service.TrainerService;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.util.*;
import com.innovaturelabs.buddymanagement.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TrainerServiceImpl implements TrainerService {
    Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private static final String USER_NOT_FOUND = "user.not.found";
    private static final String MAIL_NOT_SEND = "mail.not.send";
    private static final String MAIL_NOT_SEND_LOG = "mail not send";

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private MailTemplate mailTemplate;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Pager<UserListView> listTrainer(String search, Long employeeId, Integer s, Integer page, Integer limit) {
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
                userList = StreamSupport.stream(trainerRepository
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
                userList = StreamSupport.stream(trainerRepository
                                .findByEmployeeId(employeeId, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, userList.size(), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);

            }
            if (search != null) {
                userList = StreamSupport.stream(trainerRepository
                                .findBySearch(search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, userList.size(), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);

            }
            userList = StreamSupport.stream(trainerRepository
                            .findAllTrainer(status, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, trainerRepository.findByFirstNameStatus(status).size(), page);
            userPager.setResult(userList);
            return userPager;
        }

        return filterWithOutStatus(employeeId, search, page, limit);
    }

    private Pager<UserListView> filterWithOutStatus(Long employeeId, String search, Integer page, Integer limit) {
        Pager<UserListView> userPager;
        List<UserListView> userList;
        if (employeeId != null && search != null) {
            userList = StreamSupport.stream(trainerRepository
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
            userList = StreamSupport.stream(trainerRepository
                            .findByEmployeeIdParams(employeeId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, userList.size(), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        if (search != null) {
            userList = StreamSupport.stream(trainerRepository
                            .findBySearchParams(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, userList.size(), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);
        }
        userList = StreamSupport.stream(trainerRepository
                        .findAllTrainerInStatus(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(UserListView::new)
                .collect(Collectors.toList());
        userPager = new Pager<>(limit, trainerRepository.findByFirstName().size(), page);
        userPager.setResult(userList);
        return userPager;
    }

    private void statusCheck(byte status) {
        if ((status != 4) && (status == 0 || (!userService.checkStatus(status)))) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.status", null, "en"));
        }
    }

    public Pager<UserListView> checkIfUserList(List<UserListView> userList, Pager<UserListView> userPager) {
        if (!userList.isEmpty()) {
            return userPager;
        } else {
            log.error(USER_NOT_FOUND);
            throw new BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"));
        }
    }


    @Override
    public void trainerDelete(Integer userId) throws BadRequestException {
        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINER.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("user.already.deleted", null, "en"));
        }
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserView fetchTrainer(Integer userId) {
        return new UserView(userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINER.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"))));
    }

    @Override
    public UserView updateTrainer(Integer userId, TrainerForm form) {

        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINER.value).orElseThrow(() -> new
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
    public UserView trainerRegister(TrainerForm form) {
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
            String trainerPassword = passwordUtil.generatePassword();
            User user = userRepository.save(new User(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getEmail(),
                    form.getEmployeeId(),
                    passwordEncoder.encode(trainerPassword),
                    User.Role.TRAINER.value,
                    form.getDepartment()
            ));
            String content = mailTemplate.emailContent(user.getFirstName(), user.getEmail(), trainerPassword);
            String subject = "Here are your credentials to BudMinds";
            emailUtil.sendEmail(user.getEmail(), subject, content);
            return new UserView(user);
        } catch (MessagingException | MailSendException me) {
            log.error(MAIL_NOT_SEND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(MAIL_NOT_SEND, null, "en"), me);
        }
    }

    @Override
    public List<String> fetchFirstName() {
        List<String> firstName;
        try {
            firstName = trainerRepository.findByFirstName();
            return firstName;
        } catch (BadRequestException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("list.fetch.failed", null, "en"));
        }
    }

    @Override
    public List<TraineeTrainerView> traineeTrainerList() {
        List<TraineeTrainerView> trainerViewList;

        trainerViewList = StreamSupport.stream(trainerRepository
                        .findByAllTrainerName().spliterator(), false)
                .map(TraineeTrainerView::new)
                .collect(Collectors.toList());
        return trainerViewList;
    }

}
