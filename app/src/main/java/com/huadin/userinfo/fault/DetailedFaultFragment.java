package com.huadin.userinfo.fault;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.base.BaseFragment;
import com.huadin.bean.ReportBean;
import com.huadin.dialog.PermissionDialogFragment;
import com.huadin.dialog.PromptFragment;
import com.huadin.eventbus.EventCenter;
import com.huadin.permission.PermissionListener;
import com.huadin.permission.PermissionManager;
import com.huadin.userinfo.UpdateContract;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/20.
 * 停电报修详细信息
 */

public class DetailedFaultFragment extends BaseFragment implements DetailedFaultContract.View,
        Toolbar.OnMenuItemClickListener, PromptFragment.PromptListener, PermissionListener,
        PermissionDialogFragment.OnPermissionListener
{

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.detailed_fault_user)
  TextView mUser;
  @BindView(R.id.detailed_fault_address)
  TextView mAddress;
  @BindView(R.id.detailed_fault_content)
  TextView mContent;

  private ReportBean mReportBean;
  private UpdateContract.Presenter mPresenter;
  private PermissionManager mPermissionManager;
  private static final String KEY_POS = "POS";
  private static final String KEY_BEAN = "BEAN";
  private final int mPermissionCode = 0x14;//权限code
  private int mPosition;


  public static DetailedFaultFragment newInstance(ReportBean bean, int position)
  {
    Bundle args = new Bundle();
    args.putParcelable(KEY_BEAN, bean);
    args.putInt(KEY_POS, position);
    DetailedFaultFragment fragment = new DetailedFaultFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
//    _mActivity.setFragmentAnimator(new DefaultHorizontalAnimator());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {

    Bundle bundle = getArguments();
    mReportBean = bundle.getParcelable(KEY_BEAN);
    mPosition = bundle.getInt(KEY_POS);
    View view = getViewResId(inflater, container, R.layout.detailed_fault_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.detailed_fault_title, false);
    initMenu();
    initData();

    return view;
  }

  private void initMenu()
  {
    mToolbar.inflateMenu(R.menu.detailed_fault_menu);
    mToolbar.setOnMenuItemClickListener(this);
  }

  private void initData()
  {

    String userContent = getString(R.string.report_user_title) + mReportBean.getReportUser();
    String addressContent = getString(R.string.report_user_address)+ mReportBean.getReportUser();
    String content = getString(R.string.report_user_content) + mReportBean.getReportUser();

    mUser.setText(userContent);
    mAddress.setText(addressContent);
    mContent.setText(content);
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.report_submit_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    showMessage(R.string.submit_success);
    EventBus.getDefault().post(new EventCenter<>(EventCenter.EVENT_CODE_UPDATE_SUCCESS, mPosition));
    pop();
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(DetailedFaultContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @Override
  public String getObjectId()
  {
    return mReportBean == null ? "" : mReportBean.getObjectId();
  }

  @Override
  public ReportBean getReportBean()
  {
    return mReportBean;
  }


  @Override
  public boolean onMenuItemClick(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.detailed_fault_read:
        //标记为已处理
        PromptFragment fragment = PromptFragment.newInstance(getString(R.string.detailed_fault_sign_read));
        fragment.setOnPromptListener(this);
        fragment.show(getFragmentManager(), getClass().getSimpleName());
        break;

      case R.id.call_phone:
        //检查权限
        checkoutPermission();
        break;
    }
    return true;
  }

  private void checkoutPermission()
  {
    mPermissionManager = PermissionManager.with(this)
            .setPermissionListener(this)
            .addRequestCode(mPermissionCode)
            .permissions(Manifest.permission.CALL_PHONE)
            .request();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
  {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode)
    {
      case mPermissionCode:
        mPermissionManager.onPermissionResult(permissions, grantResults);
        break;
    }
  }

  /*已授权*/
  @Override
  public void onGranted()
  {
    String phone = mReportBean.getReportPhone();
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_CALL);
    intent.setData(Uri.parse("tel:" + phone));
    startActivity(intent);
  }

  @Override
  public void onShowRationale(String permissions)
  {
    PermissionDialogFragment fragment = PermissionDialogFragment.newInstance(getString(R.string.call_phone_rationale));
    fragment.setOnPermissionListener(this);
    fragment.show(getFragmentManager(), getClass().getSimpleName());
  }

  @Override
  public void promptOk()
  {
    mPresenter.start();
  }

  @Override
  public void dialogPositive()
  {
    settingPermission();
  }
}
