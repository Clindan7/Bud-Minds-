package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.Notification;
import com.innovaturelabs.buddymanagement.entity.Session;
import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.repository.NotificationRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.NotificationService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.NotificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private LanguageUtil languageUtil;

    @Override
    public void createNotificationForSession(User createdBy, Integer joinerGroupId, Session session) {
        String sessionMsg = createdBy.getFirstName() + " created a session for you";

        userRepository.findByJoinerGroup(joinerGroupId).stream()
                .map(user -> new Notification(user, sessionMsg, createdBy, session, null))
                .forEach(notificationRepository::save);
    }

    @Override
    public void createNotificationForTask(User createdBy, Integer joinerGroupId, Task taskId) {
        String sessionMsg = createdBy.getFirstName() + " assigned a new task for you";

        userRepository.findByJoinerGroup(joinerGroupId).stream()
                .map(user -> new Notification(user, sessionMsg, createdBy, null, taskId))
                .forEach(notificationRepository::save);
    }

    @Override
    public Notification checkIfNotificationSend(Integer groupId, Task taskId){
        List<User> users = userRepository.findByGroup(groupId);
        Notification notificationCheck = new Notification();
        for (User user: users){
             notificationCheck = notificationRepository.findByUserIdAndTaskId(user,taskId);
        }
        return notificationCheck;
    }

    @Override
    public Pager<NotificationView> notifcationList(Integer limit) {
        List<NotificationView> notifications = notificationRepository.findByUserId(SecurityUtil.getCurrentUserId(), PageRequest.of(0, limit)).stream().map(NotificationView::new).collect(Collectors.toList());
        Pager<NotificationView> notificationPager = new Pager<>(limit, notifications.size(), 1);
        notificationPager.setResult(notifications);
        return notificationPager;
    }

    @Override
    public void notifcationClose(Integer notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId).orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText("notification.not.found", null, "en")));
        if (! String.valueOf(SecurityUtil.getCurrentUserId()).equals(String.valueOf(notification.getUserId().getUserId()))) {
            throw new BadRequestException(languageUtil.getTranslatedText("notification.cannot.closed", null, "en"));
        }
        if (notification.getStatus() == Notification.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("notification.already.closed", null, "en"));
        }
        notification.setStatus(Notification.Status.INACTIVE.value);
        notificationRepository.save(notification);
    }

}
