package com.huadin.waringapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.huadin.base.BaseActivity;
import com.huadin.database.Range;
import com.huadin.login.MainActivity;
import com.huadin.permission.PermissionListener;
import com.huadin.permission.PermissionManager;
import com.huadin.service.HttpIntentService;
import com.huadin.util.LogUtil;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity implements PermissionListener, Animation.AnimationListener
{
  @BindView(R.id.activity_welcome)
  RelativeLayout mRelativeLayout;

  private static final String TAG = "WelcomeActivity";

  private String[] mNeedPermission = {
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_PHONE_STATE
  };

  private final int permissionCode = 0x11;
  /**
   * 反正重复弹出 dialog
   */
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


  /**
   * 检查权限
   */
  private void checkStoragePermission()
  {
    manager = PermissionManager.with(this)
            .addRequestCode(permissionCode)
            .permissions(mNeedPermission)
            .setPermissionListener(this)
            .request();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    //检查权限
    if (isNeedCheck)
    {
      checkStoragePermission();
    }
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

  /**
   * 授权后回调
   */
  @Override
  public void onGranted()
  {
    //应用授权
    LogUtil.i(TAG, "onGranted");
    //开启动画
    initAnimation();
  }

  /**
   * 显示去设置权限的 dialog
   *
   * @param permissions 返回需要显示说明的权限数组
   */
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

      dialog.setCancelable(false);
      dialog.show();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  //初始化动画
  private void initAnimation()
  {
    Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_anim);
    animation.setAnimationListener(this);
    mRelativeLayout.startAnimation(animation);
  }

  @Override
  public void onAnimationStart(Animation animation)
  {
    //开启服务,加载网络数据
    if (!isNetwork()) return; //没有网络
    startService();
  }

  @Override
  public void onAnimationEnd(Animation animation)
  {
    startActivity(MainActivity.class);
    finish();
  }

  @Override
  public void onAnimationRepeat(Animation animation)
  {

  }
}
