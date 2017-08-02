package com.niepeng.xue.wenshiplugin.bean.enums;


import lombok.Getter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */
@Getter
public enum EqutypeEnum {
  HUMI(1, "湿度"),
  TEMP(2, "温度"),
  TEMP_HUMI(3,"温湿度");

  private final int id;

  private final String meaning;

  private EqutypeEnum(int id, String meaning) {
    this.id = id;
    this.meaning = meaning;
  }

}
