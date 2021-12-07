package com.laoxie.controller;

import com.laoxie.mapper.UserMapper;
import com.laoxie.pojo.User;
import com.laoxie.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/user/login")
    public String Login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session
    ){
        System.out.println(username);
        User user = userMapper.findUserByUserName(username);
        if (user == null) {
            model.addAttribute("msg", "此用户不存在");
            System.out.println("meizhaodao");
            return "index";
        } else {
            if (user.getPassword().equals(Md5Utils.code(password))) {
                //登陆成功后添加在session
                session.setAttribute("loginUser", username);
                model.addAttribute("msg", "success");
                return "dashboard";
            } else {
                model.addAttribute("msg", "用户名或者密码错误");
                return "index";
            }
        }
    }

    //跳转到用户注册页面
    @RequestMapping("/register")
    public String register(Model model){
        return "register";
    }

    //用户注册测试
   // @ResponseBody
    @RequestMapping("/register01")
    public String register01(
    @RequestParam("username") String username,
    @RequestParam("password") String password,
    Model model
    ){
        User user = userMapper.findUserByUserName(username);
        if(user!=null){
            model.addAttribute("msg",0);
        }else {

            if(username==""){
                model.addAttribute("msg","请输入用户名");
                return "register";
            }
            if(password==""){
                model.addAttribute("msg","请输入密码");
                return "register";
            }
            //对密码进行加密
            String password1 = Md5Utils.code(password);

            userMapper.addUser(new User(username,password1));
            model.addAttribute("msg",1);
        }

        return "register";
    }




    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
