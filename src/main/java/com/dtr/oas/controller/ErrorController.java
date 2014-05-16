package com.dtr.oas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {
    static Logger logger = LoggerFactory.getLogger(ErrorController.class);
    
    @RequestMapping(value = {"/error/accessDeniedPage"}, method = RequestMethod.GET)
    public String sendTo403(Model model) {
        logger.debug("IN: /error/403-GET");
        return "error/403";
    }              

}
