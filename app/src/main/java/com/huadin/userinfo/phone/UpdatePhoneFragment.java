package com.huadin.userinfo.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huadin.base.BaseFragment;
import com.huadin.bean.Person;
import com.huadin.userinfo.UpdateContract;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2016/12/30.
 * 更换手机号码
 */

public class UpdatePhoneFragment extends BaseFragment implements UpdatePhoneContract.View
{

  @BindView(R.id.user_info_code_edit)
  EditText mCodeEdit;

  @BindView(R.id.user_info_phone)
  EditText mNewPhone;

  @BindView(R.id.user_info_code_text)
  TextView mCodeTV;

  @BindView(R.id.user_phone)
  TextView mUserPhone;

  private UpdatePhoneContract.Presenter mPresenter;
  private String mPhone;
  private StringBuilder mSB;
  private String _code;//重发

  public static UpdatePhoneFragment newInstance()
  {
    Bundle args = new Bundle();

    UpdatePhoneFragment fragment = new UpdatePhoneFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Person mPerson = Person.getCurrentUser(Person.class);
    if (mPerson != null) mPhone = mPerson.getMobilePhoneNumber();
    _code = getString(R.string.show_send_code_text);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.update_phone_fragment);
    ButterKnife.bind(this, view);
    mUserPhone.setText(mPhone);
    return view;
  }

  @Override
  public String getCode()
  {
    return mCodeEdit.getText().toString();
  }

  @Override
  public String getOldPhone()
  {
    return mPhone;
  }

  @Override
  public String getNewPhone()
  {
    return mNewPhone.getText().toString();
  }


  @Override
  public void codeOnFinish()
  {
    mCodeTV.setEnabled(true);
    mCodeTV.setText(R.string.send_phone_code);
  }

  @Override
  public void codeOnTick(long m)
  {
    if (mSB == null)
    {
      mSB = new StringBuilder();
    }
    mSB.append(String.valueOf(m));
    mSB.append(_code);
    mCodeTV.setText(mSB.toString());
    mSB.delete(0, mSB.length());
  }

  @Override
  public boolean networkIsAvailable()
  {
    return isNetwork();
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
    //更新成功
    showMessage(R.string.update_password_success);
    mContext.finish();
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public void setPresenter(UpdatePhoneContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @OnClick({R.id.user_info_code_text, R.id.next_step})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.user_info_code_text:
        mPresenter.getCode();
        break;
      case R.id.next_step:
        mPresenter.start();
        break;
    }
  }
}
