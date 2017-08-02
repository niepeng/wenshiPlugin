package com.niepeng.xue.wenshiplugin.dao.mapper;

import com.niepeng.xue.wenshiplugin.dao.EquipdataDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EquipdataDOMapper {

    EquipdataDO selectByPrimaryKey(int equipmentid);


    int deleteByPrimaryKey(Short equipmentid);

    int insert(EquipdataDO record);

    int insertSelective(EquipdataDO record);

    int updateByPrimaryKeySelective(EquipdataDO record);

    int updateByPrimaryKey(EquipdataDO record);
}