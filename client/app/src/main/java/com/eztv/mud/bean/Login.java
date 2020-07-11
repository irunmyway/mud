package com.eztv.mud.bean;

public class Login
{
  Enum.messageType type;
  Enum.login login;
  String name;
  String passwd;
  String role;
  Enum.sex sex = Enum.sex.male;

  public Enum.login getLogin() {
    return login;
  }

  public void setLogin(Enum.login login) {
    this.login = login;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getRole() {
    return role;
  }

  public Enum.messageType getType() {
    return type;
  }

  public void setType(Enum.messageType type) {
    this.type = type;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Enum.sex getSex() {
    return sex;
  }

  public void setSex(Enum.sex sex) {
    this.sex = sex;
  }
}
