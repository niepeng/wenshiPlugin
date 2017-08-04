package com.niepeng.xue.wenshiplugin.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/3
 */
@Setter
@Getter
public class AreaBean {

  private String name;

  // 本地区域id
  private long referId;

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    if (!(other instanceof AreaBean)) {
      return false;
    }

    final AreaBean areaBean = (AreaBean) other;
    if (areaBean.getReferId() != this.getReferId()) {
      return false;
    }

    if (areaBean.getName() != null && this.getName() != null) {
      return areaBean.getName().equals(this.getName());
    }

    if (areaBean.getName() == null && this.getName() == null) {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() + (int)(referId) : ((int) referId);
  }
}