package com.huadin.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.adapter.MessageAdapter;
import com.huadin.base.BaseFragment;
import com.huadin.database.StopPowerBean;
import com.huadin.database.WaringAddress;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NCL on 2017/3/6.
 * 消息通知
 */

public class MessageFragment extends BaseFragment
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.message_recycler)
  RecyclerView mRecyclerView;

  public static MessageFragment newInstance()
  {

    Bundle args = new Bundle();

    MessageFragment fragment = new MessageFragment();
    fragment.setArguments(args);
    return fragment;
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
    View view = getViewResId(inflater, container, R.layout.fragment_message_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar,R.string.message_notify,false);
    initAdapter();
    return view;
  }

  private void initAdapter()
  {
    WaringAddress waringAddress = DataSupport.findFirst(WaringAddress.class);
    if (waringAddress == null) return;
    String areaId = waringAddress.getWaringAreaId();
    String scope = waringAddress.getWaringAddress();

    List<StopPowerBean> beanList = DataSupport
            .where("scope like ? and orgCode = ?", "%" + scope + "%", areaId)
            .find(StopPowerBean.class);

    MessageAdapter adapter = new MessageAdapter(mContext,beanList,R.layout.search_date_adapter_item);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mRecyclerView.setAdapter(adapter);
  }

}
