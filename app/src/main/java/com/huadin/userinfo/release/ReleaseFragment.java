package com.huadin.userinfo.release;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/10.
 * 发布信息
 */

public class ReleaseFragment extends BaseFragment implements ReleaseContract.View
{

  @BindView(R.id.release_title)
  EditText mReleaseTitle;
  @BindView(R.id.release_content)
  EditText mReleaseContent;
  private ReleaseContract.Presenter mPresenter;

  public static ReleaseFragment newInstance()
  {

    Bundle args = new Bundle();

    ReleaseFragment fragment = new ReleaseFragment();
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
    View view = getViewResId(inflater, container, R.layout.release_fragment_layout);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public String releaseTitle()
  {
    return mReleaseTitle.getText().toString();
  }

  @Override
  public String releaseContent()
  {
    return mReleaseContent.getText().toString();
  }

  @Override
  public String areaId()
  {
    return null;
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
    //发布成功
    mContext.finish();
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public void setPresenter(ReleaseContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @OnClick(R.id.release_msg_submit)
  public void onClick()
  {
    mPresenter.start();
  }
}
