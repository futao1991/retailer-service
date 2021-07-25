package com.tao.cloud.service.impl;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.config.OrderMessage.OrderStatus;
import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.service.OrderService;
import com.tao.cloud.util.RedisUtil;
import com.tao.cloud.util.TimeUtils;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public OrderMessage createOrder(String orderId, Long userId, String commodityId, Integer count) {

        String createTime = TimeUtils.getCurrentTime();
        Long totalPrice = redisUtil.getCommdityPrice(commodityId);

        OrderMessage orderMessage = new OrderMessage(orderId, userId, count, totalPrice,
                                                     createTime, commodityId, OrderStatus.UNPAID);
        redisUtil.insertOrder(orderId, orderMessage);

        return orderMessage;
    }

    @Override
    public boolean commitOrder(BusinessActionContext context) {
        logger.info("事务{}提交成功", context.getXid());
        return true;
    }

    @Override
    public boolean cancelOrder(BusinessActionContext context) {
        logger.info("准备回滚事务{}", context.getXid());
        String orderId = (String)context.getActionContext("orderId");
        redisUtil.deleteOrder(orderId);
        return true;
    }

    @Override
    public OrderMessage deleteOrder(String orderId) {
        OrderMessage orderMessage = redisUtil.getOrderMessageByKey(orderId);
        if (null != orderMessage) {
            redisUtil.deleteOrder(orderId);
        }
        return orderMessage;
    }
}
