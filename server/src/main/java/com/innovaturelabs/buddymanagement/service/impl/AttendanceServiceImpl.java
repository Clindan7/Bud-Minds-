package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.*;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.repository.AttendanceRepository;
import com.innovaturelabs.buddymanagement.repository.SessionRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.AttendanceService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.AttendanceView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageUtil languageUtil;

    @Override
    public void markAttendance(Integer sessionId, byte status) {
        if (status != 0 && status != 1) {
            throw new BadRequestException(languageUtil.getTranslatedText("status.invalid", null, "en"));
        }
        Session session = sessionRepository.findByIdAndStatusNot(sessionId, Session.Status.INACTIVE.value).orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText("session.not.found", null, "en")));
        if (session.getStatus() == 3) {
            throw new BadRequestException(languageUtil.getTranslatedText("session.already.completed", null, "en"));
        } else if (session.getStatus() != 2) {
            throw new BadRequestException(languageUtil.getTranslatedText("attendance.cannot.mark", null, "en"));
        }
        Attendance attendance = attendanceRepository.findBySessionIdSessionIdAndTraineeIdUserId(sessionId, SecurityUtil.getCurrentUserId()).orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText("attendance.cannot.mark", null, "en")));
        if(attendance.getStatus()==status){
            throw new BadRequestException(languageUtil.getTranslatedText("attendance.already.marked", null, "en"));
        }
        attendance.setStatus(status);
        attendanceRepository.save(attendance);
    }

    @Override
    public Pager<AttendanceView> listAttendance(Integer sessionId, Byte status, Integer page, Integer limit) {
        if(sessionId ==null){
            throw new BadRequestException(languageUtil.getTranslatedText("session.id.required",null,"en"));
        }
        List<Byte> statusList = new ArrayList<>();
        Session session = sessionRepository.findByIdAndStatusNot(sessionId, Session.Status.INACTIVE.value).orElseThrow(() -> new BadRequestException(languageUtil.getTranslatedText("session.not.found", null, "en")));
        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        if (user.getUserRole() == 4) {
            if (user.getJoinerGroup() != null) {
                if (user.getJoinerGroup().getJoinerGroupId() != session.getjoinerGroupId().getJoinerGroupId()) {
                    throw new BadRequestException(languageUtil.getTranslatedText("permission.not.allowed", null, "en"));
                }
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText("permission.not.allowed", null, "en"));
            }
        }
        if (status == null) {
            statusList.addAll(List.of(Attendance.Status.PRESENT.value, Attendance.Status.ABSENT.value));
        } else {
            if (status != 0 && status != 1) {
                throw new BadRequestException(languageUtil.getTranslatedText("status.invalid", null, "en"));
            }
            statusList.add(status);
        }
        Pager<AttendanceView> attendancePager;
        List<AttendanceView> attendanceList;
        attendanceList = StreamSupport.stream(attendanceRepository.findBySessionIdSessionIdAndStatusIn(sessionId, statusList, PageRequest.of(page - 1, limit)).spliterator(), false).map(AttendanceView::new).collect(Collectors.toList());
        attendancePager = new Pager<>(limit, attendanceList.size(), page);
        attendancePager.setResult(attendanceList);
        return attendancePager;
    }


}
