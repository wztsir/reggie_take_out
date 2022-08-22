package com.wzt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.entity.Employee;
import com.wzt.reggie.mapper.EmployeeMapper;
import com.wzt.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;


/**
 * ServiceImpl为mybatisplus提供的
 */
@Service                                    //这两个泛型一个是实体类对应的mapper,一个是实体类
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
