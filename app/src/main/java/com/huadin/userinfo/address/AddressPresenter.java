package com.huadin.userinfo.address;

import com.huadin.bean.Person;
import com.huadin.database.City;
import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/1/10.
 * 预警地址,存储于网络
 */

public class AddressPresenter implements AddressContract.Presenter
{
  private AddressContract.View mAddressView;

  public AddressPresenter(AddressContract.View addressView)
  {
    mAddressView = addressView;
    mAddressView = checkNotNull(addressView, "addressView cannot be null");
    mAddressView.setPresenter(this);
  }

  @Override
  public void start()
  {
    String detailedAddress = mAddressView.getDetailedAddress();
    City city = mAddressView.getCity();
    String areaName = city.getAreaName();
    String areaId = city.getAreaId();
    boolean isNetwork = mAddressView.networkState();
    int errorId = 0;

    if (AMUtils.isEmpty(areaId))
    {
      errorId = R.string.select_area_id;
    } else if (AMUtils.isEmpty(detailedAddress))
    {
      errorId = R.string.detailed_address;
    } else if (!isNetwork)
    {
      errorId = R.string.no_network;
    }

    if (errorId != 0)
    {
      mAddressView.updateError(errorId);
      return;
    }

    Person person = new Person();
    person.setAreaName(areaName);
    person.setAreaId(areaId);
    person.setAddress(detailedAddress);

    Person currentPerson = Person.getCurrentUser(Person.class);
    mAddressView.showLoading();
    person.update(currentPerson.getObjectId(), new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {
        mAddressView.hindLoading();
        if (e == null)
        {
          mAddressView.updateSuccess();
        } else
        {
          int code = e.getErrorCode();
          showCode(code);
        }
      }
    });
  }

  private void showCode(int code)
  {
    switch (code)
    {
      case 9010:
        mAddressView.updateError(R.string.error_code_9010);
        break;
    }
  }
}
