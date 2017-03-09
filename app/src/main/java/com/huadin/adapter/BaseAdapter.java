package com.huadin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huadin.waringapp.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{
  private static final int TYPE_LOAD_MORE = -1;//加载更多
  private static final int STATUS_END = -2;
  private static final int STATUS_READY = -3;
  private static final int STATUS_START = -4;
  private Context mContext;
  private List<T> mDatas;
  private int mStatus = -3;
  private int mLayoutId;
  onItemClickListener mListener;
  onItemLongClickListener mLongClickListener;

  BaseAdapter(Context context, List<T> datas, int layoutId)
  {
    mDatas = datas;
    mContext = context;
    mLayoutId = layoutId;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {

    if (viewType == TYPE_LOAD_MORE)
    {
      View footerView = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_footer, parent, false);
      return new ViewHolder(footerView);
    }

    return ViewHolder.get(mContext, parent, mLayoutId);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position)
  {
    if (getItemViewType(position) == TYPE_LOAD_MORE)
    {
      bindFooterView(holder);
      return;
    }

    convert(holder, mDatas.get(position), position);
  }

  @Override
  public int getItemCount()
  {
    if (mDatas.size() == 0) return 0;

    return mDatas.size() + 1;
  }

  public abstract void convert(ViewHolder holder, T t, int position);


  private void bindFooterView(ViewHolder holder)
  {
    View view = holder.itemView.findViewById(R.id.footer_layout);
    TextView tv = (TextView) holder.itemView.findViewById(R.id.footer_tv);
    ProgressBar bar = (ProgressBar) holder.itemView.findViewById(R.id.footer_progress);
    switch (mStatus)
    {
      case STATUS_READY:
        if (mDatas.size() < 15)
        {
          holder.itemView.setVisibility(View.GONE);
          view.setVisibility(View.GONE);
        } else
        {
          view.setVisibility(View.VISIBLE);
          bar.setVisibility(View.GONE);
          tv.setText(mContext.getString(R.string.loading_ready));
        }
//        mStatus = STATUS_START;
        break;
      case STATUS_START:
        view.setVisibility(View.VISIBLE);
        bar.setVisibility(View.VISIBLE);
        tv.setText(mContext.getString(R.string.loading_start));
        mStatus = STATUS_END;
        break;

      case STATUS_END:
        view.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
        tv.setText(mContext.getString(R.string.loading_end));
        mStatus = STATUS_READY;
        break;
    }
  }

  @Override
  public int getItemViewType(int position)
  {
    if (position == getItemCount() - 1) return TYPE_LOAD_MORE;

    return super.getItemViewType(position);
  }

  public int getLoadMoreStatus()
  {
    return mStatus;
  }

  public void setLoadMoreStatus(int status)
  {
    mStatus = status;
    notifyDataSetChanged();
  }

  public void updateAdapter(List<T> datas)
  {
    mDatas.clear();
    mDatas.addAll(datas);
    notifyDataSetChanged();
  }

//  public void setFooterView(View footerView)
//  {
//    mFooterView = footerView;
//  }

  public interface onItemClickListener
  {
    void onItemClick(int pos);
  }

  public interface onItemLongClickListener
  {
    void onItemLongClick(int position);
  }

  public void setOnItemClickListener(onItemClickListener listener)
  {
    mListener = listener;
    mListener = checkNotNull(listener, "listener cannot be null");
  }

  public void setOnItemLongClickListener(onItemLongClickListener longClickListener)
  {
    mLongClickListener = longClickListener;
    mLongClickListener = checkNotNull(longClickListener, "onItemLongClickListener cannot be null");
  }

  public void clearAdapterList()
  {
    mDatas.clear();
    notifyDataSetChanged();
  }
}
