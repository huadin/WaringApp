package com.huadin.userinfo.fault;

import com.huadin.userinfo.UpdateContract;

/**
 * Created by 潇湘 on 2017/1/17.
 *
 */

public interface FaultContract
{
  interface Presenter extends UpdateContract.Presenter
  {

  }

  interface View extends UpdateContract.View<Presenter>
  {

  }
}
