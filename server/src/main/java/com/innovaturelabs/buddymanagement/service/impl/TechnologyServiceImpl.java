package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.TechnologyForm;
import com.innovaturelabs.buddymanagement.repository.TechnologyRepository;
import com.innovaturelabs.buddymanagement.service.TechnologyService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.view.TechnologyView;
import com.innovaturelabs.buddymanagement.view.TraineeTechnologyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private LanguageUtil languageUtil;
    private static final String TECHNOLOGY_NOT_FOUND = "technology.not.found";

    @Override
    public List<Technology> listTechnology() {
        List<Technology> technologyList;
        try {
            technologyList = technologyRepository.findAllByStatus();
            return technologyList;
        } catch (BadRequestException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("list.fetch.failed", null, "en"));
        }
    }

    @Override
    public void technologyDelete(Integer technologyId) throws BadRequestException {
        Technology technology = technologyRepository.findByTechnologyId(technologyId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(TECHNOLOGY_NOT_FOUND, null, "en")));
        if (technology.getStatus() == Technology.Status.INACTIVE.value) {
            throw new BadRequestException(languageUtil.getTranslatedText("technology.already.deleted", null, "en"));
        }
        technology.setStatus(Technology.Status.INACTIVE.value);
        technology.setUpdateDate(LocalDateTime.now());
        technologyRepository.save(technology);
    }

    @Override
    public TechnologyView technologyAdd(TechnologyForm form) {
        if (technologyRepository.findByTechnologyName(form.getTechnologyName()).isPresent()) {
            throw new BadRequestException(languageUtil.getTranslatedText("technology.name.exists", null, "en"));
        }
        Technology technology = technologyRepository.save(new Technology(form.getTechnologyName()));
        return new TechnologyView(technology);
    }

    @Override
    public List<TraineeTechnologyView> traineeTechnologyList() {
        List<TraineeTechnologyView> technologyViewList;

        technologyViewList = StreamSupport.stream(technologyRepository
                        .findByAllTechnologyName().spliterator(), false)
                .map(TraineeTechnologyView::new)
                .collect(Collectors.toList());
        return technologyViewList;
    }


}
