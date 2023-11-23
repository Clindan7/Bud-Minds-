package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TraineeForm;
import com.innovaturelabs.buddymanagement.repository.TraineeRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.config.SecurityConfig;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class TraineeServiceImplTest {

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private TraineeRepository traineeRepository;

    @InjectMocks
    @Autowired
    UserServiceImpl userService;

    @InjectMocks
    @Autowired
    TraineeServiceImpl traineeService;

    @MockBean
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    LanguageUtil util;

    @Mock
    Errors errors;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Test
    void deleteTrainee_save() {
        int userId = 4;
        User user = new User();
        user.setStatus(User.Status.ACTIVE.value);
        when(userRepository.findByUserIdAndUserRole(userId,User.Role.TRAINEE.value)).thenReturn(Optional.of(user));
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndStatus(userId,User.Status.ACTIVE.value);
        doReturn(user).when(userRepository).findByUserIdAndStatus(6,User.Status.ACTIVE.value);
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        assertThrows(BadRequestException.class, () -> traineeService.traineeDelete(userId));
        verify(userRepository).findByUserIdAndUserRole(userId,User.Role.TRAINEE.value);
    }

    @Test
    void traineeRegister() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User trainee = new User(1);
        trainee.setEmail(traineeForm.getEmail());
        trainee.setFirstName(traineeForm.getFirstName());
        trainee.setLastName(traineeForm.getLastName());
        trainee.setEmployeeId(traineeForm.getEmployeeId());
        trainee.setDepartment(traineeForm.getDepartment());
        doReturn(trainee).when(userRepository).save(ArgumentMatchers.any());
        UserView userView = new UserView(trainee);
        assertEquals(userView.getEmployeeId(), traineeService.traineeRegister(traineeForm).getEmployeeId());
    }

    @Test
    void trainee_register_email_checking() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User trainee = new User(1);
        trainee.setEmail(traineeForm.getEmail());
        trainee.setFirstName(traineeForm.getFirstName());
        trainee.setLastName(traineeForm.getLastName());
        trainee.setEmployeeId(traineeForm.getEmployeeId());
        trainee.setDepartment(traineeForm.getDepartment());
        doReturn(Optional.of(trainee.getEmail())).when(userRepository).findByEmail(traineeForm.getEmail());
        assertThrows(BadRequestException.class, () -> traineeService.traineeRegister(traineeForm));
    }

    @Test
    void trainee_register_employeeId_checking() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User trainee = new User(1);
        trainee.setEmail(traineeForm.getEmail());
        trainee.setFirstName(traineeForm.getFirstName());
        trainee.setLastName(traineeForm.getLastName());
        trainee.setEmployeeId(traineeForm.getEmployeeId());
        trainee.setDepartment(traineeForm.getDepartment());
        doReturn(Optional.of(trainee.getEmployeeId())).when(userRepository).findByEmployeeId(traineeForm.getEmployeeId());
        assertThrows(BadRequestException.class, () -> traineeService.traineeRegister(traineeForm));
    }

    @Test
    void trainee_register_department_checking_false() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User trainee = new User(1);
        trainee.setEmail(traineeForm.getEmail());
        trainee.setFirstName(traineeForm.getFirstName());
        trainee.setLastName(traineeForm.getLastName());
        trainee.setEmployeeId(traineeForm.getEmployeeId());
        trainee.setDepartment((byte) 0);
        if (traineeForm.getDepartment() == 0) {
            assertThrows(BadRequestException.class, () -> traineeService.traineeRegister(traineeForm));
        }
    }

    @Test
    void trainee_register_department_checking_true() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User trainee = new User(1);
        trainee.setEmail(traineeForm.getEmail());
        trainee.setFirstName(traineeForm.getFirstName());
        trainee.setLastName(traineeForm.getLastName());
        trainee.setEmployeeId(traineeForm.getEmployeeId());
        trainee.setDepartment(traineeForm.getDepartment());
        assertThrows(BadRequestException.class, () -> traineeService.updateTrainee(1, traineeForm));
    }

    @Test
    void traineeUpdate() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User user = new User(1);
        LocalDateTime dt = LocalDateTime.now();
        user.setEmail("hari@gm.co");
        user.setFirstName("hari");
        user.setLastName("as");
        user.setEmployeeId(1234L);
        user.setDepartment((byte) 1);
        user.setUserRole(User.Role.TRAINEE.value);
        user.setCreateDate(dt);
        user.setStatus(User.Status.ACTIVE.value);
        user.setUpdateDate(dt);
        user.setPassword(passwordEncoder.encode("Hari@123"));
        doReturn(Optional.of(user)).when(userRepository).findByUserIdAndUserRole(1, User.Role.TRAINEE.value);
        traineeService.updateTrainee(1, traineeForm);
        doReturn(user).when(userRepository).save(user);
        UserView view = new UserView(user);
        assertEquals(view.getUserId(), traineeService.updateTrainee(1, traineeForm).getUserId());
    }


    @Test
    void trainee_update_email_checking() {
        TraineeForm traineeForm = new TraineeForm();
        traineeForm.setFirstName("hari");
        traineeForm.setLastName("as");
        traineeForm.setEmail("hari@gm.co");
        traineeForm.setEmployeeId(1234L);
        traineeForm.setDepartment((byte) 1);
        User trainee = new User(1);
        trainee.setEmail(traineeForm.getEmail());
        trainee.setFirstName(traineeForm.getFirstName());
        trainee.setLastName(traineeForm.getLastName());
        trainee.setEmployeeId(traineeForm.getEmployeeId());
        trainee.setDepartment(traineeForm.getDepartment());
        doReturn(Optional.of(trainee)).when(userRepository).findByEmail(traineeForm.getEmail());
        if (!Optional.of(trainee).get().getUserId().equals(1)) {
            assertThrows(BadRequestException.class, () -> traineeService.updateTrainee(1, traineeForm));
        }
    }

    @Test
    void fetchTrainee() {
        int userId = 1;
        User trainee = new User(userId);
        trainee.setFirstName("hari");
        trainee.setLastName("as");
        trainee.setEmployeeId(1234L);
        trainee.setEmail("hari2gm.co");
        trainee.setPassword("Hari@123");
        UserView view = new UserView(trainee);
        when(userRepository.findByUserId(userId)).thenReturn(trainee);
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(userId, User.Role.TRAINEE.value);
        assertThrows(BadRequestException.class, () -> traineeService.fetchTrainee(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.TRAINEE.value);
    }

    @Test
    void fetchtrainee_error() {
        int userId = 1;
        User u = new User(userId);
        u.setFirstName("hari");
        u.setLastName("as");
        u.setEmployeeId(1234L);
        u.setEmail("hari2gm.co");
        u.setPassword("Hari@123");
        UserView view = new UserView(u);
        when(userRepository.findByUserId(userId)).thenReturn(u);
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(2, User.Role.TRAINEE.value);
        assertThrows(BadRequestException.class, () -> traineeService.fetchTrainee(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.TRAINEE.value);
    }


    @Test
    void getsPagedListOfTrainees_EmpId_And_Search() {
        Long employeeId = 1234L;
        String search = "hari";
        User trainee = new User(1);
        trainee.setUserId(1);
        trainee.setFirstName("hari");
        trainee.setLastName("as");
        trainee.setEmail("hari@gm.co");
        trainee.setEmployeeId(employeeId);
        trainee.setStatus(User.Status.ACTIVE.value);
        Page<User> traineePage = new PageImpl<>(Collections.singletonList(trainee));
        when(traineeRepository.findByEmployeeIdAndSearch(employeeId, search,trainee.getStatus(), PageRequest.of(1, 3))).thenReturn(traineePage);
        Page<User> trainees = traineeRepository.findByEmployeeIdAndSearch(employeeId, search,trainee.getStatus(), PageRequest.of(1, 3));
        when(traineeRepository.findAllTrainees(trainee.getStatus(),PageRequest.of(1, 3))).thenReturn(trainees);
        doThrow(NullPointerException.class).when(traineeRepository).
                findByEmployeeIdAndSearch(employeeId, search,trainee.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            traineeService.listTrainees(search, employeeId,4, 1, 3);
        });
        verify(traineeRepository).findByEmployeeIdAndSearch(employeeId, search,trainee.getStatus(), PageRequest.of(1, 3));
    }

    @Test
    void getsPagedListOfTrainees_by_EmpId() {
        Long employeeId = 1234L;
        String search = "hari";
        User trainee = new User(1);
        trainee.setUserId(1);
        trainee.setFirstName("hari");
        trainee.setLastName("as");
        trainee.setEmail("hari@gm.co");
        trainee.setEmployeeId(employeeId);
        trainee.setStatus(User.Status.ACTIVE.value);
        Page<User> traineePage = new PageImpl<>(Collections.singletonList(trainee));
        when(traineeRepository.findByEmployeeId(employeeId, trainee.getStatus(),PageRequest.of(1, 3))).thenReturn(traineePage);
        Page<User> trainees = traineeRepository.findByEmployeeId(employeeId,trainee.getStatus(), PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(traineeRepository).
                findByEmployeeId(employeeId,trainee.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            traineeService.listTrainees(null, employeeId, 4,1, 3);
        });
    }

    @Test
    void getsPagedListOfTrainees_by_EmpId_exception() {
        long employeeId = -1234L;
        assertThrows(BadRequestException.class, () -> {
            traineeService.listTrainees(null, employeeId,4, 1, 3);
        });
    }


    @Test
    void getsPagedListOfTrainees_by_FindAll() {
        User trainee = new User(1);
        trainee.setUserId(1);
        trainee.setFirstName("hari");
        trainee.setLastName("as");
        trainee.setEmail("hari@gm.co");
        trainee.setEmployeeId(123L);
        trainee.setStatus(User.Status.ACTIVE.value);
        Page<User> traineePage = new PageImpl<>(Collections.singletonList(trainee));
        when(traineeRepository.findAllTrainees(trainee.getStatus(), PageRequest.of(1, 3))).thenReturn(traineePage);
        Page<User> trainees = traineeRepository.findAll(PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(traineeRepository).
                findAll(PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            traineeService.listTrainees(null, null,4,1, 3);
        });
    }


    @Test
    void getsPagedListOfTrainees_by_Search() {
        String search = "hari";
        User trainee = new User(1);
        trainee.setUserId(1);
        trainee.setFirstName("hari");
        trainee.setLastName("as");
        trainee.setEmail("hari@gm.co");
        trainee.setEmployeeId(123L);
        trainee.setStatus(User.Status.ACTIVE.value);
        Page<User> traineePage = new PageImpl<>(Collections.singletonList(trainee));
        when(traineeRepository.findBySearch(search, trainee.getDepartment(),PageRequest.of(1, 3))).thenReturn(traineePage);
        Page<User> trainees = traineeRepository.findBySearch(search, trainee.getDepartment(), PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(traineeRepository).
                findBySearch(search,trainee.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            traineeService.listTrainees(search, null, 4,1, 3);
        });
    }


    @Test
    void fetchFirstName() {
        List<String> firstName = Lists.newArrayList("s1", "s2", "s3");
        doReturn(List.of(firstName)).when(traineeRepository).findFirstNames();
        doThrow(BadRequestException.class).when(traineeRepository).findFirstNames();
        assertThrows(BadRequestException.class, () -> traineeService.fetchFirstName());
    }




}