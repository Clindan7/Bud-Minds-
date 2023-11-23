package com.buddymanagement.user.reg.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.buddymanagement.user.reg.batch.util.EntityHelper;
import com.buddymanagement.user.reg.batch.service.UserRegBatchService;
import com.buddymanagement.user.reg.batch.util.MailServiceUtil;
import com.buddymanagement.user.reg.batch.util.PasswordUtil;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan("com.buddymanagement.user.reg.batch.entity")
public class BuddyManagementUserRegBatchApplication {

   @Bean
    public MailServiceUtil mailServiceUtil() {
        return new MailServiceUtil();
    }
    @Bean
    public EntityHelper entityHelper(){
        return new EntityHelper();
    }

    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    public static void main(String[] args) {
        SpringApplication.run(BuddyManagementUserRegBatchApplication.class, args).getBean(UserRegBatchService.class).executeBatch();
        System.exit(0);
    }
    
}
