package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.view.AttendanceView;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SocketController {
    private final SimpMessagingTemplate messagingTemplate;
    public SocketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate=messagingTemplate;
    }

    @MessageMapping
    public void attendance(List<AttendanceView> attendanceList,Integer sessionId){
        String destination="/session/"+sessionId;
        messagingTemplate.convertAndSend(destination,attendanceList);
    }

}


