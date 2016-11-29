package com.huadin.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AMUtils
{
  /*手机号码正则表达式*/
  private static final String MOBILE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|6|7|8]))\\d{8}$";

  /**
   * 检测手机号码是否符合格式
   *
   * @param phone 电话号
   * @return true
   */
  public static boolean isMobile(String phone)
  {
    Pattern patter = Pattern.compile(MOBILE_PHONE_PATTERN);
    Matcher m = patter.matcher(phone);
    return m.matches();
  }

  /**
   * 隐藏软键盘
   *
   * @param context context
   * @param e       editText
   */
  public static void onInactive(Context context, EditText e)
  {
    if (e == null) return;

    InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    manager.hideSoftInputFromWindow(e.getWindowToken(), 0);
  }

  /**
   * 显示 软键盘
   *
   * @param context context
   * @param et      editText
   */
  public static void onActive(Context context, EditText et)
  {
    if (et == null) return;

    InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    manager.showSoftInput(et, 0);
  }

  /**
   * 判断是否为空
   *
   * @param str String
   * @return boolean
   */
  public static boolean isEmpty(String str)
  {
    if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals(""))
    {
      return true;
    } else
    {
      return false;
    }
  }

  public static boolean validatePassword(String str)
  {
    if (str == null || str.length() < 6 || str.length() > 20)
    {
      return true;
    } else
    {
      return false;
    }
  }

}
