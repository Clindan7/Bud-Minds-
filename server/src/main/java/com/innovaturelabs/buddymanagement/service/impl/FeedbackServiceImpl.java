package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.entity.UserFeedback;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.UserFeedbackForm;
import com.innovaturelabs.buddymanagement.repository.FeedbackRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.FeedbackService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.FeedbackView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageUtil languageUtil;

    private static final String USER_NOT_FOUND = "user.not.found";

    private static final String FEEDBACK_NOT_FOUND = "feedback.not.found";

    private static final String FEEDBACK_START_REQUIRED = "feedback.start.required";

    private static final String FEEDBACK_END_REQUIRED = "feedback.end.required";

    private static final String INVALID_FEEDBACK_START = "invalid.feedback.start";

    private static final String INVALID_FEEDBACK_END = "invalid.feedback.end";

    private static final String INVALID_FEEDBACK_TYPE = "invalid.feedback.type";

    private static  final String SCORE_REQUIRED="score.required";

    @Override
    public void feedbackCreate(UserFeedbackForm form) {

        basicChecking(form);
        User feedbackProvider = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        if (form.getFeedbackReceiver() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"));
        }
        User feedbackReceiver = userRepository.findByUserIdAndStatus(form.getFeedbackReceiver(),User.Status.ACTIVE.value);
        feedbackReceiverChecking(feedbackReceiver, feedbackProvider);

        long daysBetween = ChronoUnit.DAYS.between(form.getFeedbackStart(), form.getFeedbackEnd());

        if (daysBetween > 27 && daysBetween < 32) {
            //monthly feedback
            //fetching current month details
            LocalDate date = LocalDate.now();
            LocalDate firstOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

            //fetching previous month details
            LocalDate earlier = date.minusMonths(1);
            LocalDate firstOfPreviousMonth = earlier.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfPreviousMonth = earlier.with(TemporalAdjusters.lastDayOfMonth());

            if (form.getFeedbackType() != 1) {
                throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_TYPE, null, "en"));
            }
            checkCurrentMonth(form,date,earlier);
            if (form.getFeedbackStart().getMonth() == date.getMonth()) {
                currentMonthChecking(form, firstOfMonth, endOfMonth);
            }
            if (form.getFeedbackStart().getMonth() == earlier.getMonth()) {
                previousMonthChecking(form, firstOfPreviousMonth, endOfPreviousMonth);
            }
                checkIfFeedbackExists(feedbackProvider, feedbackReceiver, form.getFeedbackStart());
            feedbackSave(feedbackProvider, feedbackReceiver, form);
        } else if (daysBetween == 6) {

            //weekly feedback

            if (form.getFeedbackType() != 2) {
                throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_TYPE, null, "en"));
            }

            LocalDate date = LocalDate.now();
            WeekFields weekFields = WeekFields.of(Locale.FRANCE);
            //fetching current week details
            int currentWeek = date.get(weekFields.weekOfWeekBasedYear());
            int currentWeekStart = form.getFeedbackStart().get(weekFields.weekOfWeekBasedYear());
            int currentWeekEnd = form.getFeedbackEnd().get(weekFields.weekOfWeekBasedYear());
            weekChecking(currentWeek, currentWeekStart, currentWeekEnd, form);
            checkIfFeedbackExists(feedbackProvider, feedbackReceiver, form.getFeedbackStart());
            feedbackRepository.save(new UserFeedback(
                    feedbackProvider,
                    feedbackReceiver,
                    form.getComment(),
                    form.getFeedbackStart(),
                    form.getFeedbackEnd(),
                    form.getFeedbackType()
            ));
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_TYPE, null, "en"));
        }
    }

    private void checkCurrentMonth(UserFeedbackForm form, LocalDate date,LocalDate earlier) {
        if (form.getFeedbackStart().getMonth() != date.getMonth() && form.getFeedbackStart().getMonth() != earlier.getMonth()) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.month", null, "en"));
        }
    }

    private void feedbackReceiverChecking(User feedbackReceiver, User feedbackProvider) {
        if (feedbackReceiver == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"));
        }
        if (feedbackReceiver.getMentorId() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("userMentor.not.found", null, "en"));
        }
        if (!feedbackReceiver.getMentorId().getUserId().equals(feedbackProvider.getUserId())) {
            throw new BadRequestException(languageUtil.getTranslatedText("no.permission", null, "en"));
        }
    }

    private void weekChecking(int currentWeek, int currentWeekStart, int currentWeekEnd, UserFeedbackForm form) {
        if (currentWeek != currentWeekStart) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.week", null, "en"));
        }
        if (currentWeek != currentWeekEnd) {
            throw new BadRequestException(languageUtil.getTranslatedText("invalid.week", null, "en"));
        }
        if (form.getFeedbackStart().getDayOfWeek() != DayOfWeek.from(DayOfWeek.MONDAY)) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_START, null, "en"));
        }

        if (form.getFeedbackEnd().getDayOfWeek() != DayOfWeek.from(DayOfWeek.SUNDAY)) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_END, null, "en"));
        }
    }

    private void previousMonthChecking(UserFeedbackForm form, LocalDate firstOfPreviousMonth, LocalDate endOfPreviousMonth) {
        if (form.getFeedbackStart().getDayOfMonth() != firstOfPreviousMonth.getDayOfMonth()) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_START, null, "en"));
        }
        if (form.getFeedbackEnd().getDayOfMonth() != endOfPreviousMonth.getDayOfMonth()) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_END, null, "en"));
        }
    }

    private void currentMonthChecking(UserFeedbackForm form, LocalDate firstOfMonth, LocalDate endOfMonth) {
        if (form.getFeedbackStart().getDayOfMonth() != firstOfMonth.getDayOfMonth()) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_START, null, "en"));
        }
        if (form.getFeedbackEnd().getDayOfMonth() != endOfMonth.getDayOfMonth()) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_END, null, "en"));
        }
    }

    private void basicChecking(UserFeedbackForm form) {
        if (form.getFeedbackStart() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(FEEDBACK_START_REQUIRED, null, "en"));
        }
        if (form.getFeedbackEnd() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(FEEDBACK_END_REQUIRED, null, "en"));
        }
        if (form.getFeedbackStart().isAfter(form.getFeedbackEnd())) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_START, null, "en"));
        }
        if (form.getFeedbackEnd().isBefore(form.getFeedbackStart())) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_END, null, "en"));
        }
        if (form.getFeedbackStart().equals(form.getFeedbackEnd())) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.end.same", null, "en"));
        }
    }

    private void feedbackSave(User feedbackProvider, User feedbackReceiver, UserFeedbackForm form) {
        scoreRequiredChecking(form);
        scoreRequiredChecking2(form);
        if (form.getApproachable() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getConstructive() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getSynergy() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getRespect() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getSelflessness() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        feedbackRepository.save(new UserFeedback(
                feedbackProvider,
                feedbackReceiver,
                form
        ));
    }

    private void scoreRequiredChecking2(UserFeedbackForm form) {
        if (form.getWritten() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getResponsibility() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getDecision() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getInnovation() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getOnTime() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getExcellence() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getPleasantNature() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getPunctual() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getHierarchy() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getUpgradation() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
    }

    private void scoreRequiredChecking(UserFeedbackForm form) {
        if (form.getAdaptability() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getAnalytical() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getImplementation() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getJobKnowledge() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getQuality() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getValidation() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getExpressIdeas() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getEfficientChannelUse() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getOral() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getInfoSharing() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
    }

    private void checkIfFeedbackExists(User feedbackProvider, User feedbackReceiver, LocalDate feedbackStart) {
        Optional<UserFeedback> userFeedback = feedbackRepository.findByFeedbackProviderAndFeedbackReceiverAndFeedbackStartDate(feedbackProvider.getUserId(), feedbackReceiver.getUserId(), feedbackStart);
        if (userFeedback.isPresent()) {
            throw new BadRequestException(languageUtil.getTranslatedText("feedback.already.submitted", null, "en"));
        }
    }

    @Override
    public Pager<FeedbackView> listFeedback(Byte filterType, Integer feedbackReceiver, Integer page, Integer limit) {
        Pager<FeedbackView> feedbackPager;
        List<FeedbackView> feedbackList;
        User currentUser = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en")));
        if (currentUser.getUserRole() == User.Role.MENTOR.value) {
            return ifUserIsMentor(currentUser,filterType,feedbackReceiver,page,limit);
        }
        if (filterType != null && feedbackReceiver != null) {
            feedbackList = StreamSupport.stream(feedbackRepository
                            .findByFilterTypeAndFeedbackReceiver(filterType,feedbackReceiver, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(FeedbackView::new)
                    .collect(Collectors.toList());
            feedbackPager = new Pager<>(limit, feedbackRepository.findByFilterTypeAndFeedbackReceiverCount(filterType,feedbackReceiver), page);
            feedbackPager.setResult(feedbackList);
            return checkIfFeedbackIsEmpty(feedbackList,feedbackPager);
        }
        if (filterType != null) {
            feedbackList = StreamSupport.stream(feedbackRepository
                            .findByFilterType(filterType, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(FeedbackView::new)
                    .collect(Collectors.toList());
            feedbackPager = new Pager<>(limit, feedbackRepository.findByFilterTypeCount(filterType), page);
            feedbackPager.setResult(feedbackList);
            return checkIfFeedbackIsEmpty(feedbackList,feedbackPager);
        }
        if (feedbackReceiver != null) {
            feedbackList = StreamSupport.stream(feedbackRepository
                            .findByFeedbackReceiver(feedbackReceiver, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(FeedbackView::new)
                    .collect(Collectors.toList());
            feedbackPager = new Pager<>(limit, feedbackRepository.findByFeedbackReceiverCount(feedbackReceiver), page);
            feedbackPager.setResult(feedbackList);
            return checkIfFeedbackIsEmpty(feedbackList,feedbackPager);
        }
        feedbackList = StreamSupport.stream(feedbackRepository
                        .findAllUserFeedback(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(FeedbackView::new)
                .collect(Collectors.toList());
        feedbackPager = new Pager<>(limit, feedbackRepository.findAllFeedbackCount(), page);
        feedbackPager.setResult(feedbackList);
        return feedbackPager;
    }

    private Pager<FeedbackView> ifUserIsMentor(User currentUser, Byte filterType, Integer feedbackReceiver, Integer page, Integer limit) {
        Pager<FeedbackView> feedbackPager;
        List<FeedbackView> feedbackList;
        if (filterType != null && feedbackReceiver != null) {
            feedbackList = StreamSupport.stream(feedbackRepository
                            .findByFilterTypeAndFeedbackReceiverMentor(filterType,feedbackReceiver,currentUser.getUserId(), PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(FeedbackView::new)
                    .collect(Collectors.toList());
            feedbackPager = new Pager<>(limit, feedbackRepository.findByFilterTypeAndFeedbackReceiverMentorCount(filterType,feedbackReceiver,currentUser.getUserId()), page);
            feedbackPager.setResult(feedbackList);
            if (!feedbackList.isEmpty()) {
                return feedbackPager;
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText(FEEDBACK_NOT_FOUND, null, "en"));
            }
        }
        if (filterType != null) {
            feedbackList = StreamSupport.stream(feedbackRepository
                            .findByFilterTypeMentor(filterType,currentUser.getUserId(), PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(FeedbackView::new)
                    .collect(Collectors.toList());
            feedbackPager = new Pager<>(limit, feedbackRepository.findByFilterTypeMentorCount(filterType,currentUser.getUserId()), page);
            feedbackPager.setResult(feedbackList);
            if (!feedbackList.isEmpty()) {
                return feedbackPager;
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText(FEEDBACK_NOT_FOUND, null, "en"));
            }
        }
        if (feedbackReceiver != null) {
            feedbackList = StreamSupport.stream(feedbackRepository
                            .findByFeedbackReceiverMentor(feedbackReceiver,currentUser.getUserId(), PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(FeedbackView::new)
                    .collect(Collectors.toList());
            feedbackPager = new Pager<>(limit, feedbackRepository.findByFeedbackReceiverMentorCount(feedbackReceiver,currentUser.getUserId()), page);
            feedbackPager.setResult(feedbackList);
            if (!feedbackList.isEmpty()) {
                return feedbackPager;
            } else {

                throw new BadRequestException(languageUtil.getTranslatedText(FEEDBACK_NOT_FOUND, null, "en"));
            }
        }
        feedbackList = StreamSupport.stream(feedbackRepository
                        .findByMentorFeedback(currentUser.getUserId(), PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(FeedbackView::new)
                .collect(Collectors.toList());
        feedbackPager = new Pager<>(limit, feedbackRepository.findByMentorFeedbackCount(currentUser.getUserId()), page);
        feedbackPager.setResult(feedbackList);
        return feedbackPager;
    }

    private Pager<FeedbackView> checkIfFeedbackIsEmpty(List<FeedbackView> feedbackList, Pager<FeedbackView> feedbackPager) {
        if (!feedbackList.isEmpty()) {
            return feedbackPager;
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(FEEDBACK_NOT_FOUND, null, "en"));
        }
    }


    @Override
    public FeedbackView updateFeedback(Integer feedbackId, UserFeedbackForm form) {
        Optional<UserFeedback> userFeedback1=feedbackRepository.findByFeedbackIdAndStatus(feedbackId,UserFeedback.Status.ACTIVE.value);
        if(userFeedback1.isPresent() && (!userFeedback1.get().getFeedbackProvider().getUserId().equals(SecurityUtil.getCurrentUserId()))){
            throw new BadRequestException(languageUtil.getTranslatedText("no.permission", null, "en"));
        }
            basicChecking(form);
            User feedbackProvider = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
            if (form.getFeedbackReceiver() == null) {
                throw new BadRequestException(languageUtil.getTranslatedText(USER_NOT_FOUND, null, "en"));
            }
        User feedbackReceiver = userRepository.findByUserIdAndStatus(form.getFeedbackReceiver(),User.Status.ACTIVE.value);
            feedbackReceiverChecking(feedbackReceiver, feedbackProvider);

            long daysBetween = ChronoUnit.DAYS.between(form.getFeedbackStart(), form.getFeedbackEnd());

            if (daysBetween > 27 && daysBetween < 32) {
                //monthly feedback
                //fetching current month details
                LocalDate date = LocalDate.now();
                LocalDate firstOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

                //fetching previous month details
                LocalDate earlier = date.minusMonths(1);
                LocalDate firstOfPreviousMonth = earlier.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endOfPreviousMonth = earlier.with(TemporalAdjusters.lastDayOfMonth());

                if (form.getFeedbackType() != 1) {
                    throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_TYPE, null, "en"));
                }
                checkCurrentMonth(form, date, earlier);
                if (form.getFeedbackStart().getMonth() == date.getMonth()) {
                    currentMonthChecking(form, firstOfMonth, endOfMonth);
                }
                if (form.getFeedbackStart().getMonth() == earlier.getMonth()) {
                    previousMonthChecking(form, firstOfPreviousMonth, endOfPreviousMonth);
                }
                return feedbackUpdate( form, feedbackId);
            } else if (daysBetween == 6) {

                //weekly feedback

                if (form.getFeedbackType() != 2) {
                    throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_TYPE, null, "en"));
                }

                LocalDate date = LocalDate.now();
                WeekFields weekFields = WeekFields.of(Locale.FRANCE);
                //fetching current week details
                int currentWeek = date.get(weekFields.weekOfWeekBasedYear());
                int currentWeekStart = form.getFeedbackStart().get(weekFields.weekOfWeekBasedYear());
                int currentWeekEnd = form.getFeedbackEnd().get(weekFields.weekOfWeekBasedYear());
                weekChecking(currentWeek, currentWeekStart, currentWeekEnd, form);
                UserFeedback userFeedback = feedbackRepository.findByFeedbackIdAndStatus(feedbackId, UserFeedback.Status.ACTIVE.value).orElseThrow(() -> new
                        BadRequestException(languageUtil.getTranslatedText("manager.not.found", null, "en")));

                userFeedback.setFeedbackStart(form.getFeedbackStart());
                userFeedback.setFeedbackEnd(form.getFeedbackEnd());
                userFeedback.setComment(form.getComment());
                feedbackRepository.save(userFeedback);
                return new FeedbackView(userFeedback);
            } else {
                throw new BadRequestException(languageUtil.getTranslatedText(INVALID_FEEDBACK_TYPE, null, "en"));
            }
        }

    private FeedbackView feedbackUpdate(UserFeedbackForm form, Integer feedbackId) {
        scoreRequiredChecking(form);
        scoreRequiredChecking2(form);
        if (form.getApproachable() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getConstructive() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getSynergy() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getRespect() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        if (form.getSelflessness() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(SCORE_REQUIRED, null, "en"));
        }
        UserFeedback userFeedback = feedbackRepository.findByFeedbackIdAndStatus(feedbackId, UserFeedback.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(FEEDBACK_NOT_FOUND, null, "en")));
        userFeedback.setFeedbackStart(form.getFeedbackStart());
        userFeedback.setFeedbackEnd(form.getFeedbackEnd());
        userFeedback.setComment(form.getComment());
        userFeedback.setAdaptability(form.getAdaptability());
        userFeedback.setAnalytical(form.getAnalytical());
        userFeedback.setImplementation(form.getImplementation());
        userFeedback.setJobKnowledge(form.getJobKnowledge());
        userFeedback.setQuality(form.getQuality());
        userFeedback.setValidation(form.getValidation());
        userFeedback.setExpressIdeas(form.getExpressIdeas());
        userFeedback.setEfficientChannelUse(form.getEfficientChannelUse());
        userFeedback.setOral(form.getOral());
        userFeedback.setInfoSharing(form.getInfoSharing());
        userFeedback.setWritten(form.getWritten());
        userFeedback.setResponsibility(form.getResponsibility());
        userFeedback.setDecision(form.getDecision());
        userFeedback.setInnovation(form.getInnovation());
        userFeedback.setOnTime(form.getOnTime());
        userFeedback.setExcellence(form.getExcellence());
        userFeedback.setPleasantNature(form.getPleasantNature());
        userFeedback.setPunctual(form.getPunctual());
        userFeedback.setHierarchy(form.getHierarchy());
        userFeedback.setUpgradation(form.getUpgradation());
        userFeedback.setApproachable(form.getApproachable());
        userFeedback.setConstructive(form.getConstructive());
        userFeedback.setSynergy(form.getSynergy());
        userFeedback.setRespect(form.getRespect());
        userFeedback.setSelflessness(form.getSelflessness());
        feedbackRepository.save(userFeedback);
        return new FeedbackView(userFeedback);
    }

}
