package com.tao.cloud.util;

import java.util.Random;

public class OrderUtil {

    /**
     * 生成订单编号 当前时间+随机数字
     * @return 订单编号
     */
    public static String createOrderId() {
        String timeString = TimeUtils.getTimeString();
        return timeString + Integer.toHexString(new Random().nextInt(900)+100);
    }

}
