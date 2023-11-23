package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.Notification;
import com.innovaturelabs.buddymanagement.entity.Session;
import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.NotificationView;


public interface NotificationService {

    void createNotificationForSession(User createdBy,Integer groupId, Session session);
    void createNotificationForTask(User createdBy,Integer groupId, Task taskId);


    Pager<NotificationView> notifcationList(Integer limit);

    Notification checkIfNotificationSend(Integer groupId, Task taskId);

    void notifcationClose(Integer notificationId);
}
