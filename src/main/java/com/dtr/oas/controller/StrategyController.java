package com.dtr.oas.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dtr.oas.exception.DuplicateStrategyException;
import com.dtr.oas.exception.StrategyNotFoundException;
import com.dtr.oas.model.Strategy;
import com.dtr.oas.service.StrategyService;

@Controller
@RequestMapping(value = "/strategy")
@PreAuthorize("denyAll")
public class StrategyController {
    static Logger logger = LoggerFactory.getLogger(StrategyController.class);
    static String businessObject = "strategy"; //used in RedirectAttributes messages 

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private MessageSource messageSource;
    
    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_STRATEGY_LIST_GET')")
    public String listOfStrategies(Model model) {
        logger.debug("IN: Strategy/list-GET");

        List<Strategy> strategies = strategyService.getStrategies();
        model.addAttribute("strategies", strategies);

        // if there was an error in /add, we do not want to overwrite
        // the existing strategy object containing the errors.
        if (!model.containsAttribute("strategy")) {
            logger.debug("Adding Strategy object to model");
            Strategy strategy = new Strategy();
            model.addAttribute("strategy", strategy);
        }
        return "strategy-list";
    }              
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('CTRL_STRATEGY_ADD_POST')")
    public String addingStrategy(@Valid @ModelAttribute Strategy strategy,
            BindingResult result, RedirectAttributes redirectAttrs) {

        logger.debug("IN: Strategy/add-POST");

        if (result.hasErrors()) {
            logger.debug("Strategy-add error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.strategy", result);
            redirectAttrs.addFlashAttribute("strategy", strategy);
            return "redirect:/strategy/list";
        } else {
            try {
                strategyService.addStrategy(strategy);
                String message = messageSource.getMessage("ctrl.message.success.add", 
                        new Object[] {businessObject, strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/strategy/list";
            } catch (DuplicateStrategyException e) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate.strategy",
                        new Object[] {businessObject, strategy.getType(), strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/strategy/list";
            }
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_STRATEGY_EDIT_GET')")
    public String editStrategyPage(@RequestParam(value = "id", required = true) 
            Integer id, Model model, RedirectAttributes redirectAttrs) {
        
        logger.debug("IN: Strategy/edit-GET:  ID to query = " + id);

        try {
            if (!model.containsAttribute("strategy")) {
                logger.debug("Adding Strategy object to model");
                Strategy strategy = strategyService.getStrategy(id);
                logger.debug("Strategy/edit-GET:  " + strategy.toString());
                model.addAttribute("strategy", strategy);
            }
            return "strategy-edit";
        } catch (StrategyNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", new Object[] {"strategy id", id}, Locale.US);
            model.addAttribute("error", message);
            return "redirect:/strategy/list";
        }
    }
        
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('CTRL_STRATEGY_EDIT_POST')")
    public String editingStrategy(@Valid @ModelAttribute Strategy strategy,
            BindingResult result, RedirectAttributes redirectAttrs,
            @RequestParam(value = "action", required = true) String action) {

        logger.debug("IN: Strategy/edit-POST: " + action);

        if (action.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Edit", businessObject, strategy.getName()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
        } else if (result.hasErrors()) {
            logger.debug("Strategy-edit error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.strategy", result);
            redirectAttrs.addFlashAttribute("strategy", strategy);
            return "redirect:/strategy/edit?id=" + strategy.getId();
        } else if (action.equals(messageSource.getMessage("button.action.save",  null, Locale.US))) {
            logger.debug("Strategy/edit-POST:  " + strategy.toString());
            try {
                strategyService.updateStrategy(strategy);
                String message = messageSource.getMessage("ctrl.message.success.update", 
                        new Object[] {businessObject, strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
            } catch (StrategyNotFoundException snf) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/strategy/list";
            } catch (DuplicateStrategyException dse) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/strategy/list";
            }
        }
        return "redirect:/strategy/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_STRATEGY_DELETE_GET')")
    public String deleteStrategyPage(
            @RequestParam(value = "id", required = true) Integer id,
            @RequestParam(value = "phase", required = true) String phase,
            Model model, RedirectAttributes redirectAttrs) {

        Strategy strategy;
        String message;
        
        try {
            strategy = strategyService.getStrategy(id);
        } catch (StrategyNotFoundException e) {
            message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"Strategy number", id}, Locale.US);
            redirectAttrs.addFlashAttribute("error", message);
            return "redirect:/strategy/list";
        }

        logger.debug("IN: Strategy/delete-GET | id = " + id + " | phase = " + phase + " | " + strategy.toString());

        if (phase.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Delete", businessObject, strategy.getName()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
            return "redirect:/strategy/list";
        } else if (phase.equals(messageSource.getMessage("button.action.stage", null, Locale.US))) {
            model.addAttribute("strategy", strategy);
            return "strategy-delete";
        } else if (phase.equals(messageSource.getMessage("button.action.delete", null, Locale.US))) {
            try {
                strategyService.deleteStrategy(id);
                message = messageSource.getMessage("ctrl.message.success.delete", 
                        new Object[] {businessObject, strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/strategy/list";
            } catch (StrategyNotFoundException e) {
                message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, strategy.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/strategy/list";
            }
        }

        return "redirect:/strategy/list";
    }
}
