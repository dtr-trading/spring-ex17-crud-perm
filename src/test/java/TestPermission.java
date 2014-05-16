import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dtr.oas.config.RootConfig;
import com.dtr.oas.exception.DuplicatePermissionException;
import com.dtr.oas.exception.PermissionNotFoundException;
import com.dtr.oas.model.Permission;
import com.dtr.oas.model.Role;
import com.dtr.oas.service.PermissionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class TestPermission {
    
    private static final String DUP_PERM_CREATE  = "CTRL_STRATEGY_LIST_GET";
    private static final String PERM_CREATE      = "CTRL_TEST";
    private static final String PERM_MOD         = "CTRL_TEST_01";
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private PermissionService permissionService;

    @Test(expected=DuplicatePermissionException.class) 
    public void insertDuplicatePermission() throws Exception {
        System.out.println("\n\n### Starting: insertDuplicatePermission()");
        
        Permission perm = new Permission();
        perm.setPermissionname(DUP_PERM_CREATE);
        
        permissionService.addPermission(perm);
        
        System.out.println("\n\n### Ending: insertDuplicatePermission()");
    }

    @Test(expected=PermissionNotFoundException.class)
    public void getNonExistentPermission() throws Exception {
        System.out.println("\n\n### Starting: getNonExistentPermission()");
        
        Permission perm = permissionService.getPermission("ROGUE_PERM");
        System.out.println("Permission: [" + perm.toString() +"]");
        System.out.println("\n\n### Ending: getNonExistentPermission()");
    }
    
    @Test(expected=PermissionNotFoundException.class)
    public void getNonExistentPermissionByID() throws Exception {
        System.out.println("\n\n### Starting: getNonExistentPermissionByID()");
        
        Permission perm = permissionService.getPermission(99);
        System.out.println("Permission: [" + perm.toString() +"]");
        System.out.println("\n\n### Ending: getNonExistentPermissionByID()");
    }
    
    @Test
    public void testRole() throws Exception {
        System.out.println("\n\n### Starting: testInsertPermission()");

        Permission perm = new Permission();
        perm.setPermissionname(PERM_CREATE);
        
        permissionService.addPermission(perm);
        
        System.out.println("### Ending: testInsertPermission()");
    
        
        
        System.out.println("\n\n### Starting: testGetPermissionByName(): " + PERM_CREATE);
        
        perm = permissionService.getPermission(PERM_CREATE);
        System.out.println("  " + perm.toString());

        System.out.println("### Ending: testGetPermissionByName()");

        
        
        System.out.println("\n\n### Starting: testGetPermissionByInt(): " + PERM_CREATE);
        
        perm = permissionService.getPermission(perm.getId());
        System.out.println("  " + perm.toString());

        System.out.println("### Ending: testGetPermissionByInt()");


        
        System.out.println("\n\n### Starting: testUpdatePermission()");
        
        perm = permissionService.getPermission(PERM_CREATE);
        System.out.println("  Init perm: " + perm.toString());

        perm.setPermissionname(PERM_MOD);;
        permissionService.updatePermission(perm);
        
        perm = permissionService.getPermission(perm.getId());
        System.out.println("  Mod perm: " + perm.toString());

        System.out.println("### Ending: testUpdatePermission()");

        
        
        System.out.println("\n\n### Starting: testDeletePermission()");

        perm = permissionService.getPermission(perm.getId());
        System.out.println("  " + perm.toString());

        permissionService.deletePermission(perm.getId());

        System.out.println("### Ending: testDeletePermission()\n\n\n");
    }

    @Test
    public void testGetAllPermission() throws Exception {
        System.out.println("\n\n### Starting: testGetAllPermission()");
        List<Permission> permsList = permissionService.getPermissions();
        
        System.out.println("  List:");
        for (Permission perm : permsList) {
            System.out.println("    " + perm.toString());
            printAllRoles(perm.getPermRoles());
        }
        System.out.println("### Ending: testGetAllPermission()\n\n");
    }
    
    public void testListAllBeans() throws Exception {
        final String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (final String beanName : beanNames) {   
            System.out.println("\tbean name:" + beanName); 
        }
    }

    public void printAllRoles(Set <Role> roles) throws Exception {
        for (Role role : roles) {   
            System.out.println("      role: [" + role.getAuthority() + "]"); 
        }
    }

}
