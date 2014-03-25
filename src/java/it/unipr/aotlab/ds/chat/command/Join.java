package it.unipr.aotlab.ds.chat.command;

/**
 * 
 * Class implementing the join command.
 * 
 * Moreover, it provides an implementation of the {@code toString[]} method that
 * builds a textual representation of commands.
 * 
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 * 
 **/

public final class Join implements Command {
  // Serialization identifier
  private static final long serialVersionUID = 1L;

  // User's nickname.
  private String m_name;
  private boolean m_acceptedRequest;

  public Join(final String nickname) {
    this.m_name = nickname;
    this.m_acceptedRequest = false;
  }

  public Join(final String nickname, boolean acceptedRequest) {
    this.m_name = nickname;
    this.m_acceptedRequest = acceptedRequest;
  }

  public String getName() {
    return this.m_name;
  }

  public boolean isAccepted(){
    return m_acceptedRequest;
  }
  
  public String toString() {
    return this.m_name + " joins the chat";
  }
}
