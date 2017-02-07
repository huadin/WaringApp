package com.huadin.adapter;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadin.bean.ReportBean;
import com.huadin.waringapp.R;

import java.util.List;

/**
 * 管理员故障信息查询
 */
public class FaultAdapter_1 extends BaseAdapter<ReportBean>
{

  public FaultAdapter_1(Context context, List<ReportBean> datas, int layoutId)
  {
    super(context, datas, layoutId);
  }

  @Override
  public void convert(ViewHolder holder, ReportBean reportBean, final int position)
  {

    TextView tvTitle = holder.getView(R.id.fault_adapter_item_title);
    TextView tvTime = holder.getView(R.id.fault_adapter_item_time);
    tvTitle.setText(reportBean.getReportTitle());
    tvTime.setText(reportBean.getCreatedAt());

    LinearLayout layout = holder.getView(R.id.fault_adapter_item_layout);
    layout.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (mListener != null)
        {
          mListener.onItemClick(position);
        }
      }
    });
  }

}
