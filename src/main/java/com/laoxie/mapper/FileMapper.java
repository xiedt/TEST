package com.laoxie.mapper;

import com.laoxie.pojo.Files;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FileMapper {
    //测试查出所有文件
    List<Files> queryFiles();

    //查出个人文件
    List <Files> queryFilesByOwner(String owner);

    //根据id找出指定文件
    Files queryFileById(int id);

    //更新文档
    void updateFile(Files file);

    //根据标题查找文档
    List<Files> queryFileByTitle(String filename,String currentUser);

    //根据文档内容查找文档
    List<Files> queryFileByContent(String filecontent,String currentUser);

    //根据文件所有者查找文档,左边是要查找的文件所有者，右边是登录者
    List<Files> queryFileByOwner01(String owner0,String owner1);

    //全文检索,传入关键词(可能为所有者)和当前登陆者
    List<Files> queryAll(String keys,String owner);

    //新增文档
    int addFile(Files file);

    //查出共享文件
    List<Files> queryPublicFiles();

    //根据id删除指定的文档
    int deleteFile(int id);

}
