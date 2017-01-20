package com.huadin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadin.bean.ReportBean;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/18.
 * 查看停电报修
 */

public class FaultAdapter extends RecyclerView.Adapter<FaultAdapter.FaultViewHolder>
{
  private static final int TYPE_LOAD_MORE = -1;//加载更多
  private static final int STATUS_END = -2;
  public static final int STATUS_READY = -3;
  public static final int STATUS_START = -4;
  private List<ReportBean> mBeanList;
  private OnItemClickListener mListener;
  private View mFooterView;
  private int mStatus = -3;
  private static final String TAG = "FaultAdapter";


  public FaultAdapter(List<ReportBean> beanList)
  {
    mBeanList = beanList;
    mBeanList = checkNotNull(beanList, "beanList cannot be null");
  }

  public void setFooterView(View footerView)
  {
    mFooterView = footerView;
  }

  @Override
  public FaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fault_adapter_item, parent, false);

    if (viewType == TYPE_LOAD_MORE) return new FaultViewHolder(mFooterView);

    return new FaultViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final FaultViewHolder holder, int position)
  {

    if (getItemViewType(position) == TYPE_LOAD_MORE)
    {
      bindFooterView (holder);
      return;
    }

    holder.titleTV.setText(mBeanList.get(position).getReportTitle());
    holder.timeTV.setText(mBeanList.get(position).getCreatedAt());
    boolean isRead = mBeanList.get(position).isRead();
    if (isRead)
    {
      //已读
      holder.readIV.setVisibility(View.GONE);
    } else
    {
      //未读
      holder.readIV.setVisibility(View.VISIBLE);
    }

    holder.linearLayout.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (mListener != null)
        {
          mListener.onItemClick(holder.getAdapterPosition());
        }
      }
    });
  }

  private void bindFooterView(FaultViewHolder holder)
  {
    switch (mStatus)
    {
      case STATUS_READY:
        holder.itemView.findViewById(R.id.footer_layout).setVisibility(View.GONE);
        holder.itemView.setVisibility(View.GONE);
        break;

      case STATUS_START:
        holder.itemView.findViewById(R.id.footer_layout).setVisibility(View.VISIBLE);
        holder.itemView.setVisibility(View.VISIBLE);
        mStatus = STATUS_END;
        break;

      case STATUS_END:
        holder.itemView.findViewById(R.id.footer_layout).setVisibility(View.GONE);
        holder.itemView.setVisibility(View.GONE);
        mStatus = STATUS_READY;
        break;
    }
  }

  public int getLoadMoreStatus ()
  {
    return mStatus;
  }

  public void setLoadMoreStatus(int status)
  {
    mStatus = status;
//    notifyItemChanged(0);
    notifyDataSetChanged();
  }


  @Override
  public int getItemCount()
  {
    if (mBeanList.size() == 0) return 0;

    return mBeanList.size() + 1;
  }

  @Override
  public int getItemViewType(int position)
  {
    if (position == getItemCount() - 1) return TYPE_LOAD_MORE;

    return super.getItemViewType(position);
  }


  /**
   * 更新 adapter
   *
   * @param beanList List<ReportBean>
   */
  public void updateAdapter(List<ReportBean> beanList)
  {
    this.mBeanList = beanList;
    notifyDataSetChanged();
  }

  /**
   * 刷新时服务器没有数据时调用次方法
   */
  public void clearAdapterList()
  {
    mBeanList.clear();
    notifyDataSetChanged();
  }

  class FaultViewHolder extends RecyclerView.ViewHolder
  {

    TextView titleTV;
    TextView timeTV;
    ImageView readIV;
    LinearLayout linearLayout;

    FaultViewHolder(View itemView)
    {
      super(itemView);
      if (itemView == mFooterView) return;

      titleTV = (TextView) itemView.findViewById(R.id.fault_adapter_item_title);
      timeTV = (TextView) itemView.findViewById(R.id.fault_adapter_item_time);
      readIV = (ImageView) itemView.findViewById(R.id.fault_adapter_item_read);
      linearLayout = (LinearLayout) itemView.findViewById(R.id.fault_adapter_item_layout);
    }
  }

  public void setOnItemClickListener(OnItemClickListener listener)
  {
    this.mListener = listener;
    mListener = checkNotNull(listener, "listener cannot be null");
  }

  public interface OnItemClickListener
  {
    void onItemClick(int position);
  }
}
