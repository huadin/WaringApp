package com.huadin.userinfo.address;

import com.huadin.database.City;
import com.huadin.userinfo.UpdateContract;

/**
 * Created by NCL on 2017/1/10.
 * 预警地址
 */

public interface AddressContract
{
  interface Presenter extends UpdateContract.Presenter
  {

  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 获取地区 ID
     *
     * @return String
     */
    City getCity();

    /**
     * 详细地址
     *
     * @return String
     */
    String getDetailedAddress();
  }
}
