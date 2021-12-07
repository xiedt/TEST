package com.laoxie.controller;

import com.laoxie.mapper.FileMapper;
import com.laoxie.pojo.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    FileMapper fileMapper;

    //测试，查找所有文件
//    @RequestMapping("/files")
//    public String list01(Model model){
//        Collection<Files> files = fileMapper.queryFiles();
//        model.addAttribute("filesList",files);
//        return "file/list";
//    }

       //查找个人文件
//    @RequestMapping("/privatefiles")
//    public String list(Model model, HttpSession session){
//        //从session中获取当前用户
//        String owner = (String) session.getAttribute("loginUser");
//       Collection<Files> privateFilesList = fileMapper.queryFilesByOwner(owner);
//       model.addAttribute("privateFilesList",privateFilesList);
//
//        return "file/list1";
//    }
//
       @RequestMapping("/privatefiles")
       public String list(Model model, HttpSession session){
           //从session中获取当前用户
           String owner = (String) session.getAttribute("loginUser");
           Collection<Files> privateFilesList = fileMapper.queryFilesByOwner(owner);
           model.addAttribute("filesList",privateFilesList);

           return "file/list";
       }

    //查出共享文件
    @RequestMapping("/publicfiles")
    public String list02(Model model){
        Collection<Files> files = fileMapper.queryPublicFiles();
        model.addAttribute("filesList",files);
        return "file/list";
    }

    //删除指定文件（未测试）
    @RequestMapping("/deleteFile/{id}")
    public String deleteFile(@PathVariable("id") int id, HttpSession session, Model model){
        //根据传入id找到该文件，判断该文件的所有者是否为登录用户，如果不是，则删除不成功
        Files file01 = fileMapper.queryFileById(id);
        //获取到登录的用户
        String owner = (String)session.getAttribute("loginUser");
        if(owner==null){
            model.addAttribute("msg","用户不存在");
            return "redirect:/privatefiles";
        }else if(!owner.equals(file01.getOwner())){  //不是文件所有者执行删除，删除失败
            session.setAttribute("msg",123);

           // System.out.println(404);
            return "redirect:/publicfiles";
        }else{
            fileMapper.deleteFile(id);
            model.addAttribute("msg","删除成功");
        }
        //fileMapper.deleteFile(id);
        return "redirect:/privatefiles";
    }

    //跳转到本地文件管理页面,并将文件夹里面的文件名字读取出来显示在页面里
    @RequestMapping("/toLocalFile")
    public String turnPage01(Model model){
        List<String> fileList0 = new ArrayList<>();
        //读取文件夹
        File file = new File("D:\\spring_learning\\file-sys\\src\\main\\resources\\uploads");
        fileDirs(file,fileList0);
        model.addAttribute("files",fileList0);
//        for(int i=0;i<fileList0.size();i++){
//            System.out.println(fileList0.get(i));
//        }
        return "file/localFile01";
    }


    //跳转到文件新增页面
    @RequestMapping("/onload")
    public String toAddFile(){
        return "file/add";
    }



    //新增文件操作
    //@ResponseBody
    @RequestMapping("/addFile")
    public String addFiles(Files file,HttpSession session,Model model){
           String owner = (String)session.getAttribute("loginUser");
           if(owner==null){
              model.addAttribute("msg","用户未登录") ;
               return "redirect:/publicfiles";
           }
           file.setOwner(owner);
           file.setCreatetime(new Date());
           if(file.getIsprivate()==null){
               file.setIsprivate(1);
           }
        System.out.println(file);
           fileMapper.addFile(file);
        return "redirect:/privatefiles";
    }

    //跳到修改文件页面
    @RequestMapping("/file/{id}")
    public String updateFiles(@PathVariable("id") Integer id,Model model,HttpSession session){
         //先根据id找出这个文件
        Files file = fileMapper.queryFileById(id);
        //如果当前文件的所有者不是登陆者，则无法修改
        if(!file.getOwner().equals(session.getAttribute("loginUser"))){
            return "redirect:/publicfiles";
        }
        model.addAttribute("file",file);
        session.setAttribute("filecontent",file.getFilecontent());
        System.out.println(file);
        return "file/update";
    }

    //进行修改文件操作
    //@ResponseBody
    @RequestMapping("/updateFile")
    public String updateFiles01(Files file,HttpSession session){

        System.out.println(file);
        file.setOwner((String)session.getAttribute("loginUser"));
        file.setCreatetime(new Date());
            fileMapper.updateFile(file);
           return "redirect:/privatefiles";
    }

    //输入框为空时查找所有共享文档
    @RequestMapping(value={"/findFileByTitle/","/findFileByContent/","/findFileByOwner/","/findAll/"})
    public String list002(Model model){
        Collection<Files> files = fileMapper.queryPublicFiles();
        model.addAttribute("filesList",files);
        return "file/list";
    }

    //根据标题查找文档
    @RequestMapping("/findFileByTitle/{keywords}")
    public String findFileByTitle(@PathVariable("keywords")String filename,Model model,HttpSession session){
          System.out.println(filename);
          String user = (String)session.getAttribute("loginUser");
           Collection<Files> files = fileMapper.queryFileByTitle(filename,user);
        System.out.println(files);
           model.addAttribute("filesList",files);
           return "file/list";
    }
    //根据内容查找文档
    @RequestMapping("/findFileByContent/{keywords}")
    public String findFileByContent(@PathVariable("keywords")String filecontent,Model model,HttpSession session){
        System.out.println(filecontent);
        String user = (String)session.getAttribute("loginUser");
        Collection<Files> files = fileMapper.queryFileByContent(filecontent,user);
        System.out.println(files);
        model.addAttribute("filesList",files);
        return "file/list";
    }
    //根据所有者查找文档
    @RequestMapping("/findFileByOwner/{keywords}")
    public String findFileByOwner(@PathVariable("keywords")String onwer0,Model model,HttpSession session){
        String user = (String)session.getAttribute("loginUser");
        //左边是要查找的文件所有者，右边是登录者
        Collection<Files> files = fileMapper.queryFileByOwner01(onwer0,user);
        System.out.println(files);
        model.addAttribute("filesList",files);
        return "file/list";
    }

    //全文检索，并按照匹配度进行排序
    @RequestMapping("/findAll/{keywords}")
    public String findAll(@PathVariable("keywords")String keys,Model model ,HttpSession session){
        String user = (String)session.getAttribute("loginUser");
        Collection<Files> files = fileMapper.queryAll(keys,user);
        model.addAttribute("filesList",files);
        return "file/list";
    }

    //跳到文档详情查看文档详细内容
    @RequestMapping("/todetail/{id}")
    public String toDetail(@PathVariable("id")Integer id,Model model,HttpSession session){
        //用户登录才能查看文件详情
        String username = (String)session.getAttribute("loginUser");
        if(username==null){
            model.addAttribute("msg","请先登录");
            return "index";
        }
           Files file = fileMapper.queryFileById(id);
        model.addAttribute("file",file);
           return "details";
    }

    //遍历文件夹里面的本地文件
    public static void fileDirs(File file,List<String> fileList){
        if(file!=null){
            File[]files = file.listFiles();
            for(File file0:files){
                fileList.add(file0.getName());
            }
        }else{
            System.out.println("文件不存在");
        }
    }

}
