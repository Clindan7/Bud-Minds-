package com.innovaturelabs.buddymanagement.service.impl;


import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.entity.Training;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TrainingForm;
import com.innovaturelabs.buddymanagement.repository.TechnologyRepository;
import com.innovaturelabs.buddymanagement.repository.TrainingRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.TrainingService;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TrainingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TrainingServiceImpl implements TrainingService {
    Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private static final String TRAINING_NOT_FOUND = "training.not.found";
    private static final String POSITIVE_DEPARTMENT = "positive.department";
    private static final String TRAINING_NOT_FOUND_LOG = "training not found";
    private static final String TECHNOLOGY_NOT_FOUND = "technology.not.found";
    private static final String INVALID_DEPARTMENT = "invalid.department";

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LanguageUtil languageUtil;

    @Override
    public Pager<TrainingView> listTrainings(String search, Integer technologyId, byte departmentId, Integer page, Integer limit) {
        Pager<TrainingView> trainingPager;
        List<TrainingView> trainingList;
        if (technologyId != null && search != null && departmentId != 0) {
            trainingList = StreamSupport.stream(trainingRepository
                            .findByDepartmentIdTechnologyIdAndSearch(departmentId, technologyId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }
        if (technologyId != null && search != null) {
            trainingList = StreamSupport.stream(trainingRepository
                            .findByTechnologyIdAndSearch(technologyId, search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }
        if (technologyId != null && departmentId != 0) {
            trainingList = StreamSupport.stream(trainingRepository
                            .findByDepartmentIdTechnologyId(departmentId, technologyId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }
        if (search != null && departmentId != 0) {
            trainingList = StreamSupport.stream(trainingRepository
                            .findByDepartmentIdSearch(search, departmentId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }

        if (technologyId != null) {
            Technology technology = technologyRepository.findByTechnologyId(technologyId).orElseThrow(() -> new
                    BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en")));
            trainingList = StreamSupport.stream(trainingRepository
                            .findByTechnologyId(technology, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }
        if (departmentId > 0) {
            if (departmentId > 2) {
                throw new BadRequestException(languageUtil.getTranslatedText(INVALID_DEPARTMENT, null, "en"));
            }
            trainingList = StreamSupport.stream(trainingRepository
                            .findByDepartmentId(departmentId, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }
        if (departmentId < 0) {
            throw new BadRequestException(languageUtil.getTranslatedText(POSITIVE_DEPARTMENT, null, "en"));
        }
        if (search != null) {
            trainingList = StreamSupport.stream(trainingRepository
                            .findBySearch(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(TrainingView::new)
                    .collect(Collectors.toList());
            trainingPager = new Pager<>(limit, trainingList.size(), page);
            trainingPager.setResult(trainingList);
            return checkIfTrainingList(trainingList, trainingPager);
        }
        trainingList = StreamSupport.stream(trainingRepository
                        .findAllTraining(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(TrainingView::new)
                .collect(Collectors.toList());
        trainingPager = new Pager<>(limit, trainingRepository.findByTrainingName().size(), page);
        trainingPager.setResult(trainingList);
        return trainingPager;
    }

    public Pager<TrainingView> checkIfTrainingList(List<TrainingView> trainingList, Pager<TrainingView> trainingPager) {
        if (!trainingList.isEmpty()) {
            return trainingPager;
        } else {
            log.error(TRAINING_NOT_FOUND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en"));
        }
    }

    @Override
    public TrainingView trainingAdd(TrainingForm form) {
        LocalDate dateNow = LocalDate.now();
        dateChecking(form,dateNow);
        Technology technology = technologyRepository.findByTechnologyId(form.getTechnologyId()).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TECHNOLOGY_NOT_FOUND, null, "en")));

        Training training = trainingRepository.save(new Training(
                form.getTitle(),
                form.getTrainingDescription(),
                form.getTrainingStartDate(),
                form.getTrainingEndDate(),
                SecurityUtil.getCurrentUserId(),
                technology,
                form.getDepartmentId()
        ));
        return new TrainingView(training);
    }

    @Override
    public TrainingView fetchTraining(Integer trainingId) {
        return new TrainingView(trainingRepository.findByTrainingIdAndStatus(trainingId, Training.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en"))));
    }

    @Override
    public TrainingView updateTraining(Integer trainingId, TrainingForm form) {
        LocalDate dateNow = LocalDate.now();
        Training training = trainingRepository.findByTrainingIdAndStatus(trainingId, Training.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en")));

        dateChecking(form,dateNow);
        Optional<Technology> optionalTechnology = technologyRepository.findByTechnologyId(form.getTechnologyId());
        if (optionalTechnology.isEmpty()) {
            throw new BadRequestException(languageUtil.getTranslatedText(TECHNOLOGY_NOT_FOUND, null, "en"));
        }

        training.setTitle(form.getTitle());
        training.setTrainingDescription(form.getTrainingDescription());
        training.setTrainingStartDate(form.getTrainingStartDate());
        training.setTrainingEndDate(form.getTrainingEndDate());
        training.setTechnologyId(optionalTechnology.get());
        training.setDepartmentId(form.getDepartmentId());
        training.setUpdateDate(LocalDateTime.now());
        trainingRepository.save(training);
        return new TrainingView(training);
    }

    private void dateChecking(TrainingForm form, LocalDate dateNow) {
        if (!userService.checkDepartment(form.getDepartmentId())) {
            throw new BadRequestException(languageUtil.getTranslatedText(INVALID_DEPARTMENT, null, "en"));
        }
        if (form.getTrainingStartDate() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.required", null, "en"));
        }
        if (form.getTrainingEndDate() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.required", null, "en"));
        }
        if (form.getTechnologyId() == null) {
            throw new BadRequestException(languageUtil.getTranslatedText("technology.required", null, "en"));
        }
        if (form.getTrainingStartDate().isAfter(form.getTrainingEndDate())) {
            throw new BadRequestException(languageUtil.getTranslatedText("end.date.invalid", null, "en"));
        }
        if (form.getTrainingStartDate().isBefore(dateNow)) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.invalid", null, "en"));
        }
    }

    @Override
    public void trainingDelete(Integer trainingId) throws BadRequestException {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TRAINING_NOT_FOUND, null, "en")));
        if (training.getStatus() == User.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("training.already.deleted", null, "en"));
        }
        training.setStatus(Training.Status.INACTIVE.value);
        training.setUpdateDate(LocalDateTime.now());
        trainingRepository.save(training);
    }

    public List<TrainingView> upcomingTrainings() {
        LocalDateTime dateNow = LocalDateTime.now();
        return StreamSupport.stream(trainingRepository.findByUpcomingTraining(dateNow).spliterator(),false).map(TrainingView::new).collect(Collectors.toList());
    }
}
