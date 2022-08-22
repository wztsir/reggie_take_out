package com.wzt.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzt.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

//mybatisplus
//接口只能继承接口  extends
//类才可以实现接口  implements
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
