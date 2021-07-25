package com.tao.cloud.payservice.service;

import com.tao.cloud.model.MQTransaction;
import com.tao.cloud.model.User;
import com.tao.cloud.payservice.feign.OrderService;
import com.tao.cloud.payservice.mapper.MQTransactionMapper;
import com.tao.cloud.payservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MQTransactionMapper transactionMapper;

    @Autowired
    private OrderService orderService;

    @Transactional
    public boolean addOrDecreaseMoney(Long userId, Long money, String transactionId, String orderId) {
        User user = userMapper.getUserById(userId);
        if (null != user) {
            if (userMapper.addOrDecreaseMoney(userId, money, user.getVersion()) <= 0) {
                return false;
            }
            MQTransaction transaction = new MQTransaction();
            transaction.setId(transactionId);
            transaction.setBusiness("order");
            transaction.setOrderId(orderId);
            transactionMapper.addTransaction(transaction);

            orderService.deleteOrder(orderId);

            return true;
        }
        return false;
    }
}
