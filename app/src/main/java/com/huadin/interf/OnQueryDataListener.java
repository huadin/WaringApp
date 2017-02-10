package com.huadin.interf;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 潇湘 on 2017/2/10.
 * 查询返回接口
 */

public interface OnQueryDataListener<T extends BmobObject>
{
  /**
   * 查询成功
   *
   * @param list 集合
   */
  void queryDataSuccess(List<T> list);

  /**
   * 查询成功，但是无数据
   */
  void queryDataSuccessNotData();

  /**
   * 查询异常
   *
   * @param errorResId 异常信息资源Id
   */
  void queryDataError(int errorResId);
}
