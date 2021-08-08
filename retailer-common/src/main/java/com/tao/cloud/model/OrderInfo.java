package com.tao.cloud.model;

public class OrderInfo {

    /**
     * 订单ID
     */
    private String id;

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
    private String status;

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

    private OrderInfo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getCount() {
        return count;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public String getStatus() {
        return status;
    }

    public String getPayTime() {
        return payTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public static class OrderInfoBuilder {

        private OrderInfo orderInfo;

        public OrderInfoBuilder() {
            orderInfo = new OrderInfo();
        }

        public OrderInfoBuilder withId(String id) {
            orderInfo.id = id;
            return this;
        }

        public OrderInfoBuilder withUserId(Long userId) {
            orderInfo.userId = userId;
            return this;
        }

        public OrderInfoBuilder withCount(Integer count) {
            orderInfo.count = count;
            return this;
        }

        public OrderInfoBuilder withTotalPrice(Long totalPrice) {
            orderInfo.totalPrice = totalPrice;
            return this;
        }

        public OrderInfoBuilder withCreateTime(String createTime) {
            orderInfo.createTime = createTime;
            return this;
        }

        public OrderInfoBuilder withCommodityId(String commodityId) {
            orderInfo.commodityId = commodityId;
            return this;
        }

        public OrderInfoBuilder withStatus(String status) {
            orderInfo.status = status;
            return this;
        }

        public OrderInfoBuilder withPayTime(String payTime) {
            orderInfo.payTime = payTime;
            return this;
        }

        public OrderInfoBuilder withDeliveryTime(String deliveryTime) {
            orderInfo.deliveryTime = deliveryTime;
            return this;
        }

        public OrderInfoBuilder withCompleteTime(String completeTime) {
            orderInfo.completeTime = completeTime;
            return this;
        }

        public OrderInfo build() {
            return orderInfo;
        }
    }
}
