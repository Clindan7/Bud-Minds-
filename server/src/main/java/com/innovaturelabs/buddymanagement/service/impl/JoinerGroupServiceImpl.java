package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.JoinerBatch;
import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.GroupAllocationForm;
import com.innovaturelabs.buddymanagement.form.JoinerGroupForm;
import com.innovaturelabs.buddymanagement.repository.JoinerBatchRepository;
import com.innovaturelabs.buddymanagement.repository.JoinerGroupRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.service.JoinerGroupService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.JoinerGroupView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class JoinerGroupServiceImpl implements JoinerGroupService {

    Logger log = LoggerFactory.getLogger(JoinerGroupServiceImpl.class);

    private static final String GROUP_NOT_FOUND = "group.not.found";
    private static final String BATCH_NOT_FOUND = "batch.not.found";

    @Autowired
    private JoinerGroupRepository joinerGroupRepository;

    @Autowired
    private JoinerBatchRepository joinerBatchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageUtil languageUtil;

    @Override
    public Pager<JoinerGroupView> listGroup(String groupSearch, Integer joinerBatchId, Integer page, Integer limit) {
        Pager<JoinerGroupView> groupPager;
        List<JoinerGroupView> groupList;
        if (joinerBatchId != null && groupSearch != null) {
            groupList = StreamSupport.stream(joinerGroupRepository
                            .findByJoinerBatchIdAndGroupSearch(joinerBatchId, groupSearch, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(JoinerGroupView::new)
                    .collect(Collectors.toList());
            groupPager = new Pager<>(limit, groupList.size(), page);
            groupPager.setResult(groupList);
            if (!groupList.isEmpty()) {
                return groupPager;
            } else {
                log.error(GROUP_NOT_FOUND);
                throw new BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en"));
            }
        }
        if (joinerBatchId != null) {
            JoinerBatch batchId = joinerBatchRepository.findByJoinerBatchId(joinerBatchId).orElseThrow(() -> new
                    BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en")));
            groupList = StreamSupport.stream(joinerGroupRepository
                            .findByJoinerBatch(batchId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(JoinerGroupView::new)
                    .collect(Collectors.toList());
            groupPager = new Pager<>(limit, groupList.size(), page);
            groupPager.setResult(groupList);
            if (!groupList.isEmpty()) {
                return groupPager;
            } else {
                log.error("batch not found");
                throw new BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en"));
            }
        }
        if (groupSearch != null) {
            groupList = StreamSupport.stream(joinerGroupRepository
                            .findByGroupSearch(groupSearch, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(JoinerGroupView::new)
                    .collect(Collectors.toList());
            groupPager = new Pager<>(limit, groupList.size(), page);
            groupPager.setResult(groupList);
            if (!groupList.isEmpty()) {
                return groupPager;
            } else {
                log.error(GROUP_NOT_FOUND);
                throw new BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en"));
            }
        }
        groupList = StreamSupport.stream(joinerGroupRepository
                        .findAllGroup(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(JoinerGroupView::new)
                .collect(Collectors.toList());
        groupPager = new Pager<>(limit, joinerGroupRepository.findAllByStatusOrderByCreateDateDesc(
                JoinerGroup.Status.ACTIVE.value).size(), page);
        groupPager.setResult(groupList);
        return groupPager;
    }

    @Override
    public void groupDelete(Integer joinerGroupId) throws BadRequestException {
        Integer groupId = userRepository.findByGroupActive(joinerGroupId);
        JoinerGroup joinerGroup = joinerGroupRepository.findByJoinerGroupId(joinerGroupId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en")));
        if (joinerGroup.getStatus() == JoinerGroup.Status.INACTIVE.value) {
            log.error("group already deleted");
            throw new BadRequestException(languageUtil.getTranslatedText("group.already.deleted", null, "en"));
        }
        if (groupId == 0) {
            joinerGroup.setStatus(JoinerBatch.Status.INACTIVE.value);
            joinerGroup.setUpdateDate(LocalDateTime.now());
            joinerGroupRepository.save(joinerGroup);
        } else {
            log.error("group is empty");
            throw new BadRequestException(languageUtil.getTranslatedText("group.not.empty", null, "en"));
        }
    }

    @Override
    public JoinerGroupView joinerGroupCreate(JoinerGroupForm form) {
        JoinerBatch batch = joinerBatchRepository.findByJoinerBatchIdAndStatus(form.getJoinerBatchId(),JoinerBatch.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en")));
        if (joinerGroupRepository.findByJoinerGroupNameAndJoinerBatchJoinerBatchIdAndStatus(form.getJoinerGroupName(),batch.getJoinerBatchId(),
                JoinerGroup.Status.ACTIVE.value).isPresent()) {
            throw new BadRequestException(languageUtil.getTranslatedText("groupName.already.exists", null, "en"));
        }
        JoinerGroup joinerGroup=new JoinerGroup(form.getJoinerGroupName(), batch);
        joinerGroupRepository.save(joinerGroup);
        return new JoinerGroupView(joinerGroup);
    }

    @Override
    public JoinerGroupView updateGroup(Integer joinerGroupId, JoinerGroupForm form) {
        JoinerBatch batch = joinerBatchRepository.findByJoinerBatchIdAndStatus(form.getJoinerBatchId(),JoinerBatch.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en")));
        Optional<JoinerGroup> optionalGroup=joinerGroupRepository.findByJoinerGroupNameAndJoinerBatchJoinerBatchIdAndStatus(form.getJoinerGroupName(),batch.getJoinerBatchId(),
                JoinerGroup.Status.ACTIVE.value);
        if ((optionalGroup.isPresent()) && (!optionalGroup.get().getJoinerGroupId().equals(joinerGroupId))) {
            throw new BadRequestException(languageUtil.getTranslatedText("groupName.already.exists", null, "en"));
        }
        Optional<JoinerBatch> optionalJoinerBatch=joinerBatchRepository.findByJoinerBatchId(form.getJoinerBatchId());
        if(!optionalJoinerBatch.isPresent())
        {
            throw new BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en"));
        }
        JoinerGroup joinerGroup = joinerGroupRepository.findByJoinerGroupIdAndStatus(joinerGroupId, JoinerGroup.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en")));
        joinerGroup.setJoinerGroupName(form.getJoinerGroupName());
        joinerGroup.setJoinerBatch(optionalJoinerBatch.get());
        joinerGroup.setUpdateDate(LocalDateTime.now());
        joinerGroupRepository.save(joinerGroup);
        return new JoinerGroupView(joinerGroup);
    }

    @Override
    public JoinerGroupView fetchGroup(Integer joinerGroupId) {
        return new JoinerGroupView(joinerGroupRepository.findByJoinerGroupIdAndStatus(joinerGroupId, JoinerGroup.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en"))));
    }

    @Override
    public void joinerGroupAllocate(GroupAllocationForm form, Integer joinerGroupId, byte allocationMode) {
        List<Integer> notFoundUsers = new ArrayList<>();
        List<Integer> alreadyAllocated = new ArrayList<>();
        List<Integer> alreadyDeallocated = new ArrayList<>();
        boolean skipUpdate;
        List<Integer> users=form.getUsers();
        users = users.stream()
                .distinct()
                .collect(Collectors.toList());              //Remove duplicates from user list
        users.removeAll(Collections.singletonList(null));   //Remove null values from user list
        allocationBasicChecks(users, joinerGroupId, allocationMode);
        for (Integer u : users) {
            Optional<User> optionalUser = userRepository.findUsers(u,User.Role.TRAINEE.value);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (allocationMode == (byte) 1) {
                    Optional<JoinerGroup> joinerGroup = joinerGroupRepository.findByJoinerGroupIdAndStatus
                            (joinerGroupId, JoinerGroup.Status.ACTIVE.value);
                    if ((!joinerGroup.isPresent())) {
                        throw new BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en"));
                    }
                    JoinerGroup joinerGroup1=joinerGroup.get();
                    skipUpdate=allocator(user,joinerGroup1,alreadyAllocated,u);
                } else {
                    skipUpdate=deAllocator(user,alreadyDeallocated,u);
                    user.setJoinerGroup(null);
                }
                if(!skipUpdate) {
                    user.setUpdateDate(LocalDateTime.now());
                    userRepository.save(user);
                }
            } else {
                notFoundUsers.add(u);
            }
        }
        userListErrors(notFoundUsers, alreadyDeallocated, alreadyAllocated);
    }

    @Override
    public boolean allocator(User user, JoinerGroup joinerGroup, List<Integer> alreadyAllocated, Integer u){
        boolean skipUpdate=false;
        if (user.getJoinerGroup() != null) {
            alreadyAllocated.add(u);
            skipUpdate=true;
        }
        if(!skipUpdate) {
            user.setJoinerGroup(joinerGroup);
        }
        return skipUpdate;
    }

    @Override
    public boolean deAllocator(User user,List<Integer> alreadyDeallocated,Integer u){
        boolean skipUpdate=false;
        if(user.getJoinerGroup()==null){
            alreadyDeallocated.add(u);
            skipUpdate=true;
        }
        if(!skipUpdate) {
            user.setJoinerGroup(null);
        }
        return skipUpdate;
    }
    @Override
    public void userListErrors(List<Integer> notFoundUsers, List<Integer> alreadyDeallocated, List<Integer> alreadyAllocated) {
        String errorMessage = "1943-";
        if (!notFoundUsers.isEmpty()) {
            errorMessage = errorMessage + "User not found - User IDs:" + notFoundUsers + ". ";
        }
        if (!alreadyDeallocated.isEmpty()) {
            errorMessage = errorMessage + "Group is already deallocated - User IDs:" + alreadyDeallocated + ". ";
        }
        if (!alreadyAllocated.isEmpty()) {
            errorMessage = errorMessage + "Group is already allocated - User IDs:" + alreadyAllocated + ". ";
        }
        if (!errorMessage.equals("1943-")) {
            throw new BadRequestException(errorMessage);
        }
    }

    @Override
    public void allocationBasicChecks(List<Integer> users, Integer joinerGroupId, byte allocationMode) {
        if ((allocationMode != (byte) 0) && (allocationMode != (byte) 1)) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.allocation.mode", null, "en"));
        }
        if ((allocationMode == (byte) 0) && (joinerGroupId != null)) {
            throw new BadRequestException(languageUtil.getTranslatedText("group.id.not.required", null, "en"));
        }
        if ((allocationMode == (byte) 1) && (joinerGroupId == null)) {
            throw new BadRequestException(languageUtil.getTranslatedText("group.id.required", null, "en"));
        }
        if (users.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText("users.not.selected", null, "en"));
        }
    }

    @Override
    public Pager<User> listGroupResources(Integer joinerGroupId,String search,Integer page,Integer limit) {
        if(search ==null){
            search="";
        }
        List<User> userList;
        Pager<User> userPager;
        if(joinerGroupId!=null) {
            Optional<JoinerGroup> optionalJoinerGroup = joinerGroupRepository.findByJoinerGroupIdAndStatus(joinerGroupId,JoinerGroup.Status.ACTIVE.value);
            if (!optionalJoinerGroup.isPresent()) {
                throw new BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en"));
            }
            userList=userRepository.findGroupResourcesTrainees(joinerGroupId,search,PageRequest.of(page-1,limit));
            userPager=new Pager<>(limit,userRepository.countByGroupResourcesTrainees(joinerGroupId,search),page);
        }
        else{
            userList=userRepository.findUnassignedGroupResourcesTrainees(search,PageRequest.of(page-1,limit));
            userPager=new Pager<>(limit,userRepository.countUnassignedGroupResourcesTrainees(search),page);
        }
        userPager.setResult(userList);
        return userPager;

    }

}
