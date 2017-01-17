package com.huadin.userinfo.fault;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/17.
 * 查看用户提交的报修信息
 */

public class FaultFragment extends BaseFragment implements FaultContract.View
{
  private FaultContract.Presenter mPresenter;
  public static FaultFragment newInstance()
  {

    Bundle args = new Bundle();

    FaultFragment fragment = new FaultFragment();
    fragment.setArguments(args);
    return fragment;
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
    mPresenter.start();
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.fault_date_get_in);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    //获取数据成功
  }

  @Override
  public void updateError(int errorId)
  {
    //获取失败
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(FaultContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter,"presenter cannot be null");
  }
}
