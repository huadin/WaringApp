package com.huadin.waringapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.huadin.base.BaseActivity;
import com.huadin.dialog.PermissionDialogFragment;
import com.huadin.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity implements PermissionDialogFragment.OnPermissionListener, Animation.AnimationListener
{
  private final int PERMISSION_READ_STORAGE = 0x11;

  @BindView(R.id.activity_welcome)
  RelativeLayout mRelativeLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initAnim();
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_welcome;
  }

  private void initAnim()
  {
    Animation anim = AnimationUtils.loadAnimation(this, R.anim.welcome_anim);
    mRelativeLayout.startAnimation(anim);
    anim.setAnimationListener(this);
  }

  private void checkStoragePermission()
  {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED)
    {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.WRITE_EXTERNAL_STORAGE))
      {
        try
        {
          Bundle bundle = new Bundle();
          bundle.putString(getString(R.string.permission_dialog_key), getString(R.string.permission_storage_message));
          PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance();
          dialogFragment.setOnPermissionListener(this);
          dialogFragment.setArguments(bundle);
          dialogFragment.show(getSupportFragmentManager(), getClass().getSimpleName());
        } catch (Exception e)
        {
          e.printStackTrace();
        }

      } else
      {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_READ_STORAGE);
      }
    } else
    {
      //有权限启动动画
      initAnim();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
  {
    if (requestCode == PERMISSION_READ_STORAGE)
    {
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
      {
        checkStoragePermission();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

  @Override
  public void onAnimationStart(Animation animation)
  {
    //检查权限
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
