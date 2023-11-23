package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.helper.CsvHelper;
import com.innovaturelabs.buddymanagement.repository.ManagerRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.service.CsvService;
import com.innovaturelabs.buddymanagement.service.UserService;
import com.innovaturelabs.buddymanagement.util.PasswordUtil;
import com.innovaturelabs.buddymanagement.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
@Transactional
public class CsvServiceImpl implements CsvService {
    Logger log = LoggerFactory.getLogger(CsvServiceImpl.class);
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private CsvService csvService;

    private static final String CSV_NOT_UPLOADED_LOG = "Failed to store csv file";
    private static final String INVALID_FILE="Please upload a valid csv file!";


    public void save(MultipartFile file, Byte role) {
        try {
            List<User> users = CsvHelper.csvToUsers(file.getInputStream());
            int count = 0;
            for (User user : users) {
                String password = passwordEncoder.encode(passwordUtil.generatePassword());
                user.setPassword(password);
                user.setUserRole(role);
                count++;
                if (userRepository.findByEmployeeId(user.getEmployeeId()).isPresent()) {
                    throw new BadRequestException("1932-Employee ID already exists - row number: " + count);
                }
                if (users.get(0) == null) {
                    throw new BadRequestException("1920-First Name is required - row number: " + count);
                }
                if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                    throw new BadRequestException("1930-Email already exists - row number: " + count);
                }
                if (user.getDepartment() == 0) {
                    throw new BadRequestException("1933-Department is required - row number: " + count);
                }
                if (!userService.checkDepartment(user.getDepartment())) {
                    throw new BadRequestException("1934-Invalid department - row number: " + count);
                }
                userRepository.save(user);
            }
        } catch (IOException e) {
            log.error(CSV_NOT_UPLOADED_LOG);
            throw new BadRequestException("1947-Failed to store csv file");
        }
    }

    public ByteArrayInputStream load(byte role) {
        List<User> users = userRepository.findAllUserByRegisteredStatus(role);
        return CsvHelper.userToCSV(users);
    }

    public List<User> getAllUsers() {
        return managerRepository.findAll();
    }

    @Override
    public ResponseEntity<ResponseMessage> importCsv(MultipartFile file, byte userRole) {
        if (CsvHelper.hasCSVFormat(file)) {
            if(file.getSize()>(1024*1024*10)){
                log.error("Size of csv file must be less than 10MB");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("1977", "Size of csv file must be less than 10MB"));
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line = reader.readLine();
                if (line == null) {
                    log.error("Empty file received");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("1911", "Empty file received"));
                }
                // check if the rest of the file is empty
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {csvService.save(file, userRole);
                        return ResponseEntity.ok().build();
                    }
                }
                log.error("File content is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("1935", "File content is empty"));
            } catch (IOException e) {
                log.error("Error occurred while reading file", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("1978", "Error occurred while reading file"));
            }
        } else {
            log.error(INVALID_FILE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("1910", INVALID_FILE));
        }
    }

    @Override
    public ResponseEntity<Resource> exportCsv(String fileName, byte userRole) {
        InputStreamResource file = new InputStreamResource(load(userRole));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
