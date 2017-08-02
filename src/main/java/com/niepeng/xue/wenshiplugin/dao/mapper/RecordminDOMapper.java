package com.niepeng.xue.wenshiplugin.dao.mapper;

import com.niepeng.xue.wenshiplugin.dao.RecordminDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordminDOMapper {

    long getLastRecTime();

    List<RecordminDO> queryList(Map<String, Object> map);

    int deleteByPrimaryKey(Integer recid);

    int insert(RecordminDO record);

    int insertSelective(RecordminDO record);

    RecordminDO selectByPrimaryKey(Integer recid);

    int updateByPrimaryKeySelective(RecordminDO record);

    int updateByPrimaryKey(RecordminDO record);
}