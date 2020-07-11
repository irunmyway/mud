package com.eztv.mud.bean.net;

import com.eztv.mud.bean.Enum;
import online.sanen.cdm.template.jpa.Column;
import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.NoInsert;
import online.sanen.cdm.template.jpa.Table;

import java.util.Date;

@Table(name = "account")
public class Login
{
  @NoInsert
  Enum.login login;
  @Id
  @Column(name = "account")
  String name;
  @Column(name = "pwd")
  String passwd;
  @NoInsert
  String role;
  @NoInsert
  Enum.sex sex = Enum.sex.male;
  @Column(name = "createat")
  Date createAt;
  @Column(name = "updateat")
  Date updateAt;


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

  public void setRole(String role) {
    this.role = role;
  }

  public Enum.sex getSex() {
    return sex;
  }

  public void setSex(Enum.sex sex) {
    this.sex = sex;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(Date updateAt) {
    this.updateAt = updateAt;
  }

  @Override
  public String toString() {
    return "Login{" +
            "login=" + login +
            ", name='" + name + '\'' +
            ", passwd='" + passwd + '\'' +
            ", role='" + role + '\'' +
            ", sex=" + sex +
            '}';
  }
}
