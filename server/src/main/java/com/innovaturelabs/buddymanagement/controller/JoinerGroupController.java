package com.innovaturelabs.buddymanagement.controller;


import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.form.GroupAllocationForm;
import com.innovaturelabs.buddymanagement.form.JoinerGroupForm;
import com.innovaturelabs.buddymanagement.service.JoinerGroupService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.JoinerGroupView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author clindan
 */

@RestController
@RequestMapping("/group")
public class JoinerGroupController {

    @Autowired
    private JoinerGroupService joinerGroupService;

    @GetMapping
    public Pager<JoinerGroupView> listJoinerGroup(@RequestParam(name = "groupSearch", required = false) String groupSearch,
                                                  @RequestParam(name = "joinerBatchId", required = false) Integer joinerBatchId,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return joinerGroupService.listGroup(groupSearch, joinerBatchId, page, limit);
    }

    @DeleteMapping("/{joinerGroupId}")
    public void groupDelete(@PathVariable("joinerGroupId") Integer joinerGroupId) {
        joinerGroupService.groupDelete(joinerGroupId);
    }

    @PostMapping
    public JoinerGroupView joinerGroupCreate(@Valid @RequestBody JoinerGroupForm form) {
        return joinerGroupService.joinerGroupCreate(form);
    }

    @PutMapping("/{joinerGroupId}")
    public JoinerGroupView updateGroup(@PathVariable("joinerGroupId") Integer joinerGroupId, @RequestBody @Valid JoinerGroupForm form) {
        return joinerGroupService.updateGroup(joinerGroupId, form);
    }

    @GetMapping("/{joinerGroupId}")
    public JoinerGroupView fetchGroup(@PathVariable("joinerGroupId") Integer joinerGroupId) {
        return joinerGroupService.fetchGroup(joinerGroupId);
    }

    @PostMapping("/allocation-control")
    public void joinerGroupAllocate(@Valid @RequestBody GroupAllocationForm form,
                                    @RequestParam("joinerGroupId") Integer joinerGroupId,
                                    @RequestParam("allocationMode") byte allocationMode) {
        joinerGroupService.joinerGroupAllocate(form, joinerGroupId, allocationMode);
    }

    @GetMapping("/resource-list")
    public Pager<User> listGroupResources(@RequestParam(name = "joinerGroupId", required = false) Integer joinerGroupId,
                                          @RequestParam(name = "search", required = false) String search,
                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return joinerGroupService.listGroupResources(joinerGroupId, search, page, limit);
    }


}
