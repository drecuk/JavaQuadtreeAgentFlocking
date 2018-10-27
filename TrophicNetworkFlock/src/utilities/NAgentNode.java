package utilities;
import java.util.ArrayList;
import java.util.Collections;

public class NAgentNode {
	
	public long id;
	public String name;
	public ArrayList<String> params = new ArrayList<String>();
	
	public NAgentNode(long _id, String[] _params)
	{
		id = _id;
		Collections.addAll(params, _params);
	}
}
