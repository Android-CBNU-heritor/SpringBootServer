package com.billlog.rest.api.mapper;

import com.billlog.rest.api.model.common.FoodCity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommonMapper {
    //도시 전체 목록 가져오기
    @Select("select * from food_city")
    List<FoodCity> getCitys();
}
