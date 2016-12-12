package com.huadin.fault;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 上报故障信息
 */

public class ReportFragment extends BaseFragment implements ReportContract.View
{
  @BindView(R.id.top_toolbar)
  Toolbar mToolBar;
  @BindView(R.id.report_title)
  ClearEditText mReportTitle;
  @BindView(R.id.report_content)
  ClearEditText mReportContent;
  @BindView(R.id.report_user)
  ClearEditText mReportUser;
  @BindView(R.id.report_phone)
  ClearEditText mReportPhone;
  @BindView(R.id.report_address)
  ClearEditText mReportAddress;

  private ReportContract.Presenter mPresenter;

  public static ReportFragment newInstance()
  {
    return new ReportFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.report_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolBar, R.string.blackout_repair);
    return view;
  }


  @Override
  public void submitSuccess()
  {
    // TODO: 2016/12/13 成功后操作
    mToast.showMessage("提交成功",500);
  }

  @Override
  public void submitError(int errorId)
  {
    mToast.showMessage(errorId, 1000);
  }

  @Override
  public String getReportTitle()
  {
    return mReportTitle.getText().toString();
  }

  @Override
  public String getReportContent()
  {
    return mReportContent.getText().toString();
  }

  @Override
  public String getReportUser()
  {
    return mReportUser.getText().toString();
  }

  @Override
  public String getReportPhone()
  {
    return mReportPhone.getText().toString();
  }

  @Override
  public String getReportAddress()
  {
    return mReportAddress.getText().toString();
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.report_submit_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public boolean networkIsAvailable()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(ReportContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @OnClick(R.id.report_submit)
  public void onClick()
  {
    mPresenter.start();
  }
}
