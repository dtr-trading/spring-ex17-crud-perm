import java.util.HashSet;
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
public class TestRoleInsert {
    
    private static final String ROLE_CREATE      = "ROLE_TESTER";
    private static final String PERM_01          = "CTRL_STRATEGY_LIST_GET";
    private static final String PERM_02          = "CTRL_TEST_01";
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permService;

    @Test
    public void testRoleInsert() throws Exception {
        System.out.println("\n\n### Starting: testRoleInsert()");
        
        Set <Permission> permissions = new HashSet<Permission>();
        Permission perm1 = permService.getPermission(PERM_01);
        Permission perm2 = permService.getPermission(PERM_02);
        
        permissions.add(perm1);
        permissions.add(perm2);
        
        Role role = new Role();
        role.setRolename(ROLE_CREATE);
        role.setPermissions(permissions);
        roleService.addRole(role);
        
        System.out.println("\n\n### Ending: testRoleInsert()");
    }

    public void printAllPermissions(Set <Permission> permissions) throws Exception {
        for (Permission perm : permissions) {   
            System.out.println("      perm: [" + perm.getPermissionname() + "]"); 
        }
    }
}
