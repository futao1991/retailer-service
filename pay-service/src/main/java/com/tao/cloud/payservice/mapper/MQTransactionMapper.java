package com.tao.cloud.payservice.mapper;

import com.tao.cloud.model.MQTransaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MQTransactionMapper {

    @Insert("insert into mq_transaction(id, business, orderId) values(#{id}, #{business}, #{orderId})")
    Integer addTransaction(MQTransaction transaction);

    @Select("select * from mq_transaction where id=#{transactionId}")
    MQTransaction getTransaction(String transactionId);
}
