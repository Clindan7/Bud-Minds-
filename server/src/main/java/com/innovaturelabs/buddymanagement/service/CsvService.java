package com.innovaturelabs.buddymanagement.service;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.util.ResponseMessage;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.util.List;

public interface CsvService {
    void save(MultipartFile file, Byte role);

    public ByteArrayInputStream load(byte role);

    public List<User> getAllUsers();

    ResponseEntity<ResponseMessage> importCsv(MultipartFile file, byte userRole);

    ResponseEntity<Resource> exportCsv(String fileName, byte userRole);
}
