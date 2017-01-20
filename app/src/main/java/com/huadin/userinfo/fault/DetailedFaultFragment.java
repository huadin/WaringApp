package com.huadin.userinfo.fault;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.bean.ReportBean;
import com.huadin.eventbus.EventCenter;
import com.huadin.userinfo.UpdateContract;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/20.
 * 停电报修详细信息
 */

public class DetailedFaultFragment extends BaseFragment implements DetailedFaultContract.View
{

  private UpdateContract.Presenter mPresenter;
  private ReportBean mReportBean;
  private static final String KEY_BEAN = "BEAN";
  private static final String KEY_POS = "POS";
  private int mPosition;


  public static DetailedFaultFragment newInstance(ReportBean bean,int position)
  {
    Bundle args = new Bundle();
    args.putParcelable(KEY_BEAN, bean);
    args.putInt(KEY_POS,position);
    DetailedFaultFragment fragment = new DetailedFaultFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {

    Bundle bundle = getArguments();
    mReportBean = bundle.getParcelable(KEY_BEAN);
    mPosition = bundle.getInt(KEY_POS);
    View view = getViewResId(inflater, container, R.layout.detailed_fault_fragment_layout);
    ButterKnife.bind(this, view);
    return view;
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
  public void updateSuccess()
  {
    showMessage(R.string.submit_success);
    EventBus.getDefault().post(new EventCenter<>(EventCenter.EVENT_CODE_UPDATE_SUCCESS,mPosition));
    pop();
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(DetailedFaultContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @Override
  public String getObjectId()
  {
    return mReportBean == null ? "" : mReportBean.getObjectId();
  }

  @Override
  public ReportBean getReportBean()
  {
    return mReportBean;
  }

  @OnClick(R.id.submit)
  public void onClick()
  {
    mPresenter.start();
  }

}
