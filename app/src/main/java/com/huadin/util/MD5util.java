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
    StringBuilder result = new StringBuilder();
    for (byte aByte : bytes)
    {
      result.append(HD[aByte >>> 4 & 0xf]);
      result.append(HD[aByte & 0xf]);
    }
    return result.toString();
  }
}
