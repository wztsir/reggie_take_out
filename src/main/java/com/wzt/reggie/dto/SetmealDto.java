package com.wzt.reggie.dto;

import com.wzt.reggie.entity.Setmeal;
import com.wzt.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
