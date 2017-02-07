package com.huadin.urgent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.base.BaseFragment;
import com.huadin.bean.ReleaseBean;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 潇湘 on 2017/2/7.
 * 紧急信息详细内容
 */

public class DetailedUrgentFragment extends BaseFragment
{
  private static final String KEY_BEAN = "KEY_BEAN";
  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.temp)
  TextView mTemp;

  public static DetailedUrgentFragment newInstance(ReleaseBean releaseBean)
  {

    Bundle args = new Bundle();
    args.putParcelable(KEY_BEAN, releaseBean);
    DetailedUrgentFragment fragment = new DetailedUrgentFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {

    Bundle args = getArguments();
    ReleaseBean mReleaseBean = args.getParcelable(KEY_BEAN);
    View view = getViewResId(inflater, container, R.layout.detailed_urgent_fragment_layout);
    ButterKnife.bind(this, view);

    initToolbar(mToolbar, R.string.detailed_fault_title, false);

    if (mReleaseBean != null)
    {
      String content = mReleaseBean.getReleaseContent();
      mTemp.setText(content);
    }
    return view;
  }
}
