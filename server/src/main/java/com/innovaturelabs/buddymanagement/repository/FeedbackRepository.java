package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.UserFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<UserFeedback,Integer> {
    @Query(value = "select * from user_feedback where feedback_provider=?1 and feedback_receiver=?2 and feedback_start=?3", nativeQuery = true)
    Optional<UserFeedback> findByFeedbackProviderAndFeedbackReceiverAndFeedbackStartDate(Integer provider, Integer receiver, LocalDate startDate);

    @Query(value = "select * from user_feedback where feedback_type=?1 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByFilterType(Byte filterType, PageRequest of);

    @Query(value = "select count(*) from user_feedback where feedback_type=?1 and status=1", nativeQuery = true)
    Integer findByFilterTypeCount(Byte filterType);

    @Query(value = "select * from user_feedback where feedback_receiver=?1 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByFeedbackReceiver(Integer feedbackReceiver, PageRequest of);

    @Query(value = "select count(*) from user_feedback where feedback_receiver=?1 and status=1", nativeQuery = true)
    Integer findByFeedbackReceiverCount(Integer feedbackReceiver);

    @Query(value = "select * from user_feedback where status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findAllUserFeedback(PageRequest of);

    @Query(value = "select count(*) from user_feedback where status=1", nativeQuery = true)
    Integer findAllFeedbackCount();

    @Query(value = "select * from user_feedback where feedback_type=?1 and feedback_receiver=?2 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByFilterTypeAndFeedbackReceiver(Byte filterType, Integer feedbackReceiver, PageRequest of);

    @Query(value = "select count(*) from user_feedback where feedback_type=?1 and feedback_receiver=?2 and status=1", nativeQuery = true)
    Integer findByFilterTypeAndFeedbackReceiverCount(Byte filterType, Integer feedbackReceiver);

    @Query(value = "select count(*) from user_feedback where feedback_type=?1 and feedback_receiver=?2 and feedback_provider=?3 and status=1", nativeQuery = true)
    Integer findByFilterTypeAndFeedbackReceiverMentorCount(Byte filterType, Integer feedbackReceiver,Integer mentor);


    @Query(value = "select * from user_feedback where feedback_provider=?1 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByMentorFeedback(Integer mentor,PageRequest of);


    @Query(value = "select count(*) from user_feedback where feedback_provider=?1 and status=1", nativeQuery = true)
    Integer findByMentorFeedbackCount(Integer mentor);


    @Query(value = "select * from user_feedback where feedback_type=?1 and feedback_receiver=?2 and feedback_provider=?3 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByFilterTypeAndFeedbackReceiverMentor(Byte filterType, Integer feedbackReceiver,Integer mentor, PageRequest of);

    @Query(value = "select * from user_feedback where feedback_type=?1 and feedback_provider=?2 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByFilterTypeMentor(Byte filterType,Integer mentor, PageRequest of);


    @Query(value = "select count(*) from user_feedback where feedback_type=?1 and feedback_provider=?2 and status=1", nativeQuery = true)
    Integer findByFilterTypeMentorCount(Byte filterType,Integer mentor);

    @Query(value = "select * from user_feedback where feedback_receiver=?1 and feedback_provider=?2 and status=1 order by create_date desc", nativeQuery = true)
    Page<UserFeedback> findByFeedbackReceiverMentor(Integer feedbackReceiver,Integer mentor, PageRequest of);

    @Query(value = "select count(*) from user_feedback where feedback_receiver=?1 and feedback_provider=?2 and status=1", nativeQuery = true)
    Integer findByFeedbackReceiverMentorCount(Integer feedbackReceiver,Integer mentor);

    Optional<UserFeedback> findByFeedbackIdAndStatus(Integer feedbackId, byte value);

}
