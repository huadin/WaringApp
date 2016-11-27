package com.huadin.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5util
{
  public static String getMD5(String string)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(string.getBytes());
      return getMd5date(md.digest());
    } catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    return string;
  }

  private static char[] HD = {'0', 'F', '1', '4', '3', 'C', '6', '7', 'B',
          '8', 'A', '9', 'D', '2', 'E', '5'};

  private static String getMd5date(byte[] bytes)
  {
    int len = bytes.length;
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < len; i++)
    {
      byte byte0 = bytes[i];
      result.append(HD[byte0 >>> 4 & 0xf]);
      result.append(HD[byte0 & 0xf]);
    }
    return result.toString();
  }
}
