package br.com.udemy.productapi.config.interceptor;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.udemy.productapi.config.exception.ValidationException;
import br.com.udemy.productapi.modules.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (isOption(request)) {
            return true;
        }

        if (isEmpty(request.getHeader(TRANSACTION_ID))) {
            throw new ValidationException("The transactionid header is required.");
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAuthorization(authorization);
        request.setAttribute("serviceid", UUID.randomUUID().toString());
        return true;
    }

    private boolean isOption(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }

}
