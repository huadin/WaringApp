package com.huadin.report;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

/**
 * 上报故障信息控制器
 */

public interface ReportContract
{
  interface View extends BaseView<Presenter>
  {
    /**
     * 提价成功
     */
    void submitSuccess();

    /**
     * 提交失败
     *
     * @param errorId 失败信息id
     */
    void submitError(int errorId);

    /**
     * title
     * @return String
     */
    String getReportTitle();
    /**
     * Content
     * @return String
     */
    String getReportContent();
    /**
     * User
     * @return String
     */
    String getReportUser();
    /**
     * Phone
     * @return String
     */
    String getReportPhone();
    /**
     * Address
     * @return String
     */
    String getReportAddress();

    /*可以封装到父类中*/
    void showLoading();

    void hindLoading();

    boolean networkIsAvailable();

  }

  interface Presenter extends BasePresenter
  {

  }

}
