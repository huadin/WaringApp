package com.huadin.setting.feedback;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

/**
 * Created by 潇湘 on 2017/2/13.
 * 建议
 */

public interface FeedbackContract
{

  interface View extends BaseView<Presenter>
  {
    void showLoading();

    void hindLoading();

    /**
     * 成功
     */
    void updateSuccess();

    /**
     * 失败
     */
    void updateError(int errorId);

    /**
     * 网络连接状态
     *
     * @return 有网络 - true;
     */
    boolean networkState();

    /**
     * 获取建议
     * @return String
     */
    String getFeedbackContent();
  }

  interface Presenter extends BasePresenter
  {

  }
}
