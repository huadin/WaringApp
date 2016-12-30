package com.huadin.userinfo;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.huadin.base.BaseActivity;
import com.huadin.bean.Person;
import com.huadin.dialog.PromptFragment;
import com.huadin.eventbus.EventCenter;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

import static com.huadin.waringapp.R.string.password_modify;

public class UserInfoActivity extends BaseActivity implements PromptFragment.PromptListener
{

  @BindView(R.id.user_info_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.user_info_collapsing)
  CollapsingToolbarLayout mCollapsing;
  @BindView(R.id.user_admin_info)
  LinearLayout mAdminLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initViews();
  }

  private void initViews()
  {
    //判断当前用户是否是管理员
    Person mPerson = BmobUser.getCurrentUser(Person.class);
    if (mPerson != null)
    {
      boolean isPermission = mPerson.isUserPermission();
      //没有管理员权限
      if (!isPermission)
      {
        mAdminLayout.setVisibility(View.GONE);
      }
    }

    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
    {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeButtonEnabled(true);//设置返回按钮可用
    }

    mCollapsing.setTitle(getString(R.string.user_info_title));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    //返回事件
    switch (item.getItemId())
    {
      case android.R.id.home:
        finish();
        break;
    }
    return true;
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_user_info;
  }

  @OnClick({R.id.user_info_item_user, R.id.user_info_item_waring_address,
          R.id.person_info_message, R.id.fault_info_message,
          R.id.release_info_message, R.id.user_info_item_phone,
          R.id.user_info_item_password, R.id.user_info_out})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.user_info_item_user:
        break;
      case R.id.user_info_item_waring_address:
        break;
      case R.id.person_info_message:
        break;
      case R.id.fault_info_message:
        break;
      case R.id.release_info_message:
        break;
      case R.id.user_info_item_phone:
        startActivity(UpdateUserInfoActivity.class,R.string.user_info_phone);
        break;
      case R.id.user_info_item_password:
        startActivity(UpdateUserInfoActivity.class, R.string.password_modify);
        break;
      case R.id.user_info_out:
        //退出登录
        outCurrentUser();
        break;
    }
  }

  /**
   * 退出登录
   */
  private void outCurrentUser()
  {
    PromptFragment fragment = PromptFragment.newInstance(getString(R.string.is_out_current_user));
    fragment.setOnPromptListener(this);
    fragment.show(getSupportFragmentManager(), getClass().getSimpleName());
  }

  //退出确认回调
  @Override
  public void promptOk()
  {
    Person.logOut();
    Person user = BmobUser.getCurrentUser(Person.class);
    if (user == null)
    {
      LogUtil.i(LOG_TAG, "退出成功");
      EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_OUT_SUCCESS));
      finish();
    }
  }
}
