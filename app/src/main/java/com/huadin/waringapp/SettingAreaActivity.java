package com.huadin.waringapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import com.huadin.adapter.AreaAdapter;
import com.huadin.base.BaseActivity;
import com.huadin.database.City;
import com.huadin.login.MainActivity;
import com.huadin.userinfo.address.AddressContract;
import com.huadin.userinfo.address.AddressPresenter;
import com.huadin.util.PullUtil;
import com.huadin.widget.LoadDialog;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/2/22.
 * 启动时设置区域，并获取数据
 */

public class SettingAreaActivity extends BaseActivity implements AddressContract.View
{
  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.setting_area_spinner)
  Spinner mSpinner;
  private List<City> mCityList;
  private AddressContract.Presenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    mToolbar.setTitle(R.string.setting_area_toolbar_title);
    //解析地区
    initCity();
    mPresenter = new AddressPresenter(mContext, this);
  }

  private void initCity()
  {
    mCityList = DataSupport.findAll(City.class);
    // TODO: 2017/1/17 在这解析会出现卡顿现象
    if (mCityList == null || mCityList.size() == 0)
    {
      try (
              //自动关闭
              InputStream inputStream = mContext.getAssets().open(getString(R.string.pull_xml))
      )
      {
        mCityList = PullUtil.pullParser(inputStream);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    AreaAdapter areaAdapter = new AreaAdapter(mContext, mCityList);
    mSpinner.setAdapter(areaAdapter);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    mToast.onResume();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    mToast.onPause();
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_setting_area_layout;
  }

  @OnClick(R.id.area_submit)
  public void onClick()
  {
    mPresenter.saveLocalAddress();
  }

  @Override
  public City getCity()
  {
    int pos = mSpinner.getSelectedItemPosition();
    return mCityList.get(pos);
  }

  @Override
  public String getDetailedAddress()
  {
    return getString(R.string.detailed_address);
  }

  @Override
  public void showLoading()
  {
    LoadDialog.show(mContext, getString(R.string.register_loading));
  }

  @Override
  public void hindLoading()
  {
    LoadDialog.dismiss(mContext);
  }

  @Override
  public void updateSuccess()
  {
    //本地保存成功
    SharedPreferences.Editor editor = getSharedPreferences(KEY_ADDRESS_AREA_SHARED, MODE_PRIVATE).edit();
    editor.putInt(KEY_ADDRESS_AREA_KEY, 1);
    editor.apply();

    if (isNetwork()) startService();
    startActivity(MainActivity.class);
    finish();
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(AddressContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "SettingArea Presenter cannot be null");
  }
}
