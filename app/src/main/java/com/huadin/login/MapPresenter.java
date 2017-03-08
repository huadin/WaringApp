package com.huadin.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.huadin.database.Range;
import com.huadin.database.ScopeLatLng;
import com.huadin.database.StopPowerBean;
import com.huadin.util.LogUtil;
import com.huadin.util.RangeUtil;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 地图
 */

class MapPresenter implements MapContract.MapListener
{
  private static final String TAG = "MapPresenter";
  private MapContract.View mView;
  private AMapLocationClient mClient;
  private Context mContext;// TODO: 2017/3/8 Context存在内存泄漏
  private LocationSource.OnLocationChangedListener mListener;

  private BitmapDescriptor JHMarker;//计划
  private BitmapDescriptor TDMarker;//处于停电
  private BitmapDescriptor GZMarker;//故障
  private BitmapDescriptor LLMarker;//临时
  private BitmapDescriptor mMarker;//添加到map上的marker
  private BitmapDescriptor mBeforeMarker;//前一个marker
  private Marker mHindMarker;//用于隐藏infoWindow窗口
  private Marker mTempMarker;//用于改变点击的marker的颜色

  MapPresenter(MapContract.View view, Context context)
  {
    this.mView = view;
    this.mContext = context;
    mContext = checkNotNull(context, "context cannot null");
    mView = checkNotNull(view, "view cannot null");
    mView.setPresenter(this);
    initMarker();
  }

  private void initMarker()
  {
    JHMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
    GZMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
    LLMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
    TDMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
  }

  /**
   * 定位监听
   *
   * @param aMapLocation AMapLocation
   */
  @Override
  public void onLocationChanged(AMapLocation aMapLocation)
  {
    if (mListener != null && aMapLocation != null)
    {
      if (aMapLocation.getErrorCode() == 0)
      {
        double lag = aMapLocation.getLatitude();
        double lng = aMapLocation.getLongitude();

        LatLng latLng = new LatLng(lag, lng);
        mView.latLng(latLng);
        //显示系统小蓝点
        mListener.onLocationChanged(aMapLocation);
      } else
      {
        //异常 aMapLocation.getErrorInfo()
        LogUtil.i(TAG, "定位异常信息 = " + aMapLocation.getErrorInfo() +
                "error code = " + aMapLocation.getErrorCode());
        mView.locationError(mContext.getString(R.string.location_error));
      }
    }
  }

  /**
   * 定位配置信息
   *
   * @return AMapLocationClientOption
   */
  private AMapLocationClientOption getDefaultOption()
  {
    AMapLocationClientOption option = new AMapLocationClientOption();
    //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
    option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
    option.setGpsFirst(false);
    //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
    option.setHttpTimeOut(30000);
    //可选，设置定位间隔。默认为2秒
    option.setInterval(2000);
    //可选，设置是否返回逆地理地址信息。默认是true
    option.setNeedAddress(true);
    //可选，设置是否单次定位。默认是false
    option.setOnceLocation(false);
    //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
    option.setOnceLocationLatest(false);
    //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
    AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
    //可选，设置是否使用传感器。默认是false
    option.setSensorEnable(false);
    return option;
  }

  /**
   * 开启定位
   */
  @Override
  public void startLocation(LocationSource.OnLocationChangedListener listener)
  {
    this.mListener = listener;
    mListener = checkNotNull(listener, "OnLocationChangedListener cannot null");
    mClient = new AMapLocationClient(mContext.getApplicationContext());
    mClient.setLocationOption(getDefaultOption());
    mClient.setLocationListener(this);

    mClient.startLocation();
  }

  /**
   * 恢复定位
   */
  @Override
  public void resumeLocation()
  {
    mClient.startLocation();
  }


  /**
   * 停止定位
   */
  @Override
  public void stopLocation()
  {
    mClient = checkNotNull(mClient, "AMapLocationClient null");
    mClient.stopLocation();
  }

  /**
   * 销毁定位
   */
  @Override
  public void destroyLocation()
  {
    mClient = checkNotNull(mClient, "AMapLocationClient null");
    mClient.onDestroy();
    mClient = null;
  }

  /**
   * 添加标记点
   *
   * @param scopeLatLngList 解析出来的经纬度的集合
   * @param latLng          当前位置的经纬度
   */
  @Override
  public void addMarkerToMap(List<ScopeLatLng> scopeLatLngList, @NonNull LatLng latLng)
  {
    LogUtil.i(TAG, "解析数据完成" + "long = " + System.currentTimeMillis() + " / list = " + scopeLatLngList.toString());
    if (scopeLatLngList.size() == 0)
    {
      mView.locationError(mContext.getString(R.string.http_not_data));
      return;
    }
    List<String> scopeList = RangeUtil.scopeFromRange(scopeLatLngList, latLng);
    setMarkerOptions(scopeList);
  }


  @Override
  public void markerClick(Marker marker)
  {
    mHindMarker = marker;

    //改变点击的marker的颜色
    if (marker != mTempMarker)
    {
      if (mTempMarker != null)
      {
        //改变前一个marker的颜色
        mTempMarker.setIcon(mBeforeMarker);
      }
      mTempMarker = marker;
    }

    ArrayList<BitmapDescriptor> iconList = marker.getIcons();
    if (iconList != null && iconList.size() > 0)
    {
      mBeforeMarker = iconList.get(0);
    }

    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
  }

  @Override
  public void mapClick()
  {
    if (mHindMarker != null)
    {
      mHindMarker.hideInfoWindow();
      //改变颜色
      mHindMarker.setIcon(mBeforeMarker);
    }
  }

  @Override
  public void windowClick(Marker marker)
  {
    mHindMarker.hideInfoWindow();
    marker.setIcon(mBeforeMarker);
  }

  @Override
  public void resetRange(double r)
  {
    List<ScopeLatLng> scopeLatLngs = DataSupport.findAll(ScopeLatLng.class);
    Range range = DataSupport.findFirst(Range.class);
    if (range == null)
    {
      range = new Range();
    }
    range.setRange(r);
    range.save();

    List<String> scopeList = RangeUtil.resetRange(scopeLatLngs);
    if (scopeList == null || scopeList.size() == 0)
    {
      mView.locationError(mContext.getString(R.string.no_data_range));
      return;
    }

    setMarkerOptions(scopeList);
  }

  /**
   * 设置 MarkerOptions
   *
   * @param scopeList 停电地点集合
   */
  private void setMarkerOptions(List<String> scopeList)
  {

    ArrayList<MarkerOptions> optionsArrayList = new ArrayList<>();

    for (int i = 0; i < scopeList.size(); i++)
    {
      String scope = scopeList.get(i);

      //获取经纬度
      LatLng ll = getLatLntFromScope(scope);

      //根据 scope 模糊搜索 StopPowerBean
      String content = getContentFormScope(scope);
      //防止解析地址与实际地址对不上，会丢失数据
      if (TextUtils.isEmpty(content)) continue;

      MarkerOptions options = new MarkerOptions()
              .title("停电信息")     // title
              .position(ll)          // 定位点
              .icon(mMarker)         // marker
              .snippet(content);     //内容
//                .draggable(true)     //可拖拽
//                  .perspective(true);//近大远小
      optionsArrayList.add(options);
    }
    mView.addMarker(optionsArrayList);
  }


  /**
   * 获取经纬度信息
   *
   * @param scope 地点
   * @return LatLng
   */
  private LatLng getLatLntFromScope(String scope)
  {
    ScopeLatLng scopeLatLng = DataSupport.where("scope like ?", scope).findFirst(ScopeLatLng.class);
    return new LatLng(scopeLatLng.getLatitude(), scopeLatLng.getLongitude());
  }


  /**
   * 获取停电详细信息
   *
   * @param scope 地点
   * @return 停电详细信息
   */
  private String getContentFormScope(String scope)
  {
    //为了防止添加重复的时间段
    TreeMap<String, String> treeMap = new TreeMap<>();

    StringBuilder sb = new StringBuilder();
    StringBuilder contentBuilder = new StringBuilder();
    String content = null;

    //将 xxx村委会 替换成 xxx村
//    if (scope.endsWith("委会"))
//    {
//      scope = scope.replace("委会", "").trim();
//    }
    //模糊查询所有符合条件的
    List<StopPowerBean> beanList = DataSupport.where("scope like ?", "%" + scope + "%").find(StopPowerBean.class);

    if (beanList != null && beanList.size() > 0)
    {
      // 获取 typeCode , scope ,lineName
      StopPowerBean powerBean = beanList.get(0);
      String newScope = powerBean.getScope();

      if (newScope.length() >= 20)
      {
        StringBuilder sbScope = new StringBuilder(newScope);
        sbScope.insert(15, "\n");
        newScope = sbScope.toString();
        sbScope.delete(0, sbScope.length());
      }


      String typeCode = powerBean.getTypeCode();
//      String lineName = powerBean.getLineName();

      LogUtil.i(TAG, "type  = " + typeCode);

      switch (typeCode)
      {
        case "计划停电":
          mMarker = JHMarker;
          break;
        case "临时停电":
          mMarker = LLMarker;
          break;
        case "故障停电":
          mMarker = GZMarker;
          break;
      }


      //追加 scope 的不同停电时间段
      for (int i = 0; i < beanList.size(); i++)
      {
        StopPowerBean nst = beanList.get(i);
        String date = nst.getDate();
        String time = nst.getTime();

        if (compareTime(date, time))
        {
          mMarker = TDMarker;//处于停电状态
        }

        sb.append(date);
        sb.append(" ");
        sb.append(time);

        treeMap.put(sb.toString(), null);
        sb.delete(0, sb.length());
      }

      Set<String> keySet = treeMap.keySet();
      for (String s : keySet)
      {
        sb.append(s);
        if (keySet.size() > 1)
        {
          sb.append(";");
          sb.append("\n");
        }
      }

      //组合所有信息
      contentBuilder.append("停电类型：");
      contentBuilder.append(typeCode);
      contentBuilder.append("\n");
      contentBuilder.append("停电范围：");
      contentBuilder.append(newScope);
      contentBuilder.append("\n");
//      contentBuilder.append("停电线路：");
//      contentBuilder.append("太多了");
//      contentBuilder.append("\n");
      contentBuilder.append("停电时间：");
      contentBuilder.append(sb.toString());

      content = contentBuilder.toString();

      sb.delete(0, sb.length());
      contentBuilder.delete(0, sb.length());
    }
    return content;
  }

  /**
   * 检测当前时间是否处于停电期间
   *
   * @param date 日期
   * @param time 时间
   * @return true 表示处于停电时间内
   */
  private boolean compareTime(String date, String time)
  {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


    String systemTime = sdf.format(new Date(System.currentTimeMillis()));

    String[] dateArr = systemTime.trim().split("-");//系统日期、时间

    String tempDate = dateArr[0];
    String tempTime = dateArr[1];

    String[] timeArr = time.trim().split("-");//停电时间

    String startTime = timeArr[0].trim();
    String endTime = timeArr[1].trim();

    int startIndex = startTime.compareTo(tempTime);//index < 0 ,startTime < newTime
    int endIndex = endTime.compareTo(tempTime); // index > 0 , endTime > newTime

    return tempDate.equals(date) && (startIndex < 0 && endIndex > 0);
  }


}
