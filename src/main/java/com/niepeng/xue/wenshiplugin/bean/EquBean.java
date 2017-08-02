package com.niepeng.xue.wenshiplugin.bean;


import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */
@Data
public class EquBean {

  private long address;

  // EqutypeEnum
  private int equtype;

  // 标记 == 名称
  private String mark;

  // 温度值
  private long tempValue;

  // 湿度值
  private long humiValue;

  // 采集数据客户端的区域id
  private long referId;

}