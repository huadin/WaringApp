package com.huadin.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.huadin.waringapp.R;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 确认提示 dialog
 */

public class PromptFragment extends DialogFragment
{
  private static final String MESSAGE_KEY = "title_key";
  private PromptListener mListener;

  /**
   * @param message 标题
   * @return dialogFragment
   */
  public static PromptFragment newInstance(@Nullable String message)
  {

    Bundle args = new Bundle();
    args.putString(MESSAGE_KEY,message);
    PromptFragment fragment = new PromptFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    Bundle args = getArguments();
    String message = args.getString(MESSAGE_KEY);

    AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
    dialog.setMessage(message);
    dialog.setTitle(R.string.permission_dialog_title);
    dialog.setIcon(R.drawable.icon_dialog_prompt);

    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.string_ok), new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        if (mListener != null) mListener.promptOk();
      }
    });

    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.string_cancel), new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        dialog.cancel();
      }
    });

    dialog.show();
    return dialog;
  }

  public interface PromptListener
  {
    void promptOk();
  }

  public void setOnPromptListener(PromptListener listener)
  {
    this.mListener = listener;
    mListener = checkNotNull(listener, "PromptListener cannot be null");
  }


}
