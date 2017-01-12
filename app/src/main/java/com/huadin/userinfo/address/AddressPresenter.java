package com.huadin.userinfo.address;

import com.huadin.bean.Person;
import com.huadin.database.City;
import com.huadin.database.WaringAddress;
import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

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
  private String mAddressDetailed;
  private String mAreaName;
  private String mAreaId;
  /* 如果为本地保存预警地址，isLocal = 1 */
  private String isLocal;

  public AddressPresenter(AddressContract.View addressView)
  {
    mAddressView = addressView;
    mAddressView = checkNotNull(addressView, "addressView cannot be null");
    mAddressView.setPresenter(this);
  }

  //网络保存个人信息中预警地址
  @Override
  public void start()
  {
    if (getCityInfo()) return;

    isLocal = "0";
    Person person = new Person();
    person.setAreaName(mAreaName);
    person.setAreaId(mAreaId);
    person.setAddress(mAddressDetailed);

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
          //保存本地
          saveAddress();

          mAddressView.updateSuccess();
        } else
        {
          int code = e.getErrorCode();
          showCode(code);
        }
      }
    });
  }

  //设置中预警地址,保存在本地
  @Override
  public void saveLocalAddress()
  {
    if (getCityInfo()) return;
    isLocal = "1";
    saveAddress();
    mAddressView.updateSuccess();
  }

  private boolean getCityInfo()
  {
    mAddressDetailed = mAddressView.getDetailedAddress();
    City city = mAddressView.getCity();
    mAreaName = city.getAreaName();
    mAreaId = city.getAreaId();
    boolean isNetwork = mAddressView.networkState();
    int errorId = 0;

    if (AMUtils.isEmpty(mAreaId))
    {
      errorId = R.string.select_area_id;
    } else if (AMUtils.isEmpty(mAddressDetailed))
    {
      errorId = R.string.detailed_address;
    } else if (!isNetwork)
    {
      errorId = R.string.no_network;
    }

    if (errorId != 0)
    {
      mAddressView.updateError(errorId);
      return true;
    }
    return false;
  }

  //保存
  private void saveAddress()
  {
    WaringAddress address = DataSupport.where("isLocal = ?", String.valueOf(isLocal)).findFirst(WaringAddress.class);
    if (address == null)
    {
      address = new WaringAddress();
    }
    address.setWaringArea(mAreaName);
    address.setWaringAddress(mAddressDetailed);
    address.setIsLocal(isLocal);
    address.save();
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
