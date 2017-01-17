package com.huadin.userinfo.fault;

import com.huadin.bean.ReportBean;
import com.huadin.database.WaringAddress;
import com.huadin.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/17.
 * 获取用户提交的报修信息
 */

public class FaultPresenter implements FaultContract.Presenter
{

  private static final String TAG = "FaultPresenter";
  private FaultContract.View mFaultView;

  public FaultPresenter(FaultContract.View faultView)
  {
    mFaultView = faultView;
    mFaultView = checkNotNull(faultView, "faultView cannot be null");
    mFaultView.setPresenter(this);
  }

  @Override
  public void start()
  {
    WaringAddress waringAddress = DataSupport.findFirst(WaringAddress.class);
    String areaId = waringAddress.getWaringAreaId();

    BmobQuery<ReportBean> bQ1 = new BmobQuery<>();
    bQ1.addWhereEqualTo("areaId", areaId);

    BmobQuery<ReportBean> bQ2 = new BmobQuery<>();
    bQ2.addWhereEqualTo("isHandle", false);

    List<BmobQuery<ReportBean>> listBQ = new ArrayList<>();
    listBQ.add(bQ1);
    listBQ.add(bQ2);

    BmobQuery<ReportBean> query = new BmobQuery<>();
    query.and(listBQ);

    query.findObjects(new FindListener<ReportBean>()
    {
      @Override
      public void done(List<ReportBean> list, BmobException e)
      {
        if (e == null)
        {
          LogUtil.i(TAG, "list size = " + list.size());
        } else
        {
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
        }
      }
    });

  }
}
