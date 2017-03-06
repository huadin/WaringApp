package com.huadin.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.huadin.dialog.DateDialogFragment;
import com.huadin.eventbus.EventCenter;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

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
  private boolean isClick;//避免重复点击查询


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

  //  @Override
  private void searchSuccess(List<StopPowerBean> powerBeanList)
  {
    //  查询成功去另一页面展示
    LogUtil.i(LOG_TAG, "powerBeanList Size = " + powerBeanList.size());
    mPresenter.hindLoading();
    SearchDateDetailedFragment fragment = SearchDateDetailedFragment.newInstance(powerBeanList);
    start(fragment, SupportFragment.SINGLETASK);
  }

  @Override
  public void searchError(int errorResId)
  {
    //  异常
    showMessage(errorResId);
    isClick = false;
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
  public void startSearchService(String areaId, String type, String startTime, String scope)
  {
    startService(areaId, type, startTime, scope);
  }

  @Override
  public void setPresenter(SearchContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "SearchContract.Presenter cannot be null");
  }

  @Override
  protected void fragmentOnEvent(EventCenter eventCenter)
  {
    super.fragmentOnEvent(eventCenter);
    switch (eventCenter.getEventCode())
    {
      case EventCenter.EVENT_CODE_SEARCH_HTTP_DATA:
        List<StopPowerBean> beanList = (List<StopPowerBean>) eventCenter.getData();
        searchSuccess(beanList);
        isClick = false;
        break;
      case EventCenter.EVENT_CODE_START_DATE://选择日期
        String date = (String) eventCenter.getData();
        if (!TextUtils.isEmpty(mStartDate.getText().toString()))
        {
          mStartDate.setText("");
        }
        mStartDate.setText(date);
        break;
      case EventCenter.EVENT_CODE_SEARCH_NOT_HTTP_DATA://搜索是没有数据
        isClick = false;
        dismissLoading();
        showMessage(R.string.search_not_http_data);
        break;
    }
  }

  @OnClick({R.id.search_start_date, R.id.search_submit})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.search_start_date:
        //时间选择器
        DateDialogFragment dialogFragment = DateDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(), getClass().getSimpleName());
        break;
      case R.id.search_submit:
        if (!isClick)
        {
          isClick = true;
          mPresenter.start();
        }
        break;
    }
  }

}
