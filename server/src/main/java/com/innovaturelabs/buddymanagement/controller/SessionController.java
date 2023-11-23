package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.SessionForm;
import com.innovaturelabs.buddymanagement.service.SessionService;
import com.innovaturelabs.buddymanagement.service.TechnologyService;
import com.innovaturelabs.buddymanagement.service.TrainerService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.SessionView;
import com.innovaturelabs.buddymanagement.view.TraineeTechnologyView;
import com.innovaturelabs.buddymanagement.view.TraineeTrainerView;
import com.innovaturelabs.buddymanagement.view.TraineeTrainingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author kiran
 */
@RestController
@RequestMapping("/trainingSession")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TechnologyService technologyService;

    @PostMapping()
    public void sessionCreate(@Valid @RequestBody SessionForm sessionForm) {
        sessionService.sessionCreate(sessionForm);
    }

    @GetMapping()
    public Pager<SessionView> sessionList(@RequestParam(name = "trainingId", required = false) Integer trainingId,
                                          @RequestParam(name = "trainerId", required = false) Integer trainerId,
                                          @RequestParam(name = "joinerGroupId", required = false) Integer groupId,
                                          @RequestParam(name = "technologyId", required = false) Integer technologyId,
                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                          @RequestParam(name = "status", required = false) Byte status) {
        return sessionService.sessionList(trainingId, trainerId, groupId,technologyId, page, limit,status);
    }

    @GetMapping("/{sessionId}")
    public SessionView sessionDetail(@PathVariable("sessionId")Integer sessionId){
        return sessionService.sesssionDetailView(sessionId);
    }

    @PutMapping()
    public void sessionUpdate(@RequestParam(name="sessionId",required=true)Integer sesionId,
                              @Valid @RequestBody SessionForm sessionForm){
        sessionService.sessionUpdate(sessionForm,sesionId);
    }

    @DeleteMapping("/{sessionId}")
    public void sessionDelete(@PathVariable("sessionId")Integer sessionId){
        sessionService.deleteSession(sessionId);
    }

    @PutMapping("/changeSessionStatus")
    public void sessionStatusUpdate(@RequestParam(name="sessionId",required = true) Integer sessionId,
                                    @RequestParam(name="status",required = true)byte status){
        sessionService.sessionStatus(sessionId,status);
    }

    @GetMapping("/trainingList")
    public List<TraineeTrainingView> traineeTrainingList() {
        return sessionService.traineeTrainingList();
    }

    @GetMapping("/trainerList")
    public List<TraineeTrainerView> traineeTrainerList() {
        return trainerService.traineeTrainerList();
    }

    @GetMapping("/technologyList")
    public List<TraineeTechnologyView> traineeTechnologyList() {
        return technologyService.traineeTechnologyList();
    }

    @GetMapping("/groupList")
    public List<SessionView> groupList() {
        return sessionService.groupList();
    }

}
