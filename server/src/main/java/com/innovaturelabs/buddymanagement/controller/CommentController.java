package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.SessionCommentForm;
import com.innovaturelabs.buddymanagement.service.CommentService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.CommentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/session/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/{sessionId}")
    public void addComment(@PathVariable("sessionId") Integer sessionId, @RequestBody @Valid SessionCommentForm commentForm) {
        commentService.addComment(commentForm, sessionId);
    }

    @GetMapping()
    public Pager<CommentView> listComment(@RequestParam(name = "sessionId", required = true) Integer sessionId,
                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return commentService.listComment(sessionId,page,limit);
    }
}
