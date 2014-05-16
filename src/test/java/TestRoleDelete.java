import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dtr.oas.config.RootConfig;
import com.dtr.oas.model.Role;
import com.dtr.oas.model.Permission;
import com.dtr.oas.service.PermissionService;
import com.dtr.oas.service.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class TestRoleDelete {
    
    private static final String ROLE_CREATE      = "ROLE_TESTER";
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permService;

    
    @Test
    public void testDeleteRole() throws Exception {
        System.out.println("\n\n### Starting: testDeleteRole()");
        Role role = roleService.getRole(ROLE_CREATE);
        System.out.println("  " + role.toString());
        printAllPermissions(role.getPermissions());
        roleService.deleteRole(role.getId());
        System.out.println("### Ending: testDeleteRole()\n\n\n");
    }

    public void printAllPermissions(Set <Permission> permissions) throws Exception {
        for (Permission perm : permissions) {   
            System.out.println("      perm: [" + perm.getPermissionname() + "]"); 
        }
    }
}
