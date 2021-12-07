package com.laoxie.mapper;

import com.laoxie.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    //根据用户名查找用户
    User findUserByUserName(String username);

    //根据id找用户
    User findUserById(Integer id);

    //注册新用户
    void addUser(User user);

    //更改用户信息
    void updateUser(User user);
}
