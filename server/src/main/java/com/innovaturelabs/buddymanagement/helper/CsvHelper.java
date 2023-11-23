package com.innovaturelabs.buddymanagement.helper;

import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import org.apache.commons.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvHelper {
    static Logger log = LoggerFactory.getLogger(CsvHelper.class);
    private static final String INVALID_CREDENTIALS_LOG="Invalid credentials";
    private static final String CSV_NOT_UPLOADED_LOG="Failed to store csv file";

    private CsvHelper() {
        throw new IllegalStateException("CsvHelper.class");
    }

    private static final String TYPE = "text/csv";
    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
    public static List<User> csvToUsers(InputStream is) throws IOException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        List<User> userList = new ArrayList<>();
        int count = 1;

        for (CSVRecord csvRecord : csvParser.getRecords()) {
            count++;

            validateFirstName(csvRecord.get(0), count);
            validateLastName(csvRecord.get(1), count);
            validateEmail(csvRecord.get(2), count);
            validateEmployeeId(csvRecord.get(3), count);
            validateDepartment(csvRecord.get(4), count);

            userList.add(new User(
                    csvRecord.get(0),
                    csvRecord.get(1),
                    csvRecord.get(2),
                    Long.parseLong(csvRecord.get(3)),
                    getDepartmentId(csvRecord.get(4), count)
            ));
        }
        return userList;
    }
    private static void validateFirstName(String firstName, int count) {
        if (firstName.isEmpty() || firstName.isBlank()) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1920-First Name is required - row number: " + count);
        }
        if (firstName.length() > 50) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1921-First Name size must be between 1 and 50 - row number: " + count);
        }
        if (!firstName.matches("^[a-zA-Z]+([\\s][a-zA-Z]+)*+$")) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1923-Invalid first name - row number: " + count);
        }
    }

    private static void validateLastName(String lastName, int count) {
        if (lastName.isEmpty() || lastName.isBlank()) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1924-Last Name is required - row number: " + count);
        }
        if (lastName.length() > 20) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1925-Last Name size must be between 1 and 20 - row number: " + count);
        }
        if (!lastName.matches("^[a-zA-Z]+([\\s][a-zA-Z]+)*+$")) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1926-Invalid last name - row number: " + count);
        }
    }

    private static void validateEmail(String email, int count) {
        if (email.isEmpty() || email.isBlank()) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1914-Email is required - row number: " + count);
        }
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1915-Invalid email format - row number: " + count);
        }
    }

    private static void validateEmployeeId(String employeeId, int count) {
        if (employeeId.isEmpty() || employeeId.isBlank()) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1931-Employee id is required - row number: " + count);
        }
        if (employeeId.contains("-") || employeeId.equals("0")) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1944-Invalid Employee Id - row number: " + count);
        }
        try {
            Long.parseLong(employeeId);
        } catch (NumberFormatException n) {
            throw new BadRequestException("1944-Invalid Employee Id - row number: " + count);
        }
    }

    private static void validateDepartment(String department, int count) {
        if (department.isEmpty() || department.isBlank()) {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1933-Department is required - row number: " + count);
        }
    }

    private static byte getDepartmentId(String department, int count) {
        String dept = department.toLowerCase();
        if (dept.equals("qa")) {
            return 2;
        }else if (dept.equals("development")) {
            return 1;
        } else {
            log.error(INVALID_CREDENTIALS_LOG);
            throw new BadRequestException("1934-Invalid Department - row number: " + count);
        }
    }
    public static ByteArrayInputStream userToCSV(List<User> userList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String department = null;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord("First name", "Last name", "email", "Employee ID", "Department");
            for (User user : userList) {
                if(user.getDepartment() == 1){
                    department = "Development";
                } else if(user.getDepartment() == 2){
                    department = "QA";
                }
                List<String> data = Arrays.asList(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        String.valueOf(user.getEmployeeId()),
                        String.valueOf(department)
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error(CSV_NOT_UPLOADED_LOG);
            throw new BadRequestException("1964-Failed to export CSV");
        }
    }
}
