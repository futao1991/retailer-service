package com.tao.cloud.payservice.mapper;

import com.tao.cloud.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from user_table where id=#{id}")
    User getUserById(Long id);

    @Update("update user_table set money=money-#{money}, version=version+1 where id=#{id} and money>=#{money} and version=#{version}")
    Integer addOrDecreaseMoney(@Param("id") Long id, @Param("money") Long money, @Param("version") Integer version);
}
