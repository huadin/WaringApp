package com.huadin.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.huadin.waringapp.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Snow on 2017/2/17.
 * 警告dialog
 */

public class WaringDialogFragment extends DialogFragment
{

  private static final String KEY_TITLE = "TITLE";
  private static final String KEY_MESSAGE = "MESSAGE";
  private onWaringDialogListener mListener;

  public static WaringDialogFragment newInstance(int titleResId, int messageResId)
  {

    Bundle args = new Bundle();
    args.putInt(KEY_TITLE, titleResId);
    args.putInt(KEY_MESSAGE, messageResId);

    WaringDialogFragment fragment = new WaringDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    Bundle args = getArguments();
    int titleResId = args.getInt(KEY_TITLE);
    int messageResId = args.getInt(KEY_MESSAGE);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(titleResId)
            .setMessage(messageResId)
            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialog, int which)
              {
                if (mListener != null)
                {
                  mListener.onWaringDialog();
                }
              }
            })
            .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialog, int which)
              {

              }
            });

    return builder.create();
  }

  public interface onWaringDialogListener
  {
    /**
     * 警告 dialog 回调
     */
    void onWaringDialog();
  }

  public void setOnWaringDialogListener(onWaringDialogListener listener)
  {
    mListener = listener;
    mListener = checkNotNull(listener, "onWaringDialogListener cannot be null");
  }
}
