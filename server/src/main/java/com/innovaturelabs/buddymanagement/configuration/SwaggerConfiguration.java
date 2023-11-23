/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovaturelabs.buddymanagement.configuration;

/**
 *
 * @author ajmal
 */
import io.swagger.models.auth.In;
import java.util.Arrays;
import static java.util.Collections.singletonList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(singletonList(securityContext()))
                .securitySchemes(singletonList(apiKey())).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any()).build().apiInfo(metadata());
                
                
                
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Buddy Management")
                .description("API document for Buddy Management\n Use Login API to get Authorization Token ")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Token", HttpHeaders.AUTHORIZATION, In.HEADER.name());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex("(?!\\/login|\\/users\\/register-email|\\/users\\/registration).*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope("unlimited", "access Everything")};
        return Arrays.asList(new SecurityReference("Token", authorizationScopes));
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(true)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.METHOD)
                .showExtensions(true)
                .showCommonExtensions(true)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }

}