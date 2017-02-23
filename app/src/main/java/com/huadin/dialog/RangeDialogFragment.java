package com.huadin.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huadin.waringapp.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/2/23.
 * 重置范围 DialogFragment
 */

public class RangeDialogFragment extends DialogFragment
{
  private OnRangeClickListener mListener;

  public static RangeDialogFragment newInstance()
  {
    Bundle args = new Bundle();
    RangeDialogFragment fragment = new RangeDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    View view = View.inflate(getActivity(), R.layout.range_dialog_fragment, null);
    final EditText rangeET = (EditText) view.findViewById(R.id.range_dialog_edit);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(view)
            .setTitle(R.string.range_dialog_fragment_dialot_title)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialogInterface, int i)
              {

                if (TextUtils.isEmpty(rangeET.getText().toString()))
                {
                  mListener.rangeError();
                  return;
                }
                double range = Double.valueOf(rangeET.getText().toString()) * 1000L;
                if (range < 1000)
                {
                  mListener.rangeError();
                } else
                {
                  mListener.resetRange(range);
                }
              }
            })
            .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialogInterface, int i)
              {

              }
            });
    return builder.create();
  }

  public interface OnRangeClickListener
  {
    /**
     * 重置范围
     *
     * @param range Double
     */
    void resetRange(double range);

    /**
     * 输入的范围不合理
     */
    void rangeError();
  }

  public void setOnRangeClickListener(OnRangeClickListener listener)
  {
    mListener = listener;
    mListener = checkNotNull(listener, "OnRangeClickListener cannot be null");
  }


}
