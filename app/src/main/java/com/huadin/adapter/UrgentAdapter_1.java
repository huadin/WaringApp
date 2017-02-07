package com.huadin.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadin.bean.ReleaseBean;
import com.huadin.waringapp.R;

import java.util.List;

/**
 * Created by 潇湘 on 2017/2/7.
 * 用户查看紧急信息
 */

public class UrgentAdapter_1 extends BaseAdapter<ReleaseBean>
{
  public UrgentAdapter_1(Context context, List<ReleaseBean> datas, int layoutId)
  {
    super(context, datas, layoutId);
  }

  @Override
  public void convert(ViewHolder holder, ReleaseBean releaseBean, final int position)
  {
    TextView tvTitle = holder.getView(R.id.urgent_text_title);
    TextView tvTime = holder.getView(R.id.urgent_text_time);
    tvTitle.setText(releaseBean.getReleaseTitle());
    tvTime.setText(releaseBean.getCreatedAt());

    LinearLayout layout = holder.getView(R.id.urgent_layout);
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
