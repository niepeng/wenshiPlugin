package com.niepeng.xue.wenshiplugin.bean;

import java.util.Date;
import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/3
 */
@Data
public class AlarmBean {

  private long address;

  // 报警问题
  private String reason;

  // 首次报警时间
  private Date alarmTime;

  // 最近报警时间
  private Date lastAlarmTime;

}