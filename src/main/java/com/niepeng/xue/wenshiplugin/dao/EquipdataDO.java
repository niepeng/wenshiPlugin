package com.niepeng.xue.wenshiplugin.dao;

import lombok.Data;

@Data
public class EquipdataDO {

    private Integer equipmentid;

    private Integer address;

    private Integer equitype;

    private String mark;

    private String remark;

    private Float tempup;

    private Float tempdown;

    private Float tempdev;

    private Float humiup;

    private Float humidown;

    private Float humidev;

    private Integer equiorder;

    private Integer placeid;

    private Boolean useless;

    private Boolean showpower;

    private Integer powertype;

    private String dsrsn;

    private Boolean showaccess;

    private Boolean conndata;
}