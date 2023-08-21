package com.wzt.reggie.dto;

import com.wzt.reggie.entity.OrderDetail;
import com.wzt.reggie.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: OrderDto
 * @Description: 扩展数据orderdetail
 * @Author: wzt
 * @Create: 2022-08-18 15:31
 **/

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails=new ArrayList<>();


}
