package com.niepeng.webshiplugin.test.dao;


import com.niepeng.webshiplugin.test.BaseTest;
import com.niepeng.xue.wenshiplugin.common.util.DateUtil;
import com.niepeng.xue.wenshiplugin.dao.AlarmrecDO;
import com.niepeng.xue.wenshiplugin.dao.EquipdataDO;
import com.niepeng.xue.wenshiplugin.dao.RecordminDO;
import com.niepeng.xue.wenshiplugin.dao.SysparamDO;
import com.niepeng.xue.wenshiplugin.dao.WorkplaceDO;
import com.niepeng.xue.wenshiplugin.dao.mapper.AlarmrecDOMapper;
import com.niepeng.xue.wenshiplugin.dao.mapper.EquipdataDOMapper;
import com.niepeng.xue.wenshiplugin.dao.mapper.RecordminDOMapper;
import com.niepeng.xue.wenshiplugin.dao.mapper.SysparamDOMapper;
import com.niepeng.xue.wenshiplugin.dao.mapper.WorkplaceDOMapper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * @version 1.0
 * @date 17/8/2
 */

public class DaoTest extends BaseTest {

  @Autowired
  private RecordminDOMapper recordminDOMapper;

  @Autowired
  private EquipdataDOMapper equipdataDOMapper;

  @Autowired
  private SysparamDOMapper sysparamDOMapper;

  @Autowired
  private WorkplaceDOMapper workplaceDOMapper;

  @Autowired
  private AlarmrecDOMapper alarmrecDOMapper;


  @Test
  public void test_recordmin_getLastRecTime() {
    long lastRecTime = recordminDOMapper.getLastRecTime();
    Assert.assertTrue(lastRecTime > 0);
  }

  @Test
  public void test_recordMin_queryList() {
    Map<String, Object> map = new HashMap<>();
    Date minDate = DateUtil.parse("2017-08-01 13:46:00");
    Date maxDate = DateUtil.parse("2017-08-01 14:31:00");
    map.put("minRecLong", minDate.getTime());
    map.put("maxRecLong", maxDate.getTime());
    List<RecordminDO> list = recordminDOMapper.queryList(map);
    Assert.assertTrue(!CollectionUtils.isEmpty(list));
  }

  @Test
  public void test_equipData_selectByPrimaryKey() {
    int eqipmentId = 1;
    EquipdataDO fromDB = equipdataDOMapper.selectByPrimaryKey(eqipmentId);
    Assert.assertTrue(fromDB != null);
  }

  @Test
  public void test_sysparam_selectByArgskey() {
    String key = "alarm_email_address";
    SysparamDO fromDB = sysparamDOMapper.selectByArgskey(key);
    Assert.assertTrue(fromDB != null);
  }

  @Test
  public void test_workplace_findList() {
    List<WorkplaceDO> list = workplaceDOMapper.findList();
    Assert.assertTrue(!CollectionUtils.isEmpty(list));
  }

  @Test
  public void test_alarm_selectLastOne() {
    AlarmrecDO fromDB = alarmrecDOMapper.selectAlarmLastOne();
    Assert.assertTrue(fromDB != null);
  }

  @Test
  public void test_alarm_selectLastOne1() {
    int alarmid = 3;
    List<AlarmrecDO> list = alarmrecDOMapper.selectByAfterAlaramid(alarmid);
    Assert.assertTrue(!CollectionUtils.isEmpty(list));
  }


}