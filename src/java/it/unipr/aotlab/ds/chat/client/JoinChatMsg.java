package it.unipr.aotlab.ds.chat.client;

import java.io.Serializable;

public class JoinChatMsg implements Serializable{

  private String m_user_name;
  private boolean m_accepted_request;
  
  public JoinChatMsg(String user_name){
    this.m_user_name = user_name;
    m_accepted_request = false;
  }
  
  public JoinChatMsg(String user_name, boolean accepted_request){
    this.m_user_name = user_name;
    this.m_accepted_request = accepted_request;
  }
  
  public String getUserName(){
    return m_user_name;
  }
  
  public boolean requestAccepted(){
    return m_accepted_request;
  }

}
