package br.com.udemy.productapi.config;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.udemy.productapi.config.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
    
    public static HttpServletRequest getCurentRequest() {
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
