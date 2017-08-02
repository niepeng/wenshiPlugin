package com.niepeng.xue.wenshiplugin.dao;

import lombok.Data;

@Data
public class AlarmrecDO {

    private Integer alarmid;

    private String temperature;

    private String humidity;

    private Long alarmstart;

    private Long alarmend;

    private Boolean alarmtype;

    private int state;

    private Boolean alarmmode;

    private int equipmentid;

    private String username;

    private String placename;

    private String normalarea;

    private int equitype;

    private int gprsflag;
}