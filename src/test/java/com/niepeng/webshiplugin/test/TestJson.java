package com.niepeng.webshiplugin.test;

import com.alibaba.fastjson.JSON;
import com.niepeng.xue.wenshiplugin.bean.AlarmBean;
import com.niepeng.xue.wenshiplugin.bean.EquBean;
import com.niepeng.xue.wenshiplugin.common.util.MD5Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/3
 */

public class TestJson {

  public static void main(String[] args) {
    AlarmBean bean = new AlarmBean();
    bean.setAddress(1);
    bean.setAlarmTime(new Date());
    bean.setLastAlarmTime(null);
    bean.setReason("dafds");

    String value = JSON.toJSONStringWithDateFormat(bean,"yyyy-MM-dd HH:mm:ss");
    System.out.println(value);
  }

  public static void main1(String[] args) {
    List<EquBean> list = new ArrayList<EquBean>();

    EquBean equBean = new EquBean();
    equBean.setAddress(1);
    equBean.setEqutype(2);
    equBean.setMark("woshimark");
    equBean.setTempValue(13);
    equBean.setHumiValue(14);
    equBean.setReferId(55);
    list.add(equBean);

    EquBean equBean1 = new EquBean();
    equBean1.setAddress(1);
    equBean1.setEqutype(2);
    equBean1.setMark("woshimark");
    equBean1.setTempValue(13);
    equBean1.setHumiValue(14);
    equBean1.setReferId(55);
    list.add(equBean1);

    String value = JSON.toJSONString(list);
    System.out.println(value);

    String s = MD5Util.getMD5("cq170280");
    System.out.println(s);

  }
}