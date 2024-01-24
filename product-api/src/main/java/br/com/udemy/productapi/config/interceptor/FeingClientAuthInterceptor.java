package br.com.udemy.productapi.config.interceptor;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.udemy.productapi.config.exception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

public class FeingClientAuthInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        var currentRequest = getCurentRequest();
        template.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));
    }

    private HttpServletRequest getCurentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("The current request could not be processed");
        }
    }

}
