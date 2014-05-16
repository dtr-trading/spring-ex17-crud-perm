package com.dtr.oas.dao;

import java.util.List;

import com.dtr.oas.exception.DuplicateRoleException;
import com.dtr.oas.exception.RoleNotFoundException;
import com.dtr.oas.model.Role;

public interface RoleDAO {

    public void addRole(Role role) throws DuplicateRoleException;

    public Role getRole(int id) throws RoleNotFoundException;

    public Role getRole(String roleName) throws RoleNotFoundException;

    public void updateRole(Role role) throws RoleNotFoundException, DuplicateRoleException;

    public void deleteRole(int id) throws RoleNotFoundException;

    public List<Role> getRoles();

}
