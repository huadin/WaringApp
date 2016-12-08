package com.huadin.waringapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.huadin.base.BaseActivity;
import com.huadin.dialog.PermissionDialogFragment;

import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity implements PermissionDialogFragment.OnPermissionListener
{
  private String[] needPermissions = {
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.READ_PHONE_STATE
  };

  private int permissionCode[] = {0x11,0x12,0x13,0x14,0x15};


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_welcome;
  }


  private void checkStoragePermission()
  {
    // TODO: 2016/12/8 设置请求权限

  }


  @Override
  protected void onResume()
  {
    super.onResume();
    //检查权限
    checkStoragePermission();
  }


  //App详情界面修改权限
  @Override
  public void dialogPositive()
  {
    //跳转权限设置
    Intent localIntent = new Intent();
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    if (Build.VERSION.SDK_INT > 9)
    {
      localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      localIntent.setData(Uri.fromParts("package", getPackageName(), null));
    }
    startActivity(localIntent);
    finish();
  }

  @Override
  public void dialogNegative()
  {
    finish();
  }

}
