/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.innovaturelabs.buddymanagement.security.AccessTokenProcessingFilter;
import com.innovaturelabs.buddymanagement.security.AccessTokenUserDetailsService;
import com.innovaturelabs.buddymanagement.security.config.SecurityConfig;
import com.innovaturelabs.buddymanagement.security.util.TokenGenerator;
import com.innovaturelabs.buddymanagement.view.ResponseView;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

/**
 * @author nirmal
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MANAGER_ROLE = "MANAGER";
    private static final String MENTOR_ROLE = "MENTOR";
    private static final String TRAINER_ROLE = "TRAINER";
    private static final String TRAINEE_ROLE = "TRAINEE";

    private static final String LOGIN = "/login";

    private static final String MANAGERS_USERID = "/managers/{userId}";
    private static final String BATCH_ID = "/batch/{joinerBatchId}";

    private static final String TRAINEES_USERID="/trainees/{userId}";
    private static final String GROUP_ID = "/group/{joinerGroupId}";
    private static final String MENTOR_ID = "/mentors/{userId}";

    private static final String TRAINER_ID = "/trainers/{userId}";
    private static final String TRAINING_ID = "/training/{trainingId}";

    private static final String TRAINING_SESSION = "/trainingSession";

    private static final String USER_FEEDBACK = "/userFeedback";

    private static final String USER_FEEDBACK_PUT = "/userFeedback/{feedbackId}";




    public WebSecurityConfiguration() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/error")))
                .addFilter(accessTokenProcessingFilter())
                .authenticationProvider(preAuthenticatedAuthenticationProvider())
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .headers().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .securityContext().and()
                .anonymous().and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(POST, "/users").anonymous()
                .antMatchers(HttpMethod.GET, "/users/profile").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE, TRAINEE_ROLE)
                .antMatchers(HttpMethod.PUT, "/users/profile").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE, TRAINEE_ROLE)
                .antMatchers(OPTIONS, LOGIN).anonymous()
                .antMatchers(POST, LOGIN).anonymous()
                .antMatchers(POST, "/users/forgotPassword").anonymous()
                .antMatchers(PUT, "/users/resetPassword").anonymous()
                .antMatchers(POST, "/users/token/verify").anonymous()
                .antMatchers(HttpMethod.GET, "/managers/resource-list").hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.GET, "/managers/firstName").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, MANAGERS_USERID).hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, "/managers").hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, "/manager/csvImport").hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.GET, "/manager/csvExport/{fileName:.+}").hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.PUT, MANAGERS_USERID).hasAnyRole(ADMIN_ROLE)
                .antMatchers(PUT, LOGIN).anonymous()
                .antMatchers(HttpMethod.GET, "/users").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/managers").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, "/mentors/resource-list").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, "/mentors").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.POST, "/mentors").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, TRAINING_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE)
                .antMatchers(HttpMethod.GET, "/training").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE)
                .antMatchers(HttpMethod.POST, "/training").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE)
                .antMatchers(PUT, TRAINING_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE)
                .antMatchers(DELETE, TRAINING_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE)
                .antMatchers(HttpMethod.GET,"/training/upcomingTrainings").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.POST, "/mentor/csvImport").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, "/mentor/csvExport/{fileName:.+}").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, "/mentors/firstName").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, MENTOR_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.PUT, MENTOR_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(DELETE, MENTOR_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(DELETE, MANAGERS_USERID).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.GET, "/trainers").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/trainers").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/trainer/csvImport").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/trainer/csvExport/{fileName:.+}").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/trainer/csvExport/").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/trainers/firstName").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, TRAINER_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.PUT, TRAINER_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(DELETE, TRAINER_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/batch").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.GET, "/batch/all").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.DELETE, BATCH_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/batch").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.PUT, BATCH_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, BATCH_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.GET, "/group").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.DELETE, GROUP_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/group").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/group/allocation-control").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/group/resource-list").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.PUT, GROUP_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, GROUP_ID).hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.GET, "/trainees").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(DELETE, TRAINEES_USERID).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE)
                .antMatchers(HttpMethod.GET, TRAINEES_USERID).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.PUT, TRAINEES_USERID).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/trainees").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.POST, "/trainee/csvImport").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/trainee/csvExport/{fileName:.+}").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET, "/technology").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE, MENTOR_ROLE, TRAINER_ROLE)
                .antMatchers(POST, "/technology").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(DELETE, "/technology/{technologyId}").hasAnyRole(ADMIN_ROLE, MANAGER_ROLE)
                .antMatchers(HttpMethod.POST, "/managers/allocation-control").hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, "/mentors/allocation-control").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE)
                .antMatchers(POST, TRAINING_SESSION).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(PUT, TRAINING_SESSION).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(GET, TRAINING_SESSION).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE,TRAINEE_ROLE)
                .antMatchers(DELETE,"/trainingSession/{sessionId}").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(PUT,"/trainingSession/changeSessionStatus").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.POST, "/task/add").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.POST, "/subTask").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.GET, "/task").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(PUT, "/task/{taskId}").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(PUT, "/subTask/{taskId}").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(DELETE, "/task/{taskId}").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.POST, "/task/allocation-control").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.POST, "/subTask/allocation-control").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE,TRAINER_ROLE)
                .antMatchers(HttpMethod.POST,"/score").hasAnyRole(MENTOR_ROLE)
                .antMatchers(HttpMethod.PUT,"/score/{scoreId}").hasAnyRole(MENTOR_ROLE)
                .antMatchers(HttpMethod.GET,"/score").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(HttpMethod.GET,"/score/{traineeTaskId}").hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)
                .antMatchers(POST,USER_FEEDBACK).hasAnyRole(MENTOR_ROLE)
                .antMatchers(PUT,USER_FEEDBACK_PUT).hasAnyRole(MENTOR_ROLE)
                .antMatchers(GET,USER_FEEDBACK).hasAnyRole(ADMIN_ROLE,MANAGER_ROLE,MENTOR_ROLE)




                .antMatchers( "/buddy/management").permitAll()

                .antMatchers("/v2/api-docs",
                        "/v3/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/webjars/**").anonymous()
                .anyRequest().authenticated();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            if (ex instanceof AccessDeniedException) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); // to do : need to chk proper way to set cors header
                response.setHeader("Access-Control-Allow-Credentials", "true");
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                ResponseView responseView = new ResponseView("User has no permission to access this action", "1093");
                String jsonString = mapper.writeValueAsString(responseView);
                response.getWriter().print(jsonString);
                response.setContentType("application/json;charset=UTF-8");
            }

        };
    }

    @Bean
    protected AccessTokenUserDetailsService accessTokenUserDetailsService() {
        return new AccessTokenUserDetailsService();
    }

    @Bean
    protected PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider authProvider = new PreAuthenticatedAuthenticationProvider();
        authProvider.setPreAuthenticatedUserDetailsService(accessTokenUserDetailsService());
        return authProvider;
    }

    @Bean
    protected AccessTokenProcessingFilter accessTokenProcessingFilter() throws Exception {
        AccessTokenProcessingFilter filter = new AccessTokenProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConfigurationProperties("app.security")
    public SecurityConfig securityConfig() {
        return new SecurityConfig();
    }

    @Bean
    @ConfigurationProperties("app.security.configuration")
    public TokenGenerator tokenGenerator(SecurityConfig securityConfig) {
        return new TokenGenerator(securityConfig.getTokenGeneratorPassword(), securityConfig.getTokenGeneratorSalt());
    }
}
