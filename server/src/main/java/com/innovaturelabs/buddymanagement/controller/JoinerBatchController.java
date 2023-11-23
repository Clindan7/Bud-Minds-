package com.innovaturelabs.buddymanagement.controller;

import com.innovaturelabs.buddymanagement.form.JoinerBatchForm;
import com.innovaturelabs.buddymanagement.service.JoinerBatchService;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.JoinerBatchView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import javax.validation.Valid;

/**
 *
 * @author adwaith
 */
@RestController
@RequestMapping("/batch")
public class JoinerBatchController {

    @Autowired
    private JoinerBatchService joinerBatchService;

    @DeleteMapping("/{batchId}")
    public void batchDelete(@PathVariable("batchId") Integer batchId) {
        joinerBatchService.batchDelete(batchId);
    }

    @GetMapping
    public Pager<JoinerBatchView> listBatch(@RequestParam(name = "search", required = false) String search,
                                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {
        return joinerBatchService.listBatch(search, page, limit);
    }

    @GetMapping("/all")
    public Collection<JoinerBatchView> listAllBatch() {
        return joinerBatchService.listAllBatch();
    }

    @PostMapping
    public JoinerBatchView joinerBatchCreate(@Valid @RequestBody JoinerBatchForm form) {
        return joinerBatchService.joinerBatchCreate(form);
    }

    @GetMapping("/{joinerBatchId}")
    public List<JoinerBatchView> fetchBatch(@PathVariable("joinerBatchId") Integer joinerBatchId) {
        return joinerBatchService.fetchBatch(joinerBatchId);
    }

    @PutMapping("/{joinerBatchId}")
    public JoinerBatchView updateBatch(@PathVariable("joinerBatchId") Integer joinerBatchId, @RequestBody @Valid JoinerBatchForm form) {
        return joinerBatchService.updateBatch(joinerBatchId, form);
    }
    @GetMapping("/batchName")
    public List<String> fetchBatchName() {
        return joinerBatchService.fetchBatchName();
    }

}
