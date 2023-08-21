package com.wzt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzt.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: OrderMapper
 * @Description: 订单
 * @Author: wzt
 * @Create: 2022-08-15 20:03
 **/
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
