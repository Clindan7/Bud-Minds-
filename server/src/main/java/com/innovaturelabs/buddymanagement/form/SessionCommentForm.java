package com.innovaturelabs.buddymanagement.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SessionCommentForm {
    @NotBlank(message = "{session.comment.required}")
    @Size(message = ("{session.comment.size}"), max = 250)
    @Pattern(message = "{session.invalid.comment}", regexp = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*+$|(^$)")
    private String comment;

    public String getComment() {
        return comment;
    }
}
