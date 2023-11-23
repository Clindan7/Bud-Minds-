package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.form.TechnologyForm;
import com.innovaturelabs.buddymanagement.service.TechnologyService;
import com.innovaturelabs.buddymanagement.view.TechnologyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author gokul
 */
@RestController
@Validated
@RequestMapping("/technology")
public class TechnologyController {

    @Autowired
    private TechnologyService technologyService;

    @GetMapping()
    public List<Technology> getTechnologyList() {
        return technologyService.listTechnology();
    }

    @PostMapping
    public TechnologyView traineeRegister(@Valid @RequestBody TechnologyForm form) {
        return technologyService.technologyAdd(form);
    }

    @DeleteMapping("/{technologyId}")
    public void technologyDelete(@PathVariable("technologyId") Integer technologyId) {
        technologyService.technologyDelete(technologyId);
    }

}
