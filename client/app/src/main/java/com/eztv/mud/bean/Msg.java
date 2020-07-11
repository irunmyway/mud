package com.eztv.mud.bean;

public class Msg
{
  String cmd;//指令
  String msg;//消息
  String name;
  Enum.messageType type = Enum.messageType.normal;
  String role;
  
  public String getCmd()
  {
    return this.cmd;
  }
  
  public String getMsg()
  {
    return this.msg;
  }
  
  public String getName()
  {
    return this.name;
  }


  
  public void setCmd(String paramString)
  {
    this.cmd = paramString;
  }
  
  public void setMsg(String paramString)
  {
    this.msg = paramString;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public Enum.messageType getType() {
    return type;
  }

  public void setType(Enum.messageType type) {
    this.type = type;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Msg{type=");
    localStringBuilder.append(this.type);
    localStringBuilder.append(", cmd='");
    localStringBuilder.append(this.cmd);
    localStringBuilder.append('\'');
    localStringBuilder.append(", msg='");
    localStringBuilder.append(this.msg);
    localStringBuilder.append('\'');
    localStringBuilder.append(", role='");
    localStringBuilder.append(this.role);
    localStringBuilder.append('\'');
    localStringBuilder.append(", name='");
    localStringBuilder.append(this.name);
    localStringBuilder.append('\'');
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
