package com.wzt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzt.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: OrderDetailMapper
 * @Description: 订单详情
 * @Author: wzt
 * @Create: 2022-08-15 20:05
 **/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
