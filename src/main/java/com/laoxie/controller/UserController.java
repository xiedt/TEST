package com.laoxie.controller;


import com.laoxie.mapper.UserMapper;
import com.laoxie.pojo.User;
import com.laoxie.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserMapper userMapper;


    //跳到修改用户信息页面
    @RequestMapping("/toUpdateUser")
    public String toUpdateUser(HttpSession session, Model model){
        String username = (String)session.getAttribute("loginUser");
        if(username==null){
            model.addAttribute("msg","请先登录");
            return "index";
        }
        //找出当前登录者的信息
        User user = userMapper.findUserByUserName(username);
        System.out.println(user);
        model.addAttribute("currentUser",user);
        return "file/updateuser";
    }

    @RequestMapping("/updateUser")
    public String updateUser(User user,HttpSession session,Model model){
        //用户登录才能更新成功
        String username = (String)session.getAttribute("loginUser");
        if(username==null){
            model.addAttribute("msg","请先登录");
            return "index";
        }
        //执行更新
        userMapper.updateUser(user);
        model.addAttribute("msg","更新成功");
        return "dashboard";
    }

    //跳转到密码更改页面
    @RequestMapping("/changePassword/{id}")
    public String updatePwd(@PathVariable("id") Integer id,Model model,HttpSession session){
        //先找到这个用户的所有信息
        System.out.println(id);
        User user = userMapper.findUserById(id);
        model.addAttribute("user",user);
        return "file/updatepsd";
    }

    //更改密码
    @RequestMapping("/updatepsw")
    public String updatePwd2(User user,Model model){
        String newPassword = Md5Utils.code(user.getPassword());
        user.setPassword(newPassword);
        userMapper.updateUser(user);
        model.addAttribute("msg","登录超时，请重新登录");
        return "index";
    }


    //防止错误请求
    @RequestMapping("/changePassword/")
    public String updatePwd01(HttpSession session,Model model){
        String username = (String)session.getAttribute("loginUser");
        if(username==null){
            model.addAttribute("msg","请先登录");
            return "index";
        }
        return "index";
    }


}
