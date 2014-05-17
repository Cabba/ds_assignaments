package it.unipr.aotlab.ds.chat.jms;

import java.util.ArrayList;
import java.util.List;

import it.unipr.aotlab.ds.chat.command.Command;

public final class JoinResponse implements Command {
	
	private static final long serialVersionUID = 1L;
	
	private List<Command> m_histo;
	private String m_topicName;
	private boolean m_accepted;
	
	public JoinResponse(boolean accepted, String topicName, List<Command> history){
		m_histo = history;
		m_accepted = accepted;
		m_topicName = topicName;
	}
	
	public JoinResponse(boolean accepted){
		m_accepted = accepted;
		m_histo = new ArrayList<Command>();
	}
	
	public boolean accepted(){
		return m_accepted;
	}
	
	public String getTopicName(){
		return m_topicName;
	}
	
	public List<Command> getHistory(){
		return m_histo;
	}

}
