package com.huadin.urgent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 请求数据
 */

public class UrgentPresenter implements UrgentContract.Presenter
{

  private UrgentContract.View mRepairView;

  public UrgentPresenter(UrgentContract.View repairView)
  {
    this.mRepairView = repairView;
    mRepairView = checkNotNull(repairView, "repairView cannot be null");
    mRepairView.setPresenter(this);
  }

  @Override
  public void start()
  {

  }
}
