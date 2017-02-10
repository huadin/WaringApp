package com.huadin.setting.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import butterknife.ButterKnife;

/**
 * Created by 潇湘 on 2017/2/10.
 * 联系我们
 */

public class ContactFragment extends BaseFragment
{

  public static ContactFragment newInstance()
  {

    Bundle args = new Bundle();

    ContactFragment fragment = new ContactFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {

    View view = getViewResId(inflater,container, R.layout.contact_fragment);
    ButterKnife.bind(this,view);
    return view;
  }
}
