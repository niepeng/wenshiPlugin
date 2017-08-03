package com.niepeng.xue.wenshiplugin.job;

import com.alibaba.fastjson.JSON;
import com.niepeng.xue.wenshiplugin.bean.EquBean;
import com.niepeng.xue.wenshiplugin.bean.UserBean;
import com.niepeng.xue.wenshiplugin.common.util.DateUtil;
import com.niepeng.xue.wenshiplugin.common.util.HttpClientService;
import com.niepeng.xue.wenshiplugin.services.DataService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

  private static String HOST_NAME = "http://42.121.53.218/";
  private static String urlPrefix = HOST_NAME + "json/dataAction/";
  private static String[] urls = {"doUploadArea.htm", "doUploadEquipData.htm", "doUploadAlarm.htm"};
  private static String[] keys = {"areas", "equips", "alarm"};



  @Scheduled( initialDelay = INI_DELAY_TIME, fixedDelay = EQUIP_DATA_TIME)
  public void uploadEquipData() {
    UserBean user = dataService.findUser();
    if (user == null) {
      log.info("not binding user.");
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


  private boolean httpParameterPost(String url, String key, String value, long userId, String psw) {
    Map<String, String> map = new HashMap<>();
    try {
      map.put("userId", String.valueOf(userId));
      map.put("psw", psw);
      map.put(key, value);
      String result = httpClientService.doPost(url, map);
      log.info("data: " + result);
    } catch(Exception e) {

    }
    return false;
  }



}