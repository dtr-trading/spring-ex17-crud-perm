import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dtr.oas.config.RootConfig;
import com.dtr.oas.model.Role;
import com.dtr.oas.model.User;
import com.dtr.oas.service.RoleService;
import com.dtr.oas.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class TestUserInsert {
	
    private static final String TEST_USER_CREATE = "tester001";
	private static final String INIT_PASSWORD    = "password01";
    //private static final String INIT_ROLE        = "ROLE_USER";
    private static final String INIT_ROLE        = "ROLE_TESTER";
	
	@Autowired
	private UserService userService;

    @Autowired
    private RoleService roleService;

	@Test
	public void testUser() throws Exception {
        Role role = (Role) roleService.getRole(INIT_ROLE);

        System.out.println("\n\n### Starting: testInsertUser()");
		User user = new User();
		user.setUsername(TEST_USER_CREATE);
		user.setPassword(INIT_PASSWORD);
		user.setEnabled(true);
        user.setRole(role);
		userService.addUser(user);

	    user = userService.getUser(TEST_USER_CREATE);
	    System.out.println("  " + user.toString());
	    printAllPermissions(user);
	    System.out.println("### Ending: testInsertUser()");
	}

    public void printAllPermissions(User user) throws Exception {
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority auth : authorities) {   
            System.out.println("      auth: [" + auth.getAuthority() + "]"); 
        }
    }
}
