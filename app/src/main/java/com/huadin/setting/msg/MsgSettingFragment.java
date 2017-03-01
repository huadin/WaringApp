package com.huadin.setting.msg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.huadin.base.BaseFragment;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NCL on 2017/3/1.
 * 消息设置
 */

public class MsgSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.switch_all_button)
  SwitchCompat mAllSwitch;
  @BindView(R.id.switch_part_button)
  SwitchCompat mPartSwitch;

  private boolean mAllOn;
  private boolean mPartOn;
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  private static final String KEY_PART_ON = "PART_ON";
  private static final String KEY_ALL_ON = "KEY_ALL_ON";
  private static final String KEY_SWITCH_STATE = "SWITCH_STATE";


  public static MsgSettingFragment newInstance()
  {

    Bundle args = new Bundle();

    MsgSettingFragment fragment = new MsgSettingFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mPreferences = mContext.getSharedPreferences(KEY_SWITCH_STATE, Context.MODE_PRIVATE);
    mEditor = mPreferences.edit();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {

    View view = getViewResId(inflater, container, R.layout.msg_setting_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.msg_setting, false);
    initView();
    return view;
  }

  private void initView()
  {
    mPartSwitch.setOnCheckedChangeListener(this);
    mAllSwitch.setOnCheckedChangeListener(this);
    boolean allSwitch = mPreferences.getBoolean(KEY_ALL_ON, true);
    boolean partSwitch = mPreferences.getBoolean(KEY_PART_ON, false);

    mAllSwitch.setChecked(allSwitch);
    mPartSwitch.setChecked(partSwitch);

    if (!allSwitch)
    {
      mPartSwitch.setChecked(false);
      mPartSwitch.setEnabled(false);
    }

  }

  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean check)
  {
    LogUtil.i(LOG_TAG, "boolean = " + check);
    switch (compoundButton.getId())
    {
      case R.id.switch_all_button:
        if (!check)
        {
          mPartSwitch.setChecked(false);
          mPartSwitch.setEnabled(false);
          mEditor.putBoolean(KEY_ALL_ON, false);
          mEditor.putBoolean(KEY_PART_ON, false);
        } else
        {
          mPartSwitch.setEnabled(true);
          mEditor.putBoolean(KEY_ALL_ON, true);
        }
        break;
      case R.id.switch_part_button:
        mEditor.putBoolean(KEY_PART_ON, check);
        break;
    }
    mEditor.apply();
  }
}
