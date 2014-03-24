package it.unipr.aotlab.ds.chat.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.fusesource.hawtbuf.ByteArrayInputStream;

public class Utils {

  public static byte[] toByteArray(Object o) throws IOException{
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    ObjectOutputStream s    = new ObjectOutputStream(b);

    s.writeObject(o);
    s.flush();
    s.close();
    b.close();

    return b.toByteArray();
  }
  
  public static Object toObject(byte[] b) throws IOException, ClassNotFoundException{
    ObjectInputStream s =
        new ObjectInputStream (new ByteArrayInputStream(b));

    Object o = s.readObject();
    s.close();

    return o;
  }

}
