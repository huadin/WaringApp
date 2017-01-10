package com.huadin.userinfo.release;

import com.huadin.userinfo.UpdateContract;

/**
 * Created by 潇湘 on 2017/1/10.
 * 信息发布
 */

public interface ReleaseContract
{
  interface Presenter extends UpdateContract.Presenter
  {

  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 标题
     *
     * @return String
     */
    String releaseTitle();

    /**
     * 内容
     *
     * @return String
     */
    String releaseContent();

    /**
     * 地区Id,作为推送的条件
     *
     * @return String
     */
    String areaId();
  }
}
