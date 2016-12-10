package com.huadin.permission;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class PermissionManager
{
  private Object mObject;
  private String[] mPermissions;
  private int mRequestCode;
  private PermissionListener mListener;
  // 用户是否确认了解释框的
  private boolean mIsPositive = false;

  public static PermissionManager with(Activity activity)
  {
    return new PermissionManager(activity);
  }

  public static PermissionManager with(Fragment fragment)
  {
    return new PermissionManager(fragment);
  }

  private PermissionManager(Object object)
  {
    this.mObject = object;
  }

  /**
   * 设置请求的权限组
   *
   * @param permissions 权限
   * @return PermissionManager
   */
  public PermissionManager permissions(String... permissions)
  {
    this.mPermissions = permissions;
    return this;
  }

  /**
   * 设置请求码
   *
   * @param code 请求码
   * @return PermissionManager
   */
  public PermissionManager addRequestCode(int code)
  {
    this.mRequestCode = code;
    return this;
  }

  /**
   * 设置监听
   *
   * @param listener PermissionListener
   * @return PermissionManager
   */
  public PermissionManager setPermissionListener(PermissionListener listener)
  {
    this.mListener = listener;
    return this;
  }

  /**
   * 拒绝后再次请求权限
   *
   * @return PermissionManager
   */
  public PermissionManager request()
  {
    request(mObject, mPermissions, mRequestCode);
    return this;
  }

  private void request(@NonNull Object object, String[] permissions, int requestCode)
  {
    List<String> permissionList = findDeniedPermissions(getActivity(object), permissions);
    if (null != permissionList && permissionList.size() > 0)
    {
      if (object instanceof Activity)
      {
        ActivityCompat.requestPermissions((Activity) object,
                permissionList.toArray(new String[permissionList.size()]), requestCode);
      } else if (object instanceof Fragment)
      {
        ((Fragment) object).requestPermissions(permissionList.toArray(new String[permissionList.size()]), requestCode);
      } else
      {
        throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
      }
    } else
    {
      if (mListener != null)
      {
        mListener.onGranted();
      }
    }

  }


  /**
   * 查看被拒绝的权限
   *
   * @param activity    Activity
   * @param permissions String[]
   * @return List<String>
   */
  private List<String> findDeniedPermissions(Activity activity, String[] permissions)
  {
    List<String> needRequestPermissionList = new ArrayList<>();
    for (String perm : permissions)
    {
      if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED
              || ActivityCompat.shouldShowRequestPermissionRationale(
              activity, perm))
      {
        needRequestPermissionList.add(perm);
      }
    }
    return needRequestPermissionList;
  }


  /**
   * 是否点击对话框
   *
   * @param isPositive boolean
   */
  public void setIsPositive(boolean isPositive)
  {
    this.mIsPositive = isPositive;
  }


  /**
   * 根据 requestCode 处理响应的权限
   *
   * @param permissions 权限组
   * @param results     结果集
   */
  public void onPermissionResult(String[] permissions, int[] results)
  {

    for (int i = 0; i < results.length; i++)
    {
      if (results[i] != PackageManager.PERMISSION_GRANTED)
      {
        if (mListener != null)
        {
          mListener.onShowRationale(permissions[i]);
          break;
        }
      }
    }
  }


  /*检测 object 类型*/
  private Activity getActivity(@NonNull Object object)
  {
    if (object instanceof Fragment)
    {
      return ((Fragment) object).getActivity();
    } else if (object instanceof Activity)
    {
      return (Activity) object;
    }
    return null;
  }

}
