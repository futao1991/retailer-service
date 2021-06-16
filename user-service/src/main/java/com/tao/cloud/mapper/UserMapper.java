package com.tao.cloud.mapper;

import com.tao.cloud.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user_table")
    List<User> getAllUsers();

    @Select("select * from user_table where id=#{id}")
    User getUserById(Long id);

    @Select("select * from user_table where name=#{name}")
    User getUserByName(String name);

    @Insert({"insert into user_table(name, password, money) values(#{name}, #{password}, #{money})"})
    Integer addUser(User user);
}
