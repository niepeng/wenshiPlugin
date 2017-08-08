package com.niepeng.xue.wenshiplugin.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niepeng.xue.wenshiplugin.bean.AlarmBean;
import com.niepeng.xue.wenshiplugin.bean.AreaBean;
import com.niepeng.xue.wenshiplugin.bean.EquBean;
import com.niepeng.xue.wenshiplugin.bean.UserBean;
import com.niepeng.xue.wenshiplugin.common.util.DateUtil;
import com.niepeng.xue.wenshiplugin.common.util.HttpClientService;
import com.niepeng.xue.wenshiplugin.common.util.MD5Util;
import com.niepeng.xue.wenshiplugin.services.DataService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
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
  private static String LOGIN_URL = "http://42.121.53.218/json/user/doLogin.htm?userName=%s&psw=%s";
  private static String urlPrefix = HOST_NAME + "json/dataAction/";
  private static String[] urls = {"doUploadArea.htm", "doUploadEquipData.htm", "doUploadAlarm.htm"};
  private static String[] keys = {"areas", "equips", "alarm"};

  // 在工程目录下或在打出的jar的同一目录中,新建一个文本文件,第一行是带绑定的用户名,第二行是密码,原文,程序会校验正确性并记录到数据库
  @Scheduled( initialDelay = 10 * 1000, fixedDelay = 1  * 60 * 60 * 1000)
  public void readUserAndInsert2db() {
    UserBean user = dataService.findUser();
    if (user != null) {
      return;
    }
    String fileName = "a.txt";
    log.error("------>read userName and password, check data....");
    File file = new File(fileName);
    System.out.println(file.getAbsolutePath());
    Map<Integer, String> map = readFile(file);
    if (map == null || map.size() != 2) {
      log.error("current user file not exist or format is wrong.");
      return;
    }

    try {
      String userName = map.get(1);
      String psw = map.get(2);
      String content = httpClientService.doGet(String.format(LOGIN_URL, userName, MD5Util.getMD5(psw)));
      System.out.println("content=>" + content);
      JSONObject json = JSON.parseObject(content);
      if (json == null || json.getIntValue("code") != 0) {
        log.error("login fail, userName or password is wrong.");
        return;
      }
      int id = json.getJSONObject("data").getIntValue("id");
      if (id < 1) {
        log.error("login fail, check id < 1.");
        return;
      }

      UserBean userBean = new UserBean();
      userBean.setUserId(id);
      userBean.setName(userName);
      userBean.setPassword(MD5Util.getMD5(psw));
      dataService.bindingUser(userBean);
      log.error("check data success, bingding user success.");
    } catch (Exception e) {
      log.error("check data Error", e);
    }
  }

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

      List<AlarmBean> alarmBeanList = dataService.findAlaram();
      if (StringUtils.isEmpty(alarmBeanList)) {
        return;
      }

      // JsonUtil.fields("address,reason,alarmTime,lastAlarmTime", alarmDO)
      for (AlarmBean tmpBean : alarmBeanList) {
        String value = JSON.toJSONStringWithDateFormat(tmpBean, DateUtil.DEFAULT_DATE_FMT);
        httpParameterPost(urlPrefix + urls[2], keys[2], value, user.getUserId(), user.getPassword());
        Thread.sleep(200);
      }

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


  private Map<Integer, String> readFile(File file) {
    Map<Integer, String> map = new HashMap<>();

    try {
      FileReader reader = new FileReader(file);
      BufferedReader br = new BufferedReader(reader);

      String str = null;
      int line = 1;
      while ((str = br.readLine()) != null) {
        map.put(line, str);
        System.out.println(str);
        line++;
      }
      br.close();
      reader.close();
    } catch (Exception e) {

    }
    return map;
  }





}