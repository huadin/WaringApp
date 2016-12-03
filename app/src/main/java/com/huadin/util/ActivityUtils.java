package com.huadin.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static android.R.attr.fragment;
import static android.R.attr.tabStripEnabled;
import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils
{
  public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                           @NonNull Fragment fragment, int frameId,
                                           boolean isAddToBackStack)
  {
    checkNotNull(fragmentManager);
    checkNotNull(fragment);
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    // TODO: 2016/11/30 暂时改为替换
    transaction.replace(frameId, fragment, fragment.getClass().getSimpleName());
    if (!isAddToBackStack)
    {
      transaction.addToBackStack(String.valueOf(frameId));
    }
//    transaction.show(fragment);
    transaction.commit();
  }

}
