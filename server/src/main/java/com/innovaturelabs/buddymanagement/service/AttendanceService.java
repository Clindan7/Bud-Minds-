package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.AttendanceView;


public interface AttendanceService {

    void markAttendance(Integer sessionId,byte status);

    Pager<AttendanceView> listAttendance(Integer sessionId, Byte status , Integer page, Integer limit);

}
