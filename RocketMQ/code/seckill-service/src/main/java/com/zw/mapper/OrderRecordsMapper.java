package com.zw.mapper;

import com.zw.entity.OrderRecords;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderRecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRecords record);

    int insertSelective(OrderRecords record);

    OrderRecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRecords record);

    int updateByPrimaryKey(OrderRecords record);
}