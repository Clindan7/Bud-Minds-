package com.innovaturelabs.buddymanagement.controller;


import com.innovaturelabs.buddymanagement.form.TrainingForm;
import com.innovaturelabs.buddymanagement.service.TrainingService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.TrainingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author gokul
 */
@RestController
@Validated
@RequestMapping("/training")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;


    @GetMapping
    public Pager<TrainingView> listTrainings(@RequestParam(name = "search", required = false) String search,
                                             @RequestParam(name = "technologyId", required = false) Integer technologyId,
                                             @RequestParam(name = "departmentId", required = false, defaultValue = "0") byte departmentId,
                                             @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return trainingService.listTrainings(search, technologyId, departmentId, page, limit);
    }

    @PostMapping
    public TrainingView trainingRegister(@Valid @RequestBody TrainingForm form) {
        return trainingService.trainingAdd(form);
    }

    @GetMapping("/{trainingId}")
    public TrainingView fetchTraining(@PathVariable("trainingId") Integer trainingId) {
        return trainingService.fetchTraining(trainingId);
    }

    @PutMapping("/{trainingId}")
    public TrainingView updateTraining(@PathVariable("trainingId") Integer trainingId, @RequestBody @Valid TrainingForm form) {
        return trainingService.updateTraining(trainingId, form);
    }

    @DeleteMapping("/{trainingId}")
    public void trainingDelete(@PathVariable("trainingId") Integer trainingId) {
        trainingService.trainingDelete(trainingId);
    }

    @GetMapping("/upcomingTrainings")
    public List<TrainingView> upcomingTrainings() {
        return trainingService.upcomingTrainings();
    }

}
