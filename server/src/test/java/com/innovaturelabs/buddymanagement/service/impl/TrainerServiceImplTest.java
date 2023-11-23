package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TrainerForm;
import com.innovaturelabs.buddymanagement.repository.MentorRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeRepository;
import com.innovaturelabs.buddymanagement.repository.TrainerRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.config.SecurityConfig;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.view.UserView;
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
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class TrainerServiceImplTest {


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MentorRepository mentorRepository;

    @MockBean
    private TraineeRepository traineeRepository;

    @MockBean
    private TrainerRepository trainerRepository;

    @InjectMocks
    @Autowired
    UserServiceImpl userService;

    @InjectMocks
    @Autowired
    MentorServiceImpl mentorService;

    @InjectMocks
    @Autowired
    TrainerServiceImpl trainerService;

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
    void getsPagedListOfTrainer_EmpId_And_Search() {
        Long employeeId = 1234L;
        String search = "hari";
        User trainer = new User(1);
        trainer.setUserId(1);
        trainer.setFirstName("hari");
        trainer.setLastName("as");
        trainer.setEmail("hari@gm.co");
        trainer.setEmployeeId(employeeId);
        trainer.setStatus(User.Status.ACTIVE.value);
        Page<User> mentorPage = new PageImpl<>(Collections.singletonList(trainer));
        when(trainerRepository.findByEmployeeIdAndSearch(employeeId, search,trainer.getStatus(), PageRequest.of(1, 3))).thenReturn(mentorPage);
        Page<User> trainers = trainerRepository.findByEmployeeIdAndSearch(employeeId, search,trainer.getStatus(), PageRequest.of(1, 3));
        when(trainerRepository.findAllTrainer(trainer.getStatus(),PageRequest.of(1, 3))).thenReturn(trainers);
        doThrow(NullPointerException.class).when(trainerRepository).
                findByEmployeeIdAndSearch(employeeId, search,trainer.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            trainerService.listTrainer(search, employeeId, 4,1, 3);
        });
        verify(trainerRepository).findByEmployeeIdAndSearch(employeeId, search,trainer.getStatus(), PageRequest.of(1, 3));
    }

    @Test
    void getsPagedListOfTrainers_by_EmpId() {
        Long employeeId = 1234L;
        String search = "hari";
        User trainer = new User(1);
        trainer.setUserId(1);
        trainer.setFirstName("hari");
        trainer.setLastName("as");
        trainer.setEmail("hari@gm.co");
        trainer.setEmployeeId(employeeId);
        trainer.setStatus(User.Status.ACTIVE.value);
        Page<User> trainerPage = new PageImpl<>(Collections.singletonList(trainer));
        when(trainerRepository.findByEmployeeId(employeeId,trainer.getStatus(), PageRequest.of(1, 3))).thenReturn(trainerPage);
        Page<User> trainers = trainerRepository.findByEmployeeId(employeeId,trainer.getStatus(), PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(trainerRepository).
                findByEmployeeId(employeeId,trainer.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            trainerService.listTrainer(null, employeeId,4, 1, 3);
        });
    }

    @Test
    void getsPagedListOfTrainers_by_EmpId_exception() {
        Long employeeId = -1234L;
        if (employeeId < 0) {
            assertThrows(BadRequestException.class, () -> {
                trainerService.listTrainer(null, employeeId,  4,1, 3);
            });
        }
    }


    @Test
    void getsPagedListOfTrainers_by_FindAll() {
        User trainer = new User(1);
        trainer.setUserId(1);
        trainer.setFirstName("hari");
        trainer.setLastName("as");
        trainer.setEmail("hari@gm.co");
        trainer.setEmployeeId(123L);
        trainer.setStatus(User.Status.ACTIVE.value);
        Page<User> trainerPage = new PageImpl<>(Collections.singletonList(trainer));
        when(trainerRepository.findAllTrainer(trainer.getStatus(),PageRequest.of(1, 3))).thenReturn(trainerPage);
        Page<User> trainers = trainerRepository.findAll(PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(trainerRepository).
                findAll(PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            trainerService.listTrainer(null, null, 4, 1, 3);
        });
    }


    @Test
    void getsPagedListOfTrainers_by_Search() {
        String search = "hari";
        User trainer = new User(1);
        trainer.setUserId(1);
        trainer.setFirstName("hari");
        trainer.setLastName("as");
        trainer.setEmail("hari@gm.co");
        trainer.setEmployeeId(123L);
        trainer.setStatus(User.Status.ACTIVE.value);
        Page<User> trainerPage = new PageImpl<>(Collections.singletonList(trainer));
        when(trainerRepository.findBySearch(search,trainer.getStatus(), PageRequest.of(1, 3))).thenReturn(trainerPage);
        Page<User> trainers = trainerRepository.findBySearch(search, trainer.getStatus(),PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(trainerRepository).
                findBySearch(search,trainer.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            trainerService.listTrainer(search, null,4,1, 3);
        });
    }


    @Test
    void deleteTrainer() {
        int userId = 1;
        User user = new User();
        user.setStatus(User.Status.ACTIVE.value);
        when(userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINER.value)).thenReturn(Optional.of(user));
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndStatus(1, User.Status.ACTIVE.value);
        doReturn(user).when(userRepository).findByUserIdAndStatus(1, User.Status.ACTIVE.value);
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        assertThrows(BadRequestException.class, () -> trainerService.trainerDelete(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.TRAINER.value);
    }


    @Test
    void deleteTrainer_save() {
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        when(userRepository.findByUserIdAndUserRole(userId, User.Role.TRAINER.value)).thenReturn(Optional.of(user));
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(userId,User.Role.MENTOR.value);
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        doReturn(user).when(userRepository).save(user);
        assertThrows(BadRequestException.class, () -> mentorService.mentorDelete(1));
    }

    @Test
    void fetchTrainer() {
        int userId = 1;
        User u = new User(userId);
        u.setFirstName("hari");
        u.setLastName("as");
        u.setEmployeeId(1234L);
        u.setEmail("hari2gm.co");
        u.setPassword("Hari@123");
        UserView view = new UserView(u);
        when(userRepository.findByUserId(userId)).thenReturn(u);
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(userId, User.Role.MENTOR.value);
        assertThrows(BadRequestException.class, () -> trainerService.fetchTrainer(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.TRAINER.value);
    }

    @Test
    void fetchTrainer_error() {
        int userId = 1;
        User u = new User(userId);
        u.setFirstName("hari");
        u.setLastName("as");
        u.setEmployeeId(1234L);
        u.setEmail("hari2gm.co");
        u.setPassword("Hari@123");
        UserView view = new UserView(u);
        when(userRepository.findByUserId(userId)).thenReturn(u);
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(2, User.Role.MENTOR.value);
        assertThrows(BadRequestException.class, () -> trainerService.fetchTrainer(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.TRAINER.value);
    }


    @Test
    void trainerUpdate() {
        TrainerForm userForm = new TrainerForm();
        userForm.setFirstName("hari");
        userForm.setLastName("as");
        userForm.setEmail("hari@gm.co");
        userForm.setEmployeeId(1234L);
        userForm.setDepartment((byte) 1);
        User user = new User(1);
        LocalDateTime dt = LocalDateTime.now();
        user.setEmail("hari@gm.co");
        user.setFirstName("hari");
        user.setLastName("as");
        user.setEmployeeId(1234L);
        user.setDepartment((byte) 1);
        user.setUserRole(User.Role.TRAINER.value);
        user.setCreateDate(dt);
        user.setStatus(User.Status.ACTIVE.value);
        user.setUpdateDate(dt);
        user.setPassword(passwordEncoder.encode("Hari@123"));
        doReturn(Optional.of(user)).when(userRepository).findByUserIdAndUserRole(1, User.Role.TRAINER.value);
        trainerService.updateTrainer(1, userForm);
        doReturn(user).when(userRepository).save(user);
        UserView view = new UserView(user);
        assertEquals(view.getUserId(), trainerService.updateTrainer(1, userForm).getUserId());
    }


    @Test
    void trainer_update_email_checking() {
        TrainerForm trainerForm = new TrainerForm();
        trainerForm.setFirstName("hari");
        trainerForm.setLastName("as");
        trainerForm.setEmail("hari@gm.co");
        trainerForm.setEmployeeId(1234L);
        trainerForm.setDepartment((byte) 1);
        User trainer = new User(1);
        trainer.setEmail(trainerForm.getEmail());
        trainer.setFirstName(trainerForm.getFirstName());
        trainer.setLastName(trainerForm.getLastName());
        trainer.setEmployeeId(trainerForm.getEmployeeId());
        trainer.setDepartment(trainerForm.getDepartment());
        doReturn(Optional.of(trainer)).when(userRepository).findByEmail(trainerForm.getEmail());
        if (Optional.of(trainer).isPresent() && (!Optional.of(trainer).get().getUserId().equals(1))) {
            assertThrows(BadRequestException.class, () -> trainerService.updateTrainer(1, trainerForm));
        }
    }
    @Test
    void trainerRegister() {
        TrainerForm trainerForm = new TrainerForm();
        trainerForm.setFirstName("hari");
        trainerForm.setLastName("as");
        trainerForm.setEmail("hari@gm.co");
        trainerForm.setEmployeeId(1234L);
        trainerForm.setDepartment((byte) 1);
        User trainer = new User(1);
        trainer.setEmail(trainerForm.getEmail());
        trainer.setFirstName(trainerForm.getFirstName());
        trainer.setLastName(trainerForm.getLastName());
        trainer.setEmployeeId(trainerForm.getEmployeeId());
        trainer.setDepartment(trainerForm.getDepartment());
        doReturn(trainer).when(userRepository).save(ArgumentMatchers.any());
        UserView userView = new UserView(trainer);
        assertEquals(userView.getEmployeeId(), trainerService.trainerRegister(trainerForm).getEmployeeId());
    }

    @Test
    void trainer_register_email_checking() {
        TrainerForm trainerForm = new TrainerForm();
        trainerForm.setFirstName("hari");
        trainerForm.setLastName("as");
        trainerForm.setEmail("hari@gm.co");
        trainerForm.setEmployeeId(1234L);
        trainerForm.setDepartment((byte) 1);
        User trainer = new User(1);
        trainer.setEmail(trainerForm.getEmail());
        trainer.setFirstName(trainerForm.getFirstName());
        trainer.setLastName(trainerForm.getLastName());
        trainer.setEmployeeId(trainerForm.getEmployeeId());
        trainer.setDepartment(trainerForm.getDepartment());
        doReturn(Optional.of(trainer.getEmail())).when(userRepository).findByEmail(trainerForm.getEmail());
        assertThrows(BadRequestException.class, () -> trainerService.trainerRegister(trainerForm));
    }

    @Test
    void trainer_register_employeeId_checking() {
        TrainerForm trainerForm = new TrainerForm();
        trainerForm.setFirstName("hari");
        trainerForm.setLastName("as");
        trainerForm.setEmail("hari@gm.co");
        trainerForm.setEmployeeId(1234L);
        trainerForm.setDepartment((byte) 1);
        User trainer = new User(1);
        trainer.setEmail(trainerForm.getEmail());
        trainer.setFirstName(trainerForm.getFirstName());
        trainer.setLastName(trainerForm.getLastName());
        trainer.setEmployeeId(trainerForm.getEmployeeId());
        trainer.setDepartment(trainerForm.getDepartment());
        doReturn(Optional.of(trainer.getEmployeeId())).when(userRepository).findByEmployeeId(trainerForm.getEmployeeId());
        assertThrows(BadRequestException.class, () -> trainerService.trainerRegister(trainerForm));
    }

    @Test
    void trainer_register_department_checking_false() {
        TrainerForm trainerForm = new TrainerForm();
        trainerForm.setFirstName("hari");
        trainerForm.setLastName("as");
        trainerForm.setEmail("hari@gm.co");
        trainerForm.setEmployeeId(1234L);
        trainerForm.setDepartment((byte) 0);
        User trainer= new User(1);
        trainer.setEmail(trainerForm.getEmail());
        trainer.setFirstName(trainerForm.getFirstName());
        trainer.setLastName(trainerForm.getLastName());
        trainer.setEmployeeId(trainerForm.getEmployeeId());
        trainer.setDepartment((byte) 0);
        if (trainerForm.getDepartment() == 0) {
            assertThrows(BadRequestException.class, () -> trainerService.trainerRegister(trainerForm));
        }
    }

    @Test
    void trainer_register_department_checking_true() {
        TrainerForm trainerForm = new TrainerForm();
        trainerForm.setFirstName("hari");
        trainerForm.setLastName("as");
        trainerForm.setEmail("hari@gm.co");
        trainerForm.setEmployeeId(1234L);
        trainerForm.setDepartment((byte) 1);
        User trainer = new User(1);
        trainer.setEmail(trainerForm.getEmail());
        trainer.setFirstName(trainerForm.getFirstName());
        trainer.setLastName(trainerForm.getLastName());
        trainer.setEmployeeId(trainerForm.getEmployeeId());
        trainer.setDepartment(trainerForm.getDepartment());
        assertThrows(BadRequestException.class, () -> trainerService.updateTrainer(1, trainerForm));
    }



}