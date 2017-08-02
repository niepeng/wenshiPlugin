package com.niepeng.xue.wenshiplugin.bean.convert;
/**
 * Created by lsb on 17/8/2.
 */


import com.niepeng.xue.wenshiplugin.bean.AreaBean;
import com.niepeng.xue.wenshiplugin.bean.EquBean;
import com.niepeng.xue.wenshiplugin.dao.RecordminDO;
import com.niepeng.xue.wenshiplugin.dao.WorkplaceDO;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */

public class DataConvert {

  public static EquBean getEquBean(RecordminDO r) {
    EquBean bean = new EquBean();
    bean.setAddress(r.getAddress());
    bean.setTempValue((long)(r.getTemp().floatValue()));
    bean.setHumiValue((long)(r.getHumi().floatValue()));
    return bean;
  }

  public static AreaBean getAreaBean(WorkplaceDO w) {
    AreaBean bean = new AreaBean();
    bean.setName(w.getPlacename());
    bean.setReferId(w.getPlaceid());
    return bean;
  }

}