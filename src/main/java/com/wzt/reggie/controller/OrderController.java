package com.wzt.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzt.reggie.common.BaseContext;
import com.wzt.reggie.common.R;
import com.wzt.reggie.dto.OrderDto;
import com.wzt.reggie.entity.OrderDetail;
import com.wzt.reggie.entity.Orders;
import com.wzt.reggie.entity.ShoppingCart;
import com.wzt.reggie.service.OrderDetailService;
import com.wzt.reggie.service.OrderService;
import com.wzt.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: OrderController
 * @Description: 订单
 * @Author: wzt
 * @Create: 2022-08-15 20:24
 **/
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 下订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据:{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }



    /**
     * 订单派送，修改status
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> send(@RequestBody Orders orders){
        Long id = orders.getId();
        Integer status = orders.getStatus();
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,id);
        Orders one = orderService.getOne(queryWrapper);
        one.setStatus(status);
        orderService.updateById(one);
        return R.success("派送成功");
    }

    /**
     * 客户端订单显示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage=new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getThreadLocalId());
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, orderDtoPage, "records");


        List<Orders> records =pageInfo.getRecords();
        List<OrderDto> list=records.stream().map((item)->{
//         补充orderDetail的信息
            OrderDto orderDto=new OrderDto();
            BeanUtils.copyProperties(item, orderDto);

            Long orderId=item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> res = orderDetailService.list(queryWrapper1);
            orderDto.setOrderDetails(res);
            return orderDto;
        }).collect(Collectors.toList());


        orderDtoPage.setRecords(list);

        log.info("查询订单数据为：{}", orderDtoPage);
        return R.success(orderDtoPage);
    }


    /**
     * 管理端订单管理
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number,String beginTime,String endTime){
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        Page<OrderDto> ordersDtoPage=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //根据number进行模糊查询
        queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
        //根据Datetime进行时间范围查询

//        log.info("开始时间：{}",beginTime);
//        log.info("结束时间：{}",endTime);
        if(beginTime!=null&&endTime!=null){
//            ge 大于
//            le 小于
            queryWrapper.ge(Orders::getOrderTime,beginTime);
            queryWrapper.le(Orders::getOrderTime,endTime);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //进行分页查询
        orderService.page(pageInfo,queryWrapper);

//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
//
//        List<Orders> records=pageInfo.getRecords();
////添加数据用户name
//        List<OrderDto> list=records.stream().map((item)->{
//            OrderDto ordersDto=new OrderDto();
//
//            BeanUtils.copyProperties(item,ordersDto);
//            String name=item.getUserId().toString();
//
//            ordersDto.setUserName(name);
//            return ordersDto;
//        }).collect(Collectors.toList());

//        ordersDtoPage.setRecords(list);
        return R.success(pageInfo);
    }


    /**
     * 再来一单，将数据加入购物车，并且注意添加数据的时间
     * @param orders
     * @return
     */

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        Long id = orders.getId();
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> list = orderDetailService.list(queryWrapper);

//
        List<ShoppingCart> res=list.stream().map((item)->{

            ShoppingCart shoppingCart=new ShoppingCart();
            shoppingCart.setUserId(BaseContext.getThreadLocalId());

            BeanUtils.copyProperties(item,shoppingCart);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartService.saveBatch(res);
        return R.success("数据添加成功");
    }


}


