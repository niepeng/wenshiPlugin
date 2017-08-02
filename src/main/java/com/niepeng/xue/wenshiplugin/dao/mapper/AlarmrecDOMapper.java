package com.niepeng.xue.wenshiplugin.dao.mapper;

import com.niepeng.xue.wenshiplugin.dao.AlarmrecDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmrecDOMapper {

    AlarmrecDO selectAlarmLastOne();

    List<AlarmrecDO> selectByAfterAlaramid(Integer alarmid);



    int deleteByPrimaryKey(Integer alarmid);

    int insert(AlarmrecDO record);

    int insertSelective(AlarmrecDO record);

    AlarmrecDO selectByPrimaryKey(Integer alarmid);

    int updateByPrimaryKeySelective(AlarmrecDO record);

    int updateByPrimaryKey(AlarmrecDO record);
}