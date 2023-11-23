package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.service.NotificationService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.NotificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping()
    public Pager<NotificationView> notificationList(
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return notificationService.notifcationList(limit);
    }

    @PostMapping("/{notificationId}")
    public void notificationClose(@PathVariable("notificationId") Integer notificationId) {
        notificationService.notifcationClose(notificationId);
    }
}
