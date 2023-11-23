package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Notification;
import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends Repository<Notification,Integer> {

    Notification save (Notification notification);

    @Query(value = "select * from notification where user_id=?1 ORDER BY create_date DESC",nativeQuery = true)
    List<Notification> findByUserId(Integer userId, Pageable of);


    @Query(value = "select * from notification where user_id=?1 and task_id",nativeQuery = true)
    Notification findByUserIdAndTaskId(User userId, Task taskId);

    @Query(value = "select * from notification where notification_id=?1",nativeQuery = true)
    Optional<Notification> findByNotificationId(Integer notificationId);
}
