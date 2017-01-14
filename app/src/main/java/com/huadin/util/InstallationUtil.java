package com.huadin.util;

import android.content.Context;

import com.huadin.base.InstallationListener;
import com.huadin.bean.PushInstallation;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/9.
 * BmobInstallation
 */

public class InstallationUtil
{

  private InstallationListener mListener;
  private Context mContext;

  public static InstallationUtil newInstance()
  {

    return new InstallationUtil();
  }

  /**
   * 添加接口
   *
   * @param listener InstallationListener
   * @return InstallationUtil
   */
  public InstallationUtil addInstallationListener(InstallationListener listener)
  {
    this.mListener = listener;
    mListener = checkNotNull(listener, "listener cannot be null");
    return this;
  }

  /**
   * 添加 context
   *
   * @param context Context
   * @return InstallationUtil
   */
  public InstallationUtil with(Context context)
  {
    this.mContext = context;
    mContext = checkNotNull(context, "context cannot be null");
    return this;
  }

  /**
   * 绑定推送
   *
   * @param userName 推送tag
   */
  public void bindingPush(final String userName)
  {
    BmobQuery<PushInstallation> query = new BmobQuery<>();
    query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(mContext));
    query.findObjects(new FindListener<PushInstallation>()
    {
      @Override
      public void done(List<PushInstallation> list, BmobException e)
      {
        if (e == null)
        {
          if (list.size() > 0)
          {
            PushInstallation pInstallation = list.get(0);
            pInstallation.setPushUserName(userName);
            pInstallation.update(new UpdateListener()
            {
              @Override
              public void done(BmobException e)
              {
                if (e == null)
                {
                  mListener.installationSuccess(userName);
                } else
                {
                  errorCode(e);
                }
              }
            });
          }
        } else
        {
          errorCode(e);
        }
      }
    });
  }

  /**
   * 异常
   *
   * @param e BmobException
   */
  private void errorCode(BmobException e)
  {
    int code = e.getErrorCode();
    mListener.installationError(code);
  }
}
