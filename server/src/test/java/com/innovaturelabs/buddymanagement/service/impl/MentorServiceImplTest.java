package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.MentorForm;
import com.innovaturelabs.buddymanagement.repository.MentorRepository;
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
import org.springframework.data.domain.Pageable;
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
class MentorServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MentorRepository mentorRepository;

    @MockBean
    private TraineeRepository traineeRepository;

    @InjectMocks
    @Autowired
    UserServiceImpl userService;

    @InjectMocks
    @Autowired
    MentorServiceImpl mentorService;

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
    void deleteMentor() {
        int userId = 1;
        User user = new User();
        user.setStatus(User.Status.ACTIVE.value);
        when(userRepository.findByUserIdAndUserRole(userId, User.Role.MENTOR.value)).thenReturn(Optional.of(user));
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndStatus(1, User.Status.ACTIVE.value);
        doReturn(user).when(userRepository).findByUserIdAndStatus(1, User.Status.ACTIVE.value);
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        assertThrows(BadRequestException.class, () -> mentorService.mentorDelete(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.MENTOR.value);
    }


    @Test
    void deleteMentor_save() {
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        when(userRepository.findByUserIdAndUserRole(userId, User.Role.MENTOR.value)).thenReturn(Optional.of(user));
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(userId,User.Role.MENTOR.value);
        user.setStatus(User.Status.INACTIVE.value);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        doReturn(user).when(userRepository).save(user);
        assertThrows(BadRequestException.class, () -> mentorService.mentorDelete(1));
    }

    @Test
    void delete_countIn() {
        int userId = 1;
        User user = new User(1);
        User mentor=new User(2);
        user.setMentorId(mentor);
        user.setStatus(User.Status.ACTIVE.value);
        if (mentorRepository.countInMentor(user.getUserId()) != 0) {
            assertThrows(BadRequestException.class, () -> mentorService.mentorDelete(userId));
        }
    }

    @Test
    void mentorRegister() {
        MentorForm mentorForm = new MentorForm();
        mentorForm.setFirstName("hari");
        mentorForm.setLastName("as");
        mentorForm.setEmail("hari@gm.co");
        mentorForm.setEmployeeId(1234L);
        mentorForm.setDepartment((byte) 1);
        User mentor = new User(1);
        mentor.setEmail(mentorForm.getEmail());
        mentor.setFirstName(mentorForm.getFirstName());
        mentor.setLastName(mentorForm.getLastName());
        mentor.setEmployeeId(mentorForm.getEmployeeId());
        mentor.setDepartment(mentorForm.getDepartment());
        doReturn(mentor).when(userRepository).save(ArgumentMatchers.any());
        UserView userView = new UserView(mentor);
        assertEquals(userView.getEmployeeId(), mentorService.mentorRegister(mentorForm).getEmployeeId());
    }

    @Test
    void mentor_register_email_checking() {
        MentorForm mentorForm = new MentorForm();
        mentorForm.setFirstName("hari");
        mentorForm.setLastName("as");
        mentorForm.setEmail("hari@gm.co");
        mentorForm.setEmployeeId(1234L);
        mentorForm.setDepartment((byte) 1);
        User mentor = new User(1);
        mentor.setEmail(mentorForm.getEmail());
        mentor.setFirstName(mentorForm.getFirstName());
        mentor.setLastName(mentorForm.getLastName());
        mentor.setEmployeeId(mentorForm.getEmployeeId());
        mentor.setDepartment(mentorForm.getDepartment());
        doReturn(Optional.of(mentor.getEmail())).when(userRepository).findByEmail(mentorForm.getEmail());
        assertThrows(BadRequestException.class, () -> mentorService.mentorRegister(mentorForm));
    }

    @Test
    void mentor_register_employeeId_checking() {
        MentorForm mentorForm = new MentorForm();
        mentorForm.setFirstName("hari");
        mentorForm.setLastName("as");
        mentorForm.setEmail("hari@gm.co");
        mentorForm.setEmployeeId(1234L);
        mentorForm.setDepartment((byte) 1);
        User mentor = new User(1);
        mentor.setEmail(mentorForm.getEmail());
        mentor.setFirstName(mentorForm.getFirstName());
        mentor.setLastName(mentorForm.getLastName());
        mentor.setEmployeeId(mentorForm.getEmployeeId());
        mentor.setDepartment(mentorForm.getDepartment());
        doReturn(Optional.of(mentor.getEmployeeId())).when(userRepository).findByEmployeeId(mentorForm.getEmployeeId());
        assertThrows(BadRequestException.class, () -> mentorService.mentorRegister(mentorForm));
    }

    @Test
    void mentor_register_department_checking_false() {
        MentorForm mentorForm = new MentorForm();
        mentorForm.setFirstName("hari");
        mentorForm.setLastName("as");
        mentorForm.setEmail("hari@gm.co");
        mentorForm.setEmployeeId(1234L);
        mentorForm.setDepartment((byte) 0);
        User mentor = new User(1);
        mentor.setEmail(mentorForm.getEmail());
        mentor.setFirstName(mentorForm.getFirstName());
        mentor.setLastName(mentorForm.getLastName());
        mentor.setEmployeeId(mentorForm.getEmployeeId());
        mentor.setDepartment((byte) 0);
        if (mentorForm.getDepartment() == 0) {
            assertThrows(BadRequestException.class, () -> mentorService.mentorRegister(mentorForm));
        }
    }

    @Test
    void mentor_register_department_checking_true() {
        MentorForm mentorForm = new MentorForm();
        mentorForm.setFirstName("hari");
        mentorForm.setLastName("as");
        mentorForm.setEmail("hari@gm.co");
        mentorForm.setEmployeeId(1234L);
        mentorForm.setDepartment((byte) 1);
        User mentor = new User(1);
        mentor.setEmail(mentorForm.getEmail());
        mentor.setFirstName(mentorForm.getFirstName());
        mentor.setLastName(mentorForm.getLastName());
        mentor.setEmployeeId(mentorForm.getEmployeeId());
        mentor.setDepartment(mentorForm.getDepartment());
        assertThrows(BadRequestException.class, () -> mentorService.updateMentor(1, mentorForm));
    }

    @Test
    void mentorUpdate() {
        MentorForm userForm = new MentorForm();
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
        user.setUserRole(User.Role.MENTOR.value);
        user.setCreateDate(dt);
        user.setStatus(User.Status.ACTIVE.value);
        user.setUpdateDate(dt);
        user.setPassword(passwordEncoder.encode("Hari@123"));
        doReturn(Optional.of(user)).when(userRepository).findByUserIdAndUserRole(1, User.Role.MENTOR.value);
        mentorService.updateMentor(1, userForm);
        doReturn(user).when(userRepository).save(user);
        UserView view = new UserView(user);
        assertEquals(view.getUserId(), mentorService.updateMentor(1, userForm).getUserId());
    }


    @Test
    void mentor_update_email_checking() {
        MentorForm mentorForm = new MentorForm();
        mentorForm.setFirstName("hari");
        mentorForm.setLastName("as");
        mentorForm.setEmail("hari@gm.co");
        mentorForm.setEmployeeId(1234L);
        mentorForm.setDepartment((byte) 1);
        User mentor = new User(1);
        mentor.setEmail(mentorForm.getEmail());
        mentor.setFirstName(mentorForm.getFirstName());
        mentor.setLastName(mentorForm.getLastName());
        mentor.setEmployeeId(mentorForm.getEmployeeId());
        mentor.setDepartment(mentorForm.getDepartment());
        doReturn(Optional.of(mentor)).when(userRepository).findByEmail(mentorForm.getEmail());
        if (!Optional.of(mentor).get().getUserId().equals(1)) {
            assertThrows(BadRequestException.class, () -> mentorService.updateMentor(1, mentorForm));
        }
    }

    @Test
    void fetchMentor() {
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
        assertThrows(BadRequestException.class, () -> mentorService.fetchMentor(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.MENTOR.value);
    }

    @Test
    void fetchMentor_error() {
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
        assertThrows(BadRequestException.class, () -> mentorService.fetchMentor(userId));
        verify(userRepository).findByUserIdAndUserRole(userId, User.Role.MENTOR.value);
    }


    @Test
    void getsPagedListOfMentors_EmpId_And_Search() {
        Long employeeId = 1234L;
        String search = "hari";
        User mentor = new User(1);
        mentor.setUserId(1);
        mentor.setFirstName("hari");
        mentor.setLastName("as");
        mentor.setEmail("hari@gm.co");
        mentor.setEmployeeId(employeeId);
        mentor.setStatus(User.Status.ACTIVE.value);
        Page<User> mentorPage = new PageImpl<>(Collections.singletonList(mentor));
        when(mentorRepository.findByEmployeeIdAndSearch(employeeId, search,mentor.getStatus(), PageRequest.of(1, 3))).thenReturn(mentorPage);
        Page<User> mentors = mentorRepository.findByEmployeeIdAndSearch(employeeId, search,mentor.getStatus(), PageRequest.of(1, 3));
        when(mentorRepository.findAllMentor(mentor.getStatus(),PageRequest.of(1, 3))).thenReturn(mentors);
        doThrow(NullPointerException.class).when(mentorRepository).
                findByEmployeeIdAndSearch(employeeId, search,mentor.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            mentorService.listMentor(search, employeeId, 4,1, 3);
        });
        verify(mentorRepository).findByEmployeeIdAndSearch(employeeId, search, mentor.getStatus(),PageRequest.of(1, 3));
    }

    @Test
    void getsPagedListOfMentors_by_EmpId() {
        Long employeeId = 1234L;
        String search = "hari";
        User mentor = new User(1);
        mentor.setUserId(1);
        mentor.setFirstName("hari");
        mentor.setLastName("as");
        mentor.setEmail("hari@gm.co");
        mentor.setEmployeeId(employeeId);
        mentor.setStatus(User.Status.ACTIVE.value);
        Page<User> mentorPage = new PageImpl<>(Collections.singletonList(mentor));
        when(mentorRepository.findByEmployeeId(employeeId,mentor.getStatus(), PageRequest.of(1, 3))).thenReturn(mentorPage);
        Page<User> mentors = mentorRepository.findByEmployeeId(employeeId,mentor.getStatus(), PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(mentorRepository).
                findByEmployeeId(employeeId,mentor.getStatus(), PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            mentorService.listMentor(null, employeeId,  4,1, 3);
        });
    }

    @Test
    void getsPagedListOfMentors_by_EmpId_exception() {
        long employeeId = -1234L;
        if (employeeId < 0) {
            assertThrows(BadRequestException.class, () -> {
                mentorService.listMentor(null, employeeId, 4,1, 3);
            });
        }
    }


    @Test
    void getsPagedListOfMentors_by_FindAll() {
        User mentor = new User(1);
        mentor.setUserId(1);
        mentor.setFirstName("hari");
        mentor.setLastName("as");
        mentor.setEmail("hari@gm.co");
        mentor.setEmployeeId(123L);
        mentor.setStatus(User.Status.ACTIVE.value);
        Page<User> mentorPage = new PageImpl<>(Collections.singletonList(mentor));
        when(mentorRepository.findAllMentor(mentor.getStatus(),PageRequest.of(1, 3))).thenReturn(mentorPage);
        Page<User> mentors = mentorRepository.findAll(PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(mentorRepository).
                findAll(PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            mentorService.listMentor(null, null, 4,1, 3);
        });
    }


    @Test
    void getsPagedListOfMentors_by_Search() {
        String search = "hari";
        User mentor = new User(1);
        mentor.setUserId(1);
        mentor.setFirstName("hari");
        mentor.setLastName("as");
        mentor.setEmail("hari@gm.co");
        mentor.setEmployeeId(123L);
        mentor.setStatus(User.Status.ACTIVE.value);
        Page<User> mentorPage = new PageImpl<>(Collections.singletonList(mentor));
        when(mentorRepository.findBySearch(search,mentor.getStatus(), PageRequest.of(1, 3))).thenReturn(mentorPage);
        Page<User> mentors = mentorRepository.findBySearch(search,mentor.getStatus(), PageRequest.of(1, 3));
        doThrow(NullPointerException.class).when(mentorRepository).
                findBySearch(search, mentor.getStatus(),PageRequest.of(1, 3));
        assertThrows(NullPointerException.class, () -> {
            mentorService.listMentor(search, null,  4,1, 3);
        });
    }


    @Test
    void fetchFirstName() {
        List<String> firstName = Lists.newArrayList("s1", "s2", "s3");
        doReturn(List.of(firstName)).when(mentorRepository).findByFirstName();
        doThrow(BadRequestException.class).when(mentorRepository).findByFirstName();
        assertThrows(BadRequestException.class, () -> mentorService.fetchFirstName());
    }

    // @Test
    // void listMentorResourcesMentorNotPresent() {
    //     User mentor = new User(1);
    //     mentor.setEmail("hari@gm.co");
    //     mentor.setFirstName("hari");
    //     mentor.setLastName("as");
    //     mentor.setEmployeeId(1234L);
    //     mentor.setDepartment((byte) 1);
    //     doReturn(mentor).when(userRepository).save(ArgumentMatchers.any());
    //     assertThrows(BadRequestException.class, () -> mentorService.listMentorResources(2));
    // }


    // @Test
    // void listManagerResources() {
    //     List<User> userList = new ArrayList<>();
    //     User mentor = new User(1);
    //     mentor.setEmail("hari@gm.co");
    //     mentor.setFirstName("hari");
    //     mentor.setLastName("as");
    //     mentor.setEmployeeId(1234L);
    //     mentor.setDepartment((byte) 1);
    //     mentor.setUserRole((byte) 2);
    //     doReturn(mentor).when(userRepository).save(ArgumentMatchers.any());
    //     User trainee = new User(2);
    //     trainee.setEmail("haris@gm.co");
    //     trainee.setFirstName("hari");
    //     trainee.setLastName("as");
    //     trainee.setEmployeeId(12345L);
    //     trainee.setDepartment((byte) 1);
    //     userList.add(trainee);
    //     when(userRepository.findByUserIdAndUserRoleAndStatus(1, (byte) 2, (byte) 1)).thenReturn(Optional.of(mentor));
    //     when(traineeRepository.findMentorResourcesTrainees(1)).thenReturn(userList);
    //     assertEquals(userList, mentorService.listMentorResources(1));
    // }


    // @Test
    // void listMentorResourcesMentorIdNull() {
    //     List<User> userList = new ArrayList<>();
    //     User trainee = new User(2);
    //     trainee.setEmail("haris@gm.co");
    //     trainee.setFirstName("hari");
    //     trainee.setLastName("as");
    //     trainee.setEmployeeId(12345L);
    //     trainee.setDepartment((byte) 1);
    //     userList.add(trainee);
    //     when(traineeRepository.findUnassignedMentorResourcesTrainees()).thenReturn(userList);
    //     assertEquals(userList, mentorService.listMentorResources(null));
    // }

    @Test
    void userListErrors() {
        List<Integer> notFound = new ArrayList<>();
        List<Integer> alreadyDeallocated = new ArrayList<>();
        List<Integer> alreadyAllocated = new ArrayList<>();
        List<Integer> traineesWithoutMentor = new ArrayList<>();
        notFound.add(1);
        alreadyAllocated.add(1);
        alreadyDeallocated.add(1);
        traineesWithoutMentor.add(1);
        assertThrows(BadRequestException.class, () -> mentorService.userListErrors(notFound, alreadyDeallocated, alreadyAllocated, traineesWithoutMentor));
    }

    @Test
    void allocationBasicChecksEmptyList() {
        List<Integer> users = new ArrayList<>();
        assertThrows(BadRequestException.class, () -> mentorService.allocationBasicChecks(users, (byte) 1, 1));
    }

    @Test
    void allocationBasicChecksInvalidAllocationMode() {
        assertThrows(BadRequestException.class, () -> mentorService.allocationBasicChecks(null, (byte) 3, 1));
    }

    @Test
    void allocationBasicChecksMentorIdNull() {
        assertThrows(BadRequestException.class, () -> mentorService.allocationBasicChecks(null, (byte) 1, null));
    }


    @Test
    void allocatorUpdateSkipped() {
        List<Integer> alreadyAllocated = new ArrayList<>();
        List<Integer> traineesWithoutMentor = new ArrayList<>();
        User user = new User(1);
        User mentor = new User(2);
        User manager = new User(3);
        user.setMentorId(mentor);
        user.setManagerId(manager);
        assertTrue(mentorService.allocator(user, null, alreadyAllocated, traineesWithoutMentor));
    }

    @Test
    void allocatorUpdateNotSkipped() {
        User user = new User(1);
        User mentor = new User(3);
        List<Integer> traineesWithoutMentor = new ArrayList<>();
        assertTrue(mentorService.allocator(user, mentor, null, traineesWithoutMentor));
    }

    @Test
    void deAllocatorAlreadyDeallocated() {
        List<Integer> alreadyDeallocated = new ArrayList<>();
        User user = new User(1);
        assertTrue(mentorService.deAllocator(user, alreadyDeallocated, null, null));
    }

    @Test
    void deAllocatorMentorIdNotMatching() {
        List<Integer> notFound = new ArrayList<>();
        User user = new User(1);
        User mentor = new User(2);
        user.setMentorId(mentor);
        assertTrue(mentorService.deAllocator(user, null, notFound, 3));
    }

    @Test
    void deAllocatorUpdateNotSkipped() {
        User user = new User(1);
        User mentor = new User(2);
        user.setMentorId(mentor);
        assertFalse(mentorService.deAllocator(user, null, null, 2));
    }

    @Test
    void allocateManagerControlsManagerNotFound() {
        List<Integer> users = new ArrayList<>();
        User user1 = new User(1);
        User user2 = new User(2);
        users.add(1);
        users.add(2);
        when(userRepository.findUsers(1, (byte) 4)).thenReturn(Optional.of(user1));
        when(userRepository.findUsers(2, (byte) 4)).thenReturn(Optional.of(user2));
        assertThrows(BadRequestException.class, () -> mentorService.allocateMentorControls(users, (byte) 1, 3));

    }

}