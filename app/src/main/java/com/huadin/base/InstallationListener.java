package com.huadin.base;

/**
 * Created by 潇湘 on 2017/1/9.
 * 推送绑定成功
 */

public interface InstallationListener
{
  /**
   * 绑定成功
   */
  void installationSuccess();

  /**
   * 绑定异常
   *
   * @param code 异常码
   */
  void installationError(int code);

}
