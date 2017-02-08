package com.huadin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class ViewHolder extends RecyclerView.ViewHolder
{
  private SparseArray<View> mViews;
  private View mCovertView;

  ViewHolder(View itemView)
  {
    super(itemView);
    mCovertView = itemView;
    mViews = new SparseArray<>();
  }

  public static ViewHolder get(Context context, ViewGroup parent, int layoutId)
  {
    View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
    return new ViewHolder(view);
  }

  <T extends View> T getView(int viewId)
  {
    View view = mViews.get(viewId);
    if (view == null)
    {
      view = mCovertView.findViewById(viewId);
      mViews.put(viewId, view);
    }
    return (T) view;
  }

}
