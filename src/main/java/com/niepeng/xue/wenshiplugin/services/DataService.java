package com.niepeng.xue.wenshiplugin.services;
/**
 * Created by lsb on 17/8/2.
 */


import com.niepeng.xue.wenshiplugin.bean.AreaBean;
import com.niepeng.xue.wenshiplugin.bean.EquBean;
import com.niepeng.xue.wenshiplugin.bean.UserBean;
import com.niepeng.xue.wenshiplugin.bean.convert.DataConvert;
import com.niepeng.xue.wenshiplugin.common.Constant;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */

@Service
public class DataService {

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


  /**
   * 数据库查找是否有绑定的账户:返回非null,即为有用户绑定
   */
  public UserBean findUser() {
    SysparamDO userIdParam = sysparamDOMapper.selectByArgskey(Constant.ANDROID_IPHONE_USER_ID);
    SysparamDO userNameParam = sysparamDOMapper.selectByArgskey(Constant.ANDROID_IPHONE_USER_NAME);
    SysparamDO passwordParam = sysparamDOMapper.selectByArgskey(Constant.ANDROID_IPHONE_PSW);
    if (userIdParam == null || userNameParam == null || passwordParam == null) {
      return null;
    }
    String idStr = userIdParam.getArgsvalue();
    String userNameStr = userNameParam.getArgsvalue();
    String passwordStr = passwordParam.getArgsvalue();

    long userId = 0L;
    try {
      userId = Long.valueOf(idStr);
    } catch (Exception e) {
    }

    if (userId > 0 && !StringUtils.isEmpty(userNameStr) && !StringUtils.isEmpty(passwordStr)) {
      UserBean bean = new UserBean();
      bean.setUserId(userId);
      bean.setName(userNameStr);
      bean.setPassword(passwordStr);
      return bean;
    }
    return null;
  }

  /**
   * 获取设备数据:
   * 间隔大于1分钟,就要执行下
   */
  public List<EquBean> findEquipData() {
    // 1.获取设备温湿度数据
    long lastTime = recordminDOMapper.getLastRecTime();
    Map<String, Object> map = new HashMap<>();
    long min = lastTime - Constant.REC_DEFAULT_DISTANCE;
    long max = lastTime + Constant.REC_DEFAULT_DISTANCE;
    map.put("minRecLong", min);
    map.put("maxRecLong", max);
    List<RecordminDO> list = recordminDOMapper.queryList(map);

    Map<Integer, EquipdataDO> equipdataDOMap = new HashMap<>();
    List<EquBean> resultList = new ArrayList<>();

    // 2.获取设备基本信息
    EquBean tmpEquBean;
    for (RecordminDO r : list) {
      tmpEquBean = DataConvert.getEquBean(r);
      resultList.add(tmpEquBean);
      EquipdataDO tmpEquipData = null;
      if (equipdataDOMap.containsKey(r.getEquipmentid())) {
        tmpEquipData = equipdataDOMap.get(r.getEquipmentid());
      } else {
        tmpEquipData = equipdataDOMapper.selectByPrimaryKey(r.getEquipmentid());
        if (tmpEquipData != null) {
          equipdataDOMap.put(r.getEquipmentid(), tmpEquipData);
        }
      }

      if (tmpEquipData != null) {
        tmpEquBean.setEqutype(tmpEquipData.getEquitype());
        tmpEquBean.setMark(tmpEquipData.getMark());
        tmpEquBean.setReferId(tmpEquipData.getPlaceid());
      }
    }
    return resultList;
  }

  /**
   * 获取区域列表数据
   */
  public List<AreaBean> findArea() {
    List<AreaBean> result = new ArrayList<>();
    List<WorkplaceDO> list = workplaceDOMapper.findList();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }

    for (WorkplaceDO w : list) {
      if ("未定义".equals(w.getPlacename().trim())) {
        continue;
      }
      result.add(DataConvert.getAreaBean(w));
    }
    return result;
  }

  /**
   * 获取报警数据:
   * 1.如果重来都没有上传过报警数据,那么获取一条最新的报警数据,上传,并记录最近一次上传的alarmid
   * 2.如果有上传过报警数据,那么从该报警数据开始之后的数据上传,并记录最近一次上传的alarmid
   */
  public void findAlaram() {

    AlarmrecDO fromDB = alarmrecDOMapper.selectAlarmLastOne();
    int lastUploadAlarmId = 3;
    List<AlarmrecDO> list = alarmrecDOMapper.selectByAfterAlaramid(lastUploadAlarmId);

  }



}