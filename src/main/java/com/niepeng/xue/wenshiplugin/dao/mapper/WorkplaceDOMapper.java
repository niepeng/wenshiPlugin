package com.niepeng.xue.wenshiplugin.dao.mapper;

import com.niepeng.xue.wenshiplugin.dao.WorkplaceDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkplaceDOMapper {

    List<WorkplaceDO> findList();

    int deleteByPrimaryKey(int placeid);

    int insert(WorkplaceDO record);

    int insertSelective(WorkplaceDO record);

    WorkplaceDO selectByPrimaryKey(int placeid);

    int updateByPrimaryKeySelective(WorkplaceDO record);

    int updateByPrimaryKey(WorkplaceDO record);
}