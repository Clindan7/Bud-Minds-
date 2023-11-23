package com.buddymanagement.user.reg.batch.service.impl;

import com.buddymanagement.user.reg.batch.entity.BatchInfo;
import com.buddymanagement.user.reg.batch.entity.User;
import com.buddymanagement.user.reg.batch.exception.BadRequestException;
import com.buddymanagement.user.reg.batch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.buddymanagement.user.reg.batch.service.UserRegBatchService;
import com.buddymanagement.user.reg.batch.util.EntityHelper;
import com.buddymanagement.user.reg.batch.util.MailServiceUtil;
import com.buddymanagement.user.reg.batch.util.MailTemplate;
import com.buddymanagement.user.reg.batch.util.PasswordUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author ajmal
 */
@Service
public class UserRegServiceImpl implements UserRegBatchService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailServiceUtil mailServiceUtil;
    @Autowired
    private EntityHelper entityHelper;
    @Autowired
    private MailTemplate mailTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegServiceImpl.class);

    @Override
    public void executeBatch() {
        LOGGER.info("User registration Batch Started");
        if (entityHelper.isLastBatchPending()) {
            LOGGER.error("Last Batch Not completed... Exiting");
            System.exit(0);
            throw new BadRequestException("1945-Last Batch not completed... Exiting");
        }
        boolean status = true;
        BatchInfo batch = new BatchInfo();
        LOGGER.info("User registration Batch Started");
        try {
            entityHelper.saveBatchInfo(batch);
            List<String> user = userRepository.findAllEmailByPendingStatus();
            if (user.isEmpty()) {
                LOGGER.info("Number of mails for send: 0");
            } else {
                for (String userList : user) {
                    String managerPassword = passwordUtil.generatePassword();
                    User userG = userRepository.findByEmail(user.get(user.indexOf(userList)));
                    userG.setPassword(passwordEncoder.encode(managerPassword));
                    userG.setStatus(User.Status.REGISTERED.value);
                    userRepository.save(userG);
                    String content = mailTemplate.emailContent(userG.getFirstName(), user.get(user.indexOf(userList)), managerPassword);
                    String subject = "Credentials to BudMinds";
                    mailServiceUtil.sendEmail(user.get(user.indexOf(userList)), subject, content);
                    LOGGER.info("User registration Batch Completed");
                }
            }
        } catch (MailSendException me) {
            status = false;
            LOGGER.error("Mail sending failed", me);
            throw new BadRequestException("1933-Mail sending failed");
        } catch (Exception e) {
            status = false;
            LOGGER.error("Mail sending failed", e);
        } finally {
            entityHelper.updateBatchInfo(batch, status);
        }
    }

}
