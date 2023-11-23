package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.GroupAllocationForm;
import com.innovaturelabs.buddymanagement.form.JoinerGroupForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.JoinerGroupView;

import java.util.List;

public interface JoinerGroupService {

    Pager<JoinerGroupView> listGroup(String groupSearch, Integer joinerBatchId, Integer page, Integer limit);

    void groupDelete(Integer joinerGroupId) throws BadRequestException;

    JoinerGroupView joinerGroupCreate(JoinerGroupForm form);

    JoinerGroupView updateGroup(Integer joinerGroupId, JoinerGroupForm form);


    JoinerGroupView fetchGroup(Integer joinerGroupId);

    void joinerGroupAllocate(GroupAllocationForm form, Integer joinerGroupId, byte allocationMode);

    boolean allocator(User user, JoinerGroup joinerGroup, List<Integer> alreadyAllocated, Integer u);

    boolean deAllocator(User user, List<Integer> alreadyDeallocated, Integer u);

    void userListErrors(List<Integer> notFoundUsers, List<Integer> alreadyDeallocated, List<Integer> alreadyAllocated);

    void allocationBasicChecks(List<Integer> users, Integer joinerGroupId, byte allocationMode);

    Pager<User> listGroupResources(Integer joinerGroupId,String search,Integer page,Integer limit);
}
