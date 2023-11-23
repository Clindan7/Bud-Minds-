package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.MentorForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.UserListView;
import com.innovaturelabs.buddymanagement.view.UserView;

import java.util.List;


public interface MentorService {

    Pager<UserListView> listMentor(String search, Long employeeId, Integer status,Integer page, Integer limit);
    void mentorDelete(Integer userId) throws BadRequestException;

    UserView fetchMentor(Integer userId);

    UserView mentorRegister(MentorForm form);

    UserView updateMentor(Integer userId, MentorForm form);

    List<String> fetchFirstName();

    Pager<User> listMentorResources(Integer managerId, String name, Integer page, Integer limit);

    void allocateMentorControls(List<Integer> trainees,byte allocationMode, Integer mentorId);

    boolean deAllocator(User trainee, List<Integer> alreadyDeallocated,List<Integer> notFound,Integer mentorId);

    boolean allocator(User trainee,User mentor,List<Integer> alreadyAllocated,List<Integer> traineesWithoutManager);

    void allocationBasicChecks(List<Integer> trainees,byte allocationMode,Integer managerId);

    void userListErrors(List<Integer> notFound,List<Integer> alreadyDeallocated,List<Integer> alreadyAllocated,List<Integer> traineesWithoutManager);

}
