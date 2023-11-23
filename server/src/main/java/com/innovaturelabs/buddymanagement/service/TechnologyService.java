package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TechnologyForm;
import com.innovaturelabs.buddymanagement.view.TechnologyView;
import com.innovaturelabs.buddymanagement.view.TraineeTechnologyView;

import java.util.List;


public interface TechnologyService {

    List<Technology> listTechnology();
    void technologyDelete(Integer technologyId) throws BadRequestException;
    TechnologyView technologyAdd(TechnologyForm form);


    List<TraineeTechnologyView> traineeTechnologyList();
}
