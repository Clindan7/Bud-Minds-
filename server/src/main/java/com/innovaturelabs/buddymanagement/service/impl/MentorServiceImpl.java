package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.MentorForm;
import com.innovaturelabs.buddymanagement.repository.MentorRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.service.MentorService;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MentorServiceImpl implements MentorService {

    Logger log = LoggerFactory.getLogger(MentorServiceImpl.class);
    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private MailTemplate mailTemplate;

    private static final String USER_NOT_FOUND = "user.not.found";
    private static final String MAIL_NOT_SEND = "mail.not.send";
    private static final String MAIL_NOT_SEND_LOG = "mail not send";

    @Override
    @Transactional
    public void mentorDelete(Integer userId) throws BadRequestException {
        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.MENTOR.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("user.already.deleted", null, "en"));
        }
        if (mentorRepository.countInMentor(user.getUserId()) != 0) {
            log.error("mentor is allocated, can't be deleted");
            throw new BadRequestException(languageUtil.getTranslatedText("mentor.allocated", null, "en"));
        }
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);

    }

    @Override
    public Pager<UserListView> listMentor(String search, Long employeeId, Integer s, Integer page, Integer limit) {
        Pager<UserListView> userPager = null;
        List<UserListView> userList = null;
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
                userList = StreamSupport.stream(mentorRepository
                                .findByEmployeeIdAndSearch(employeeId, search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, mentorRepository.countByEmployeeIdAndSearch(employeeId, search, status), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);

            }
            if (employeeId != null) {
                if (employeeId < 0) {
                    throw new BadRequestException(languageUtil.getTranslatedText("positive.employeeId", null, "en"));

                }
                userList = StreamSupport.stream(mentorRepository
                                .findByEmployeeId(employeeId, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, mentorRepository.countByEmployeeId(employeeId, status), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);

            }
            if (search != null) {
                userList = StreamSupport.stream(mentorRepository
                                .findBySearch(search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, mentorRepository.countBySearch(search, status), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);

            }
            userList = StreamSupport.stream(mentorRepository
                            .findAllMentor(status, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, mentorRepository.findByFirstNameStatus(status).size(), page);
            userPager.setResult(userList);
            return userPager;
        }
        return filterWithOutStatus(employeeId, search, page, limit);

    }

    private Pager<UserListView> filterWithOutStatus(Long employeeId, String search, Integer page, Integer limit) {
        Pager<UserListView> userPager = null;
        List<UserListView> userList = null;
        if (employeeId != null && search != null) {
            userList = StreamSupport.stream(mentorRepository
                            .findByEmployeeIdAndSearchParams(employeeId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, mentorRepository.countByEmployeeIdAndSearchParams(employeeId, search), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        if (employeeId != null) {
            if (employeeId < 0) {
                throw new BadRequestException(languageUtil.getTranslatedText("positive.employeeId", null, "en"));

            }
            userList = StreamSupport.stream(mentorRepository
                            .findByEmployeeIdParams(employeeId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, mentorRepository.countByEmployeeIdParams(employeeId), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        if (search != null) {
            userList = StreamSupport.stream(mentorRepository
                            .findBySearchParams(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, mentorRepository.countBySearchParams(search), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);

        }
        userList = StreamSupport.stream(mentorRepository.findAllMentorInStatus(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(UserListView::new)
                .collect(Collectors.toList());
        userPager = new Pager<>(limit, mentorRepository.findByFirstName().size(), page);
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
    public UserView fetchMentor(Integer userId) {
        return new UserView(userRepository.findByUserIdAndUserRole(userId, User.Role.MENTOR.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"))));
    }

    @Override
    @Transactional
    public UserView mentorRegister(MentorForm form) {
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
            String mentorPassword = passwordUtil.generatePassword();
            User user = userRepository.save(new User(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getEmail(),
                    form.getEmployeeId(),
                    passwordEncoder.encode(mentorPassword),
                    User.Role.MENTOR.value,
                    form.getDepartment()
            ));
            String content = mailTemplate.emailContent(user.getFirstName(), user.getEmail(), mentorPassword);
            String subject = "Here are your credentials to BudMinds";
            emailUtil.sendEmail(user.getEmail(), subject, content);
            return new UserView(user);
        } catch (MessagingException | MailSendException me) {
            log.error(MAIL_NOT_SEND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(MAIL_NOT_SEND, null, "en"), me);
        }
    }

    @Override
    public UserView updateMentor(Integer userId, MentorForm form) {

        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.MENTOR.value).orElseThrow(() -> new
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
    public List<String> fetchFirstName() {
        List<String> firstName;
        try {
            firstName = mentorRepository.findByFirstName();
            return firstName;
        } catch (BadRequestException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("list.fetch.failed", null, "en"));
        }
    }

    @Override
    public Pager<User> listMentorResources(Integer mentorId, String name, Integer page, Integer limit) {
        List<User> userList;
        Pager<User> userPager;
        if (mentorId != null) {
            Optional<User> mentor = userRepository.findByUserIdAndUserRoleAndStatus(mentorId, User.Role.MENTOR.value, User.Status.ACTIVE.value);
            if (mentor.isEmpty()) {
                throw new BadRequestException(languageUtil.getTranslatedText("mentor.not.found", null, "en"));
            }
            userList = traineeRepository.findMentorResourcesTrainees(mentorId, name, PageRequest.of(page - 1, limit));
            userPager = new Pager<>(limit, traineeRepository.findMentorResourcesTraineesCount(mentorId, name), page);
        } else {
            userList = traineeRepository.findUnassignedMentorResourcesTrainees(name, PageRequest.of(page - 1, limit));
            userPager = new Pager<>(limit, traineeRepository.findUnassignedMentorResourcesTraineesCount(name), page);
        }
        userPager.setResult(userList);
        return userPager;
    }

    @Override
    public void allocateMentorControls(List<Integer> users, byte allocationMode, Integer mentorId) {
        List<Integer> notFound = new ArrayList<>();
        List<Integer> alreadyDeallocated = new ArrayList<>();
        List<Integer> alreadyAllocated = new ArrayList<>();
        List<Integer> traineesWithoutManager = new ArrayList<>();
        boolean skipUpdate;
        users = users.stream()
                .distinct()
                .collect(Collectors.toList());              //Remove duplicates from user list
        users.removeAll(Collections.singletonList(null));   //Remove null values from user list
        allocationBasicChecks(users, allocationMode, mentorId);
        for (Integer userId : users) {
            Optional<User> optionalUser = userRepository.findUsers(userId, User.Role.TRAINEE.value);
            if (optionalUser.isPresent()) {
                User trainee = optionalUser.get();
                if (allocationMode == (byte) 0) {
                    skipUpdate = deAllocator(trainee, alreadyDeallocated, notFound, mentorId);
                } else {
                    User mentor = userRepository.findByUserIdAndUserRoleAndStatus(mentorId, User.Role.MENTOR.value, User.Status.ACTIVE.value).orElseThrow(() -> new
                            BadRequestException(languageUtil.getTranslatedText("mentor.not.found", null, "en")));
                    skipUpdate = allocator(trainee, mentor, alreadyAllocated, traineesWithoutManager);
                }
                if (!skipUpdate) {
                    trainee.setUpdateDate(LocalDateTime.now());
                    userRepository.save(trainee);
                }
            } else {
                notFound.add(userId);
            }
        }
        userListErrors(notFound, alreadyDeallocated, alreadyAllocated, traineesWithoutManager);
    }

    @Override
    public boolean deAllocator(User trainee, List<Integer> alreadyDeallocated, List<Integer> notFound, Integer mentorId) {
        boolean skipUpdate = false;
        if (trainee.getMentorId() == null) {
            alreadyDeallocated.add(trainee.getUserId());
            skipUpdate = true;
        } else {
            if ((!trainee.getMentorId().getUserId().equals(mentorId))) {
                notFound.add(trainee.getUserId());
                skipUpdate = true;
            }
        }
        if (!skipUpdate) {
            trainee.setMentorId(null);
        }
        return skipUpdate;
    }

    @Override
    public boolean allocator(User trainee, User mentor, List<Integer> alreadyAllocated, List<Integer> traineesWithoutManager) {
        boolean skipUpdate = false;
        if (trainee.getMentorId() != null) {
            alreadyAllocated.add(trainee.getUserId());
            skipUpdate = true;
        }
        if (trainee.getManagerId() == null) {
            traineesWithoutManager.add(trainee.getUserId());
            skipUpdate = true;
        }
        if (!skipUpdate) {
            trainee.setMentorId(mentor);
        }
        return skipUpdate;
    }

    @Override
    public void allocationBasicChecks(List<Integer> users, byte allocationMode, Integer mentorId) {
        if ((allocationMode != (byte) 0) && (allocationMode != (byte) 1)) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.allocation.mode", null, "en"));
        }
        if (mentorId == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("mentor.id.required", null, "en"));
        }
        if (users.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText("users.not.selected", null, "en"));
        }
    }

    @Override
    public void userListErrors(List<Integer> notFound, List<Integer> alreadyDeallocated, List<Integer> alreadyAllocated, List<Integer> traineesWithoutManager) {
        String errorMessage = "1940-";
        if (!notFound.isEmpty()) {
            errorMessage = errorMessage + "User not found - User IDs:" + notFound + ". ";
        }
        if (!alreadyDeallocated.isEmpty()) {
            errorMessage = errorMessage + "Mentor is already deallocated - User IDs:" + alreadyDeallocated + ". ";
        }
        if (!alreadyAllocated.isEmpty()) {
            errorMessage = errorMessage + "Mentor is already allocated - User IDs:" + alreadyAllocated + ". ";
        }
        if (!traineesWithoutManager.isEmpty()) {
            errorMessage = errorMessage + "Manager is not assigned for the trainees - User IDs:" + traineesWithoutManager + ". ";
        }
        if (!errorMessage.equals("1940-")) {
            throw new BadRequestException(errorMessage);
        }
    }
}


