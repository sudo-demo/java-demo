package com.cobazaar.common.utils;

import com.cobazaar.common.vo.UserInfoVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class UserContextTest {

    @Test
    public void testUserContext() {
        // 1. Set User
        UserInfoVO user = new UserInfoVO();
        user.setId(123L);
        user.setUsername("testUser");
        user.setRoles(Arrays.asList("admin", "user"));
        user.setPermissions(Arrays.asList("user:add", "user:edit"));

        UserContext.setUser(user);

        // 2. Verify User
        Assertions.assertEquals(123L, UserContext.getUserId());
        Assertions.assertEquals("testUser", UserContext.getUsername());
        Assertions.assertTrue(UserContext.hasRole("admin"));
        Assertions.assertTrue(UserContext.hasPermission("user:add"));
        Assertions.assertFalse(UserContext.hasRole("superadmin"));

        // 3. Clear User
        UserContext.clear();
        Assertions.assertNull(UserContext.getUser());
        Assertions.assertNull(UserContext.getUserId());
    }
}
