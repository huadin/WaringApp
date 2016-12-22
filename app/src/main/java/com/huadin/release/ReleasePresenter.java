package com.huadin.release;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 发布控制器
 */

public class ReleasePresenter implements ReleaseContract.Presenter
{
  private ReleaseContract.View mReleaseView;

  public ReleasePresenter(ReleaseContract.View releaseView)
  {
    this.mReleaseView = releaseView;
    mReleaseView = checkNotNull(releaseView,"release cannot be null");
    mReleaseView.setPresenter(this);
  }

  @Override
  public void start()
  {

  }

}
