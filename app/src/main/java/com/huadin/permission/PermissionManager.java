package com.huadin.permission;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
   * 请求权限
   *
   * @return PermissionManager
   */
  public PermissionManager request()
  {
    request(mObject, mPermissions, mRequestCode);
    return this;
  }

  private void request(Object object, String[] permissions, int requestCode)
  {
    Map<String, List<String>> maps = findDeniedPermissions(getActivity(object), permissions);
    List<String> deniedPermissions = maps.get("deny");
    List<String> rationales = maps.get("rationale");
    if (deniedPermissions.size() > 0)
    {
      if (rationales.size() > 0 && !mIsPositive)
      {
        if (mListener != null)
        {
          mListener.onShowRationale(rationales.toArray(new String[deniedPermissions.size()]));
        }
        return;
      }
      if (object instanceof Activity)
      {
        ActivityCompat.requestPermissions((Activity) object,
                deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
      } else if (object instanceof Fragment)
      {
        ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
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
   * 是否点击对话框
   *
   * @param isPositive boolean
   */
  public void setIsPositive(boolean isPositive)
  {
    this.mIsPositive = isPositive;
  }

  private Map<String, List<String>> findDeniedPermissions(Activity activity, String... permissions)
  {
    Map<String, List<String>> map = new HashMap<>();
    List<String> denyList = new ArrayList<>();//未授权的权限
    List<String> rationaleList = new ArrayList<>();//需要显示提示框的权限

    for (String permission : permissions)
    {
      if (ContextCompat.checkSelfPermission(activity, permission) !=
              PackageManager.PERMISSION_GRANTED)
      {
        //申请劝降
        denyList.add(permission);
        //是否显示弹窗
        if (shouldShowRequestPermissionRationale(permission))
        {
          rationaleList.add(permission);
        }
      }
    }

    map.put("deny", denyList);
    map.put("rationale", rationaleList);
    return map;
  }

  /* 显示弹窗的权限 ,给予用户解释 */
  private boolean shouldShowRequestPermissionRationale(String permission)
  {
    if (mObject instanceof Activity)
    {
      return ActivityCompat.shouldShowRequestPermissionRationale((Activity) mObject, permission);
    } else if (mObject instanceof Fragment)
    {
      return ((Fragment) mObject).shouldShowRequestPermissionRationale(permission);
    } else
    {
      throw new IllegalArgumentException(mObject.getClass().getName() + " is not supported");
    }
  }

  /**
   * 根据 requestCode 处理响应的权限
   *
   * @param permissions 权限组
   * @param results     结果集
   */
  public void onPermissionResult(String[] permissions, int[] results)
  {
    List<String> deniedPermissions = new ArrayList<>();
    for (int i = 0; i < permissions.length; i++)
    {
      if (results[i] != PackageManager.PERMISSION_GRANTED)
      {
        deniedPermissions.add(permissions[i]);
      }
    }

    if (deniedPermissions.size() > 0)
    {
      if (mListener != null)
      {
        mListener.onDenied();
      }
    } else
    {
      if (mListener != null)
      {
        mListener.onGranted();
      }
    }
  }

  /*检测 object 类型*/
  private Activity getActivity(Object object)
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
