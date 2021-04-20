package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class MyTests {
    @Autowired
    private UserService userService;
    @Test
    public void testMy(){
        User user = userService.findUserById(160);
        System.out.println(user.getPassword());
        System.out.println(CommunityUtil.md5(123+"a7d61"));
    }
}
