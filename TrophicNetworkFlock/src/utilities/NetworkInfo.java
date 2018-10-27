package utilities;

import java.util.ArrayList;

public class NetworkInfo {

	public static ArrayList<NAgentNode> nagents = new ArrayList<NAgentNode>();
	public static ArrayList<NAgentEdge> nedges = new ArrayList<NAgentEdge>();

	// --------------------- FILE SAVE
	//static FileWriter nodeWriter;
	//static FileWriter edgeWriter;
	
	//static BufferedWriter popOut;
	//static BufferedWriter timeOut;
	
	public NetworkInfo()
	{
		System.out.println("OBJECT CREATED----->> NetworkInfo");
	}
	
	public static void addAgent(NAgentNode _nagent)
	{
		boolean agentFound = false;
		
		for(int i=0; i < nagents.size(); i++)
		{
			if(nagents.get(i).id == _nagent.id)
			{
				agentFound = true;
				break;
			}	
		}
		
		if(!agentFound) nagents.add(_nagent);
	}
	
	public static void addEdge(NAgentEdge _nedge)
	{
		boolean edgeFound = false;
		
		for(int i=0; i < nedges.size(); i++)
		{
			if((nedges.get(i).agentid == _nedge.agentid) && (nedges.get(i).linkid == _nedge.linkid))
			{
				edgeFound = true;
				break;
			}
		}
		
		if(!edgeFound) nedges.add(_nedge);
	}
	
	
}
