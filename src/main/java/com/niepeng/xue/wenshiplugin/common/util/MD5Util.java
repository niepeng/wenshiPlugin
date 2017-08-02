package com.niepeng.xue.wenshiplugin.common.util;

import java.security.MessageDigest;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */
public class MD5Util {

  static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static String getMD5(String content) {
    return getMD5(content.getBytes());
  }

  public static String getMD5(byte[] source) {
    String s = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(source);
      byte tmp[] = md.digest();
      char str[] = new char[16 * 2];
      int k = 0;
      for (int i = 0; i < 16; i++) {
        byte byte0 = tmp[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      s = new String(str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return s;
  }
}