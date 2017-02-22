package com.huadin.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.huadin.MyApplication;
import com.huadin.base.BaseFragment;
import com.huadin.database.ScopeLatLng;
import com.huadin.eventbus.EventCenter;
import com.huadin.permission.PermissionListener;
import com.huadin.permission.PermissionManager;
import com.huadin.util.AMapGeoCode;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.huadin.waringapp.R.id.map;

/**
 * 地图定位
 */

public class MapFragment extends BaseFragment implements PermissionListener,
        MapContract.View, LocationSource, AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMap.OnInfoWindowClickListener
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(map)
  MapView mMapView;
  @BindView(R.id.network_text_view)
  TextView mNetworkTextView;

  private final int mPermissionCode = 0x11;
  private AMap mMap;
  private MapContract.MapListener mPresenter;
  private PermissionManager mPermissionManager;
  private List<ScopeLatLng> scopeLatLngs = new ArrayList<>();
  private boolean isCompleteLocation;

  public static MapFragment newInstance()
  {
    return new MapFragment();
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    //设置fragment横向动画
    _mActivity.setFragmentAnimator(new DefaultHorizontalAnimator());
    //设置竖向动画
//    _mActivity.setFragmentAnimator(new DefaultVerticalAnimator());
    //全局无动画
//    _mActivity.setFragmentAnimator(new DefaultNoAnimator());
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    MyApplication.mMapFragmentCreate = true;
    checkPermission();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.map_fragment_layout);
    ButterKnife.bind(this, view);
    mMapView.onCreate(savedInstanceState);
    initView();
//    checkPermission();
    return view;
  }

  private void checkPermission()
  {
    mPermissionManager = PermissionManager.with(this)
            .addRequestCode(mPermissionCode)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .setPermissionListener(this)
            .request();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
  {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode)
    {
      case mPermissionCode:
        mPermissionManager.onPermissionResult(permissions, grantResults);
        break;
    }
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
  public void onStop()
  {
    super.onStop();
    //停止定位
    deactivate();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    MyApplication.mMapFragmentCreate = false;
    mMapView.onDestroy();
    if (mPresenter != null)
    {
      mPresenter.destroyLocation();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  private void initView()
  {
    initToolbarHome(mToolbar, R.string.map_location, getActivity());

    if (mMap == null)
    {
      mMap = mMapView.getMap();
    }

    //marker 点击事件
    mMap.setOnMarkerClickListener(this);
    //地图点击事件
    mMap.setOnMapClickListener(this);
    //infoWindow窗口点击事件
    mMap.setOnInfoWindowClickListener(this);
    setUpMap();
  }

  //设置交互控件参数
  private void setUpMap()
  {
    //设置放大级别
    CameraUpdate update = CameraUpdateFactory.zoomTo(16);
    mMap.moveCamera(update);
    //设置交互控件
    UiSettings mSetting = mMap.getUiSettings();
    //缩放按钮位置
    mSetting.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    //指南针
    mSetting.setCompassEnabled(true);
    //显示比例尺控件
    mSetting.setScaleControlsEnabled(true);
    //显示默认的定位按钮
    mSetting.setMyLocationButtonEnabled(true);
    //设置定位源
    mMap.setLocationSource(this);
    // 可触发定位并显示定位层
    mMap.setMyLocationEnabled(true);
  }


  //授权成功回调
  @Override
  public void onGranted()
  {
    LogUtil.i(LOG_TAG, "onGranted = " + System.currentTimeMillis());

    //初始化控制器,在权限检测之前?
    new MapPresenter(this, mContext);

    if (!isNetwork())
    {
      // TODO: 2016/12/10 检测GPS是否开启 ,未开启则提示用户
      //无网络
      showMessage(R.string.error_code_9016);
    }
  }

  //显示权限解释及设置权限
  @Override
  public void onShowRationale(String permissions)
  {
    showDialogPermission();
  }

  /**
   * mapPresenter 回调
   */
  @Override
  public void latLng(LatLng latLng)
  {
    checkNotNull(latLng, "LatLng 没有数据");
    if (isCompleteLocation)
    {
      mPresenter.addMarkerToMap(scopeLatLngs, latLng);
      isCompleteLocation = false;
    }
  }

  /**
   * mapPresenter 回调
   *
   * @param errorCode 错误码
   * @param errorInfo 错误信息
   */
  @Override
  public void locationError(int errorCode, String errorInfo)
  {
    LogUtil.i(LOG_TAG, "errorCode = " + errorCode);
    LogUtil.i(LOG_TAG, "errorInfo = " + errorInfo);
  }

  @Override
  public void setPresenter(MapContract.MapListener presenter)
  {
    mPresenter = checkNotNull(presenter, "presenter cannot null");
  }

  //激活定位
  @Override
  public void activate(OnLocationChangedListener onLocationChangedListener)
  {
    //检测网络
    if (!isNetwork())
    {
      //无网络提示
      mNetworkTextView.setVisibility(View.VISIBLE);
    }
    mPresenter.startLocation(onLocationChangedListener);
  }

  //停止定位
  @Override
  public void deactivate()
  {
    if (mPresenter != null)
    {
      mPresenter.stopLocation();
    }
  }

  @Override
  public void addMarker(ArrayList<MarkerOptions> options)
  {
    mMap.clear();
    mMap.addMarkers(options, true);
  }

  private void showDialogPermission()
  {
    try
    {

      final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
      dialog.setTitle(R.string.permission_dialog_title);
      dialog.setMessage(getString(R.string.permission_storage_message));
      dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.permission_dialog_positive),
              new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                  //跳转权限设置
                  settingPermission();
                  mContext.finish();

                }
              });
      dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.permission_dialog_negative),
              new DialogInterface.OnClickListener()
              {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                  mContext.finish();
                }
              });

      dialog.setCancelable(false);
      dialog.show();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void onHiddenChanged(boolean hidden)
  {
    super.onHiddenChanged(hidden);
    //地图不再前台时，停止定位
    if (hidden)
    {
      mPresenter.stopLocation();
    }
  }

  @Override
  protected void fragmentOnEvent(EventCenter eventCenter)
  {
    super.fragmentOnEvent(eventCenter);
    switch (eventCenter.getEventCode())
    {
      case EventCenter.GEO_CODE_COMPLETE:
        List<ScopeLatLng> list = (List<ScopeLatLng>) eventCenter.getData();
        /*
        * 此位置调用  mPresenter.addMarkerToMap(scopeLatLngs, latLng) 时
        * 可能出现定位结果未返回现象,既 latLnt = null;
        */
        scopeLatLngs.addAll(list);
        isCompleteLocation = true;
        break;

      case EventCenter.GEO_CODE_START://开始解析
        AMapGeoCode geoCode = new AMapGeoCode(mContext);
        geoCode.startGeoCode();
        break;
      case EventCenter.EVENT_CODE_NETWORK:
        boolean network = (boolean) eventCenter.getData();
        if (network)
        {
          mNetworkTextView.setVisibility(View.GONE);
          startService();
        }
        break;
    }

  }

  //标记点击事件
  @Override
  public boolean onMarkerClick(Marker marker)
  {
    mPresenter.markerClick(marker);
    return false;
  }

  //地图点击事件
  @Override
  public void onMapClick(LatLng latLng)
  {
    mPresenter.mapClick();
  }

  //窗口点击事件
  @Override
  public void onInfoWindowClick(Marker marker)
  {
    mPresenter.windowClick(marker);
  }
}
