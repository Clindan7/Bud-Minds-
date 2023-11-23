package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.form.SessionCommentForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.CommentView;

public interface CommentService {

    void addComment(SessionCommentForm form, Integer sessionId);

    Pager<CommentView> listComment(Integer sessionId,Integer page,Integer limit);
}
