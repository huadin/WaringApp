package com.huadin.adapter;

import android.content.Context;
import android.widget.TextView;

import com.huadin.database.StopPowerBean;
import com.huadin.waringapp.R;

import java.util.List;

/**
 * Created by NCL on 2017/2/27.
 * 展示搜索结果
 */

public class SearchDateAdapter extends BaseAdapter<StopPowerBean>
{
  public SearchDateAdapter(Context context, List<StopPowerBean> datas, int layoutId)
  {
    super(context, datas, layoutId);
  }

  @Override
  public void convert(ViewHolder holder, StopPowerBean stopPowerBean, int position)
  {
    TextView typeView = holder.getView(R.id.search_adapter_type);
    TextView dateView = holder.getView(R.id.search_adapter_date);
    TextView scopeView = holder.getView(R.id.search_adapter_scope);

    StringBuilder sb = new StringBuilder();

    sb.append("停电类型：");
    sb.append(stopPowerBean.getTypeCode());
    typeView.setText(sb.toString());
    sb.delete(0, sb.length());

    sb.append("停电时间：");
    sb.append(stopPowerBean.getDate());
    sb.append(" ");
    sb.append(stopPowerBean.getTime());
    dateView.setText(sb.toString());
    sb.delete(0, sb.length());

    sb.append("停电范围：");
    sb.append(stopPowerBean.getScope());
    scopeView.setText(sb.toString());
    sb.delete(0, sb.length());
  }


}
