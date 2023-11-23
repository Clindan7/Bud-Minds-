package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Attendance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends Repository<Attendance, Integer> {
    Attendance save(Attendance attendance);

    Optional<Attendance> findBySessionIdSessionIdAndTraineeIdUserId(Integer sessionId,Integer userId);
    List<Attendance> findBySessionIdSessionIdAndStatusIn(Integer sessionId, List<Byte> status, Pageable of);
    List<Attendance> findBySessionIdSessionId(Integer sessionId);



}
