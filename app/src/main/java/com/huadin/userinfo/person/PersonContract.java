package com.huadin.userinfo.person;

import com.huadin.bean.Person;
import com.huadin.userinfo.UpdateContract;

import java.util.List;

/**
 * Created by 潇湘 on 2017/2/9.
 * 用户管理
 */

public interface PersonContract
{
  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 查询成功
     *
     * @param personList 人员集合
     */
    void querySuccess(List<Person> personList);

    /**
     * 获取所在区
     *
     * @return 区名
     */
    String getArea();
  }

  interface Presenter extends UpdateContract.Presenter
  {
    /**
     * 刷新
     */
    void refresh();

    /**
     * 加载更多
     */
    void loadMore();
  }

}
