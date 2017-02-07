package com.huadin.userinfo;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.huadin.adapter.BaseAdapter;
import com.huadin.adapter.FaultAdapter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/19.
 * 上拉加载更多
 */

public class LoadMoreOnScrollListener extends RecyclerView.OnScrollListener
{

  private RecyclerView.LayoutManager mLayoutManager;
  // 最后一个可见的 item
//  private int mLastVisibleItem;
  // 总的 item 个数
//  private int mTotalItem;
  private boolean isScrollDown;

  private onLoadMore mLoadMoreListener;


  public LoadMoreOnScrollListener(RecyclerView.LayoutManager layoutManager)
  {
    mLayoutManager = layoutManager;
  }

  @Override
  public void onScrollStateChanged(RecyclerView recyclerView, int newState)
  {
    super.onScrollStateChanged(recyclerView, newState);
    int mLastVisibleItem;
    BaseAdapter adapter = (BaseAdapter) recyclerView.getAdapter();
    if (newState == RecyclerView.SCROLL_STATE_IDLE)//滑动停止
    {
      mLayoutManager = recyclerView.getLayoutManager();
      if (mLayoutManager instanceof LinearLayoutManager)
      {
        //获取最后可见的 item
        mLastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
      } else
      {
        throw new RuntimeException("Unsupported LayoutManager used");
      }
      // 获取总的item
      int mTotalItem = mLayoutManager.getItemCount();

      if (adapter.getLoadMoreStatus() == FaultAdapter.STATUS_READY && mLastVisibleItem >= mTotalItem - 1 && isScrollDown)
      {
        //加载数据
        if (mLoadMoreListener != null)
        {
          adapter.setLoadMoreStatus(FaultAdapter.STATUS_START);
          mLoadMoreListener.loadMore();
        }
      }
    }
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy)
  {
    super.onScrolled(recyclerView, dx, dy);
    isScrollDown = dy > 0;
  }

  public void setOnLoadMore(onLoadMore listener)
  {
    mLoadMoreListener = listener;
    mLoadMoreListener = checkNotNull(listener, "listener cannot be null");
  }

  public interface onLoadMore
  {
    void loadMore();
  }

}
