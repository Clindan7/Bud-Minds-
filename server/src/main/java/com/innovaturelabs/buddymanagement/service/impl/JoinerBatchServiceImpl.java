package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.JoinerBatch;
import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.JoinerBatchForm;
import com.innovaturelabs.buddymanagement.repository.JoinerBatchRepository;
import com.innovaturelabs.buddymanagement.repository.JoinerGroupRepository;
import com.innovaturelabs.buddymanagement.service.JoinerBatchService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.JoinerBatchView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JoinerBatchServiceImpl implements JoinerBatchService {

    Logger log = LoggerFactory.getLogger(JoinerBatchServiceImpl.class);

    @Autowired
    private JoinerBatchRepository joinerBatchRepository;

    @Autowired
    private JoinerGroupRepository joinerGroupRepository;

    @Autowired
    private LanguageUtil languageUtil;

    private static final String BATCH_NOT_FOUND = "batch.not.found";

    @Override
    public void batchDelete(Integer batchId) throws BadRequestException {
        JoinerBatch joinerBatch = joinerBatchRepository.findByJoinerBatchId(batchId).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en")));
        if (joinerBatch.getStatus() == JoinerBatch.Status.INACTIVE.value) {
            log.error("batch already deleted");
            throw new BadRequestException(languageUtil.getTranslatedText("batch.already.deleted", null, "en"));
        }
        if (joinerGroupRepository.countInBatch(joinerBatch.getJoinerBatchId()) != 0) {
            log.error("batch not empty");
            throw new BadRequestException(languageUtil.getTranslatedText("batch.not.empty", null, "en"));
        }
        joinerBatch.setStatus(JoinerBatch.Status.INACTIVE.value);
        joinerBatch.setUpdateDate(LocalDateTime.now());
        joinerBatchRepository.save(joinerBatch);
    }

    @Override
    public Pager<JoinerBatchView> listBatch(String search, Integer page, Integer limit) {
        Pager<JoinerBatchView> batchPager;
        List<JoinerBatchView> batchList;
        if (search != null) {
            batchList = StreamSupport.stream(joinerBatchRepository
                            .findBySearch(search, PageRequest.of(page - 1, limit)).spliterator(), false)
                    .map(batch -> {
                                List<JoinerGroup> group = StreamSupport.stream(joinerGroupRepository
                                                .findByJoinerBatch(batch).spliterator(), false)
                                        .collect(Collectors.toList());
                                return new JoinerBatchView(batch, group);
                            }
                    ).collect(Collectors.toList());
            batchPager = new Pager<>(limit, batchList.size(), page);
            batchPager.setResult(batchList);
            if (!batchList.isEmpty()) {
                return batchPager;
            } else {
                log.error("Batch not found");
                throw new BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en"));
            }
        }
        batchList = StreamSupport.stream(joinerBatchRepository
                        .sortBatch(PageRequest.of(page - 1, limit)).spliterator(), false)
                .map(batch -> {
                            List<JoinerGroup> group = StreamSupport.stream(joinerGroupRepository
                                            .findByJoinerBatch(batch).spliterator(), false)
                                    .collect(Collectors.toList());
                            return new JoinerBatchView(batch, group);
                        }
                ).collect(Collectors.toList());
        batchPager = new Pager<>(limit, joinerBatchRepository.findAllByStatusOrderByCreateDateDesc(
                JoinerBatch.Status.ACTIVE.value).size(), page);
        batchPager.setResult(batchList);
        return batchPager;
    }

    @Override
    public JoinerBatchView joinerBatchCreate(JoinerBatchForm form) {
        int currentYear = Year.now().getValue();
        yearChecking(currentYear,form);
        String batchName = form.getJoinerBatchMonth() + " " + form.getJoinerBatchYear() + " " + form.getJoinerBatchName();
        if (joinerBatchRepository.findByJoinerBatchNameAndStatus(batchName,JoinerBatch.Status.ACTIVE.value).isPresent()) {
            log.error("batch name already exists");
            throw new BadRequestException(languageUtil.getTranslatedText("batchName.already.exists", null, "en"));
        }
        JoinerBatch joinerBatch=new JoinerBatch(batchName);
        joinerBatchRepository.save(joinerBatch);
        return new JoinerBatchView(joinerBatch);
    }

    private void yearChecking(int currentYear, JoinerBatchForm form) {
        if ((currentYear - 1) > form.getJoinerBatchYear()) {
            log.error("year is not valid");
            throw new BadRequestException(languageUtil.getTranslatedText("year.not.valid", null, "en"));
        }
    }

    @Override
    public List<JoinerBatchView> fetchBatch(Integer joinerBatchId) {
        if (joinerBatchRepository.findByJoinerBatchIdAndStatus(joinerBatchId, JoinerBatch.Status.ACTIVE.value).isPresent()) {
            return joinerBatchRepository.findByJoinerBatchId(joinerBatchId)
                    .stream().map(batch
                            -> new JoinerBatchView(batch, joinerGroupRepository.findByJoinerBatchJoinerBatchIdAndStatus(batch.getJoinerBatchId(), JoinerGroup.Status.ACTIVE.value)))
                    .collect(Collectors.toList());
        } else {
            log.error("batch not found");
            throw new BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en"));
        }
    }

    @Override
    public JoinerBatchView updateBatch(Integer joinerBatchId, JoinerBatchForm form) {
        JoinerBatch joinerBatch = joinerBatchRepository.findByJoinerBatchIdAndStatus(joinerBatchId, JoinerBatch.Status.ACTIVE.value).orElseThrow(() -> new
                BadRequestException(languageUtil.getTranslatedText(BATCH_NOT_FOUND, null, "en")));
        int y = Year.now().getValue();
        yearChecking(y,form);
        String batchName = form.getJoinerBatchMonth() + " " + form.getJoinerBatchYear() + " " + form.getJoinerBatchName();
        Optional<JoinerBatch> optionalBatch=joinerBatchRepository.findByJoinerBatchNameAndStatus(batchName,JoinerBatch.Status.ACTIVE.value);
        if ((optionalBatch.isPresent()) && (!optionalBatch.get().getJoinerBatchId().equals(joinerBatchId))) {
            log.error("batchName exists");
            throw new BadRequestException(languageUtil.getTranslatedText("batchName.already.exists", null, "en"));
        }
        joinerBatch.setJoinerBatchName(batchName);
        joinerBatch.setUpdateDate(LocalDateTime.now());
        joinerBatchRepository.save(joinerBatch);
        return new JoinerBatchView(joinerBatch);
    }

    @Override
    public Collection<JoinerBatchView> listAllBatch() {
        List<JoinerBatchView> batchList;
        batchList = StreamSupport.stream(joinerBatchRepository
                        .findAllByStatusOrderByCreateDateDesc(JoinerBatch.Status.ACTIVE.value).spliterator(), false)
                .map(batch -> {
                            List<JoinerGroup> group = StreamSupport.stream(joinerGroupRepository
                                            .findByJoinerBatch(batch).spliterator(), false)
                                    .collect(Collectors.toList());
                            return new JoinerBatchView(batch, group);
                        }
                ).collect(Collectors.toList());
        return batchList;
    }

    @Override
    public List<String> fetchBatchName() {
        List<String> batchName;
        try {
            batchName = joinerBatchRepository.fetchBatchName();
            return batchName;
        } catch (BadRequestException e) {
            throw new BadRequestException(languageUtil.getTranslatedText("list.fetch.failed", null, "en"));
        }
    }
}
