package com.huadin.search;

import android.text.TextUtils;

import com.huadin.database.City;
import com.huadin.waringapp.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/2/24.
 * 搜索
 */

public class SearchPresenter implements SearchContract.Presenter
{
  private SearchContract.View mSearchView;

  public SearchPresenter(SearchContract.View searchView)
  {
    mSearchView = searchView;
    mSearchView = checkNotNull(searchView, "SearchContract.View cannot be null");
    mSearchView.setPresenter(this);
  }

  @Override
  public void start()
  {
    City city = mSearchView.getArea();
    String scope = mSearchView.getScope().trim();//可以为空
    String startDate = mSearchView.getStartDate().trim();
    String type = mSearchView.getType();
    boolean isNetwork = mSearchView.networkState();
    String areaId;
    int errorId = 0;

    if (city == null)
    {
      errorId = R.string.search_city_error;
    } else if (TextUtils.isEmpty(type))
    {
      errorId = R.string.search_org_code_error;
    } else if (TextUtils.isEmpty(startDate))
    {
      errorId = R.string.search_start_error;
    }else if (!isNetwork)
    {
      errorId = R.string.no_network;
    }

    if (errorId != 0)
    {
      mSearchView.searchError(errorId);
      return;
    }

    areaId = city.getAreaId();
    String typeCode = getTypeCode(type);
    mSearchView.showLoading();
    mSearchView.startSearchService(areaId, typeCode, startDate, scope);
  }


  @Override
  public void hindLoading()
  {
    mSearchView.hindLoading();
  }

  /**
   * 获取type类型编码
   *
   * @param type 停电类型
   * @return 01, 02, 07
   */
  private String getTypeCode(String type)
  {
    switch (type)
    {
      case "计划停电":
        type = "01";
        break;
      case "故障停电":
        type = "02";
        break;
      case "临时停电":
        type = "07";
        break;
    }
    return type;
  }
}
