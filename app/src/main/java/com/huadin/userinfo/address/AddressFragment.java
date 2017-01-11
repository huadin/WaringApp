package com.huadin.userinfo.address;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.huadin.adapter.AreaAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.bean.Person;
import com.huadin.database.City;
import com.huadin.util.LogUtil;
import com.huadin.util.PullUtil;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/1/10.
 * 设置个人预警地址
 */

public class AddressFragment extends BaseFragment implements AddressContract.View
{
  @BindView(R.id.address_area_id)
  Spinner mAddressSpinner;
  @BindView(R.id.address_msg)
  EditText mAddressDetailed;

  private static final String TAG = "AddressFragment";
  private AddressContract.Presenter mPresenter;
  private List<City> mCityList;

  public static AddressFragment newInstance()
  {

    Bundle args = new Bundle();

    AddressFragment fragment = new AddressFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);

    mCityList = DataSupport.findAll(City.class);
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
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.address_fragment_layout);
    ButterKnife.bind(this, view);
    LogUtil.i(TAG, "mCityList = " + mCityList);
    initAdapter();
    return view;
  }

  private void initAdapter()
  {
    AreaAdapter areaAdapter = new AreaAdapter(mContext, mCityList);
    mAddressSpinner.setAdapter(areaAdapter);

    //显示服务器上的数据
    Person person = BmobUser.getCurrentUser(Person.class);
    String areaName = person.getAreaName();
    String address = person.getAddress();
    if (!TextUtils.isEmpty(areaName))
    {
      for (int i = 0; i < mCityList.size(); i++)
      {
        if (areaName.equals(mCityList.get(i).getAreaName()))
        {
          mAddressSpinner.setSelection(i);
        }
      }
      mAddressDetailed.setText(address);
    }
  }

  @Override
  public City getCity()
  {
    int position = mAddressSpinner.getSelectedItemPosition();
    return mCityList.get(position);
  }

  @Override
  public String getDetailedAddress()
  {
    return mAddressDetailed.getText().toString();
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.register_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    // TODO: 2017/1/11 成功后启动预警服务,覆盖掉设置中的预警地址
    //保存成功
    showMessage(R.string.address_submit_success);
    mContext.finish();
  }

  @Override
  public void updateError(int errorId)
  {
    //保存异常
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
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @OnClick(R.id.address_submit)
  public void onClick()
  {
    mPresenter.start();
  }
}
