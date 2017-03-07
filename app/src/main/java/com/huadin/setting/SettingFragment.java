package com.huadin.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.setting.contact.ContactFragment;
import com.huadin.setting.feedback.FeedbackFragment;
import com.huadin.setting.feedback.FeedbackPresenter;
import com.huadin.setting.msg.MsgSettingFragment;
import com.huadin.setting.msg.SettingPresenter;
import com.huadin.userinfo.UpdateContract;
import com.huadin.userinfo.address.AddressFragment;
import com.huadin.userinfo.address.AddressPresenter;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import me.yokeyword.fragmentation.SupportFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 设置
 */

public class SettingFragment extends BaseFragment implements UpdateContract.View<SettingPresenter>
{
  private SettingPresenter mPresenter;

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;

  public static SettingFragment newInstance()
  {

    Bundle args = new Bundle();
    SettingFragment fragment = new SettingFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.setting_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbarHome(mToolbar, R.string.setting, mContext);
    return view;
  }


  @OnClick({R.id.address_linear_layout, R.id.msg_linear_layout,
          R.id.contact_us_linear_layout, R.id.feedback_linear_layout,
          R.id.app_update,R.id.shared_linear_layout})
  public void onClick(View view)
  {
    int lock = 0;
    switch (view.getId())
    {
      case R.id.address_linear_layout:
        startAddressFragment();//预警
        break;
      case R.id.msg_linear_layout://消息设置
        startMsgFragment();
        break;
      case R.id.contact_us_linear_layout://联系我们
        startContactFragment();
        break;
      case R.id.feedback_linear_layout://意见反馈
        startFeedbackFragment();
        break;

      case R.id.app_update://检查更新
        lock = R.id.app_update;
        new SettingPresenter(this);
        mPresenter.start();
        break;

      case R.id.shared_linear_layout:
        lock = R.id.shared_linear_layout;
        startShared();
        break;
    }
    //锁定抽屉
    if (lock == 0) lockDrawer();
  }

  /**
   * 启动预警地址
   */
  private void startAddressFragment()
  {
    AddressFragment addressFragment = findFragment(AddressFragment.class);
    if (addressFragment == null)
    {
      addressFragment = AddressFragment.newInstance(getString(R.string.setting_info_flag_key));
    }
    start(addressFragment, SupportFragment.SINGLETASK);
    new AddressPresenter(mContext, addressFragment);
  }

  /**
   * 联系我们
   */
  private void startContactFragment()
  {
    ContactFragment contactFragment = findFragment(ContactFragment.class);
    if (contactFragment == null)
    {
      contactFragment = ContactFragment.newInstance();
    }
    start(contactFragment, SupportFragment.SINGLETASK);
  }

  /**
   * 意见反馈
   */
  private void startFeedbackFragment()
  {
    FeedbackFragment feedbackFragment = findFragment(FeedbackFragment.class);
    if (feedbackFragment == null)
    {
      feedbackFragment = FeedbackFragment.newInstance();
    }
    start(feedbackFragment, SupportFragment.SINGLETASK);
    new FeedbackPresenter(feedbackFragment);
  }

  private void startMsgFragment()
  {
    MsgSettingFragment msgSettingFragment = findFragment(MsgSettingFragment.class);
    if (msgSettingFragment == null)
    {
      msgSettingFragment = MsgSettingFragment.newInstance();
    }
    start(msgSettingFragment, SupportFragment.SINGLETASK);
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.fault_date_get_in);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    showMessage(R.string.not_update_app);
  }

  @Override
  public void updateError(int errorId)
  {
    showMessage(errorId);
  }

  private void startShared()
  {
    OnekeyShare oks = new OnekeyShare();
    //关闭sso授权
    oks.disableSSOWhenAuthorize();
    // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
    oks.setTitle("标题");
    // text是分享文本，所有平台都需要这个字段
    oks.setText("测试分享");
    //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
    oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
    // url仅在微信（包括好友和朋友圈）中使用
    oks.setUrl("http://sharesdk.cn");

    // 启动分享GUI
    oks.show(mContext);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(SettingPresenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "SettingPresenter cannot be null");
  }
}