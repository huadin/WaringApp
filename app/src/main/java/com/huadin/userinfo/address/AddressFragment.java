package com.huadin.userinfo.address;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.huadin.base.BaseFragment;
import com.huadin.util.PullUtil;
import com.huadin.waringapp.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/1/10.
 * 设置个人预警地址
 */

public class AddressFragment extends BaseFragment implements AddressContract.View
{
  @BindView(R.id.address_area_id)
  Spinner mAddressAreaId;
  @BindView(R.id.address_msg)
  EditText mAddressDetailed;
  private AddressContract.Presenter mPresenter;

  public static AddressFragment newInstance()
  {

    Bundle args = new Bundle();

    AddressFragment fragment = new AddressFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    try
    {
      InputStream inputStream = mContext.getAssets().open(getString(R.string.pull_xml));
      PullUtil.pullParser(inputStream);
      // TODO: 2017/1/11 没有关闭
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.address_fragment_layout);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public String getAreaId()
  {

    return null;
  }

  @Override
  public String getDetailedAddress()
  {
    return mAddressDetailed.getText().toString();
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.register_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    //保存成功
    mContext.finish();
  }

  @Override
  public void updateError(int errorId)
  {
    //保存异常
    showLoading(errorId);
  }

  @Override
  public void setPresenter(AddressContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }
}
