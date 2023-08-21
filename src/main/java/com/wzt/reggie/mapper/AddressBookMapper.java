package com.wzt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzt.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: AddressBookMapper
 * @Description: 地址簿
 * @Author: wzt
 * @Create: 2022-08-15 14:38
 **/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
