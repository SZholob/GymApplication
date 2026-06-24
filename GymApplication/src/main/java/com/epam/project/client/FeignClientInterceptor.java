package com.epam.project.client;

import com.epam.project.security.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String MDC_TRANSACTION_ID_KEY = "transactionId";
    private static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";

    private final JwtService jwtService;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        String systemToken = jwtService.generateSystemToken();
        requestTemplate.header(AUTHORIZATION_HEADER, "Bearer " + systemToken);

        String transactionId = MDC.get(MDC_TRANSACTION_ID_KEY);
        if (transactionId != null) {
            requestTemplate.header(TRANSACTION_ID_HEADER, transactionId);
        }
    }
}