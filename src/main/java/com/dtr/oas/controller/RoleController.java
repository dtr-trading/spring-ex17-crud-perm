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

import com.dtr.oas.exception.DuplicateRoleException;
import com.dtr.oas.exception.RoleNotFoundException;
import com.dtr.oas.model.Role;
import com.dtr.oas.service.RoleService;

@Controller
@RequestMapping(value = "/role")
@PreAuthorize("denyAll")
public class RoleController {

    static Logger logger = LoggerFactory.getLogger(RoleController.class);
    static String businessObject = "role"; //used in RedirectAttributes messages 

    @Autowired
    private RoleService roleService;

    @Autowired
    private MessageSource messageSource;
    
    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_ROLE_LIST_GET')")
    public String listOfRoles(Model model) {
        logger.debug("IN: Role/list-GET");

        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);

        // if there was an error in /add, we do not want to overwrite
        // the existing role object containing the errors.
        if (!model.containsAttribute("role")) {
            logger.debug("Adding Role object to model");
            Role role = new Role();
            model.addAttribute("role", role);
        }
        return "role-list";
    }              
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('CTRL_ROLE_ADD_POST')")
    public String addRole(@Valid @ModelAttribute Role role,
            BindingResult result, RedirectAttributes redirectAttrs) {

        logger.debug("IN: Role/add-POST");

        if (result.hasErrors()) {
            logger.debug("Role-add error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
            redirectAttrs.addFlashAttribute("role", role);
            return "redirect:/role/list";
        } else {
            try {
                roleService.addRole(role);
                String message = messageSource.getMessage("ctrl.message.success.add", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/role/list";
            } catch (DuplicateRoleException e) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/role/list";
            }
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_ROLE_EDIT_GET')")
    public String editRolePage(@RequestParam(value = "id", required = true) 
            Integer id, Model model, RedirectAttributes redirectAttrs) {
        
        logger.debug("IN: Role/edit-GET:  ID to query = " + id);

        try {
            if (!model.containsAttribute("role")) {
                logger.debug("Adding Role object to model");
                Role role = roleService.getRole(id);
                logger.debug("Role/edit-GET:  " + role.toString());
                model.addAttribute("role", role);
            }
            return "role-edit";
        } catch (RoleNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"role id", id}, Locale.US);
            model.addAttribute("error", message);
            return "redirect:/role/list";
        }
    }
        
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('CTRL_ROLE_EDIT_POST')")
    public String editRole(@Valid @ModelAttribute Role role,
            BindingResult result, RedirectAttributes redirectAttrs,
            @RequestParam(value = "action", required = true) String action) {

        logger.debug("IN: Role/edit-POST: " + action);

        if (action.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Edit", businessObject, role.getRolename()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
        } else if (result.hasErrors()) {
            logger.debug("Role-edit error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
            redirectAttrs.addFlashAttribute("role", role);
            return "redirect:/role/edit?id=" + role.getId();
        } else if (action.equals(messageSource.getMessage("button.action.save",  null, Locale.US))) {
            logger.debug("Role/edit-POST:  " + role.toString());
            try {
                roleService.updateRole(role);
                String message = messageSource.getMessage("ctrl.message.success.update", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
            } catch (RoleNotFoundException snf) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/role/list";
            } catch (DuplicateRoleException dse) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/role/list";
            }
        }
        return "redirect:/role/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_ROLE_DELETE_GET')")
    public String deleteRolePage(
            @RequestParam(value = "id", required = true) Integer id,
            @RequestParam(value = "phase", required = true) String phase,
            Model model, RedirectAttributes redirectAttrs) {

        Role role;
        String message;
        
        try {
            role = roleService.getRole(id);
        } catch (RoleNotFoundException e) {
            message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"Role number", id}, Locale.US);
            redirectAttrs.addFlashAttribute("error", message);
            return "redirect:/role/list";
        }

        logger.debug("IN: Role/delete-GET | id = " + id + " | phase = " + phase + " | " + role.toString());

        if (phase.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Delete", businessObject, role.getRolename()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
            return "redirect:/role/list";
        } else if (phase.equals(messageSource.getMessage("button.action.stage", null, Locale.US))) {
            model.addAttribute("role", role);
            return "role-delete";
        } else if (phase.equals(messageSource.getMessage("button.action.delete", null, Locale.US))) {
            try {
                roleService.deleteRole(id);
                message = messageSource.getMessage("ctrl.message.success.delete", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/role/list";
            } catch (RoleNotFoundException e) {
                message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, role.getRolename()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/role/list";
            }
        }

        return "redirect:/role/list";
    }
    
}
