package com.huadin.eventbus;

/**
 * EventBus 工具类
 */

public class EventCenter<T>
{

  //  // 关闭所有Activity
//  public static final int EVENT_CODE_CLOSE_ALL_ACTIVITY = -1;
  // 登录成功
  public static final int EVENT_CODE_LOGIN_SUCCESS = 100;
  //退出成功
  public static final int EVENT_CODE_OUT_SUCCESS = 106;
  // 注册成功
//  public static final int EVENT_CODE_REGISTER_SUCCESS = 101;
  // 修改成功
  public static final int EVENT_CODE_UPDATE_SUCCESS = 102;
//  //	修改个人信息成功
//  public static final int EVENT_CODE_Userinfo_INFO_UPDATE = 103;

  //加载注册 Fragment
//  public static final int EVENT_CODE_LOAD_REGISTER = 104;
  //有网络
  public static final int EVENT_CODE_NETWORK = 105;
  //其他设备登录
  public static final int EVENT_CODE_OTHER_DEVICE_LOGIN = 107;
  //编码解析完成
  public static final int EVENT_CODE_GEO_CODE_COMPLETE = 109;
  //开始解析编码
  public static final int EVENT_CODE_GEO_CODE_START = 108;
  //搜索获取网络数据
  public static final int EVENT_CODE_SEARCH_HTTP_DATA = 109;
  //时间回调
  public static final int EVENT_CODE_START_DATE = 110;
  //没有停电信息
  public static final int EVENT_CODE_NOT_HTTP_DATA = 111;
  //启动预警服务
  public static final int EVENT_CODE_START_LONG_RUN_SERVICE = 112;
  //搜索是没有数据
  public static final int EVENT_CODE_SEARCH_NOT_HTTP_DATA = 113;

  /**
   * reserved data
   */
  private T data;

  /**
   * code 用来区分不同的事件
   */
  private int eventCode = -1;

  public EventCenter(int eventCode)
  {
    this(eventCode, null);
  }

  public EventCenter(int eventCode, T data)
  {
    this.eventCode = eventCode;
    this.data = data;
  }

  /**
   * get event code
   *
   * @return int
   */
  public int getEventCode()
  {
    return this.eventCode;
  }

  /**
   * get event reserved data
   *
   * @return T
   */
  public T getData()
  {
    return this.data;
  }
}
