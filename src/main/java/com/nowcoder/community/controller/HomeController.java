package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private DiscussPostService discussPostService;
    private UserService userService;

    @Autowired
    public HomeController(DiscussPostService discussPostService, UserService userService) {
        this.discussPostService = discussPostService;
        this.userService = userService;
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model
        //所以，在thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> posts = new ArrayList<>();
        for (DiscussPost post : discussPosts) {
            Map<String, Object> map = new HashMap<>();
            map.put("post", post);
            User user = userService.findUserById(post.getUserId());
            map.put("user", user);
            posts.add(map);
        }
        model.addAttribute("discussPosts", posts);
        return "/index";//这个方法返回后，SpringMVC会将Model一起返回给view
    }
}
