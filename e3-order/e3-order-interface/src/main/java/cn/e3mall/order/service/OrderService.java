package cn.e3mall.order.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

public interface OrderService {
    /**
     * 创建订单
     * @param orderInfo     订单信息，包含订单详细信息
     * @return              返回添加的订单编号
     */
    E3Result createOrder(OrderInfo orderInfo);
}
