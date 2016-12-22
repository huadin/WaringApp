package com.huadin.release;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 发布紧急信息
 */

public class ReleaseFragment extends BaseFragment implements ReleaseContract.View
{

  private ReleaseContract.Presenter mPresenter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public String releaseTitle()
  {
    //标题
    return null;
  }

  @Override
  public String releaseContent()
  {
    //内容
    return null;
  }

  @Override
  public void showLoading()
  {
    //显示进度
  }

  @Override
  public void hindLoading()
  {
    //隐藏进度
  }

  @Override
  public void releaseSuccess()
  {
    //发布成功
  }

  @Override
  public void releaseError(int errorResId)
  {
    //发布失败
  }

  @Override
  public boolean networkIsAvailable()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(ReleaseContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }
}
