package com.huadin.waringapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.huadin.base.BaseActivity;
import com.huadin.permission.PermissionListener;
import com.huadin.permission.PermissionManager;
import com.huadin.util.LogUtil;

import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity implements PermissionListener
{
  private String[] needPermissions = {
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_PHONE_STATE
  };

  private final int permissionCode = 0x11;

  private boolean isNeedCheck = true;
  private PermissionManager manager;

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
    manager = PermissionManager.with(this)
            .addRequestCode(permissionCode)
            .permissions(needPermissions)
            .setPermissionListener(this)
            .request();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    //检查权限
    mToast.onResume();
    if (isNeedCheck)
    {
      checkStoragePermission();
    }
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    mToast.onPause();
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
  {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode)
    {
      case permissionCode:
        manager.onPermissionResult(permissions, grantResults);
        break;
    }
  }

  @Override
  public void onGranted()
  {
    //应用授权
  }

  @Override
  public void onDenied()
  {
    //被拒绝
    LogUtil.i(LOG_TAG, "--- onDenied ---");
    showDialogPermission();
  }

  @Override
  public void onShowRationale(String permissions)
  {
    showDialogPermission();
    isNeedCheck = false;
  }

  private void showDialogPermission()
  {
    try
    {

      final AlertDialog dialog = new AlertDialog.Builder(this).create();
      dialog.setTitle(R.string.permission_dialog_title);
      dialog.setMessage(getString(R.string.permission_storage_message));
      dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.permission_dialog_positive),
              new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
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
              });
      dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.permission_dialog_negative),
              new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                  finish();
                }
              });
      dialog.setCanceledOnTouchOutside(true);
      dialog.show();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
