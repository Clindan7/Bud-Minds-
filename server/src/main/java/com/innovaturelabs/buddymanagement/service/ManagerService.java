package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.ManagerForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserView;
import com.innovaturelabs.buddymanagement.view.UserListView;

import java.util.List;


public interface ManagerService {

    Pager<UserListView> listManager(String search, Long employeeId, Integer status, Integer page, Integer limit);

    void managerDelete(Integer userId) throws BadRequestException;

    UserView fetchManager(Integer userId);

    UserView managerRegister(ManagerForm form);

    UserView updateManager(Integer userId, ManagerForm form);

    List<String> fetchFirstName();

    void allocateManagerControls(List<Integer> users, byte userRole, byte allocationMode, Integer managerId);

    boolean deAllocator(User user, List<Integer> alreadyDeallocated, List<Integer> notFound, Integer managerId);

    boolean allocator(User user, User manager, List<Integer> alreadyAllocated);

    void allocationBasicChecks(List<Integer> users, byte userRole, byte allocationMode, Integer managerId);

    void userListErrors(List<Integer> notFound, List<Integer> alreadyDeallocated, List<Integer> alreadyAllocated);

    Pager<User> listManagerResources(Integer managerId, byte userRole, String name, Integer page, Integer limit);
}
