package it.unipr.aotlab.ds.chat.client;

import java.io.Serializable;

public class Message implements Serializable{

  private String user_name;
  private String message;
  
  public Message(String user_name, String message){
    this.user_name = user_name;
    this.message = message;
  }
  
  public String getUserName(){
    return user_name;
  }
  
  public String getMessage(){
    return message;
  }

}
