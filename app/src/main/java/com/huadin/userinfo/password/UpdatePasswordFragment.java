package com.huadin.userinfo.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.huadin.base.BaseFragment;
import com.huadin.userinfo.UpdateContract;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by 潇湘 on 2016/12/29.
 * 修改密码
 */

public class UpdatePasswordFragment extends BaseFragment implements UpdatePasswordContract.View
{

  @BindView(R.id.old_password)
  EditText mOldPassword;
  @BindView(R.id.new_password)
  EditText mNewPassword;
  private UpdatePasswordContract.Presenter mPresenter;

  public static UpdatePasswordFragment newInstance()
  {

    Bundle args = new Bundle();

    UpdatePasswordFragment fragment = new UpdatePasswordFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.update_password_fragment);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public String oldPassword()
  {
    return mOldPassword.getText().toString();
  }

  @Override
  public String newPassword()
  {
    return mNewPassword.getText().toString();
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
    //更新成功
    mContext.finish();
  }

  @Override
  public void updateError(int errorId)
  {
    //更新失败,显示失败原因
  }


  @Override
  public void setPresenter(UpdateContract.Presenter presenter)
  {
    this.mPresenter = (UpdatePasswordContract.Presenter) presenter;
    mPresenter = (UpdatePasswordContract.Presenter) checkNotNull(presenter, "UpdatePasswordContract.Presenter cannot be null");
  }

  @OnClick(R.id.password_submit)
  public void onClick()
  {
    mPresenter.start();
  }
}
