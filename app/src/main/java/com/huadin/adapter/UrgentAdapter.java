package com.huadin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.waringapp.R;

import java.util.List;

/**
 * 紧急信息 adapter
 */

public class UrgentAdapter extends RecyclerView.Adapter<UrgentAdapter.UrgentViewHolder>
{

  private List<String> mList;

  public UrgentAdapter(List<String> mList)
  {
    this.mList = mList;
  }


  @Override
  public UrgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.urgent_item_adapter, parent, false);

    return new UrgentViewHolder(view);
  }

  @Override
  public void onBindViewHolder(UrgentViewHolder holder, int position)
  {
    holder.tv.setText(mList.get(position));
  }


  @Override
  public int getItemCount()
  {
    return mList.size();
  }

  static class UrgentViewHolder extends RecyclerView.ViewHolder
  {
    TextView tv;

    public UrgentViewHolder(View itemView)
    {
      super(itemView);
      tv = (TextView) itemView.findViewById(R.id.urgent_text_view);
    }
  }
}
