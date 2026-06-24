package com.epam.project.client;

import com.epam.project.security.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtService jwtService;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        String systemToken = jwtService.generateSystemToken();
        requestTemplate.header(AUTHORIZATION_HEADER, "Bearer " + systemToken);
    }
}