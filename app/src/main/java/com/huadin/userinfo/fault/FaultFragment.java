package com.huadin.userinfo.fault;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.adapter.FaultAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.bean.ReportBean;
import com.huadin.util.LinearDecoration;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/17.
 * 查看用户提交的报修信息
 */

public class FaultFragment extends BaseFragment implements FaultContract.View, SwipeRefreshLayout.OnRefreshListener
{
  @BindView(R.id.fault_fragment_refresh)
  SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.fault_fragment_recycler_view)
  RecyclerView mRecyclerView;
  @BindView(R.id.fault_fragment_empty)
  TextView mEmpty;

  private FaultContract.Presenter mPresenter;
  private FaultAdapter mFaultAdapter;

  public static FaultFragment newInstance()
  {
    return new FaultFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.fault_fragment_layout);
    ButterKnife.bind(this, view);
    initAdapter();
    return view;
  }

  private void initAdapter()
  {
    mRefreshLayout.setColorSchemeColors(Color.BLUE, Color.BLUE, Color.BLACK, Color.YELLOW);
    mRefreshLayout.setOnRefreshListener(this);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.addItemDecoration(new LinearDecoration(mContext, LinearDecoration.VERTICAL_LIST));

    mFaultAdapter = new FaultAdapter(new ArrayList<ReportBean>());
    mRecyclerView.setAdapter(mFaultAdapter);
    mPresenter.start();//开始获取数据
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.fault_date_get_in);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    //下拉刷新时,数据库没有数据时回调
    mRefreshLayout.setRefreshing(false);
    mFaultAdapter.clearAdapterList();
    mEmpty.setVisibility(View.VISIBLE);
  }

  @Override
  public void updateError(int errorId)
  {
    //获取失败
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(FaultContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @Override
  public void querySuccess(List<ReportBean> beanList)
  {
    mRefreshLayout.setRefreshing(false);

    //获取数据成功,关联到 Adapter
    if (beanList.size() == 0)
    {
      //下拉加载没有数据
      showMessage(R.string.fault_load_more_null);
      return;
    }
    mFaultAdapter.updateAdapter(beanList);
  }

  @Override
  public void onRefresh()
  {
    //下拉刷新
    mPresenter.refresh();
  }

  @OnClick(R.id.fault_adapter_temp)
  public void onClick()
  {
    mPresenter.loadMore();
  }
}
