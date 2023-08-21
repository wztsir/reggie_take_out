package com.wzt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzt.reggie.entity.Orders;

/**
 * @ClassName: OrderService
 * @Description: 订单
 * @Author: wzt
 * @Create: 2022-08-15 20:06
 **/
public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
