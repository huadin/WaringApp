package com.huadin.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.huadin.bean.Person;
import com.huadin.waringapp.R;

import java.util.List;

/**
 * Created by 潇湘 on 2017/2/9.
 * 人员管理
 */

public class PersonAdapter extends BaseAdapter<Person>
{
  public PersonAdapter(Context context, List<Person> datas, int layoutId)
  {
    super(context, datas, layoutId);
  }

  @Override
  public void convert(ViewHolder holder, Person person, final int position)
  {
    TextView personTV = holder.getView(R.id.person_text);
    personTV.setText(person.getAreaName());
    personTV.setOnClickListener(new View.OnClickListener()
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

    personTV.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v)
      {
        if (mLongClickListener != null)
        {
          mLongClickListener.onItemLongClick(position);
        }
        return false;
      }
    });
  }
}
