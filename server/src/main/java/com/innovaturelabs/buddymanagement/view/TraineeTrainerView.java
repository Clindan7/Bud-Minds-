package com.innovaturelabs.buddymanagement.view;

import com.innovaturelabs.buddymanagement.entity.User;

public class TraineeTrainerView {

    private final Integer userId;

    private final String userName;

    public String getUserName() {
        return userName;
    }

    public Integer getUserId() {
        return userId;
    }




    public TraineeTrainerView(User user) {
        this.userId = user.getUserId();
        this.userName=user.getFirstName().concat(" "+user.getLastName());
    }
}
