package com.innovaturelabs.buddymanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.form.ManagerForm;
import com.innovaturelabs.buddymanagement.service.ManagerService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    @Test
    void testListManager() {
        String search = "test";
        Long employeeId = 1L;
        Integer page = 1;
        Integer limit = 10;
        Pager<UserListView> expectedPager = new Pager<>(1, 3, 4);
        when(managerService.listManager(search, employeeId,1, page, limit))
                .thenReturn(expectedPager);
        Pager<UserListView> actualPager = managerController.listManager(search, employeeId,1, page, limit);
        verify(managerService).listManager(search, employeeId, 1 ,page, limit);
        assertEquals(expectedPager.getCurrentPage(), actualPager.getCurrentPage());
        assertEquals(expectedPager.getNumItems(), actualPager.getNumItems());
        assertEquals(expectedPager.getDisplayCount(), actualPager.getDisplayCount());
    }

    @Test
    void testManagerDelete() {
        Integer userId = 1;
        managerController.managerDelete(userId);
        verify(managerService).managerDelete(userId);
    }
    @Test
    void testFetchManager() {
        ManagerService managerService = Mockito.mock(ManagerService.class);
        User manager = new User(12);
        manager.setUserId(12);
        manager.setFirstName("hari");
        manager.setLastName("as");
        manager.setEmail("hari@gm.co");
        UserView expectedUserView = new UserView(manager); Mockito.when(managerService.fetchManager(123)).thenReturn(expectedUserView);// Create a new instance of the ManagerController and inject the mock service
        ManagerController managerController = new ManagerController();
        managerController.managerService = managerService;
        UserView actualUserView = managerController.fetchManager(123);
        assertEquals(expectedUserView, actualUserView);
        Mockito.verify(managerService, Mockito.times(1)).fetchManager(123);
    }
    @Test
    void testUpdateManager() {
        // Create a ManagerForm object to use as the request body for the updateManager method
        ManagerForm form = new ManagerForm();
        form.setFirstName("John");
        form.setLastName("Doe");
        form.setEmail("johndoe@example.com");
        User manager = new User(12);
        manager.setUserId(12);
        manager.setFirstName("hari");
        manager.setLastName("as");
        manager.setEmail("hari@gm.co");

        // Create a UserView object to be returned by the mock service
        UserView expectedUserView = new UserView(manager);

        // Stub the updateManager method of the mock service to return the expected UserView
        Mockito.when(managerService.updateManager(123, form)).thenReturn(expectedUserView);

        // Call the updateManager method of the controller with the mocked userId and request body
        UserView actualUserView = managerController.updateManager(123, form);

        // Assert that the returned UserView is equal to the expected one
        assertEquals(expectedUserView, actualUserView);

        // Verify that the updateManager method of the mock service was called exactly once with the correct arguments
        Mockito.verify(managerService, Mockito.times(1)).updateManager(123, form);
    }
    @Test
    void testRegisterManager() {
        // Create a ManagerForm object to use as the request body for the updateManager method
        ManagerForm form = new ManagerForm();
        form.setFirstName("John");
        form.setLastName("Doe");
        form.setEmail("johndoe@example.com");
        User manager = new User(12);
        manager.setUserId(12);
        manager.setFirstName("hari");
        manager.setLastName("as");
        manager.setEmail("hari@gm.co");

        // Create a UserView object to be returned by the mock service
        UserView expectedUserView = new UserView(manager);

        // Stub the updateManager method of the mock service to return the expected UserView
        Mockito.when(managerService.managerRegister( form)).thenReturn(expectedUserView);

        // Call the updateManager method of the controller with the mocked userId and request body
        UserView actualUserView = managerController.managerRegister(form);

        // Assert that the returned UserView is equal to the expected one
        assertEquals(expectedUserView, actualUserView);

        // Verify that the updateManager method of the mock service was called exactly once with the correct arguments
        Mockito.verify(managerService, Mockito.times(1)).managerRegister(form);
    }

}
