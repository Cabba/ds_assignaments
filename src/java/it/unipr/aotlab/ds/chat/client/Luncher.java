package it.unipr.aotlab.ds.chat.client;

public class Luncher {

  public static void main(String[] args) {
    try{
      ChatGui cg = new ChatGui(new ChatClientImpl());
      cg.listen();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

}