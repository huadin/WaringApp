package com.huadin.base_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huadin.util.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AutoLayoutActivity
{
  /**
   * log tag
   */
  protected static String LOG_TAG = null;
  protected Context mContext;
  protected ToastUtil mToast;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mContext = this;
    LOG_TAG = this.getClass().getSimpleName();
    mToast = new ToastUtil(mContext);

    if (getContentViewLayoutID() != 0)
    {
      setContentView(getContentViewLayoutID());
    } else
    {
      throw new IllegalArgumentException(
              "You must return a right contentView layout resource Id");
    }
  }

  /**
   * Activity 布局Id
   *
   * @return int
   */
  protected abstract int getContentViewLayoutID();

  protected void toActivity(Context context, Class<?> cls)
  {
    Intent intent = new Intent(context, cls);
    startActivity(intent);
  }

}
