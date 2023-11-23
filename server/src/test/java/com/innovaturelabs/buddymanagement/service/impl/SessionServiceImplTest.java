package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.*;
import com.innovaturelabs.buddymanagement.form.SessionForm;
import com.innovaturelabs.buddymanagement.repository.JoinerGroupRepository;
import com.innovaturelabs.buddymanagement.repository.SessionRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.view.SessionView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class SessionServiceImplTest {

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JoinerGroupRepository joinerGroupRepository;

    @InjectMocks
    @Autowired
    private SessionServiceImpl sessionService;

    @Test
    void listSession(){
        Integer trainingId=1;
        Integer trainerId=2;
        Integer groupId=3;
        Integer technologyId=4;
        Integer page=1;
        Integer limit=10;
        User trainer=new User(2);
        trainer.setFirstName("kanna");
        trainer.setLastName("P");
        JoinerGroup joinerGroup=new JoinerGroup(groupId);
        joinerGroup.setJoinerGroupName("July");
        joinerGroup.setJoinerBatch(new JoinerBatch(1));
        Training training=new Training(3);
        training.setTechnologyId(new Technology("angular"));
        Session session=new Session(1);
        session.setjoinerGroupId(joinerGroup);
        session.setTrainerId(trainer);
        session.setTrainingId(training);
        session.setSessionStart(LocalDateTime.now().plusHours(2));
        session.setSessionEnd(LocalDateTime.now().plusHours(3));
        session.setCreatedBy(new User(2));
        session.setStatus((byte) 1);
        SessionView sessionView=new SessionView(session);
        User currentUser=new User(1);
        currentUser.setUserRole((byte) 4);
        currentUser.setJoinerGroup(joinerGroup);
        Page<Session> sessions=new PageImpl<>(Collections.singletonList(session));
        when(userRepository.findByUserId(isNull())).thenReturn(currentUser);
        List<Byte> statusList=new ArrayList<>();
        statusList.addAll(List.of(Session.Status.CREATED.value, Session.Status.ONPROGRESS.value,
                Session.Status.COMPLETED.value));
        when(sessionRepository.findByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(trainingId,trainerId,groupId,statusList, PageRequest.of(0, 10))).thenReturn(sessions);
        when(sessionRepository.countByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(trainingId,trainerId,groupId,statusList)).thenReturn(10);
        assertThrows(NullPointerException.class,()->sessionService.sessionList(trainingId,trainerId,groupId,technologyId,page,limit,null));

    }

}
