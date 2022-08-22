package com.wzt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.entity.ShoppingCart;
import com.wzt.reggie.mapper.ShoppingCartMapper;
import com.wzt.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ShoppingCartServiceImpl
 * @Description: 购物车
 * @Author: wzt
 * @Create: 2022-08-15 19:13
 **/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
