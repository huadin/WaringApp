package com.huadin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huadin.bean.ReportBean;
import com.huadin.waringapp.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/18.
 * 查看停电报修
 */

public class FaultAdapter extends RecyclerView.Adapter<FaultAdapter.FaultViewHolder>
{

  private List<ReportBean> mBeanList;

  public FaultAdapter(List<ReportBean> beanList)
  {
    mBeanList = beanList;
    mBeanList = checkNotNull(beanList, "beanList cannot be null");
  }

  @Override
  public FaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fault_adapter_item, parent, false);
    return new FaultViewHolder(view);
  }

  @Override
  public void onBindViewHolder(FaultViewHolder holder, int position)
  {
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
  }

  @Override
  public int getItemCount()
  {
    return mBeanList.size();
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

  static class FaultViewHolder extends RecyclerView.ViewHolder
  {

    TextView titleTV;
    TextView timeTV;
    ImageView readIV;

    public FaultViewHolder(View itemView)
    {
      super(itemView);
      titleTV = (TextView) itemView.findViewById(R.id.fault_adapter_item_title);
      timeTV = (TextView) itemView.findViewById(R.id.fault_adapter_item_time);
      readIV = (ImageView) itemView.findViewById(R.id.fault_adapter_item_read);
    }
  }
}
