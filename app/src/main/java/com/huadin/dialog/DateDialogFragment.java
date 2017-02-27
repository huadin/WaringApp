package com.huadin.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.huadin.eventbus.EventCenter;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by NCL on 2017/2/25.
 * 时间选择器
 */

public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
  DatePickerDialog mDialog;
  private String mDate;


  public static DateDialogFragment newInstance()
  {

    Bundle args = new Bundle();

    DateDialogFragment fragment = new DateDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    if (mDialog == null)
    {
      mDialog = new DatePickerDialog(getContext(), this, year, month, day)
      {
        @Override
        protected void onStop()
        {
          //重新改方法,防止 onDateSet 调用两次
        }
      };
    }

    return mDialog;
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int day)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(year);
    sb.append("-");
    sb.append(intToString(month + 1));
    sb.append("-");
    sb.append(intToString(day));

    mDate = sb.toString();
    sb.delete(0, sb.length());
  }

  private String intToString(int number)
  {
    return number < 10 ? String.valueOf("0" + number) : String.valueOf(number);
  }


  @Override
  public void onDestroy()
  {
    super.onDestroy();
    EventCenter<String> eventCenter = new EventCenter<>(EventCenter.EVENT_CODE_START_DATE, mDate);
    EventBus.getDefault().post(eventCenter);
  }
}
