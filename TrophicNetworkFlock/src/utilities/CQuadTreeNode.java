package utilities;

import java.util.ArrayList;

import simulation.CEmitter;

import life.COrganism;


public class CQuadTreeNode {
	
	public NodeType nodeType;
	public int ID;
	public int parentID;
	public int layerID;	
	public int top, bottom, left, right;
	public int width, height;
	public Point3D position;
	public boolean visible;
	
	public int[] childIndex;
	
	public ArrayList<COrganism> agents;
	public ArrayList<CEmitter> emitters;
	
	public CQuadTreeNode(int id, int parentID, int layerID, int top, int btm, int lf, int rg, int width, int height)
	{
		agents = new ArrayList<COrganism>();
		emitters = new ArrayList<CEmitter>();
		
		childIndex = new int[4];
		childIndex[0] = -1;
		childIndex[1] = -1;
		childIndex[2] = -1;
		childIndex[3] = -1;
		
		position = new Point3D(0,0,0);
		
		this.ID = id;
		this.nodeType = NodeType.leaf; // this is a leaf when first created!
		this.parentID = parentID;
		this.layerID = layerID;
		this.top = top;
		this.bottom = btm;
		this.left = lf;
		this.right = rg;
		this.width = width;
		this.height = height;
		this.visible = false;
	}
	
	public void removeAgent(CWorldObject o)
	{
		for(int i=0; i<agents.size(); i++)
		{
			//System.out.println("removeInd-"+agents.get(i));
			if(o == agents.get(i))
			{
				agents.remove(i);
				//System.out.println("**------------>> NODE:" + ID + "Removed Index: " + o.toString());
				return;
			}
		}
		//System.out.println("**------------>> NODE:" + ID + "Index for removal not found: " + o.toString());
		
		// make this invisible if the size is 0
		if(agents.size() <= 0)
		{
			visible = false;
		}
	}
	
}
