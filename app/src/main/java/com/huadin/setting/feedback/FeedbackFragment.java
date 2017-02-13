package com.huadin.setting.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/2/13.
 * 意见反馈
 */

public class FeedbackFragment extends BaseFragment implements FeedbackContract.View, Toolbar.OnMenuItemClickListener
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.feedback_fragment_edit)
  EditText mContentEdit;

  private FeedbackContract.Presenter mPresenter;

  public static FeedbackFragment newInstance()
  {

    Bundle args = new Bundle();

    FeedbackFragment fragment = new FeedbackFragment();
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
    View view = getViewResId(inflater, container, R.layout.feedback_fragmnet_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.feedback, false);
    mToolbar.inflateMenu(R.menu.feedback_submit_menu);
    mToolbar.setOnMenuItemClickListener(this);
    return view;
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.register_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    //提交成功
    popTopFragment();
  }

  @Override
  public void updateError(int errorId)
  {
    //建议提交异常
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public String getFeedbackContent()
  {
    return mContentEdit.getText().toString();
  }

  @Override
  public void setPresenter(FeedbackContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "FeedbackContract.Presenter cannot be null");
  }

  @Override
  public boolean onMenuItemClick(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.feedback_submit:
        mPresenter.start();
        break;
    }
    return true;
  }
}
