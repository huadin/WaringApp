package com.huadin.setting.contact;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.dialog.PermissionDialogFragment;
import com.huadin.dialog.WeChatFragment;
import com.huadin.permission.PermissionListener;
import com.huadin.permission.PermissionManager;
import com.huadin.waringapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 潇湘 on 2017/2/10.
 * 联系我们
 */

public class ContactFragment extends BaseFragment implements PermissionListener
{

  private PermissionManager mPermissionManager;
  private int PERMISSION_CODE = 0x72;

  public static ContactFragment newInstance()
  {

    Bundle args = new Bundle();

    ContactFragment fragment = new ContactFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {

    View view = getViewResId(inflater, container, R.layout.contact_fragment);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick({R.id.contact_layout_http, R.id.contact_layout_phone,
          R.id.contact_weiBo_layout, R.id.contact_weiXi_layout})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.contact_layout_http:
        visitWebsite();
        break;
      case R.id.contact_layout_phone:
        callPhone();
        break;
      case R.id.contact_weiBo_layout:
        visitSina();
        break;
      case R.id.contact_weiXi_layout:
        visitWeChat();
        break;
    }
  }

  //官网
  private void visitWebsite()
  {
    String uri = getString(R.string.website_http);
    String action = getString(R.string.action_view);
    visitStart(uri, action);
  }

  //微博
  private void visitSina()
  {
    String uri = getString(R.string.sina_http);
    String action = getString(R.string.action_view);
    visitStart(uri, action);
  }

  //打电话
  private void callPhone()
  {
    //检查权限
    mPermissionManager = PermissionManager.with(this)
            .permissions(Manifest.permission.CALL_PHONE)
            .addRequestCode(PERMISSION_CODE)
            .setPermissionListener(this)
            .request();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
  {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_CODE)
    {
      mPermissionManager.onPermissionResult(permissions, grantResults);
    }
  }

  //微信
  private void visitWeChat()
  {
    //dialog显示二维码
    WeChatFragment dialogFragemnt = WeChatFragment.newInstance();
    dialogFragemnt.show(getFragmentManager(), getClass().getSimpleName());
  }

  @Override
  public void onGranted()
  {
    //权限通过
    String callPhone = getString(R.string.call_phone_number);
    String action = Intent.ACTION_CALL;
    visitStart(callPhone, action);
  }

  @Override
  public void onShowRationale(String permissions)
  {
    String rationale = getString(R.string.call_phone_rationale);
    PermissionDialogFragment dialogFragment = PermissionDialogFragment.newInstance(rationale);
    dialogFragment.show(getFragmentManager(), getClass().getSimpleName());
  }

  /**
   * 启动访问
   *
   * @param uri    uri
   * @param action action
   */
  private void visitStart(String uri, String action)
  {
    Uri httpUri = Uri.parse(uri);
    Intent intent = new Intent();
    intent.setAction(action);
    intent.setData(httpUri);
    startActivity(intent);
  }
}
