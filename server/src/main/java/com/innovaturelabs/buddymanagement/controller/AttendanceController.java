package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.service.AttendanceService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.AttendanceView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PutMapping()
    public void markAttendance(@RequestParam(name = "sessionId", required = true) Integer sessionId,
                               @RequestParam(name = "status", required = true) byte status) {
        attendanceService.markAttendance(sessionId, status);
    }

    @GetMapping()
    public Pager<AttendanceView> listAttendance(@RequestParam(name = "sessionId", required = true) Integer sessionId,
                                                @RequestParam(name = "status", required = false) Byte status,
                                                @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return attendanceService.listAttendance(sessionId, status, page, limit);
    }
}
