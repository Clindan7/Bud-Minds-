package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.ScoreForm;
import com.innovaturelabs.buddymanagement.service.ScoreService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.ScoreView;
import com.innovaturelabs.buddymanagement.view.UserScoreListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public void scoreAdd(@Valid @RequestBody ScoreForm form) {
        scoreService.scoreAdd(form);
    }

    @GetMapping("/{traineeTaskId}")
    public ScoreView fetchScoreDetail(@PathVariable("traineeTaskId") Integer traineeTaskId) {
        return scoreService.fetchScoreDetail(traineeTaskId);
    }

    @GetMapping()
    public Pager<UserScoreListView> scoreList(@RequestParam(name = "taskId") Integer taskId,
                                              @RequestParam(name = "mentorId", required = false) Integer mentorId,
                                              @RequestParam(name = "traineeId", required = false) Integer traineeId,
                                              @RequestParam(name = "range", required = false) Integer range,
                                              @RequestParam(name = "managerId", required = false) Integer managerId,
                                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                              @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limt) {
        return scoreService.listTaskWithScore(taskId, mentorId, traineeId, range, managerId, page, limt);
    }

    @PutMapping("/{scoreId}")
    public void scoreUpdate(@PathVariable("scoreId") Integer scoreId,
                            @Valid @RequestBody ScoreForm form) {
        scoreService.scoreUpdate(form, scoreId);
    }

}