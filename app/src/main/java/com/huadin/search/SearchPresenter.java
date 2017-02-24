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
  }

  @Override
  public void start()
  {
    //先获取本地，本地没有获取网络
    City city = mSearchView.getArea();
    String scope = mSearchView.getScope().trim();//可以为空
    String startDate = mSearchView.getStartDate().trim();
    String orgCode = mSearchView.getType();
    String areaId;
    int errorId = 0;

    if (city == null)
    {
      errorId = R.string.search_city_error;
    } else if (TextUtils.isEmpty(orgCode))
    {
      errorId = R.string.search_org_code_error;
    } else if (TextUtils.isEmpty(startDate))
    {
      errorId = R.string.search_start_error;
    }

    if (errorId != 0)
    {
      mSearchView.searchError(errorId);
      return;
    }

    areaId = city.getAreaId();


  }

}
