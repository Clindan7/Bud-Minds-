package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.entity.Training;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TrainingForm;
import com.innovaturelabs.buddymanagement.repository.TrainingRepository;
import com.innovaturelabs.buddymanagement.view.TrainingView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class TrainingServiceImplTest {

    @InjectMocks
    @Autowired
    TrainingServiceImpl trainingService;

    @MockBean
    private TrainingRepository trainingRepository;

    @Test
     void getsPagedTrainingList() {
        byte departmentId = 1;
        int technologyId = 4;
        String search="Training one";
        Training training = new Training(1);
        training.setTrainingId(1);
        training.setTitle("c++");
        training.setTrainingDescription("This training is for c++");
        training.setTrainingCreator(6);
        training.setTrainingStartDate(LocalDate.parse("2023-10-23"));
        training.setTrainingEndDate(LocalDate.parse("2023-10-26"));
        training.setDepartmentId((byte) 1);
        training.setTechnologyId(new Technology(3));
        training.setStatus(Training.Status.ACTIVE.value);
        Page<Training> trainingPage = new PageImpl<>(Collections.singletonList(training));
        when(trainingRepository.findAllTraining(PageRequest.of(1,3))).thenReturn(trainingPage);
        Page<Training> trainings =trainingRepository.findAllTraining(PageRequest.of(1,3));
        when(trainingRepository.findAllTraining(PageRequest.of(1,3))).thenReturn(trainings);
        doThrow(NullPointerException.class).when(trainingRepository).findByDepartmentIdTechnologyIdAndSearch(departmentId, technologyId,search,PageRequest.of(1,3));
        doThrow(NullPointerException.class).when(trainingRepository).findBySearch(search,PageRequest.of(1,3));
        doThrow(NullPointerException.class).when(trainingRepository).findByDepartmentId(departmentId,PageRequest.of(1,3));
        doThrow(NullPointerException.class).when(trainingRepository).findByDepartmentIdTechnologyId(departmentId, technologyId,PageRequest.of(1,3));
        doThrow(NullPointerException.class).when(trainingRepository).findByDepartmentId(departmentId,PageRequest.of(1,3));
        assertThrows(NullPointerException.class,()->trainingService.listTrainings(search,technologyId, departmentId,1,3));
        verify(trainingRepository).findAllTraining(PageRequest.of(1,3));
    }


    @Test
    void mentorTraining() {
        TrainingForm trainingForm = new TrainingForm();
        trainingForm.setTitle("c++");
        trainingForm.setTrainingDescription("#@!@$@#sgfd dsgfgf &^");
        trainingForm.setDepartmentId((byte) 1);
        trainingForm.setTechnologyId(3);
        trainingForm.setTrainingStartDate(LocalDate.parse("2023-12-04"));
        trainingForm.setTrainingEndDate(LocalDate.parse("2023-12-06"));

        Training training = new Training(1);
        training.setTitle("c++");
        training.setTrainingDescription("#@!@$@#sgfd dsgfgf &^");
        training.setDepartmentId((byte) 1);
        training.setTechnologyId(new Technology(3));
        training.setTrainingStartDate(LocalDate.parse("2023-12-04"));
        training.setTrainingEndDate(LocalDate.parse("2023-12-06"));
        doReturn(training).when(trainingRepository).save(ArgumentMatchers.any());
        TrainingView trainingView = new TrainingView(training);
        assertEquals(trainingView.getDepartmentId(), trainingService.trainingAdd(trainingForm).getDepartmentId());
    }

    @Test
    void trainingUpdate() {
        TrainingForm trainingForm = new TrainingForm();
        trainingForm.setTitle("c++");
        trainingForm.setTrainingDescription("#@!@$@#sgfd dsgfgf &^");
        trainingForm.setDepartmentId((byte) 1);
        trainingForm.setTechnologyId(3);
        trainingForm.setTrainingStartDate(LocalDate.parse("2023-12-04"));
        trainingForm.setTrainingEndDate(LocalDate.parse("2023-12-06"));

        Training training = new Training(1);
        LocalDateTime dt = LocalDateTime.now();
        training.setTitle("c++");
        training.setTrainingDescription("#@!@$@#sgfd dsgfgf &^");
        training.setDepartmentId((byte) 1);
        training.setTechnologyId(new Technology(3));
        training.setTrainingStartDate(LocalDate.parse("2023-12-04"));
        training.setTrainingEndDate(LocalDate.parse("2023-12-06"));
        training.setStatus(Training.Status.ACTIVE.value);
        training.setCreateDate(dt);
        training.setUpdateDate(dt);
        doReturn(Optional.of(training)).when(trainingRepository).findByTrainingIdAndStatus(5);
        trainingService.updateTraining(5, trainingForm);
        doReturn(training).when(trainingRepository).save(training);
        TrainingView trainingView = new TrainingView(training);
        assertEquals(trainingView.getTrainingId(), trainingService.updateTraining(5, trainingForm).getTrainingId());
    }

    @Test
    void fetchTraining() {
        int trainingId = 5;
        Training training = new Training(trainingId);
        training.setTitle("c++");
        training.setTrainingDescription("#@!@$@#sgfd dsgfgf &^");
        training.setDepartmentId((byte) 1);
        training.setTechnologyId(new Technology(3));
        training.setTrainingStartDate(LocalDate.parse("2023-12-04"));
        training.setTrainingEndDate(LocalDate.parse("2023-12-06"));
        training.setStatus(Training.Status.ACTIVE.value);
        LocalDateTime dt = LocalDateTime.now();
        training.setCreateDate(dt);
        training.setUpdateDate(dt);

        when(trainingRepository.findByTrainingIdAndStatus(trainingId)).thenReturn(Optional.of(training));
        doThrow(BadRequestException.class).when(trainingRepository).findByTrainingIdAndStatus(5);
        assertThrows(BadRequestException.class, () -> trainingService.fetchTraining(trainingId));
        verify(trainingRepository).findByTrainingIdAndStatus(trainingId);
    }

}