package com.huadin;

import com.huadin.waringapp.R;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;

public class MyApplication extends LitePalApplication
{
  @Override
  public void onCreate()
  {
    super.onCreate();

    LitePal.initialize(this);

    configBmob();
  }

  private void configBmob()
  {
    Bmob.initialize(this, getString(R.string.bmob_app_key));
    BmobConfig config = new BmobConfig.Builder(this)
            //设置appkey
            .setApplicationId(getString(R.string.bmob_app_key))
            //请求超时时间（单位为秒）：默认15s
            .setConnectTimeout(20)
            //文件分片上传时每片的大小（单位字节），默认512*1024
            .setUploadBlockSize(1024 * 1024)
            //文件的过期时间(单位为秒)：默认1800s
            .setFileExpiration(2500)
            .build();
    Bmob.initialize(config);
    //使用推送服务时的初始化操作
    BmobInstallation.getCurrentInstallation().save();
    //启动推送
    BmobPush.startWork(this);
  }

}
