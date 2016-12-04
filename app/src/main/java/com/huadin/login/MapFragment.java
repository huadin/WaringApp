package com.huadin.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;

/**
 * 地图定位
 */

public class MapFragment extends BaseFragment
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.map)
  MapView mMapView;

  AMap aMap;

  private OnFragmentOpenDrawerListener mListener;


  public static MapFragment newInstance()
  {
    return new MapFragment();
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    if (context instanceof OnFragmentOpenDrawerListener)
    {
      //打开 DrawerLayout
      mListener = (OnFragmentOpenDrawerListener) context;
    }
    //设置fragment横向动画
    _mActivity.setFragmentAnimator(new DefaultHorizontalAnimator());
    //设置竖向动画
//    _mActivity.setFragmentAnimator(new DefaultVerticalAnimator());
    //全局无动画
//    _mActivity.setFragmentAnimator(new DefaultNoAnimator());
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.map_fragment_layout);
    ButterKnife.bind(this, view);
    mMapView.onCreate(savedInstanceState);

    initView();
    return view;
  }

  @Override
  public void onResume()
  {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  public void onPause()
  {
    super.onPause();
    mMapView.onPause();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    mMapView.onDestroy();
  }

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  private void initView()
  {
    mToolbar.setTitle(R.string.map_location);
    initToolbarNav(mToolbar);
    if (aMap == null)
    {
      aMap = mMapView.getMap();
    }

  }

  private void initToolbarNav(Toolbar mToolbar)
  {
    mToolbar.setNavigationIcon(R.drawable.icon_home_72px);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (mListener != null)
        {
          mListener.onOpenDrawer();
        }
      }
    });
  }


  //打开抽屉回调接口
  public interface OnFragmentOpenDrawerListener
  {
    void onOpenDrawer();
  }

}
