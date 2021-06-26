package com.tao.cloud.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

public class Commodity {

    /**
     * 商品id
     */
    private String commodityId;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 当前库存
     */
    private Integer warehouseCount;

    /**
     * 商品单件价格(分)
     */
    private Long commodityPrice;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public Integer getWarehouseCount() {
        return warehouseCount;
    }

    public void setWarehouseCount(Integer warehouseCount) {
        this.warehouseCount = warehouseCount;
    }

    public Long getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(Long commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("commodityId: ").append(commodityId).append(',');
        sb.append(" commodityName: ").append(commodityName).append(',');
        sb.append(" commodityPrice: ").append(commodityPrice).append(',');
        sb.append(" warehouseCount: ").append(warehouseCount);
        return sb.toString();
    }

    public static Commodity toCommodity(String json) {
        JSONObject object = JSON.parseObject(json);
        Commodity commodity = new Commodity();
        commodity.setCommodityId(object.getString("commodityId"));
        commodity.setCommodityName(object.getString("commodityName"));
        commodity.setCommodityPrice(object.getLong("commodityPrice"));
        commodity.setWarehouseCount(object.getInteger("warehouseCount"));
        return commodity;
    }

    public static void main(String[] args) {
        Commodity commodity = new Commodity();
        commodity.setCommodityId("1001");
        commodity.setCommodityName("T-Shirt");
        commodity.setCommodityPrice(15000L);
        commodity.setWarehouseCount(1000);

        String json = JSON.toJSONString(commodity);
        System.out.println(json);
        commodity = toCommodity(json);
        System.out.println(commodity);
    }
}
