package com.huadin.userinfo.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.huadin.base.BaseFragment;
import com.huadin.userinfo.UpdateContract;
import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 更改用户名
 */
public class UpdateUserNameFragment extends BaseFragment implements UserContract.View, TextWatcher
{

  @BindView(R.id.update_user_name)
  EditText mUserName;
  private UserContract.Presenter mPresenter;

  public static UpdateUserNameFragment newInstance()
  {
    Bundle args = new Bundle();

    UpdateUserNameFragment fragment = new UpdateUserNameFragment();
    fragment.setArguments(args);
    return fragment;
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.update_user_name_fragment);
    ButterKnife.bind(this, view);
    mUserName.addTextChangedListener(this);
    return view;
  }

  @Override
  public String updateUserName()
  {
    return mUserName.getText().toString();
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
    showMessage(R.string.update_password_success);
    pop();
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public void setPresenter(UpdateContract.Presenter presenter)
  {
    this.mPresenter = (UserContract.Presenter) presenter;
    mPresenter = (UserContract.Presenter) checkNotNull(presenter, "presenter cannot be null");
  }

  @OnClick(R.id.submit_user_name)
  public void onClick()
  {
    mPresenter.start();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after)
  {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count)
  {
    //过滤汉字
    String userName = mUserName.getText().toString();
    String stringFilter = AMUtils.stringFilter(userName);
    if (!userName.equals(stringFilter))
    {
      mUserName.setText(stringFilter);
      mUserName.setSelection(stringFilter.length());
    }
  }

  @Override
  public void afterTextChanged(Editable s)
  {

  }
}
