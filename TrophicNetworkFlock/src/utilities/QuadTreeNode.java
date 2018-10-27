package utilities;

import java.util.ArrayList;

public class QuadTreeNode {
	
	public NodeType nodeType;
	public int ID;
	public int parentID;
	public int layerID;
	
	public int top, bottom, left, right;
	public int width, height;
	public Point3D position;
	public boolean visible;
	
	public int[] childIndex;
	
	public ArrayList<Integer> indices;
	public ArrayList<Integer> envIndices;
	
	public QuadTreeNode(int id, int parentID, int layerID, int top, int btm, int lf, int rg, int width, int height)
	{
		indices = new ArrayList<Integer>();
		envIndices = new ArrayList<Integer>();
		
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
	
	public void removeIndex(int index)
	{
		for(int i=0; i<indices.size(); i++)
		{
			System.out.println("removeInd-"+indices.get(i));
			if(index == indices.get(i))
			{
				indices.remove(i);
				System.out.println("**------------>> NODE:" + ID + "Removed Index: " + index);
				return;
			}
		}
		System.out.println("**------------>> NODE:" + ID + "Index for removal not found: " + index);
		
		// make this invisible if the size is 0
		if(indices.size() <= 0)
		{
			visible = false;
		}
	}
	
}
