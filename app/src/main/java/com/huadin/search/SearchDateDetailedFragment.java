package com.huadin.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.adapter.SearchDateAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.database.StopPowerBean;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NCL on 2017/2/27.
 * 搜索数据详细展示
 */

public class SearchDateDetailedFragment extends BaseFragment
{
  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.search_date_detailed_recycler)
  RecyclerView mRecyclerView;


  private static final String KEY_POWER_BEAN = "POWER_BEAN";

  public static SearchDateDetailedFragment newInstance(List<StopPowerBean> powerBeanList)
  {

    Bundle args = new Bundle();
    ArrayList<StopPowerBean> arrayList = new ArrayList<>();
    arrayList.clear();
    arrayList.addAll(powerBeanList);
    args.putParcelableArrayList(KEY_POWER_BEAN, arrayList);
    SearchDateDetailedFragment fragment = new SearchDateDetailedFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.search_date_detailed_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.search_date_result, false);
    initAdapter();
    return view;
  }

  private void initAdapter()
  {
    Bundle args = getArguments();
    List<StopPowerBean> beanList = args.getParcelableArrayList(KEY_POWER_BEAN);
    SearchDateAdapter adapter = new SearchDateAdapter(mContext, beanList, R.layout.search_date_adapter_item);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.setAdapter(adapter);
  }


}
