package com.dtr.oas.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

import com.dtr.oas.exception.DuplicatePermissionException;
import com.dtr.oas.exception.PermissionNotFoundException;
import com.dtr.oas.exception.RoleNotFoundException;
import com.dtr.oas.model.Permission;
import com.dtr.oas.model.Role;
import com.dtr.oas.service.PermissionService;
import com.dtr.oas.service.RoleService;

@Controller
@RequestMapping(value = "/permission")
@PreAuthorize("denyAll")
public class PermissionController {

    static Logger logger = LoggerFactory.getLogger(PermissionController.class);
    static String businessObject = "permission"; //used in RedirectAttributes messages 
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("allRoles")
    @PreAuthorize("hasAnyRole('CTRL_PERM_LIST_GET','CTRL_PERM_EDIT_GET')")
    public List<Role> getAllRoles() {
        return roleService.getRoles();
    }

    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_PERM_LIST_GET')")
    public String listPermissions(Model model) {
        logger.debug("IN: Permission/list-GET");

        List<Permission> permissions = permissionService.getPermissions();
        model.addAttribute("permissions", permissions);

        // if there was an error in /add, we do not want to overwrite
        // the existing user object containing the errors.
        if (!model.containsAttribute("permissionDTO")) {
            logger.debug("Adding PermissionDTO object to model");
            PermissionDTO permissionDTO = new PermissionDTO();
            model.addAttribute("permissionDTO", permissionDTO);
        }
        return "permission-list";
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('CTRL_PERM_ADD_POST')")
    public String addPermission(@Valid @ModelAttribute PermissionDTO permissionDTO,
            BindingResult result, RedirectAttributes redirectAttrs) {
        
        logger.debug("IN: Permission/add-POST");
        logger.debug("  DTO: " + permissionDTO.toString());

        if (result.hasErrors()) {
            logger.debug("PermissionDTO add error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.permissionDTO", result);
            redirectAttrs.addFlashAttribute("permissionDTO", permissionDTO);
            return "redirect:/permission/list";
        } else {
            Permission perm = new Permission();

            try {
                perm = getPermission(permissionDTO);
                permissionService.addPermission(perm);
                String message = messageSource.getMessage("ctrl.message.success.add", 
                        new Object[] {businessObject, perm.getPermissionname()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/permission/list";
            } catch (DuplicatePermissionException e) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, permissionDTO.getPermissionname()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/permission/list";
           } catch (RoleNotFoundException e) {
               String message = messageSource.getMessage("ctrl.message.error.notfound", 
                       new Object[] {"role ids", permissionDTO.getPermRoles().toString()}, Locale.US);
               redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/permission/list";
            }
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('CTRL_PERM_EDIT_POST')")
    public String editPermission(@Valid @ModelAttribute PermissionDTO permissionDTO,
            BindingResult result, RedirectAttributes redirectAttrs,
            @RequestParam(value = "action", required = true) String action) {

        logger.debug("IN: Permission/edit-POST: " + action);

        if (action.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Edit", businessObject, permissionDTO.getPermissionname()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
        } else if (result.hasErrors()) {
            logger.debug("Permission-edit error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.permissionDTO", result);
            redirectAttrs.addFlashAttribute("permissionDTO", permissionDTO);
            return "redirect:/permission/edit?id=" + permissionDTO.getId();
        } else if (action.equals(messageSource.getMessage("button.action.save",  null, Locale.US))) {
            logger.debug("Permission/edit-POST:  " + permissionDTO.toString());
            try {
                Permission permission = getPermission(permissionDTO);
                permissionService.updatePermission(permission);
                String message = messageSource.getMessage("ctrl.message.success.update", 
                        new Object[] {businessObject, permissionDTO.getPermissionname()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
            } catch (DuplicatePermissionException unf) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, permissionDTO.getPermissionname()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/permission/list";
            } catch (PermissionNotFoundException unf) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, permissionDTO.getPermissionname()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/permission/list";
            } catch (RoleNotFoundException unf) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {"role ids", permissionDTO.getPermRoles().toString()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/permission/list";
            }
        }
        return "redirect:/permission/list";
    }
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_PERM_EDIT_GET')")
    public String editPermissionPage(@RequestParam(value = "id", required = true)
            Integer id, Model model, RedirectAttributes redirectAttrs) {

        logger.debug("IN: Permission/edit-GET:  ID to query = " + id);

        try {
            if (!model.containsAttribute("permissionDTO")) {
                logger.debug("Adding permissionDTO object to model");
                Permission perm = permissionService.getPermission(id);
                PermissionDTO permissionDTO = getPermissionDTO(perm);
                logger.debug("Permission/edit-GET:  " + permissionDTO.toString());
                model.addAttribute("permissionDTO", permissionDTO);
            }
            return "permission-edit";
        } catch (PermissionNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"user id", id}, Locale.US);
            model.addAttribute("error", message);
            return "redirect:/permission/list";
        }
    }

    
    
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @PreAuthorize("hasRole('CTRL_PERM_DELETE_GET')")
    public String deletePermission(
            @RequestParam(value = "id", required = true) Integer id,
            @RequestParam(value = "phase", required = true) String phase,
            Model model, RedirectAttributes redirectAttrs) {

        Permission permission;
        try {
            permission = permissionService.getPermission(id);
        } catch (PermissionNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"permission id", id}, Locale.US);
            redirectAttrs.addFlashAttribute("error", message);
            return "redirect:/permission/list";
        }

        logger.debug("IN: Permission/delete-GET | id = " + id + " | phase = " + phase + " | " + permission.toString());

        if (phase.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Delete", businessObject, permission.getPermissionname()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
            return "redirect:/permission/list";
        } else if (phase.equals(messageSource.getMessage("button.action.stage", null, Locale.US))) {
            logger.debug("     deleting permission : " + permission.toString());
            model.addAttribute("permission", permission);
            return "permission-delete";
        } else if (phase.equals(messageSource.getMessage("button.action.delete", null, Locale.US))) {
            try {
                permissionService.deletePermission(permission.getId());
                String message = messageSource.getMessage("ctrl.message.success.delete", 
                        new Object[] {businessObject, permission.getPermissionname()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/permission/list";
            } catch (PermissionNotFoundException e) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {"permission id", id}, Locale.US);
               redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/permission/list";
           }
        }

        return "redirect:/permission/list";
    }

    @PreAuthorize("hasAnyRole('CTRL_PERM_EDIT_GET','CTRL_PERM_DELETE_GET')")
    public PermissionDTO getPermissionDTO(Permission perm) {
        List<Integer> roleIdList = new ArrayList<Integer>();
        PermissionDTO permDTO = new PermissionDTO();
        permDTO.setId(perm.getId());
        permDTO.setPermissionname(perm.getPermissionname());
        for (Role role : perm.getPermRoles()) {
            roleIdList.add(role.getId());
        }
        permDTO.setPermRoles(roleIdList);
        return permDTO;
    }

    @PreAuthorize("hasAnyRole('CTRL_PERM_ADD_POST','CTRL_PERM_EDIT_POST')")
    public Permission getPermission(PermissionDTO permissionDTO) throws RoleNotFoundException {
        Set<Role> roleList = new HashSet<Role>();
        Permission perm = new Permission();
        Role role = new Role();
        
        perm.setId(permissionDTO.getId());
        perm.setPermissionname(permissionDTO.getPermissionname());
        if (permissionDTO.getPermRoles() != null) {
            for (Integer roleId : permissionDTO.getPermRoles()) {
                role = roleService.getRole(roleId);
                logger.debug("  ROLE: " + role.toString());
                roleList.add(role);
            }
            perm.setPermRoles(roleList);
        }
        logger.debug("  PERM: " + perm.toString());
        return perm;
    }
    

}
