package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.controller.SocketController;
import com.innovaturelabs.buddymanagement.entity.*;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.SessionForm;
import com.innovaturelabs.buddymanagement.repository.*;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.NotificationService;
import com.innovaturelabs.buddymanagement.service.SessionService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.AttendanceView;
import com.innovaturelabs.buddymanagement.view.SessionView;
import com.innovaturelabs.buddymanagement.view.TraineeTrainingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SessionServiceImpl implements SessionService {

    Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
    private static final String TRAINING_NOT_FOUND = "training.not.found";
    private static final String TRAINER_NOT_FOUND = "trainer.not.found";
    private static final String GROUP_NOT_FOUND = "group.not.found";
    private static final String CANNOT_UPDATE_SESSION = "session.cannot.update";

    private static final String SESSION_NOT_FOUND_LOG = "session.not.found";

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private JoinerGroupRepository joinerGroupRepository;

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SocketController socketController;

    @Override
    public void sessionCreate(SessionForm sessionForm) {

        if (sessionForm.getGroupId() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("group.id.require", null, "en"));
        }

        if (sessionForm.getSessionStart() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.required", null, "en"));
        }
        if (sessionForm.getSessionEnd() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.required", null, "en"));
        }

        Training training = trainingRepository.findByTrainingIdAndStatus(sessionForm.getTrainingId()).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en")));

        User trainer = trainerRepository
                .findByUserIdAndStatusAndUserRoleIn(sessionForm.getTrainerId(), User.Status.ACTIVE.value,
                        Arrays.asList((byte) 2, (byte) 3))
                .orElseThrow(
                        () -> new BadRequestException(languageUtil.getTranslatedText(TRAINER_NOT_FOUND, null, "en")));

        JoinerGroup joinerGroup = joinerGroupRepository
                .findByJoinerGroupIdAndStatus(sessionForm.getGroupId(), JoinerGroup.Status.ACTIVE.value).orElseThrow(
                        () -> new BadRequestException(languageUtil.getTranslatedText(GROUP_NOT_FOUND, null, "en")));

        LocalDateTime sessionStart = sessionForm.getSessionStart();
        LocalDateTime sessionEnd = sessionForm.getSessionEnd();
        sessionDateValidation(sessionStart, sessionEnd, training);

        trainerAlreadyAssigned(sessionForm.getTrainerId(), sessionStart, sessionEnd, null);

        groupAlreadyAssigned(sessionForm.getGroupId(), sessionStart, sessionEnd, null);

        User createdBy = userRepository.findByUserId(SecurityUtil.getCurrentUserId());

        Session session = new Session(training, trainer, joinerGroup, createdBy, sessionForm.getDescription(),
                sessionStart, sessionEnd);
        List<User> user = userRepository.findByJoinerGroup(sessionForm.getGroupId());
        if (user.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText("group.users.empty", null, "en"));
        }
        Session savedSession = sessionRepository.save(session);
        notificationService.createNotificationForSession(createdBy, sessionForm.getGroupId(), savedSession);

    }

    @Override
    public Pager<SessionView> sessionList(Integer trainingId, Integer trainerId, Integer groupId, Integer technologyId, Integer page,
                                          Integer limit, Byte status) {
        Pager<SessionView> sessionPager;
        List<SessionView> sessionListing;

        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        List<Byte> statusList = new ArrayList<>();
        ifStatusNull(statusList, status);
        if (user.getUserRole() == 4) {
            checkUserGroup(user);
            if (trainingId != null && trainerId != null) {
                sessionListing = StreamSupport.stream(
                        sessionRepository.findByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(trainingId, trainerId,
                                user.getJoinerGroup().getJoinerGroupId(), statusList,
                                PageRequest.of(page - 1, limit)).spliterator(),
                        false).map(SessionView::new).collect(Collectors.toList());
                sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(trainingId, trainerId, user.getJoinerGroup().getJoinerGroupId(), statusList), page);
                sessionPager.setResult(sessionListing);
                return checkIfSessionList(sessionListing, sessionPager);
            }
            if (technologyId != null && trainerId != null) {
                sessionListing = StreamSupport.stream(
                        sessionRepository.findByTechnologyIdAndTrainerIdAndJoinerGroupIdAndStatus(technologyId, trainerId,
                                user.getJoinerGroup().getJoinerGroupId(), statusList,
                                PageRequest.of(page - 1, limit)).spliterator(),
                        false).map(SessionView::new).collect(Collectors.toList());
                sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(technologyId, trainerId, user.getJoinerGroup().getJoinerGroupId(), statusList), page);
                sessionPager.setResult(sessionListing);
                return checkIfSessionList(sessionListing, sessionPager);
            }
            if (trainingId != null) {
                sessionListing = StreamSupport.stream(
                        sessionRepository.findByTrainingIdAndJoinerGroupIdAndStatus(trainingId, user.getJoinerGroup().getJoinerGroupId(), statusList, PageRequest.of(page - 1, limit))
                                .spliterator(),
                        false).map(SessionView::new).collect(Collectors.toList());
                sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndJoinerGroupIdAndStatus(trainingId, user.getJoinerGroup().getJoinerGroupId(), statusList), page);
                sessionPager.setResult(sessionListing);
                return checkIfSessionList(sessionListing, sessionPager);
            }

            if (trainerId != null) {
                sessionListing = StreamSupport.stream(
                        sessionRepository.findByTrainerIdAndJoinerGroupIdAndStatus(trainerId, user.getJoinerGroup().getJoinerGroupId(), statusList, PageRequest.of(page - 1, limit))
                                .spliterator(),
                        false).map(SessionView::new).collect(Collectors.toList());
                sessionPager = new Pager<>(limit, sessionRepository.countByTrainerIdAndJoinerGroupIdAndStatus(trainerId, user.getJoinerGroup().getJoinerGroupId(), statusList), page);
                sessionPager.setResult(sessionListing);
                return checkIfSessionList(sessionListing, sessionPager);
            }

            if (technologyId != null) {
                sessionListing = StreamSupport.stream(
                        sessionRepository.findByTechnologyIdAndJoinerGroupIdAndStatus(technologyId, user.getJoinerGroup().getJoinerGroupId(), statusList, PageRequest.of(page - 1, limit))
                                .spliterator(),
                        false).map(SessionView::new).collect(Collectors.toList());
                sessionPager = new Pager<>(limit, sessionRepository.countByTechnologyIdAndJoinerGroupIdAndStatus(technologyId, user.getJoinerGroup().getJoinerGroupId(), statusList), page);
                sessionPager.setResult(sessionListing);
                return checkIfSessionList(sessionListing, sessionPager);
            }
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByAllTraineeUserAndStatus(user.getJoinerGroup().getJoinerGroupId(), statusList,
                            PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.findByAllCountTraineeUserAndStatus(user.getJoinerGroup().getJoinerGroupId(), statusList), page);
            sessionPager.setResult(sessionListing);
            return sessionPager;
        } else {
            return sessionListingFilter1(trainingId, trainerId, groupId, technologyId, statusList, page, limit);
        }

    }

    private void checkUserGroup(User user) {
        if (user.getJoinerGroup() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en"));
        }
    }

    private void ifStatusNull(List<Byte> statusList, Byte status) {
        if (status != null) {
            statusList.add(status);
        } else {
            statusList.addAll(List.of(Session.Status.CREATED.value, Session.Status.ONPROGRESS.value,
                    Session.Status.COMPLETED.value));
        }
    }

    public Pager<SessionView> sessionListingFilter1(Integer trainingId, Integer trainerId, Integer groupId, Integer technologyId,
                                                    List<Byte> statusList, Integer page, Integer limit) {
        Pager<SessionView> sessionPager;
        List<SessionView> sessionListing;

        if (trainingId != null && trainerId != null && groupId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(trainingId, trainerId,
                            groupId, statusList,
                            PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndTrainerIdAndJoinerGroupIdAndStatus(trainingId, trainerId, groupId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (technologyId != null && trainerId != null && groupId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByTechnologyIdAndTrainerIdAndJoinerGroupIdAndStatus(technologyId, trainerId,
                            groupId, statusList,
                            PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTechnologyIdAndTrainerIdAndJoinerGroupIdAndStatus(technologyId, trainerId, groupId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (trainingId != null && trainerId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository
                            .findByTrainingIdAndTrainerIdAndStatus(trainingId, trainerId, statusList,
                                    PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndTrainerIdAndStatus(trainingId, trainerId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (trainingId != null && groupId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository
                            .findByTrainingIdAndJoinerGroupIdAndStatus(trainingId, groupId, statusList,
                                    PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndJoinerGroupIdAndStatus(trainingId, groupId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (trainerId != null && groupId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository
                            .findByTrainerIdAndJoinerGroupIdAndStatus(trainerId, groupId, statusList,
                                    PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainerIdAndJoinerGroupIdAndStatus(trainerId, groupId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }
        if (trainerId != null && technologyId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository
                            .findByTrainerIdAndTechnologyIdAndStatus(trainerId, technologyId, statusList,
                                    PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainerIdAndTechnologyIdAndStatus(trainerId, technologyId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (technologyId != null && groupId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository
                            .findByTechnologyIdAndJoinerGroupIdAndStatus(technologyId, groupId, statusList,
                                    PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTechnologyIdAndJoinerGroupIdAndStatus(technologyId, groupId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }
        return sessionListingFilter2(trainingId, trainerId, groupId, technologyId, statusList, page, limit);
    }


    public Pager<SessionView> sessionListingFilter2(Integer trainingId, Integer trainerId, Integer groupId, Integer technologyId,
                                                    List<Byte> statusList, Integer page, Integer limit) {
        Pager<SessionView> sessionPager;
        List<SessionView> sessionListing;

        if (trainingId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByTrainingIdAndStatus(trainingId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainingIdAndStatus(trainingId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (trainerId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByTrainerIdAndStatus(trainerId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTrainerIdAndStatus(trainerId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }

        if (groupId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByJoinerGroupIdAndStatus(groupId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByJoinerGroupIdAndStatus(groupId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }


        if (technologyId != null) {
            sessionListing = StreamSupport.stream(
                    sessionRepository.findByTechnologyIdAndStatus(technologyId, statusList, PageRequest.of(page - 1, limit))
                            .spliterator(),
                    false).map(SessionView::new).collect(Collectors.toList());
            sessionPager = new Pager<>(limit, sessionRepository.countByTechnologyIdAndStatus(technologyId, statusList), page);
            sessionPager.setResult(sessionListing);
            return checkIfSessionList(sessionListing, sessionPager);
        }
        sessionListing = StreamSupport
                .stream(sessionRepository
                                .findAllByStatusInOrderByCreateDateDesc(statusList, PageRequest.of(page - 1, limit))
                                .spliterator(),
                        false)
                .map(SessionView::new).collect(Collectors.toList());
        sessionPager = new Pager<>(limit, sessionRepository.countByStatusIn(statusList), page);
        sessionPager.setResult(sessionListing);
        return sessionPager;
    }

    public Pager<SessionView> checkIfSessionList(List<SessionView> sessionList, Pager<SessionView> sessionPager) {
        if (!sessionList.isEmpty()) {
            return sessionPager;
        } else {
            log.error(SESSION_NOT_FOUND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en"));
        }
    }

    @Override
    public SessionView sesssionDetailView(Integer sessionId) {
        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        Session session = sessionRepository.findByIdAndStatusNot(sessionId, Session.Status.INACTIVE.value).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en")));
        if (user.getUserRole() == 4 && (!String.valueOf(session.getjoinerGroupId().getJoinerGroupId())
                .equals(String.valueOf(user.getJoinerGroup().getJoinerGroupId())))) {
            throw new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en"));

        }
        return new SessionView(session);
    }

    @Override
    public void deleteSession(Integer sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en")));
        if (session.getStatus() == 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("session.already.deleted", null, "en"));
        }
        session.setStatus(Session.Status.INACTIVE.value);
        session.setUpdateDate(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    public void sessionUpdate(SessionForm sessionForm, Integer sessionId) {
        if (sessionId == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en"));
        }
        Session session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en")));

        if (sessionForm.getSessionStart() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.required", null, "en"));
        }
        if (sessionForm.getSessionEnd() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.required", null, "en"));
        }

        Training training = trainingRepository.findByTrainingIdAndStatus(sessionForm.getTrainingId()).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en")));

        User trainer = trainerRepository
                .findByUserIdAndStatusAndUserRoleIn(sessionForm.getTrainerId(), User.Status.ACTIVE.value,
                        Arrays.asList((byte) 2, (byte) 3))
                .orElseThrow(
                        () -> new BadRequestException(languageUtil.getTranslatedText(TRAINER_NOT_FOUND, null, "en")));

        LocalDateTime sessionStart = sessionForm.getSessionStart();
        LocalDateTime sessionEnd = sessionForm.getSessionEnd();
        sessionDateValidation(sessionStart, sessionEnd, training);

        trainerAlreadyAssigned(sessionForm.getTrainerId(), sessionStart, sessionEnd, sessionId);

        groupAlreadyAssigned(session.getjoinerGroupId().getJoinerGroupId(), sessionStart, sessionEnd, sessionId);

        User createdBy = userRepository.findByUserId(SecurityUtil.getCurrentUserId());

        session.setTrainingId(training);
        session.setTrainerId(trainer);
        session.setSessionStart(sessionStart);
        session.setSessionEnd(sessionEnd);
        session.setSessionDescription(sessionForm.getDescription());
        session.setCreatedBy(createdBy);
        session.setUpdateDate(LocalDateTime.now());
        sessionRepository.save(session);

    }

    @Override
    public void sessionStatus(Integer sessionId, byte status) {
        if (sessionId == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en"));
        }
        Session session = sessionRepository.findByIdAndStatusNot(sessionId, Session.Status.INACTIVE.value).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(SESSION_NOT_FOUND_LOG, null, "en")));
        if (!checkSessionStatus(status)) {
            throw new BadRequestException(languageUtil.getTranslatedText("status.invalid", null, "en"));
        }
        if (session.getStatus() == 3) {
            throw new BadRequestException(languageUtil.getTranslatedText(CANNOT_UPDATE_SESSION, null, "en"));
        }
        if (session.getStatus() == 2 && status != 3) {
            throw new BadRequestException(languageUtil.getTranslatedText("session.already.started", null, "en"));
        }
        LocalDateTime dateNow = LocalDateTime.now();
        if (status == 2) {
            if (!(dateNow.isAfter(session.getSessionStart()) && dateNow.isBefore(session.getSessionEnd()))) {
                throw new BadRequestException(languageUtil.getTranslatedText(CANNOT_UPDATE_SESSION, null, "en"));
            }
            createSessionAttendance(session);
        } else if (status == 3 && (!dateNow.isAfter(session.getSessionStart()) || session.getStatus() != 2)) {
            throw new BadRequestException(languageUtil.getTranslatedText(CANNOT_UPDATE_SESSION, null, "en"));
        } else if (status == 1 && dateNow.isAfter(session.getSessionStart())) {
            throw new BadRequestException(languageUtil.getTranslatedText(CANNOT_UPDATE_SESSION, null, "en"));
        }
        session.setStatus(status);
        session.setUpdateDate(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    public List<TraineeTrainingView> traineeTrainingList() {
        List<TraineeTrainingView> trainingList;
        trainingList = StreamSupport.stream(
                sessionRepository.findByAllTraineeUserAndStatus(Arrays.asList((byte) 1, (byte) 2, (byte) 3))
                        .spliterator(),
                false).map(TraineeTrainingView::new).collect(Collectors.toList());
        return trainingList;
    }

    public static LocalDateTime convertUTCtoIST(LocalDateTime date) {
        // Convert UTC to IST
        return date.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
                .toLocalDateTime();

    }

    public void trainerAlreadyAssigned(Integer trainerId, LocalDateTime sessionStart, LocalDateTime sessionEnd,
                                       Integer sessionId) {

        Integer trainerCount;

        if (sessionId != null) {
            trainerCount = sessionRepository.countInTrainer(trainerId, sessionId, sessionStart, sessionEnd);
        } else {
            trainerCount = sessionRepository.countInTrainer(trainerId, sessionStart, sessionEnd);
        }

        if (trainerCount > 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("trainer.already.assigned", null, "en"));
        }
    }

    public void groupAlreadyAssigned(Integer groupId, LocalDateTime sessionStart, LocalDateTime sessionEnd,
                                     Integer sessionId) {
        Integer groupCount;
        if (sessionId != null) {
            groupCount = sessionRepository.countInGroup(groupId, sessionId, sessionStart, sessionEnd);
        } else {
            groupCount = sessionRepository.countInGroup(groupId, sessionStart, sessionEnd);

        }
        if (groupCount > 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("group.already.assigned", null, "en"));
        }
    }

    public void sessionDateValidation(LocalDateTime start, LocalDateTime end, Training training) {
        LocalDateTime dateNow = LocalDateTime.now();

        if (start.isAfter(end)) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.invalid", null, "en"));
        }
        if (start.isEqual(end)) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.invalid", null, "en"));
        }
        if (start.isBefore(dateNow)) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.invalid", null, "en"));
        }

        if (start.isBefore(training.getTrainingStartDate().atStartOfDay()) || start.isAfter(training.getTrainingEndDate().atTime(LocalTime.MAX))) {
            throw new BadRequestException(languageUtil.getTranslatedText("session.startDate.invalid", null, "en"));
        }

        if (end.isAfter(training.getTrainingEndDate().atTime(LocalTime.MAX))) {
            throw new BadRequestException(languageUtil.getTranslatedText("session.endDate.invalid", null, "en"));

        }
    }

    public void createSessionAttendance(Session session) {
        StreamSupport.stream(userRepository.findByJoinerGroup(session.getjoinerGroupId().getJoinerGroupId()).spliterator(), false).map(user ->
                new Attendance(session, user)).forEach(attendanceRepository::save);

        List<AttendanceView> attendanceList = StreamSupport.stream(attendanceRepository.findBySessionIdSessionId(session.getSessionId()).spliterator(), false).map(AttendanceView::new).collect(Collectors.toList());
        socketController.attendance(attendanceList, session.getSessionId());
    }

    public boolean checkSessionStatus(byte sessionStatus) {
        for (Session.Status status : Session.Status.values()) {
            if (status.value == sessionStatus) {
                return true;
            }
        }
        return false;
    }



    @Override
    public List<SessionView> groupList() {
        List<SessionView> sessionViews;

        sessionViews = StreamSupport.stream(sessionRepository
                        .findByJoinerGroupIdFilter(Arrays.asList((byte) 1, (byte) 2, (byte) 3)).spliterator(), false)
                .map(SessionView::new)
                .collect(Collectors.toList());
        return sessionViews;
    }
}
