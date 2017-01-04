package com.huadin.release;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

/**
 * 发布紧急信息
 */

interface ReleaseContract
{

  interface View extends BaseView<Presenter>
  {
    /**
     * 标题
     *
     * @return String
     */
    String releaseTitle();

    /**
     * 发布的内容
     *
     * @return String
     */
    String releaseContent();

    void showLoading();

    void hindLoading();


    void releaseSuccess();

    /**
     * 异常信息
     *
     * @param errorResId int
     */
    void releaseError(int errorResId);

    /**
     * 网络
     *
     * @return boolean
     */
    boolean networkIsAvailable();
  }

  interface Presenter extends BasePresenter
  {

  }
}
