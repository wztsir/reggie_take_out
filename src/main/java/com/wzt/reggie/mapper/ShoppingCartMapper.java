package com.wzt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzt.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ShoppingCartMapper
 * @Description: 购物车
 * @Author: wzt
 * @Create: 2022-08-15 19:11
 **/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {


}
