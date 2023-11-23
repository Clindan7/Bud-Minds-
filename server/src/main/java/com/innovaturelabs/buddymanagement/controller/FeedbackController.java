package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.UserFeedbackForm;
import com.innovaturelabs.buddymanagement.service.FeedbackService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.FeedbackView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/userFeedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public void feedbackCreate(@Valid @RequestBody UserFeedbackForm form) {
        feedbackService.feedbackCreate(form);
    }

    @GetMapping
    public Pager<FeedbackView> listFeedback(@RequestParam(name = "feedbackType", required = false) Byte filterType,
                                            @RequestParam(name = "feedbackReceiver", required = false) Integer feedbackReceiver,
                                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return feedbackService.listFeedback(filterType, feedbackReceiver, page, limit);
    }

    @PutMapping("/{feedbackId}")
    public FeedbackView updateTask(@PathVariable("feedbackId") Integer feedbackId, @RequestBody @Valid UserFeedbackForm form) {
        return feedbackService.updateFeedback(feedbackId, form);
    }
}
