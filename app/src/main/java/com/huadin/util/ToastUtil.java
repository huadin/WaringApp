package com.huadin.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import android.widget.Toast;

import com.huadin.waringapp.R;

/**
 * 封装Toast
 */

public class ToastUtil extends Toast
{
  private Context mContext;
  private boolean screenFront;

  public ToastUtil(Context context)
  {
    super(context);
    this.mContext = context;
    init();
  }

  private TextView textMsgTitle;
  private View relMsgLayout;

  private void init()
  {
    setGravity(Gravity.CENTER, 0, 0);

    View view = LayoutInflater.from(mContext).inflate(R.layout.custom_toast_layout, null);
    textMsgTitle = (TextView) view.findViewById(R.id.textMsgTitle);
    relMsgLayout = view.findViewById(R.id.relMsgLayout);

    relMsgLayout.setVisibility(View.GONE);

    setView(view);
  }

  public void onResume()
  {
    screenFront = true;
  }

  public void onPause()
  {
    screenFront = false;
  }

  public void showMessage(int titleRes, int duration)
  {
    String title = null;
    if (titleRes > 0) title = mContext.getString(titleRes);
    showMessage(title, duration);
  }

  public void showMessage(String title, int duration)
  {
    //如果当前界面不在用户前面，则取消弹出
    if (!screenFront)
    {
      return;
    }
    if (title != null)
    {
      textMsgTitle.setVisibility(View.VISIBLE);
      textMsgTitle.setText(title);
    }
    relMsgLayout.setVisibility(View.VISIBLE);
    setDuration(duration);
    show();
  }

  @Override
  public void show()
  {
    super.show();
//    Animation scaleAnimation = new ScaleAnimation(1.1f, 1f, 1.1f, 1f);
//    Animation translateAnimation = new TranslateAnimation(0f, 0f, 0f, 0f);
    AnimationSet animationSet = new AnimationSet(true);
//    animationSet.addAnimation(scaleAnimation);
//    animationSet.addAnimation(translateAnimation);
    animationSet.setDuration(200);
    relMsgLayout.startAnimation(animationSet);
  }


}
