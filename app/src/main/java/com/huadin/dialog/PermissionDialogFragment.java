package com.huadin.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.huadin.waringapp.R;

/**
 * 弹出解释权限的dialog
 */

public class PermissionDialogFragment extends DialogFragment
{

  private OnPermissionListener listener;
  private static final String PERMISSION_DIALOG_KEY = "PERMISSION_DIALOG_KEY";

  public static PermissionDialogFragment newInstance(String title)
  {
    Bundle args = new Bundle();
    args.putString(PERMISSION_DIALOG_KEY, title);
    PermissionDialogFragment dialogFragment = new PermissionDialogFragment();
    dialogFragment.setArguments(args);
    return dialogFragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    Bundle bundle = getArguments();
    String permissionTitleValue = bundle.getString(getString(R.string.permission_dialog_key));
    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
    dialog.setTitle(R.string.permission_dialog_title);
    dialog.setMessage(permissionTitleValue);

    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.permission_dialog_positive),
            new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialogInterface, int which)
              {
                if (listener != null)
                {
                  listener.dialogPositive();
                }
              }
            });

    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.permission_dialog_negative),
            new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialogInterface, int which)
              {
                dialog.cancel();
              }
            });

    dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失

    return dialog;
  }

  public interface OnPermissionListener
  {
    void dialogPositive();
  }

  public void setOnPermissionListener(OnPermissionListener listener)
  {
    this.listener = listener;
  }

}
