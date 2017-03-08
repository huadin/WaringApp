package com.huadin.waringapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadin.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BootPageActivity extends BaseActivity implements ViewPager.OnPageChangeListener
{

  @BindView(R.id.boot_page_view)
  ViewPager mViewPage;
  @BindView(R.id.start_view)
  TextView mStartView;
  private int mCurrentIndex;
  private List<View> viewList;
  private List<View> mImViews;

  @Override

  protected void onCreate(Bundle savedInstanceState)
  {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initViews();
    initPoint();
    initAdapter();
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_boot_page;
  }

  private void initViews()
  {
    viewList = new ArrayList<>();
    View page_1 = View.inflate(this, R.layout.boot_page_1, null);
    View page_2 = View.inflate(this, R.layout.boot_page_2, null);
    View page_3 = View.inflate(this, R.layout.boot_page_3, null);
    View page_4 = View.inflate(this, R.layout.boot_page_4, null);
    viewList.add(page_1);
    viewList.add(page_2);
    viewList.add(page_3);
    viewList.add(page_4);
  }

  private void initPoint()
  {
    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.boot_page_linear);
    int count = linearLayout.getChildCount();
    mImViews = new ArrayList<>();
    for (int i = 0; i < count; i++)
    {
      ImageView imageView = (ImageView) linearLayout.getChildAt(i);
      mImViews.add(imageView);
      if (i == 0)
      {
        imageView.setEnabled(true);
      } else
      {
        imageView.setEnabled(false);
      }
    }
    mCurrentIndex = 0;
  }

  private void initAdapter()
  {
    BootPageAdapter adapter = new BootPageAdapter();
    mViewPage.addOnPageChangeListener(this);
    mViewPage.setAdapter(adapter);
  }

  @OnClick(R.id.start_view)
  public void onClick(View view)
  {
    //进入主界面
    startActivity(SettingAreaActivity.class);
    SharedPreferences.Editor editor =
            getSharedPreferences(KEY_ADDRESS_AREA_SHARED, MODE_PRIVATE).edit();
    editor.putBoolean(KEY_FIRST_KEY, false);
    editor.apply();
    finish();
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
  {

  }

  @Override
  public void onPageSelected(int position)
  {
    setCurrentPoint(position);
    if (position == viewList.size() - 1)
    {
      mStartView.setVisibility(View.VISIBLE);
    } else
    {
      mStartView.setVisibility(View.GONE);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state)
  {

  }

  /**
   * 设置当前点
   */
  private void setCurrentPoint(int position)
  {
    if (position < 0 || position >= mImViews.size()) return;

    mImViews.get(position).setEnabled(true);
    mImViews.get(mCurrentIndex).setEnabled(false);
    mCurrentIndex = position;
  }


  private class BootPageAdapter extends PagerAdapter
  {
    @Override
    public int getCount()
    {
      return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
      return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
      container.addView(viewList.get(position));
      return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
      container.removeView(viewList.get(position));
    }
  }


}
