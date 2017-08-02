package com.niepeng.xue.wenshiplugin.dao.mapper;

import com.niepeng.xue.wenshiplugin.dao.SysparamDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysparamDOMapper {

    SysparamDO selectByArgskey(String key);

    int deleteByPrimaryKey(Integer id);

    int insert(SysparamDO record);

    int insertSelective(SysparamDO record);

    SysparamDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysparamDO record);

    int updateByPrimaryKey(SysparamDO record);
}