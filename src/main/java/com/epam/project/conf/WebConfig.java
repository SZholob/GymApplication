package com.epam.project.conf;

import com.epam.project.interceptor.AuthInterceptor;
import com.epam.project.interceptor.LoggingInterceptor;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Gym REST API", version = "1.0", description = "Gym CRM Application REST API Documentation"))
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loggingInterceptor).addPathPatterns("/api/**");

        registry.addInterceptor(authInterceptor).addPathPatterns("/api/**");
    }
}