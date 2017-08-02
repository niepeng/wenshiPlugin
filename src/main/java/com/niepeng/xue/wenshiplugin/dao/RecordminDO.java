package com.niepeng.xue.wenshiplugin.dao;

import java.util.Date;
import lombok.Data;

@Data
public class RecordminDO {

    // 唯一自增id
    private Integer recid;

    // 设备id
    private int equipmentid;

    // 设备地址
    private int address;

    // 温度
    private Float temp;

    // 湿度
    private Float humi;

    private Float dewpoint;

    private Float powerv;

    private int state;

    private Date rectime;

    private Long reclong;

    private int mark;
}