package com.wzt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.entity.OrderDetail;
import com.wzt.reggie.mapper.OrderDetailMapper;
import com.wzt.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: OrderDetailServicelmpl
 * @Description: 订单详情
 * @Author: wzt
 * @Create: 2022-08-15 20:10
 **/
@Service
public class OrderDetailServicelmpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
