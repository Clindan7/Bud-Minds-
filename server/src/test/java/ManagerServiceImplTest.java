import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.ManagerForm;
import com.innovaturelabs.buddymanagement.repository.ManagerRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.config.SecurityConfig;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator;
import com.innovaturelabs.buddymanagement.service.impl.ManagerServiceImpl;
import com.innovaturelabs.buddymanagement.service.impl.UserServiceImpl;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserListView;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ManagerServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ManagerRepository managerRepository;

    @MockBean
    private TraineeRepository traineeRepository;

    @InjectMocks
    @Autowired
    UserServiceImpl userService;

    @InjectMocks
    @Autowired
    ManagerServiceImpl managerService;

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

    @MockBean
    private ManagerForm managerForm;

    @Test
    void managerRegister() {
        ManagerForm managerForm = new ManagerForm();
        managerForm.setFirstName("hari");
        managerForm.setLastName("as");
        managerForm.setEmail("hari@gm.co");
        managerForm.setEmployeeId(1234L);
        managerForm.setDepartment((byte) 1);
        User mentor = new User(1);
        mentor.setEmail("hari@gm.co");
        mentor.setFirstName("hari");
        mentor.setLastName("as");
        mentor.setEmployeeId(1234L);
        mentor.setDepartment((byte) 1);
        doReturn(mentor).when(userRepository).save(ArgumentMatchers.any());
        UserView userView = new UserView(mentor);
        assertEquals(userView.getEmployeeId(), managerService.managerRegister(managerForm).getEmployeeId());
    }
    @Test
    void managerRegisterEmpId() {
        managerForm.setFirstName("hari");
        managerForm.setLastName("as");
        managerForm.setEmail("hari@gm.co");
        managerForm.setEmployeeId(1234L);
        managerForm.setDepartment((byte) 0);
        when(managerForm.getDepartment()).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> managerService.managerRegister(managerForm));
    }

    @Test
    void managerRegisterDepartmentRequired() {
        managerForm.setFirstName("hari");
        managerForm.setLastName("as");
        managerForm.setEmail("hari@gm.co");
        managerForm.setEmployeeId(1234L);
        managerForm.setDepartment((byte) 0);
        doThrow(BadRequestException.class).when(managerForm).getDepartment();
        assertThrows(BadRequestException.class, () -> managerService.managerRegister(managerForm));
    }

    @Test
    void fetchManager() {
        int userId = 1;
        User u = new User(userId);
        u.setFirstName("hari");
        u.setLastName("as");
        u.setEmployeeId(1234L);
        u.setEmail("hari2gm.co");
        u.setPassword("Hari@123");
        UserView view = new UserView(u);
        when(userRepository.findByUserId(userId)).thenReturn(u);
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(userId, User.Role.MANAGER.value);
        assertThrows(BadRequestException.class, () -> managerService.fetchManager(userId));
        verify(userRepository,times(1)).findByUserIdAndUserRole(userId,User.Role.MANAGER.value);
    }

    @Test
    void fetchManagerFailed() {
        int userId = 1;
        User u = new User(userId);
        u.setFirstName("hari");
        u.setLastName("as");
        u.setEmployeeId(1234L);
        u.setEmail("hari2gm.co");
        u.setPassword("Hari@123");
        UserView view = new UserView(u);
        when(userRepository.findByUserId(2)).thenReturn(u);
        doThrow(BadRequestException.class).when(userRepository).findByUserIdAndUserRole(2, User.Role.MANAGER.value);
        assertThrows(BadRequestException.class, () -> managerService.fetchManager(userId));
    }

    // @Test
    // void listManagerResourcesInvalidUserRole(){
    //     User manager = new User(1);
    //     manager.setEmail("hari@gm.co");
    //     manager.setFirstName("hari");
    //     manager.setLastName("as");
    //     manager.setEmployeeId(1234L);
    //     manager.setDepartment((byte) 1);
    //     doReturn(manager).when(userRepository).save(ArgumentMatchers.any());
    //     assertThrows(BadRequestException.class, () -> managerService.listManagerResources(1,(byte) 1,));
    // }

    // @Test
    // void listManagerResourcesManagerNotPresent(){
    //     User manager = new User(1);
    //     manager.setEmail("hari@gm.co");
    //     manager.setFirstName("hari");
    //     manager.setLastName("as");
    //     manager.setEmployeeId(1234L);
    //     manager.setDepartment((byte) 1);
    //     doReturn(manager).when(userRepository).save(ArgumentMatchers.any());
    //     assertThrows(BadRequestException.class, () -> managerService.listManagerResources(2,(byte) 2));
    // }

    // @Test
    // void listManagerResources(){
    //     List<User> userList=new ArrayList<>();
    //     User manager = new User(1);
    //     manager.setEmail("hari@gm.co");
    //     manager.setFirstName("hari");
    //     manager.setLastName("as");
    //     manager.setEmployeeId(1234L);
    //     manager.setDepartment((byte) 1);
    //     manager.setUserRole((byte) 1);
    //     doReturn(manager).when(userRepository).save(ArgumentMatchers.any());
    //     User trainee = new User(2);
    //     trainee.setEmail("haris@gm.co");
    //     trainee.setFirstName("hari");
    //     trainee.setLastName("as");
    //     trainee.setEmployeeId(12345L);
    //     trainee.setDepartment((byte) 1);
    //     userList.add(trainee);
    //     when(userRepository.findByUserIdAndUserRoleAndStatus(1,(byte) 1,(byte) 1)).thenReturn(Optional.of(manager));
    //     when(traineeRepository.findManagerResourcesTrainees(1)).thenReturn(userList);
    //     assertEquals(userList, managerService.listManagerResources(1,(byte) 4));
    // }

    // @Test
    // void listManagerResourcesManagerIdNull(){
    //     List<User> userList=new ArrayList<>();
    //     User trainee = new User(2);
    //     trainee.setEmail("haris@gm.co");
    //     trainee.setFirstName("hari");
    //     trainee.setLastName("as");
    //     trainee.setEmployeeId(12345L);
    //     trainee.setDepartment((byte) 1);
    //     userList.add(trainee);
    //     when(traineeRepository.findUnassignedManagerResourcesTrainees()).thenReturn(userList);
    //     assertEquals(userList, managerService.listManagerResources(null,(byte) 4));
    // }
    @Test
    void userListErrors(){
        List<Integer> notFound=new ArrayList<>();
        List<Integer> alreadyDeallocated=new ArrayList<>();
        List<Integer> alreadyAllocated=new ArrayList<>();
        notFound.add(1);
        alreadyAllocated.add(1);
        alreadyDeallocated.add(1);
        assertThrows(BadRequestException.class, () -> managerService.userListErrors(notFound,alreadyDeallocated,alreadyAllocated));
    }

    @Test
    void allocationBasicChecksEmptyList(){
        List<Integer> users=new ArrayList<>();
        assertThrows(BadRequestException.class, () -> managerService.allocationBasicChecks(users,(byte) 2,(byte) 1,1));
    }

    @Test
    void allocationBasicChecksInvalidAllocationMode(){
        assertThrows(BadRequestException.class, () -> managerService.allocationBasicChecks(null,(byte) 2,(byte) 3,1));
    }

    @Test
    void allocationBasicChecksManagerIdNull(){
        assertThrows(BadRequestException.class, () -> managerService.allocationBasicChecks(null,(byte) 2,(byte) 1,null));
    }

    @Test
    void allocationBasicChecksInvalidUserRole(){
        List<Integer> users=new ArrayList<>();
        users.add(1);
        assertThrows(BadRequestException.class, () -> managerService.allocationBasicChecks(users,(byte) 0,(byte) 1,1));
    }

    @Test
    void allocatorUpdateSkipped(){
        List<Integer> alreadyAllocated=new ArrayList<>();
        User user=new User(1);
        User manager=new User(2);
        user.setManagerId(manager);
        assertEquals(true, managerService.allocator(user,null,alreadyAllocated));
    }

    @Test
    void allocatorUpdateNotSkipped(){
        User user=new User(1);
        User manager=new User(2);
        assertEquals(false, managerService.allocator(user,manager,null));
    }

    @Test
    void deAllocatorAlreadyDeallocated(){
        List<Integer> alreadyDeallocated=new ArrayList<>();
        User user=new User(1);
        assertEquals(true, managerService.deAllocator(user,alreadyDeallocated,null,null));
    }
    @Test
    void deAllocatorManagerIdNotMatching(){
        List<Integer> notFound=new ArrayList<>();
        User user=new User(1);
        User manager=new User(2);
        user.setManagerId(manager);
        assertEquals(true, managerService.deAllocator(user,null,notFound,3));
    }
    @Test
    void deAllocatorUpdateNotSkipped(){
        User user=new User(1);
        User manager=new User(2);
        user.setManagerId(manager);
        assertEquals(false, managerService.deAllocator(user,null,null,2));
    }

    @Test
    void allocateManagerControlsManagerNotFound(){
        List<Integer> users=new ArrayList<>();
        User user1=new User(1);
        User user2=new User(2);
        users.add(1);
        users.add(2);
        when(userRepository.findUsers(1,(byte) 4)).thenReturn(Optional.of(user1));
        when(userRepository.findUsers(2,(byte) 4)).thenReturn(Optional.of(user2));
        assertThrows(BadRequestException.class, () -> managerService.allocateManagerControls(users,(byte) 4,(byte) 1,3));

    }

    @Test
    void getsPagedMentorsList() {
        int pageNumber = 1;
        int pageSize = 3;
        Long employeeId=1234L;
        String search="hari";
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User mentor = new User(1);
        mentor.setUserId(1);
        mentor.setFirstName("hari");
        mentor.setLastName("as");
        mentor.setEmail("hari@gm.co");
        mentor.setEmployeeId(employeeId);
        mentor.setStatus(User.Status.ACTIVE.value);
        Page<User> travellerPage = new PageImpl<>(Collections.singletonList(mentor));
        when(managerRepository.findAllManager(mentor.getStatus(), PageRequest.of(1,3))).thenReturn(travellerPage);
        Page<User> travellers =managerRepository.findAllManager(mentor.getStatus(), PageRequest.of(1,3));
        when(managerRepository.findAllManager(mentor.getStatus(), PageRequest.of(1,3))).thenReturn(travellers);
        doThrow(NullPointerException.class).when(managerRepository).findByEmployeeIdAndSearch(employeeId,search, mentor.getStatus() ,PageRequest.of(1,3));
        doThrow(NullPointerException.class).when(managerRepository).findBySearch(search, mentor.getStatus(), PageRequest.of(1,3));
        doThrow(NullPointerException.class).when(managerRepository).findByEmployeeId(employeeId, mentor.getStatus(), PageRequest.of(1,3));
        assertThrows(NullPointerException.class,()->managerService.listManager(search,employeeId, 1, 1,3));
        verify(managerRepository).findAllManager(mentor.getStatus(),PageRequest.of(1,3));
    }
}
