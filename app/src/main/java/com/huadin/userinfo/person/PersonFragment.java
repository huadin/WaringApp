package com.huadin.userinfo.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.adapter.BaseAdapter;
import com.huadin.adapter.PersonAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.bean.Person;
import com.huadin.userinfo.LoadMoreOnScrollListener;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/2/9.
 * 用户管理
 */

public class PersonFragment extends BaseFragment implements PersonContract.View, SwipeRefreshLayout.OnRefreshListener,
        LoadMoreOnScrollListener.onLoadMore, Toolbar.OnMenuItemClickListener, BaseAdapter.onItemClickListener
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.fragment_recycler_view)
  RecyclerView mRecyclerView;
  @BindView(R.id.fragment_refresh)
  SwipeRefreshLayout mRefresh;
  @BindView(R.id.fragment_empty)
  TextView mEmpty;

  private PersonContract.Presenter mPresenter;
  private PersonAdapter mPersonAdapter;
  private List<Person> mPersonList;
  private String mPersonArea;//管理人员所在的区,默认为城区

  public static PersonFragment newInstance()
  {

    Bundle args = new Bundle();

    PersonFragment fragment = new PersonFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mPersonList = new ArrayList<>();
    mPersonArea = getString(R.string.area_11401);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.person_fragment);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.person_info, true);
    initToolbarMenu();
    initAdapter();
    return view;
  }

  private void initToolbarMenu()
  {
    mToolbar.inflateMenu(R.menu.person_area_menu);
    mToolbar.setOnMenuItemClickListener(this);

  }

  private void initAdapter()
  {

    mPersonAdapter = new PersonAdapter(mContext, mPersonList, R.layout.person_adapter_item);
    mPersonAdapter.setOnItemClickListener(this);
    LinearLayoutManager manager = new LinearLayoutManager(mContext);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setAdapter(mPersonAdapter);

    LoadMoreOnScrollListener listener = new LoadMoreOnScrollListener(manager);
    listener.setOnLoadMore(this);
    mRecyclerView.addOnScrollListener(listener);

    mRefresh.setOnRefreshListener(this);

    mPresenter.start();
  }

  @Override
  public String getArea()
  {
    return mPersonArea;
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
    mRefresh.setRefreshing(false);
    mPersonAdapter.clearAdapterList();
    mEmpty.setVisibility(View.VISIBLE);
  }

  @Override
  public void querySuccess(List<Person> personList)
  {
    //查询成功回调
    mRefresh.setRefreshing(false);
    mEmpty.setVisibility(View.GONE);
    mPersonAdapter.updateAdapter(personList);
  }

  @Override
  public void updateError(int errorId)
  {
    //异常时回调
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(PersonContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "PersonPresenter cannot be null");
  }

  @Override
  public void onRefresh()
  {
    mPresenter.refresh();
  }

  @Override
  public void loadMore()
  {
    mPresenter.loadMore();
  }

  @Override
  public boolean onMenuItemClick(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.area_11401:
        mPersonArea = getString(R.string.area_11401);
        break;
      case R.id.area_11402:
        mPersonArea = getString(R.string.area_11402);
        break;
      case R.id.area_11403:
        mPersonArea = getString(R.string.area_11403);
        break;
      case R.id.area_11404:
        mPersonArea = getString(R.string.area_11404);
        break;
      case R.id.area_11405:
        mPersonArea = getString(R.string.area_11405);
        break;
      case R.id.area_11406:
        mPersonArea = getString(R.string.area_11406);
        break;
      case R.id.area_11407:
        mPersonArea = getString(R.string.area_11407);
        break;
      case R.id.area_11408:
        mPersonArea = getString(R.string.area_11408);
        break;
      case R.id.area_11409:
        mPersonArea = getString(R.string.area_11409);
        break;
      case R.id.area_11410:
        mPersonArea = getString(R.string.area_11410);
        break;
      case R.id.area_11411:
        mPersonArea = getString(R.string.area_11411);
        break;
      case R.id.area_11412:
        mPersonArea = getString(R.string.area_11412);
        break;
      case R.id.area_11414:
        mPersonArea = getString(R.string.area_11414);
        break;
      case R.id.area_11415:
        mPersonArea = getString(R.string.area_11415);
        break;
      case R.id.area_11416:
        mPersonArea = getString(R.string.area_11416);
        break;
    }

    mPresenter.start();
    return true;
  }

  @Override
  public void onItemClick(int pos)
  {
    showMessage(String.valueOf(pos));
  }
}
