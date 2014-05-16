package com.dtr.oas.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    static Logger logger = LoggerFactory.getLogger(AccessDeniedExceptionHandler.class);
    
    private String errorPage;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException arg2) throws IOException, ServletException {

        logger.debug(String.format("### AccessDeniedHandler: URL [%s] ", request.getServletPath()));
        logger.debug("### Error page: [" + errorPage + "]");
        //response.sendRedirect(getErrorPage());
        request.getRequestDispatcher(errorPage).forward(request, response);
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
