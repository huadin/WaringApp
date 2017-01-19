package com.huadin.urgent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.adapter.UrgentAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 故障信息，查看管理人员发布的紧急信息
 */
public class UrgentFragment extends BaseFragment implements UrgentContract.View
{

  private UrgentContract.Presenter mPresenter;
  private List<String> mList;

  @BindView(R.id.urgent_recycler)
  RecyclerView mRecyclerView;
  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;

  public static UrgentFragment newInstance()
  {
    return new UrgentFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    initData();
  }

  private void initData()
  {
    mList = new ArrayList<>();

    for (int i = 0; i < 100; i++)
    {
      mList.add(String.valueOf(i + 1));
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.urgemt_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbarHome(mToolbar,R.string.fault_info,getActivity());

    UrgentAdapter adapter = new UrgentAdapter(mList);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.setAdapter(adapter);
    return view;
  }

  @Override
  public void showLoading()
  {

  }

  @Override
  public void hindLoading()
  {

  }

  @Override
  public void success()
  {

  }

  @Override
  public void error(int errorId)
  {

  }

  @Override
  public void setPresenter(UrgentContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }
}
