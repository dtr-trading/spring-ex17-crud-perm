import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dtr.oas.config.RootConfig;
import com.dtr.oas.model.User;
import com.dtr.oas.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class TestUserDelete {
	
    private static final String TEST_USER_CREATE = "tester001";
	
	@Autowired
	private UserService userService;

	@Test
	public void testUser() throws Exception {
		System.out.println("\n\n### Starting: testDeleteUser()");
		User user = userService.getUser(TEST_USER_CREATE);
		System.out.println("  " + user.toString());
        printAllPermissions(user);
		userService.deleteUser(user.getId());
		System.out.println("### Ending: testDeleteUser()\n\n\n");
	}

    public void printAllPermissions(User user) throws Exception {
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority auth : authorities) {   
            System.out.println("      auth: [" + auth.getAuthority() + "]"); 
        }
    }

}
