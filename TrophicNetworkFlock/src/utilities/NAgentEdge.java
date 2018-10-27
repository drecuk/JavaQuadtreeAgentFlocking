package utilities;

import java.util.ArrayList;
import java.util.Collections;

public class NAgentEdge {
	
	// agentid and linkid are mandatory, params are optional
	public long agentid;
	public long linkid;
	public ArrayList<String> params = new ArrayList<String>();
	
	public NAgentEdge(long _agentid, long _linkid, String[] _params)
	{
		agentid = _agentid;
		linkid = _linkid;
		Collections.addAll(params, _params);
	}
}
