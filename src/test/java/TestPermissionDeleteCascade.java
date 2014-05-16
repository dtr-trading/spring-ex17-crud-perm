import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dtr.oas.config.RootConfig;
import com.dtr.oas.model.Permission;
import com.dtr.oas.service.PermissionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class TestPermissionDeleteCascade {
    
    private static final String PERM_MOD = "CTRL_TEST_01";
    
    @Autowired
    private PermissionService permissionService;

    @Test
    public void testRole() throws Exception {
        System.out.println("\n\n### Starting: testDeletePermission()");

        Permission perm = permissionService.getPermission(PERM_MOD);
        System.out.println("  " + perm.toString());
        permissionService.deletePermission(perm.getId());

        System.out.println("### Ending: testDeletePermission()\n\n\n");
    }
}
