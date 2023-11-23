package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.ManagerForm;
import com.innovaturelabs.buddymanagement.repository.ManagerRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.service.ManagerService;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.util.*;
import com.innovaturelabs.buddymanagement.view.UserView;
import com.innovaturelabs.buddymanagement.view.UserListView;
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
public class ManagerServiceImpl implements ManagerService {

    Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);
    @Autowired
    private ManagerRepository managerRepository;

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
    public Pager<UserListView> listManager(String search, Long employeeId, Integer s, Integer page, Integer limit) {
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
                userList = StreamSupport.stream(managerRepository
                                .findByEmployeeIdAndSearch(employeeId, search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, managerRepository.findByEmployeeIdAndSearchCount(employeeId, search, status), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);
            }
            if (employeeId != null) {
                if (employeeId < 0) {
                    throw new BadRequestException(languageUtil.getTranslatedText("positive.employeeId", null, "en"));

                }
                userList = StreamSupport.stream(managerRepository
                                .findByEmployeeId(employeeId, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, managerRepository.findByEmployeeIdCount(employeeId, status), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);
            }
            if (search != null) {
                userList = StreamSupport.stream(managerRepository
                                .findBySearch(search, status, PageRequest.of(page - 1, limit)).spliterator(), false)
                        .map(UserListView::new)
                        .collect(Collectors.toList());
                userPager = new Pager<>(limit, managerRepository.findBySearchCount(search, status), page);
                userPager.setResult(userList);
                return checkIfUserList(userList, userPager);
            }
            userList = StreamSupport.stream(managerRepository
                            .findAllManager(status, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, managerRepository.findByFirstNameStatus(status).size(), page);
            userPager.setResult(userList);
            return userPager;
        }
        return filterWithOutStatus(employeeId, search, page, limit);

    }

    private Pager<UserListView> filterWithOutStatus(Long employeeId, String search, Integer page, Integer limit) {
        Pager<UserListView> userPager;
        List<UserListView> userList;
        if (employeeId != null && search != null) {
            userList = StreamSupport.stream(managerRepository
                            .findByEmployeeIdAndSearchParams(employeeId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, managerRepository.findByEmployeeIdAndSearchParamsCount(employeeId, search), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);
        }
        if (employeeId != null) {
            if (employeeId < 0) {
                throw new BadRequestException(languageUtil.getTranslatedText("positive.employeeId", null, "en"));

            }
            userList = StreamSupport.stream(managerRepository
                            .findByEmployeeIdParams(employeeId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, managerRepository.findByEmployeeIdParamsCount(employeeId), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);
        }
        if (search != null) {
            userList = StreamSupport.stream(managerRepository
                            .findBySearchParams(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(UserListView::new)
                    .collect(Collectors.toList());
            userPager = new Pager<>(limit, managerRepository.findBySearchParamsCount(search), page);
            userPager.setResult(userList);
            return checkIfUserList(userList, userPager);
        }
        userList = StreamSupport.stream(managerRepository
                        .findAllManagerInStatus(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(UserListView::new)
                .collect(Collectors.toList());
        userPager = new Pager<>(limit, managerRepository.findByFirstName().size(), page);
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
    public void managerDelete(Integer userId) throws BadRequestException {
        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.MANAGER.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (user.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("user.already.deleted", null, "en"));
        }
        if (managerRepository.countInManager(user.getUserId()) != 0) {
            log.error("manager is allocated, can't be deleted");
            throw new BadRequestException(languageUtil.getTranslatedText("manager.allocated", null, "en"));
        }
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserView fetchManager(Integer userId) {
        return new UserView(userRepository.findByUserIdAndUserRole(userId, User.Role.MANAGER.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"))));
    }

    @Override
    @Transactional
    public UserView managerRegister(ManagerForm form) {
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
            String managerPassword = passwordUtil.generatePassword();
            User user = userRepository.save(new User(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getEmail(),
                    form.getEmployeeId(),
                    passwordEncoder.encode(managerPassword),
                    User.Role.MANAGER.value,
                    form.getDepartment()
            ));
            String content = mailTemplate.emailContent(user.getFirstName(), user.getEmail(), managerPassword);
            String subject = "Here are your credentials to BudMinds";
            emailUtil.sendEmail(user.getEmail(), subject, content);
            return new UserView(user);
        } catch (MessagingException | MailSendException me) {
            log.error(MAIL_NOT_SEND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(MAIL_NOT_SEND, null, "en"), me);
        }
    }

    @Override
    public UserView updateManager(Integer userId, ManagerForm form) {

        User user = userRepository.findByUserIdAndUserRole(userId, User.Role.MANAGER.value).orElseThrow(() -> new
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
            firstName = managerRepository.findByFirstName();
            return firstName;
        } catch (BadRequestException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("list.fetch.failed", null, "en"));
        }
    }

    @Override
    public void allocateManagerControls(List<Integer> users, byte userRole, byte allocationMode, Integer managerId) {
        List<Integer> notFound = new ArrayList<>();
        List<Integer> alreadyDeallocated = new ArrayList<>();
        List<Integer> alreadyAllocated = new ArrayList<>();
        boolean skipUpdate;
        users = users.stream()
                .distinct()
                .collect(Collectors.toList());              //Remove duplicates from user list
        users.removeAll(Collections.singletonList(null));   //Remove null values from user list
        allocationBasicChecks(users, userRole, allocationMode, managerId);
        for (Integer userId : users) {
            Optional<User> optionalUser = (userRole == (byte) 4) ? (userRepository.findUsers(userId, userRole)) : (userRepository.findByUserIdAndUserRoleAndStatus(userId, User.Role.MENTOR.value, User.Status.ACTIVE.value));
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (allocationMode == (byte) 0) {
                    skipUpdate = deAllocator(user, alreadyDeallocated, notFound, managerId);
                } else {
                    User manager = userRepository.findByUserIdAndUserRoleAndStatus(managerId, User.Role.MANAGER.value, User.Status.ACTIVE.value).orElseThrow(() -> new
                            BadRequestException(languageUtil.getTranslatedText("manager.not.found", null, "en")));
                    skipUpdate = allocator(user, manager, alreadyAllocated);
                }
                if (!skipUpdate) {
                    user.setUpdateDate(LocalDateTime.now());
                    userRepository.save(user);
                }
            } else {
                notFound.add(userId);
            }
        }
        userListErrors(notFound, alreadyDeallocated, alreadyAllocated);
    }

    @Override
    public boolean deAllocator(User user, List<Integer> alreadyDeallocated, List<Integer> notFound, Integer managerId) {
        boolean skipUpdate = false;
        if (user.getManagerId() == null) {
            alreadyDeallocated.add(user.getUserId());
            skipUpdate = true;
        } else {
            if ((!user.getManagerId().getUserId().equals(managerId))) {
                notFound.add(user.getUserId());
                skipUpdate = true;
            }
        }
        if (!skipUpdate) {
            user.setManagerId(null);
        }
        return skipUpdate;
    }

    @Override
    public boolean allocator(User user, User manager, List<Integer> alreadyAllocated) {
        boolean skipUpdate = false;
        if (user.getManagerId() != null) {
            alreadyAllocated.add(user.getUserId());
            skipUpdate = true;
        }
        if (!skipUpdate) {
            user.setManagerId(manager);
        }
        return skipUpdate;
    }

    @Override
    public void allocationBasicChecks(List<Integer> users, byte userRole, byte allocationMode, Integer managerId) {
        if ((allocationMode != (byte) 0) && (allocationMode != (byte) 1)) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.allocation.mode", null, "en"));
        }
        if (managerId == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("manager.id.required", null, "en"));
        }
        if (users.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText("users.not.selected", null, "en"));
        }
        if (userRole != User.Role.MENTOR.value && userRole != User.Role.TRAINEE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.user.role", null, "en"));
        }
    }

    @Override
    public void userListErrors(List<Integer> notFound, List<Integer> alreadyDeallocated, List<Integer> alreadyAllocated) {
        String errorMessage = "1940-";
        if (!notFound.isEmpty()) {
            errorMessage = errorMessage + "User not found - User IDs:" + notFound + ". ";
        }
        if (!alreadyDeallocated.isEmpty()) {
            errorMessage = errorMessage + "Manager is already deallocated - User IDs:" + alreadyDeallocated + ". ";
        }
        if (!alreadyAllocated.isEmpty()) {
            errorMessage = errorMessage + "Manager is already allocated - User IDs:" + alreadyAllocated + ". ";
        }
        if (!errorMessage.equals("1940-")) {
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    public Pager<User> listManagerResources(Integer managerId, byte userRole, String search, Integer page, Integer limit) {
        if (search == null) {
            search = "";
        }
        List<User> userList;
        Pager<User> userPager;
        if (userRole != User.Role.MENTOR.value && userRole != User.Role.TRAINEE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.user.role", null, "en"));
        }
        if (managerId != null) {
            Optional<User> manager = userRepository.findByUserIdAndUserRoleAndStatus(managerId, User.Role.MANAGER.value, User.Status.ACTIVE.value);
            if (!manager.isPresent()) {
                throw new BadRequestException(languageUtil.getTranslatedText("manager.not.found", null, "en"));
            }
            if (userRole == (byte) 4) {
                userList = traineeRepository.findManagerResourcesTrainees(managerId, search, PageRequest.of(page - 1, limit));
                userPager = new Pager<>(limit, traineeRepository.countByManagerResourcesTrainees(managerId, search), page);
            } else {
                userList = userRepository.findAllByManagerIdUserIdAndUserRoleAndStatus(managerId, search, PageRequest.of(page - 1, limit));
                userPager = new Pager<>(limit, userRepository.countByManagerIdUserIdAndUserRoleAndStatus(managerId, search), page);
            }
        } else {
            if (userRole == (byte) 4) {
                userList = traineeRepository.findUnassignedManagerResourcesTrainees(search, PageRequest.of(page - 1, limit));
                userPager = new Pager<>(limit, traineeRepository.countUnassignedManagerResourcesTrainees(search), page);
            } else {
                userList = userRepository.findAllByManagerIdUserIdAndUserRoleAndStatus(null, search, PageRequest.of(page - 1, limit));
                userPager = new Pager<>(limit, userRepository.countByManagerIdUserIdAndUserRoleAndStatus(null, search), page);
            }
        }
        userPager.setResult(userList);
        return userPager;
    }

}




