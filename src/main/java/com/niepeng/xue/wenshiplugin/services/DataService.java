package com.niepeng.xue.wenshiplugin.services;

import com.niepeng.xue.wenshiplugin.bean.AlarmBean;
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

  // 最近成功上传的报警数据
  static int lastUploadAlarmId = 0;

  static final long DISTANCE_ALARM_TIMES = 10 * 60 * 1000;

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

  public void bindingUser(UserBean userBean) {
    SysparamDO userId = new SysparamDO();
    userId.setArgskey(Constant.ANDROID_IPHONE_USER_ID);
    userId.setArgsvalue(String.valueOf(userBean.getUserId()));

    SysparamDO userName = new SysparamDO();
    userName.setArgskey(Constant.ANDROID_IPHONE_USER_NAME);
    userName.setArgsvalue(userBean.getName());

    SysparamDO password = new SysparamDO();
    password.setArgskey(Constant.ANDROID_IPHONE_PSW);
    password.setArgsvalue(userBean.getPassword());

    sysparamDOMapper.insert(userId);
    sysparamDOMapper.insert(userName);
    sysparamDOMapper.insert(password);
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
      EquipdataDO tmpEquipData = null;
      if (equipdataDOMap.containsKey(r.getEquipmentid())) {
//        tmpEquipData = equipdataDOMap.get(r.getEquipmentid());
        // 一个设备一次只上传一条数据
        continue;
      } else {
        tmpEquipData = equipdataDOMapper.selectByPrimaryKey(r.getEquipmentid());
        if (tmpEquipData != null) {
          equipdataDOMap.put(r.getEquipmentid(), tmpEquipData);
        }
      }

      tmpEquBean = DataConvert.getEquBean(r);
      resultList.add(tmpEquBean);

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
  public List<AlarmBean> findAlaram() {
    // 软件刚重启的时候
    if (lastUploadAlarmId == 0) {
      AlarmrecDO fromDB = alarmrecDOMapper.selectAlarmLastOne();
      if (fromDB == null) {
        return null;
      }
      // 最近一次报警如果超过5分钟,那么就不上传了
      Date alarmTime = new Date(fromDB.getAlarmstart());
      if (DateUtil.distance(new Date(), alarmTime) < DISTANCE_ALARM_TIMES) {
        List<AlarmBean> result = new ArrayList<>();
        AlarmBean alarmBean = getAlarmBean(fromDB);
        if (alarmBean != null) {
          result.add(alarmBean);
        }
        return result;
      }
      lastUploadAlarmId = fromDB.getAlarmid();
      return null;
    }

    // 非软件重新启动的时候
    List<AlarmrecDO> list = alarmrecDOMapper.selectByAfterAlaramid(lastUploadAlarmId);
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }

    List<AlarmBean> result = new ArrayList<>();
    lastUploadAlarmId = list.get(list.size() - 1).getAlarmid();
    for (AlarmrecDO tmpBean : list) {
      AlarmBean alarmBean = getAlarmBean(tmpBean);
      if (alarmBean != null) {
        result.add(alarmBean);
      }
    }
    return result;
  }

  private AlarmBean getAlarmBean(AlarmrecDO alarmrecDO) {
    EquipdataDO equipdataFromDB = equipdataDOMapper.selectByPrimaryKey(alarmrecDO.getEquipmentid());
    if (equipdataFromDB == null) {
      return null;
    }
    Date alarmTime = new Date(alarmrecDO.getAlarmstart());
    AlarmBean bean = new AlarmBean();
    bean.setLastAlarmTime(alarmTime);
    bean.setAlarmTime(alarmTime);
    bean.setAddress(equipdataFromDB.getAddress());
    String reason = "仪器名:" + equipdataFromDB.getMark() + ",温度:" + alarmrecDO.getTemperature() + "湿度:" + alarmrecDO.getHumidity() + ", " + alarmrecDO.getNormalarea();
    bean.setReason(reason);
    return bean;
  }

}