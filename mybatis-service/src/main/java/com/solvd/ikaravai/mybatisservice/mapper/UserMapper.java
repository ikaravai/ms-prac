package com.solvd.ikaravai.mybatisservice.mapper;

import com.solvd.ikaravai.mybatisservice.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findAllUsers();
    User findUserById(Long id);
    void saveUser(User user);
    void updateUser(User user, Long id);
    void deleteUser(Long id);
}
