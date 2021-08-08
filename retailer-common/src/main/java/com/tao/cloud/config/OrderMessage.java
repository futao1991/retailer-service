package com.tao.cloud.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tao.cloud.model.OrderInfo;
import com.tao.cloud.model.OrderInfo.OrderInfoBuilder;

/**
 * 订单类
 */
public class OrderMessage {

    public enum OrderStatus {

        UNPAID("unpaid"),

        WAITDELIVERY("wait_delivery"),

        DELIVERED("delivered"),

        COMPLETED("completed");

        private String status;

        OrderStatus(String status) {
            this.status = status;
        }
    }

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 商品总价格
     */
    private Long totalPrice;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 商品ID
     */
    private String commodityId;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 发货时间
     */
    private String deliveryTime;

    /**
     * 完成时间
     */
    private String completeTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public OrderMessage() {

    }

    public OrderMessage(String orderId, Long userId, Integer count, Long totalPrice,
                        String createTime, String commodityId, OrderStatus status) {
        this.orderId = orderId;
        this.userId = userId;
        this.count = count;
        this.totalPrice = totalPrice;
        this.createTime = createTime;
        this.commodityId = commodityId;
        this.status = status;
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public static OrderMessage toOrderMessage(String value) {
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(value);
        } catch (Exception e) {
            return null;
        }

        String orderId = jsonObject.getString("orderId");
        Long userId = jsonObject.getLong("userId");
        Integer count = jsonObject.getInteger("count");
        Long totalPrice = jsonObject.getLong("totalPrice");
        String createTime = jsonObject.getString("createTime");
        String commodityId = jsonObject.getString("commodityId");
        OrderStatus status = OrderStatus.valueOf(jsonObject.getString("status"));
        String payTime = jsonObject.getString("payTime");
        String deliveryTime = jsonObject.getString("deliveryTime");
        String completeTime = jsonObject.getString("completeTime");

        OrderMessage orderMessage = new OrderMessage(orderId, userId, count, totalPrice, createTime, commodityId, status);
        if (null != payTime) {
            orderMessage.setPayTime(payTime);
        }
        if (null != deliveryTime) {
            orderMessage.setDeliveryTime(deliveryTime);
        }
        if (null != completeTime) {
            orderMessage.setCompleteTime(completeTime);
        }

        return orderMessage;
    }

    public OrderInfo toOrderInfo() {

        OrderInfoBuilder orderInfoBuilder = new OrderInfoBuilder();
        return orderInfoBuilder.withId(this.orderId)
                .withCommodityId(this.commodityId)
                .withUserId(this.userId)
                .withCount(this.count)
                .withStatus(this.status.status)
                .withCreateTime(this.createTime)
                .withDeliveryTime(this.deliveryTime)
                .withCompleteTime(this.completeTime)
                .withTotalPrice(this.totalPrice)
                .build();
    }
}
