package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.JoinerBatchForm;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.JoinerBatchView;

import java.util.Collection;
import java.util.List;

public interface JoinerBatchService {

    void batchDelete(Integer batchId) throws BadRequestException;

    Pager<JoinerBatchView> listBatch(String search, Integer page, Integer limit);

    Collection<JoinerBatchView> listAllBatch();

    JoinerBatchView joinerBatchCreate(JoinerBatchForm form);

    List<JoinerBatchView> fetchBatch(Integer joinerBatchId);

    JoinerBatchView updateBatch(Integer joinerBatchId, JoinerBatchForm form);
    List<String> fetchBatchName();

}
