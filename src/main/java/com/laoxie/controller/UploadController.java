package com.laoxie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {
    //接收上传文件请求,返回字符串
    @ResponseBody
    @RequestMapping("/uploadFile")
    public String uploadLocal(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return "上传失败，文件为空";
        }
        try{
            byte[] bytes = file.getBytes();
            Path path = Paths.get("D:\\spring_learning\\file-sys\\src\\main\\resources\\uploads\\" + file.getOriginalFilename());
            Files.write(path, bytes);
            return "上传成功";

        }catch (Exception e){
            e.printStackTrace();
        }
        return "上传失败！";
    }
    }

