package com.innovaturelabs.buddymanagement;

import com.innovaturelabs.buddymanagement.util.EmailUtil;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.PasswordUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BuddyManagementApplication {

    @Value("${dev.ui.endpoint}")
    private String devEndPoint;

    @Value("${local.host.endpoint}")
    private String localEndPoint;

    @Value("${prod.ui.endpoint}")
    private String prodEndPoint;
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*")
                        .allowedOrigins(localEndPoint,devEndPoint,prodEndPoint)
                        .allowCredentials(true);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BuddyManagementApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LanguageUtil languageUtil() {
        return new LanguageUtil();
    }

    @Bean
    public EmailUtil emailUtil() {
        return new EmailUtil();
    }

    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
