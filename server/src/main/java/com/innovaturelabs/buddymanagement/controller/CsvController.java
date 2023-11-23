package com.innovaturelabs.buddymanagement.controller;


import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.service.CsvService;
import com.innovaturelabs.buddymanagement.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * @author gokul
 */
@RestController
@RequestMapping("/")
public class CsvController {

    @Autowired
    private CsvService csvService;
    static Logger log = LoggerFactory.getLogger(CsvController.class);

    @PostMapping("/manager/csvImport")
    public ResponseEntity<ResponseMessage> managerCsvUpload(@RequestParam("file") MultipartFile file) {
        return csvService.importCsv(file,User.Role.MANAGER.value);
    }

    @PostMapping("/mentor/csvImport")
    public ResponseEntity<ResponseMessage> mentorCsvUpload(@RequestParam("file") MultipartFile file) {
        return csvService.importCsv(file,User.Role.MENTOR.value);
    }

    @PostMapping("/trainee/csvImport")
    public ResponseEntity<ResponseMessage> traineeCsvUpload(@RequestParam("file") MultipartFile file) {
        return csvService.importCsv(file,User.Role.TRAINEE.value);
    }

    @PostMapping("/trainer/csvImport")
    public ResponseEntity<ResponseMessage> trainerCsvUpload(@RequestParam("file") MultipartFile file) {
        return csvService.importCsv(file,User.Role.TRAINER.value);
    }

    @GetMapping("/manager/csvExport/{fileName:.+}")
    public ResponseEntity<Resource> managerCsvExport(@PathVariable String fileName) {
        return csvService.exportCsv(fileName,User.Role.MANAGER.value);
    }
    @GetMapping("/mentor/csvExport/{fileName:.+}")
    public ResponseEntity<Resource> mentorCsvExport(@PathVariable String fileName) {
        return csvService.exportCsv(fileName,User.Role.MENTOR.value);
    }

    @GetMapping("/trainee/csvExport/{fileName:.+}")
    public ResponseEntity<Resource> traineeCsvExport(@PathVariable String fileName) {
        return csvService.exportCsv(fileName,User.Role.TRAINEE.value);
    }

    @GetMapping("/trainer/csvExport/{fileName:.+}")
    public ResponseEntity<Resource> trainerCsvExport(@PathVariable String fileName) {
        return csvService.exportCsv(fileName,User.Role.TRAINER.value);
    }

}
