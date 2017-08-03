package com.niepeng.xue.wenshiplugin.job;

import com.alibaba.fastjson.JSON;
import com.niepeng.xue.wenshiplugin.bean.AreaBean;
import com.niepeng.xue.wenshiplugin.bean.EquBean;
import com.niepeng.xue.wenshiplugin.bean.UserBean;
import com.niepeng.xue.wenshiplugin.common.util.DateUtil;
import com.niepeng.xue.wenshiplugin.common.util.HttpClientService;
import com.niepeng.xue.wenshiplugin.services.DataService;
import java.awt.geom.Area;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/3
 */
@Component
@Slf4j
public class AutoUploadJob {

  // 延迟启动时间
  public final static long INI_DELAY_TIME =  1 * 60 * 1000;
//  public final static long INI_DELAY_TIME =  1 * 10 * 1000;

  // 温湿度设备上传周期
  public final static long EQUIP_DATA_TIME =  1 * 60 * 1000;
//  public final static long EQUIP_DATA_TIME =  1 * 20 * 1000;

  // 区域数据上传周期
  public final static long AREA_DATA_TIME =  1 * 60 * 1000;

  // 报警数据上传周期
  public final static long ALARM_DATA_TIME =  5 * 1000;


  @Autowired
  protected DataService dataService;

  @Autowired
  protected HttpClientService httpClientService;



  // 区域数据是否在上传
  private static AtomicBoolean isAreaRemoteing = new AtomicBoolean(false);
  // 设备数据是否在上传
  private static AtomicBoolean isEquipRemoteing = new AtomicBoolean(false);
  // 报警数据是否在上传
  private static AtomicBoolean isAlarmRemoteing = new AtomicBoolean(false);

  static List<AreaBean> lastUploadAreaList = null;

  private static String HOST_NAME = "http://42.121.53.218/";
  private static String urlPrefix = HOST_NAME + "json/dataAction/";
  private static String[] urls = {"doUploadArea.htm", "doUploadEquipData.htm", "doUploadAlarm.htm"};
  private static String[] keys = {"areas", "equips", "alarm"};



  // 上传温湿度数据信息
  @Scheduled( initialDelay = INI_DELAY_TIME, fixedDelay = EQUIP_DATA_TIME)
  public void uploadEquipData() {
    UserBean user = dataService.findUser();
    if (user == null) {
      log.info("uploadEquipData:not binding user.");
      return;
    }

    if (isEquipRemoteing.get()) {
      return;
    }

    isEquipRemoteing.set(true);
    try {
      List<EquBean> equBeanList = dataService.findEquipData();
      if (equBeanList == null || equBeanList.size() == 0) {
        return;
      }

      String value = JSON.toJSONString(equBeanList);
      httpParameterPost(urlPrefix + urls[1], keys[1], value, user.getUserId(), user.getPassword());

    } catch (Exception e) {
      log.error("uploadEquipDataError", e);
    } finally {
      isEquipRemoteing.set(false);
    }
  }

  // 上传区域数据(注意有变化才上传,记录最近一次上传的数据)
  @Scheduled( initialDelay = INI_DELAY_TIME, fixedDelay = AREA_DATA_TIME)
  public void  uploadArea() {
    if (isAreaRemoteing.get()) {
      return;
    }

    isAreaRemoteing.set(true);
    try {
      UserBean user = dataService.findUser();
      if (user == null) {
//        log.info("uploadArea:not binding user.");
        return;
      }

      List<AreaBean> areaList = dataService.findArea();
      if (StringUtils.isEmpty(areaList)) {
        return;
      }

      if (!compareSameLastUploadArea(areaList)) {
        String value = JSON.toJSONString(areaList);
        log.error("uploadAreaData2Server........");
        boolean flag = httpParameterPost(urlPrefix + urls[0], keys[0], value, user.getUserId(), user.getPassword());
        if (flag) {
          lastUploadAreaList = areaList;
        }
        return;
      }

    } catch (Exception e) {
      log.error("uploadAreaError", e);
    } finally {
      isAreaRemoteing.set(false);
    }
  }


  // 上传报警数据(注意有变化才上传,记录最近一次上传的数据)
  @Scheduled( initialDelay = INI_DELAY_TIME, fixedDelay = ALARM_DATA_TIME)
  public void uploadAlarm() {
    if (isAlarmRemoteing.get()) {
      return;
    }

    isAlarmRemoteing.set(true);
    try {
      UserBean user = dataService.findUser();
      if (user == null) {
        return;
      }


//      List<AreaBean> areaList = dataService.findArea();
//      if (StringUtils.isEmpty(areaList)) {
//        return;
//      }
//
//      if (!compareSameLastUploadArea(areaList)) {
//        String value = JSON.toJSONString(areaList);
//        log.error("uploadAreaData2Server........");
//        boolean flag = httpParameterPost(urlPrefix + urls[0], keys[0], value, user.getUserId(), user.getPassword());
//        if (flag) {
//          lastUploadAreaList = areaList;
//        }
//        return;
//      }

    } catch (Exception e) {
      log.error("uploadAlarmError", e);
    } finally {
      isAlarmRemoteing.set(false);
    }
  }


  private boolean compareSameLastUploadArea(List<AreaBean> areaList) {
    if (lastUploadAreaList == null || lastUploadAreaList.size() != areaList.size()) {
      return false;
    }

    boolean same = false;
    for (AreaBean newArea : areaList) {
      for (AreaBean lastArea : lastUploadAreaList) {
        if (newArea.equals(lastArea)) {
          same = true;
          break;
        }
      }
      if (!same) {
        return false;
      }
    }

    return true;
  }


  private boolean httpParameterPost(String url, String key, String value, long userId, String psw) {
    Map<String, String> map = new HashMap<>();
    try {
      map.put("userId", String.valueOf(userId));
      map.put("psw", psw);
      map.put(key, value);
      String result = httpClientService.doPost(url, map);
      log.info("data: " + result);
    } catch(Exception e) {
      return false;
    }
    return true;
  }



}