package com.huadin.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.huadin.waringapp.R;

/**
 * Created by 潇湘 on 2017/2/13.
 * 显示微信
 */

public class WeChatFragment extends DialogFragment
{
  public static WeChatFragment newInstance()
  {

    Bundle args = new Bundle();

    WeChatFragment fragment = new WeChatFragment();
    fragment.setArguments(args);
    return fragment;
  }

//  @Override
//  public void onCreate(@Nullable Bundle savedInstanceState)
//  {
//    super.onCreate(savedInstanceState);
//    setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    final Window window = getDialog().getWindow();
    //需要用android.R.id.content这个view
    View view = inflater.inflate(R.layout.we_chat, ((ViewGroup) window.findViewById(android.R.id.content)), false);
    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
//    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    window.setLayout(480, 480);
    return view;
  }

//  @NonNull
//  @Override
//  public Dialog onCreateDialog(Bundle savedInstanceState)
//  {
//    View view = View.inflate(getActivity(), R.layout.we_chat, null);
//
//    AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
//    dialog.setView(view);
//    dialog.show();
//    return dialog;
//  }
}
