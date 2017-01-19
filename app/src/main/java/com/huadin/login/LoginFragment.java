package com.huadin.login;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.dialog.PermissionDialogFragment;
import com.huadin.eventbus.EventCenter;
import com.huadin.permission.PermissionListener;
import com.huadin.permission.PermissionManager;
import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.google.common.base.Preconditions.checkNotNull;

public class LoginFragment extends BaseFragment implements LoginContract.View, TextWatcher, PermissionListener, PermissionDialogFragment.OnPermissionListener
{

  @BindView(R.id.login_name)
  ClearEditText mNameET;
  @BindView(R.id.login_password)
  ClearEditText mPasswordET;
  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  private final int mPermissionCode = 0x21;

  private LoginContract.Presenter mPresenter;
  private PermissionManager mManager;

  public static LoginFragment newInstance()
  {
    return new LoginFragment();
  }

  @Override
  public void setPresenter(@NonNull LoginContract.Presenter presenter)
  {
    mPresenter = checkNotNull(presenter, "presenter cannot null");
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
    View view = getViewResId(inflater, container, R.layout.login_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.action_sign_in,false);
    mNameET.addTextChangedListener(this);
    return view;
  }


  @Override
  public void showLoading()
  {
    showLoading(R.string.loading_login);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void loginSuccess()
  {
    //登录成功
    showMessage(R.string.login_success);
    //发送订阅事件,改变 DrawerLayout 上用户名
    EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_LOGIN_SUCCESS));
    pop();
  }

  @Override
  public void loginError(int errorMsg)
  {
    //登录异常
    showMessage(errorMsg);
  }

  @Override
  public String getLoginName()
  {
    return mNameET.getText().toString();
  }

  @Override
  public String getLoginPassword()
  {
    return mPasswordET.getText().toString();
  }

  @Override
  public boolean networkIsAvailable()
  {
    return isNetwork();
  }

  @OnClick({R.id.login_app, R.id.to_register, R.id.to_forget_password})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      //登录
      case R.id.login_app:
        //检查权限
        checkPermission();
        break;
      //注册
      case R.id.to_register:
        RegisterFragment registerFragment = RegisterFragment.newInstance(RegisterFragment.REGISTER_FLAG);
        new RegisterPresenter(registerFragment);
        start(registerFragment);
        break;
      //重置密码
      case R.id.to_forget_password:
        RegisterFragment _registerFragment = RegisterFragment.newInstance(RegisterFragment.RESET_FLAG);
        new ResetPresenter(_registerFragment);
        start(_registerFragment);
        break;
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after)
  {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count)
  {
    if (s.length() == 11)
    {
      AMUtils.onInactive(getApplicationContext(), mNameET);
    }
  }

  @Override
  public void afterTextChanged(Editable s)
  {

  }

  /* 检查权限 */
  private void checkPermission()
  {
    mManager = PermissionManager.with(this)
            .addRequestCode(mPermissionCode)
            .permissions(Manifest.permission.READ_PHONE_STATE)
            .setPermissionListener(this)
            .request();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
  {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode)
    {
      case mPermissionCode:
        mManager.onPermissionResult(permissions, grantResults);
        break;
    }
  }

  //授权后回调
  @Override
  public void onGranted()
  {
    //登录
    mPresenter.start();
  }

  /**
   * 显示去设置权限的 dialog
   *
   * @param permissions 返回需要显示说明的权限数组
   */
  @Override
  public void onShowRationale(String permissions)
  {
    PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(getString(R.string.read_phone_state));
    dialogFragment.setOnPermissionListener(this);
    dialogFragment.show(getFragmentManager(),getClass().getSimpleName());
  }

  @Override
  public void dialogPositive()
  {
    //进入设置
    Intent localIntent = new Intent();
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    if (Build.VERSION.SDK_INT > 9)
    {
      localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
    }
    startActivity(localIntent);
  }
}
