package com.huadin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huadin.database.City;
import com.huadin.waringapp.R;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/11.
 * 地区
 */

public class AreaAdapter extends BaseAdapter
{
  private List<City> mCityList;
  private LayoutInflater mInflater;

  public AreaAdapter(Context context, List<City> cityList)
  {
    mCityList = cityList;
    checkNotNull(context, "context cannot be null");
    mCityList = checkNotNull(cityList, "cityList cannot be null");
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount()
  {
    return mCityList.size();
  }

  @Override
  public Object getItem(int position)
  {
    return mCityList.get(position);
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    ViewHolder viewHolder;
    if (convertView == null)
    {
      convertView = mInflater.inflate(R.layout.area_adapter_item_layout, parent, false);
      viewHolder = new ViewHolder();
      viewHolder.areaTv = (TextView) convertView.findViewById(R.id.area_adapter_item_text);
      convertView.setTag(viewHolder);
    } else
    {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    City city = mCityList.get(position);
    viewHolder.areaTv.setText(city.getAreaName());
    return convertView;
  }

  private static class ViewHolder
  {
    TextView areaTv;
  }

}
