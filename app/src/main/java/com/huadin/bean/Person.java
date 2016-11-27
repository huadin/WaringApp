package com.huadin.bean;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject
{
  private String login_name;
  private String user_name;
  private String user_password;
  private String user_phone;
  private String user_address;
  private String create_date;
  private boolean user_permission;

  public String getLogin_name()
  {
    return login_name;
  }

  public void setLogin_name(String login_name)
  {
    this.login_name = login_name;
  }

  public String getUser_name()
  {
    return user_name;
  }

  public void setUser_name(String user_name)
  {
    this.user_name = user_name;
  }

  public String getUser_password()
  {
    return user_password;
  }

  public void setUser_password(String user_password)
  {
    this.user_password = user_password;
  }

  public String getUser_phone()
  {
    return user_phone;
  }

  public void setUser_phone(String user_phone)
  {
    this.user_phone = user_phone;
  }

  public String getUser_address()
  {
    return user_address;
  }

  public void setUser_address(String user_address)
  {
    this.user_address = user_address;
  }

  public String getCreate_date()
  {
    return create_date;
  }

  public void setCreate_date(String create_date)
  {
    this.create_date = create_date;
  }

  public boolean isUser_permission()
  {
    return user_permission;
  }

  public void setUser_permission(boolean user_permission)
  {
    this.user_permission = user_permission;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (user_permission != person.user_permission) return false;
    if (!login_name.equals(person.login_name)) return false;
    if (!user_name.equals(person.user_name)) return false;
    if (!user_password.equals(person.user_password)) return false;
    if (!user_phone.equals(person.user_phone)) return false;
    if (!user_address.equals(person.user_address)) return false;
    return create_date.equals(person.create_date);

  }

  @Override
  public int hashCode()
  {
    int result = login_name.hashCode();
    result = 31 * result + user_name.hashCode();
    result = 31 * result + user_password.hashCode();
    result = 31 * result + user_phone.hashCode();
    result = 31 * result + user_address.hashCode();
    result = 31 * result + create_date.hashCode();
    result = 31 * result + (user_permission ? 1 : 0);
    return result;
  }

  @Override
  public String toString()
  {
    return "Person{" +
            "login_name='" + login_name + '\'' +
            ", user_name='" + user_name + '\'' +
            ", user_password='" + user_password + '\'' +
            ", user_phone='" + user_phone + '\'' +
            ", user_address='" + user_address + '\'' +
            ", create_date='" + create_date + '\'' +
            ", user_permission=" + user_permission +
            '}';
  }
}
