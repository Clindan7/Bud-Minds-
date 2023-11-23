package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.Score;
import com.innovaturelabs.buddymanagement.entity.Task;
import com.innovaturelabs.buddymanagement.entity.TraineeTask;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.ScoreForm;
import com.innovaturelabs.buddymanagement.repository.ScoreRepository;
import com.innovaturelabs.buddymanagement.repository.TaskRepository;
import com.innovaturelabs.buddymanagement.repository.TraineeTaskRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.ScoreService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.ScoreView;
import com.innovaturelabs.buddymanagement.view.UserScoreListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TraineeTaskRepository traineeTaskRepository;

    private static final String NO_PERMISSION = "permission.not.allowed";

    private static final String TRAINEE_TASK_NOT_FOUND = "trainee.task.id.not.found";

    private static final String TASK_VALUATION_NOT_FOUND="taskValuation.not.found";

    private static final String TASK_NOT_FOUND="task.not.found";

    @Override
    public void scoreAdd(ScoreForm form) {

        if (form.getTraineeTaskId() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en"));
        }
        TraineeTask traineeTask = traineeTaskRepository.findById(form.getTraineeTaskId()).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en")));
        if (traineeTask.getTraineeId().getMentorId() != null) {
            if (!traineeTask.getTraineeId().getMentorId().getUserId().equals(SecurityUtil.getCurrentUserId())) {
                throw new BadRequestException(languageUtil.getTranslatedText(NO_PERMISSION, null, "en"));
            }
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(NO_PERMISSION, null, "en"));
        }
        Task task = taskRepository.findById(traineeTask.getTaskId().getTaskId()).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        if (task.getTaskParentId() != null) {
            throw new BadRequestException(languageUtil.getTranslatedText("taskValuation.cannot.given", null, "en"));
        }

        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
        if (score != null) {
            throw new BadRequestException(languageUtil.getTranslatedText("taskValuation.already.given", null, "en"));
        }
        Integer calculatedScore = calculateScore(form);
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        User scoreAssignee = traineeTask.getTraineeId();
        byte completionTime = traineeTask.getStatus();
        byte completionStatus = 0;
        if (completionTime == TraineeTask.Status.PENDING.value) {
            completionTime = 15;
            completionStatus = 0;
        } else if (completionTime == TraineeTask.Status.COMPLETED.value) {
            completionTime = 5;
            completionStatus = 2;
        } else if (completionTime == TraineeTask.Status.DELAYED.value) {
            completionTime = 0;
            completionStatus = 1;
        }

        Integer overAllScore = calculatedScore + completionTime;
        scoreRepository.save(new Score(traineeTask, user, scoreAssignee, completionStatus, form, overAllScore));
    }

    @Override
    public void scoreUpdate(ScoreForm form, Integer scoreId) {

        Score score = scoreRepository.findByScoreId(scoreId);
        if (score == null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TASK_VALUATION_NOT_FOUND, null, "en"));
        }

        TraineeTask traineeTask = traineeTaskRepository.findById(score.getTraineeTaskId().getTraineeTaskId())
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText(TRAINEE_TASK_NOT_FOUND, null, "en")));
        if (traineeTask.getTraineeId().getMentorId() != null) {
            if (!traineeTask.getTraineeId().getMentorId().getUserId().equals(SecurityUtil.getCurrentUserId())) {
                throw new BadRequestException(languageUtil.getTranslatedText(NO_PERMISSION, null, "en"));
            }
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(NO_PERMISSION, null, "en"));
        }
        Task task = taskRepository.findById(traineeTask.getTaskId().getTaskId()).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        if (task.getTaskParentId() != null) {
            throw new BadRequestException(languageUtil.getTranslatedText("taskValuation.cannot.given", null, "en"));
        }

        Integer calculatedScore = calculateScore(form);
        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        byte completionTime = traineeTask.getStatus();
        byte completionStatus = 0;
        if (completionTime == TraineeTask.Status.PENDING.value) {
            completionTime = 15;
            completionStatus = 0;
        } else if (completionTime == TraineeTask.Status.COMPLETED.value) {
            completionTime = 5;
            completionStatus = 2;
        } else if (completionTime == TraineeTask.Status.DELAYED.value) {
            completionTime = 0;
            completionStatus = 1;
        }
        Integer overAllScore = calculatedScore + completionTime;

        score.setScoreAssigner(user);
        score.setCompletionTime(completionStatus);
        score.setAdaptability(form.getAdaptability());
        score.setEnthusisamToLearn(form.getEnthusiamToLearn());
        score.setExtraEffort(form.getExtraEffort());
        score.setQualityOfWork(form.getQualityOfWork());
        score.setTeamWork(form.getTeamWork());
        score.setComment(form.getComment());
        score.setOverallScore(overAllScore);
        LocalDateTime dt = LocalDateTime.now();
        score.setUpdateDate(dt);
        scoreRepository.save(score);
    }

    @Override
    public ScoreView fetchScoreDetail(Integer traineeTaskId) {
        User user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        Score score = scoreRepository.findByTraineeTaskIdAndStatus(traineeTaskId, Score.Status.ACTIVE.value)
                .orElseThrow(() -> new BadRequestException(
                        languageUtil.getTranslatedText(TASK_VALUATION_NOT_FOUND, null, "en")));
        if (user.getUserRole() == 2 && (!score.getScoreAssigner().getUserId().equals(user.getUserId()))) {
            throw new BadRequestException(languageUtil.getTranslatedText(NO_PERMISSION, null, "en"));
        }
        return new ScoreView(score);
    }

    public Integer calculateScore(ScoreForm form) {
        Integer teamWork = 0;
        Integer isEnthusist = 0;
        Integer qualityGood = 0;
        Integer adaptability = 0;
        Integer extraEffort = 0;

        if (form.getTeamWork() == Score.TeamWork.GOOD.value) {
            teamWork = 15;
        } else if (form.getTeamWork() == Score.TeamWork.AVERAGE.value) {
            teamWork = 5;
        }

        if (form.getEnthusiamToLearn() == Score.EnthusiamToLearn.PASSIONATE.value) {
            isEnthusist = 15;
        } else if (form.getEnthusiamToLearn() == Score.EnthusiamToLearn.AVERAGE.value) {
            isEnthusist = 5;
        }

        if (form.getQualityOfWork() == Score.QualityOfWork.GOOD.value) {
            qualityGood = 20;
        } else if (form.getQualityOfWork() == Score.QualityOfWork.MODERATE.value) {
            qualityGood = 10;
        } else if (form.getQualityOfWork() == Score.QualityOfWork.BELOW_AVERAGE.value) {
            qualityGood = 5;
        }

        if (form.getAdaptability() == Score.Adaptability.IMPRESSIVE.value) {
            adaptability = 20;
        } else if (form.getAdaptability() == Score.Adaptability.MODERATE.value) {
            adaptability = 10;
        }

        if (form.getExtraEffort() == Score.ExtraEffort.YES.value) {
            extraEffort = 15;
        } else if (form.getExtraEffort() == Score.ExtraEffort.DO_NOT_KNOW.value) {
            extraEffort = 5;
        }

        return teamWork + isEnthusist + qualityGood + adaptability + extraEffort;
    }

    public Pager<UserScoreListView> listTaskWithScore(Integer taskId, Integer mentorId, Integer traineeId,
            Integer range, Integer managerId, Integer page, Integer limit) {

        User user = userRepository.findByUserIdAndStatus(SecurityUtil.getCurrentUserId(), User.Status.ACTIVE.value);
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en")));
        if (task.getTaskParentId() != null) {
            throw new BadRequestException(languageUtil.getTranslatedText(TASK_NOT_FOUND, null, "en"));
        }
        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        if (user.getUserRole() == 2) {
            return listTaskWithScoreForMentor(taskId, traineeId, range, managerId, page, limit);
        }
        if (traineeId != null && mentorId != null && managerId != null && range != null) {
            return listWithAllFilter(taskId, traineeId, mentorId, managerId, range, page, limit);
        }
        if (traineeId != null && mentorId != null && managerId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository.findByTraineeIdMentorIdManagerId(traineeId,
                    mentorId, managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByTraineeIdMentorIdManagerId(traineeId, mentorId, managerId, taskId),
                    page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (traineeId != null && mentorId != null && range != null) {
            return listWithTraineeMentorRange(taskId, traineeId, mentorId, range, page, limit);
        }
        if (traineeId != null && managerId != null && range != null) {
            return listWithTraineeManagerAndRange(taskId,traineeId,managerId,range,page,limit);
        }

        return listFilter2(taskId, traineeId, mentorId, managerId, range, page, limit);
    }

    public Pager<UserScoreListView> listFilter2(Integer taskId, Integer traineeId, Integer mentorId, Integer managerId,
            Integer range, Integer page, Integer limit) {

        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        Integer rangeStart = 0;
        Integer rangeEnd = 0;
        if (range != null) {
            int[] rangeArray = scoreRangeCheck(range, rangeStart, rangeEnd);
            rangeStart = rangeArray[0];
            rangeEnd = rangeArray[1];
        }
        if (mentorId != null && managerId != null && range != null) {
            return listWithMentorManagerRange(taskId, mentorId, managerId, range, page, limit);
        }
        if (traineeId != null && mentorId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByTraineeIdMentorId(traineeId, mentorId, taskId, PageRequest.of(page - 1, limit))
                    .spliterator(), false).map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByTraineeIdMentorId(traineeId, mentorId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (traineeId != null && managerId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByTraineeManagerId(traineeId, managerId, taskId, PageRequest.of(page - 1, limit))
                    .spliterator(), false).map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByTraineeManagerId(traineeId, managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (mentorId != null && managerId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByMentorIdManagerId(mentorId, managerId, taskId, PageRequest.of(page - 1, limit))
                    .spliterator(), false).map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByMentorIdManagerId(mentorId, managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (traineeId != null && range != null) {
            if (range == 0) {
                userScoreList = StreamSupport.stream(traineeTaskRepository
                        .findByTraineeIdAndRangeNoScore(traineeId, taskId, PageRequest.of(page - 1, limit))
                        .spliterator(), false).map(traineeTask -> {
                            Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                            int overallScore = Optional.ofNullable(score)
                                    .map(Score::getOverallScore)
                                    .orElse(0);
                            return new UserScoreListView(traineeTask, overallScore);
                        }).collect(Collectors.toList());
                userScoreListPager = new Pager<>(limit,
                        traineeTaskRepository.countByTraineeIdAndRangeNoScore(traineeId, taskId), page);
                userScoreListPager.setResult(userScoreList);
                return checkIfValuationList(userScoreList, userScoreListPager);
            } else {
                userScoreList = StreamSupport.stream(traineeTaskRepository.findByTraineeIdAndRange(rangeStart, rangeEnd,
                        traineeId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false).map(traineeTask -> {
                            Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                            int overallScore = Optional.ofNullable(score)
                                    .map(Score::getOverallScore)
                                    .orElse(0);
                            return new UserScoreListView(traineeTask, overallScore);
                        }).collect(Collectors.toList());
                userScoreListPager = new Pager<>(limit,
                        traineeTaskRepository.countByTraineeIdAndRange(rangeStart, rangeEnd, traineeId, taskId), page);
                userScoreListPager.setResult(userScoreList);
                return checkIfValuationList(userScoreList, userScoreListPager);
            }
        }

        return listFilter3(taskId, traineeId, mentorId, managerId, range, page, limit);
    }

    public Pager<UserScoreListView> listFilter3(Integer taskId, Integer traineeId, Integer mentorId, Integer managerId,
            Integer range, Integer page, Integer limit) {

        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        Integer rangeStart = 0;
        Integer rangeEnd = 0;
        if (range != null) {
            int[] rangeArray = scoreRangeCheck(range, rangeStart, rangeEnd);
            rangeStart = rangeArray[0];
            rangeEnd = rangeArray[1];
        }
        if (mentorId != null && range != null) {
            return listWithMentorAndRange(taskId, mentorId, range, rangeStart, rangeEnd, page, limit);
        }
        if (managerId != null && range != null) {
            if (range == 0) {
                userScoreList = StreamSupport.stream(traineeTaskRepository
                        .findByManagerIdAndRangeNoScore(managerId, taskId, PageRequest.of(page - 1, limit))
                        .spliterator(), false).map(traineeTask -> {
                            Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                            int overallScore = Optional.ofNullable(score)
                                    .map(Score::getOverallScore)
                                    .orElse(0);
                            return new UserScoreListView(traineeTask, overallScore);
                        }).collect(Collectors.toList());
                userScoreListPager = new Pager<>(limit,
                        traineeTaskRepository.countByManagerIdAndRangeNoScore(managerId, taskId), page);
                userScoreListPager.setResult(userScoreList);
                return checkIfValuationList(userScoreList, userScoreListPager);
            } else {
                userScoreList = StreamSupport.stream(traineeTaskRepository.findByManagerIdAndRange(rangeStart, rangeEnd,
                        managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false).map(traineeTask -> {
                            Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                            int overallScore = Optional.ofNullable(score)
                                    .map(Score::getOverallScore)
                                    .orElse(0);
                            return new UserScoreListView(traineeTask, overallScore);
                        }).collect(Collectors.toList());
                userScoreListPager = new Pager<>(limit,
                        traineeTaskRepository.countByManagerIdAndRange(rangeStart, rangeEnd, managerId, taskId), page);
                userScoreListPager.setResult(userScoreList);
                return checkIfValuationList(userScoreList, userScoreListPager);
            }
        }
        if (traineeId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByTraineeId(traineeId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByTraineeId(traineeId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (mentorId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByMentorId(mentorId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByMentorId(mentorId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (managerId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByManagerId(managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByManagerId(managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (range != null) {
            if (range == 0) {
                userScoreList = StreamSupport.stream(
                        traineeTaskRepository.findByRangeNoScore(taskId, PageRequest.of(page - 1, limit)).spliterator(),
                        false).map(traineeTask -> {
                            Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                            int overallScore = Optional.ofNullable(score)
                                    .map(Score::getOverallScore)
                                    .orElse(0);
                            return new UserScoreListView(traineeTask, overallScore);
                        }).collect(Collectors.toList());

                userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByRangeNoScore(taskId), page);
                userScoreListPager.setResult(userScoreList);
                return userScoreListPager;
            } else {
                userScoreList = StreamSupport.stream(traineeTaskRepository
                        .findByRange(rangeStart, rangeEnd, taskId, PageRequest.of(page - 1, limit)).spliterator(),
                        false).map(traineeTask -> {
                            Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                            int overallScore = Optional.ofNullable(score)
                                    .map(Score::getOverallScore)
                                    .orElse(0);
                            return new UserScoreListView(traineeTask, overallScore);
                        }).collect(Collectors.toList());
                userScoreListPager = new Pager<>(limit,
                        traineeTaskRepository.countByRange(rangeStart, rangeEnd, taskId), page);
                userScoreListPager.setResult(userScoreList);
                return userScoreListPager;
            }
        }
        userScoreList = StreamSupport
                .stream(traineeTaskRepository.findByTask(taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(traineeTask -> {
                    Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                    int overallScore = Optional.ofNullable(score)
                            .map(Score::getOverallScore)
                            .orElse(0);
                    return new UserScoreListView(traineeTask, overallScore);
                }).collect(Collectors.toList());
        userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByTask(taskId), page);
        userScoreListPager.setResult(userScoreList);
        return userScoreListPager;
    }

    public Pager<UserScoreListView> listWithAllFilter(Integer taskId, Integer traineeId, Integer mentorId,
            Integer managerId, Integer range, Integer page, Integer limit) {
        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        int[] rangeArray = scoreRangeCheck(range, 0, 0);
        Integer rangeStart = rangeArray[0];
        Integer rangeEnd = rangeArray[1];

        if (range == 0) {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByTraineeIdMentorIdManagerIdRangeNoScore(traineeId, mentorId,
                            managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository
                    .countByTraineeIdMentorIdManagerIdRangeNoScore(traineeId, mentorId, managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        } else {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByTraineeIdMentorIdManagerIdRange(rangeStart, rangeEnd, traineeId,
                            mentorId, managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByTraineeIdMentorIdManagerIdRange(
                    rangeStart, rangeEnd, traineeId, mentorId, managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
    }

    public Pager<UserScoreListView> listWithTraineeMentorRange(Integer taskId, Integer traineeId, Integer mentorId,
            Integer range, Integer page, Integer limit) {

        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        int[] rangeArray = scoreRangeCheck(range, 0, 0);
        Integer rangeStart = rangeArray[0];
        Integer rangeEnd = rangeArray[1];

        if (range == 0) {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByTraineeIdMentorIdAndRangeNoScore(traineeId, mentorId,
                            taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByTraineeIdMentorIdAndRangeNoScore(traineeId, mentorId, taskId),
                    page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        } else {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByTraineeIdMentorIdAndRange(rangeStart, rangeEnd, traineeId,
                            mentorId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository
                    .countByTraineeIdMentorIdAndRange(rangeStart, rangeEnd, traineeId, mentorId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
    }

    public Pager<UserScoreListView> listWithTraineeManagerAndRange(Integer taskId,Integer traineeId,Integer managerId,Integer range,Integer page,Integer limit){
        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        int[] rangeArray = scoreRangeCheck(range, 0, 0);
        Integer rangeStart = rangeArray[0];
        Integer rangeEnd = rangeArray[1];

        if (range == 0) {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByTraineeManagerIdAndRangeNoScore(traineeId, managerId,
                            taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByTraineeManagerIdAndRangeNoScore(traineeId, managerId, taskId),
                    page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        } else {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByTraineeManagerIdAndRange(rangeStart, rangeEnd, traineeId,
                            managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository
                    .countByTraineeManagerIdAndRange(rangeStart, rangeEnd, traineeId, managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
    }
    public Pager<UserScoreListView> listWithMentorManagerRange(Integer taskId, Integer mentorId, Integer managerId,
            Integer range, Integer page, Integer limit) {
        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        int[] rangeArray = scoreRangeCheck(range, 0, 0);
        Integer rangeStart = rangeArray[0];
        Integer rangeEnd = rangeArray[1];

        if (range == 0) {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByMentorIdManagerIdAndRangeNoScore(mentorId, managerId,
                            taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByMentorIdManagerIdAndRangeNoScore(mentorId, managerId, taskId),
                    page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        } else {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByMentorIdManagerIdAndRange(rangeStart, rangeEnd, mentorId,
                            managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository
                    .countByMentorIdManagerIdAndRange(rangeStart, rangeEnd, mentorId, managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
    }

    public Pager<UserScoreListView> listWithMentorAndRange(Integer taskId, Integer mentorId, Integer range,
            Integer rangeStart, Integer rangeEnd, Integer page, Integer limit) {
        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        if (range == 0) {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByMentorIdAndRangeNoScore(mentorId, taskId, PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByMentorIdAndRangeNoScore(mentorId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        } else {
            userScoreList = StreamSupport.stream(traineeTaskRepository
                    .findByMentorIdAndRange(rangeStart, rangeEnd, mentorId, taskId, PageRequest.of(page - 1, limit))
                    .spliterator(), false).map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByMentorIdAndRange(rangeStart, rangeEnd, mentorId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
    }

    public Pager<UserScoreListView> listTaskWithScoreForMentor(Integer taskId, Integer traineeId, Integer range,
            Integer managerId, Integer page, Integer limit) {
        List<UserScoreListView> userScoreList;
        Pager<UserScoreListView> userScoreListPager;
        Integer rangeStart = 0;
        Integer rangeEnd = 0;
        if (range != null) {
            int[] rangeArray = scoreRangeCheck(range, 0, 0);
            rangeStart = rangeArray[0];
            rangeEnd = rangeArray[1];
        }
        if (traineeId != null && managerId != null && range != null) {
            return listWithAllFilter(taskId, traineeId, SecurityUtil.getCurrentUserId(), managerId, range, page, limit);
        }
        if (traineeId != null && managerId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository.findByTraineeIdMentorIdManagerId(traineeId,
                    SecurityUtil.getCurrentUserId(), managerId, taskId, PageRequest.of(page - 1, limit)).spliterator(),
                    false).map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit, traineeTaskRepository.countByTraineeIdMentorIdManagerId(traineeId,
                    SecurityUtil.getCurrentUserId(), managerId, taskId), page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }

        if (traineeId != null && range != null) {
            return listWithTraineeMentorRange(taskId, traineeId, SecurityUtil.getCurrentUserId(), range, page, limit);
        }

        if (managerId != null && range != null) {
            return listWithMentorManagerRange(taskId, SecurityUtil.getCurrentUserId(), managerId, range, page, limit);
        }

        if (traineeId != null) {
            userScoreList = StreamSupport.stream(traineeTaskRepository.findByTraineeIdMentorId(traineeId,
                    SecurityUtil.getCurrentUserId(), taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByTraineeIdMentorId(traineeId, SecurityUtil.getCurrentUserId(), taskId),
                    page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }

        if (managerId != null) {
            userScoreList = StreamSupport
                    .stream(traineeTaskRepository.findByMentorIdManagerId(SecurityUtil.getCurrentUserId(), managerId,
                            taskId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(traineeTask -> {
                        Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                        int overallScore = Optional.ofNullable(score)
                                .map(Score::getOverallScore)
                                .orElse(0);
                        return new UserScoreListView(traineeTask, overallScore);
                    }).collect(Collectors.toList());
            userScoreListPager = new Pager<>(limit,
                    traineeTaskRepository.countByMentorIdManagerId(SecurityUtil.getCurrentUserId(), managerId, taskId),
                    page);
            userScoreListPager.setResult(userScoreList);
            return checkIfValuationList(userScoreList, userScoreListPager);
        }
        if (range != null) {
            return listWithMentorAndRange(taskId, SecurityUtil.getCurrentUserId(), range, rangeStart, rangeEnd, page,
                    limit);
        }
        userScoreList = StreamSupport.stream(traineeTaskRepository
                .findByMentorId(SecurityUtil.getCurrentUserId(), taskId, PageRequest.of(page - 1, limit)).spliterator(),
                false).map(traineeTask -> {
                    Score score = scoreRepository.findByTraineeTaskId(traineeTask);
                    int overallScore = Optional.ofNullable(score)
                            .map(Score::getOverallScore)
                            .orElse(0);
                    return new UserScoreListView(traineeTask, overallScore);
                }).collect(Collectors.toList());
        userScoreListPager = new Pager<>(limit,
                traineeTaskRepository.countByMentorId(SecurityUtil.getCurrentUserId(), taskId), page);
        userScoreListPager.setResult(userScoreList);
        return userScoreListPager;
    }

    public Pager<UserScoreListView> checkIfValuationList(List<UserScoreListView> userScoreList,
            Pager<UserScoreListView> userScoreListPager) {
        if (!userScoreList.isEmpty()) {
            return userScoreListPager;
        } else {
            throw new BadRequestException(languageUtil.getTranslatedText(TASK_VALUATION_NOT_FOUND, null, "en"));
        }
    }

    public int[] scoreRangeCheck(Integer range, Integer rangeStart, Integer rangeEnd) {
        if (range != 0 && range != 1 && range != 2 && range != 3) {
            throw new BadRequestException(languageUtil.getTranslatedText("taskValuation.invalid.range", null, "en"));
        }
        if (range == 1) {
            rangeStart = 0;
            rangeEnd = 50;
        } else if (range == 2) {
            rangeStart = 50;
            rangeEnd = 70;
        } else if (range == 3) {
            rangeStart = 70;
            rangeEnd = 101;
        } else if (range == 0) {
            rangeStart = 0;
            rangeEnd = 101;
        }
        return new int[] { rangeStart, rangeEnd };
    }

}