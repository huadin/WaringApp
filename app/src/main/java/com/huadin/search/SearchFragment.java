package com.huadin.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.huadin.adapter.AreaAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.database.City;
import com.huadin.database.StopPowerBean;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/2/24.
 * 搜索停电信息
 */

public class SearchFragment extends BaseFragment implements SearchContract.View
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.search_spinner_area)
  Spinner mSpinnerArea;
  @BindView(R.id.search_spinner_type)
  Spinner mSpinnerType;
  @BindView(R.id.search_start_date)
  TextView mStartDate;
  @BindView(R.id.search_scope)
  ClearEditText mScope;
  private SearchContract.Presenter mPresenter;
  private String[] scopes = {"计划停电", "故障停电", "临时停电"};
  private List<City> mCityList;


  public static SearchFragment newInstance()
  {

    Bundle args = new Bundle();

    SearchFragment fragment = new SearchFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.search_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbarHome(mToolbar, R.string.blackout_search, mContext);
    initAdapter();
    return view;
  }

  private void initAdapter()
  {
    //地区
    mCityList = DataSupport.findAll(City.class);
    AreaAdapter areaAdapter = new AreaAdapter(mContext, mCityList);
    mSpinnerArea.setAdapter(areaAdapter);

    //类型
    ArrayAdapter<String> scopeAdapter = new ArrayAdapter<>(mContext,
            R.layout.support_simple_spinner_dropdown_item, scopes);
    mSpinnerType.setAdapter(scopeAdapter);
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.search_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void searchSuccess(List<StopPowerBean> powerBeanList)
  {
    //  查询成功去另一页面展示
  }

  @Override
  public void searchError(int errorResId)
  {
    //  异常
    showMessage(errorResId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public String getStartDate()
  {
    return mStartDate.getText().toString();
  }

  @Override
  public City getArea()
  {
    int pos = mSpinnerArea.getSelectedItemPosition();

    return mCityList.get(pos);
  }

  @Override
  public String getScope()
  {
    return mScope.getText().toString();
  }

  @Override
  public String getType()
  {
    int pos = mSpinnerType.getSelectedItemPosition();
    return scopes[pos];
  }

  @Override
  public void setPresenter(SearchContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "SearchContract.Presenter cannot be null");
  }


  @OnClick({R.id.search_start_date, R.id.search_submit})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.search_start_date:
        //时间选择器
        break;
      case R.id.search_submit:
        mPresenter.start();
        break;
    }
  }
}
