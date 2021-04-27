package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private UserService userService;
    private HostHolder hostHolder;

    @Autowired
    public UserController(UserService userService, HostHolder hostHolder) {
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @RequestMapping(path = "/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @RequestMapping(path = "upload", method = RequestMethod.POST)
    //这个名为headerImage的参数会自动接收post过来的名为headerImage的内容
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片！");
            return "/site/setting";
        }

        //检查后缀
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确！");
            return "/site/setting";
        }

        //生成一个随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的位置
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        //更新当前用户头像的web访问路径
        //例如http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    //因为返回的是二进制数据，所以直接用流向浏览器输出
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //图片在服务器中存放的位置
        fileName = uploadPath + "/" + fileName;
        //向浏览器输出之前要明确输出的格式
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        //用字节流输出图片
        try ( //response的流是springMVC帮助管理的，不用手动关闭
              ServletOutputStream os = response.getOutputStream();
              //fis是自己创建的，需要手动关闭
              FileInputStream fis = new FileInputStream(fileName);) {
            //使用缓冲区,每次从文件中读取1024字节的数据，每次向浏览器输出1024字节
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取图像失败" + e.getMessage());
        }

    }

    @RequestMapping(path = "password", method = RequestMethod.POST)
    public String setPassword(){

    }
}
