package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.form.UserFeedbackForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.FeedbackView;


public interface FeedbackService {
    void feedbackCreate(UserFeedbackForm form);

    Pager<FeedbackView> listFeedback(Byte filterType, Integer feedbackReceiver, Integer page, Integer limit);

    FeedbackView updateFeedback(Integer feedbackId, UserFeedbackForm form);
}
