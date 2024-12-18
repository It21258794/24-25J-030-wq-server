package com.waterboard.waterqualityprediction.filters;

import com.waterboard.waterqualityprediction.exceptions.http.BaseException;
import com.waterboard.waterqualityprediction.exceptions.http.InternalErrorException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException exp) {
            exp.printStackTrace();
            log.error("exception occurred error = {}", exp.getMessage());
            setErrorResponse((HttpServletResponse) response, exp);
        } catch (Exception exp) {
            exp.printStackTrace();
            log.error("exception occurred error = {}", exp.getMessage());
            BaseException exception = new InternalErrorException(exp.getMessage());
            this.setErrorResponse((HttpServletResponse) response, exception);
        }
    }

    private void setErrorResponse(HttpServletResponse response, BaseException exp) {
        response.setStatus(exp.getCode().value());
        response.setContentType("application/json");
        try {
            String json = exp.getJsonAsString(null);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return !path.startsWith("/api/");
    }


}
