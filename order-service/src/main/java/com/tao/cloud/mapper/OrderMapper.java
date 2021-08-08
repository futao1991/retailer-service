package com.tao.cloud.mapper;

import com.tao.cloud.model.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {

    @Insert("insert into order_info(id, userid, commodity_id, `count`, "
            + "total_price, status, create_time, delivery_time, complete_time) "
            + "values(#{id}, #{userId}, #{commodityId}, ${count}, ${totalPrice}, "
            + "'${status}', '${createTime}', '${deliveryTime}', '${completeTime}')")
    Integer addOrder(OrderInfo orderInfo);

    @Update("update order_info set status=`${status}`, create_time=`${create_time}`, "
            + "delivery_time=`${delivery_time}`, complete_time=`${complete_time}`")
    Insert updateOrder(OrderInfo orderInfo);
}
