package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import simulation.CEmitter;
import simulation.CEnvironment;

import life.COrganism;


public class CQuadTree {
	private static int nodeCount = 0;	// for divideNode use
	private static int nodeSize; // for createNode use
	private static int nodeID = 0;	// "static" is to make sure that nodeID isn't reinitialised - for placeObjectInNode()
	
	private static int divideThreshold;
	private static int maxLevel;
	public float minSizeOfQuad;
	
	public static int nodeIndex; // nodeIndex is global for maintaining value - because this is in a recursive function
	public static ArrayList<CQuadTreeNode> qtNodes = new ArrayList<CQuadTreeNode>();
	
	public static boolean drawQT = true;
	
	public CQuadTree(int top, int bottom, int left, int right, int maxLevels, int divideThreshold)
	{
		System.out.println("OBJECT CREATED----->> CQuadTree");
		
		// calculate the number of nodes for memory allocation
		CQuadTree.nodeSize = calculateNodeSize(maxLevels);
		CQuadTree.divideThreshold = divideThreshold;
		CQuadTree.maxLevel = maxLevels;
		
		minSizeOfQuad = calculateMinQuadSize(bottom - top, maxLevels);	// minimum size of quad at deepest level
		
		// create root node and add it to quadtreenode arraylist
		//(int id, int nodeType, int parentID, int layerID, int top, int btm, int lf, int rg, int width, int height)
		CQuadTreeNode rootnode = new CQuadTreeNode(0, 0, 0, top, bottom, left, right, left+right, top+bottom);		
		qtNodes.add(rootnode);
	}
	
	public boolean objectOffBoundary(Point3D pos, int top, int bottom, int left, int right)
	{
		boolean isOff = false;
		
		if((pos.x < left) || (pos.x > right))
			isOff = true;
		
		if((pos.y < top) || (pos.y > bottom))
			isOff = true;
		
		return isOff;
	}
	
	public void getStats()
	{	
		for(int i=0; i < qtNodes.size(); i++)			// QT
		{			
			// ------------------------- loop through EMITTERS
			for(int j=0; j < qtNodes.get(i).agents.size(); j++)		// Agents within QT
			{	
				
				for(int k=0; k < Statistics.species.size(); k++)	// stats
				{
					if(new String(qtNodes.get(i).agents.get(j).name).equals(Statistics.species.get(k).name))
					{	
						//System.out.println(Statistics.species.get(k).name);
						Statistics.species.get(k).tempCount++;	// if same, add to temp count
					}
				}
			}
		}
		
		// add this end point, add tempCount to the population;
		for(int k=0; k < Statistics.species.size(); k++)	// stats
		{		
			Statistics.species.get(k).population.add(Statistics.species.get(k).tempCount);	// if same, add to temp count
			Statistics.species.get(k).tempCount = 0; // reset
		}
		
		//Statistics.species.get(k).tempCount = 0;
	}
	
	public void update(CEnvironment env)
	{
		// ------------------------- LOOP THROUGH EACH qtNode
		// within each node, loop through all agents and emitters to process them...
		for(int i=0; i < qtNodes.size(); i++)
		{			
			// ------------------------- loop through EMITTERS
			for(int j=0; j < qtNodes.get(i).emitters.size(); j++)
			{
				// TODO -------------- EMITTER DEAD FLAG
				// check to see if this emitter is dead
				//**** 
				if (qtNodes.get(i).emitters.get(j).isDead)
				{
					// remove this object
					qtNodes.get(i).emitters.remove(j);
					
					// no need to process this emitter, skip bottom code
					continue; // jump to next item
				}
				
				// -------------------------------- EMITTER HAS LEFT NODE, reposition it
				//****
				if (objectOffBoundary(qtNodes.get(i).emitters.get(j).position, 
						qtNodes.get(i).top, qtNodes.get(i).bottom, qtNodes.get(i).left, qtNodes.get(i).right))
				{					
					// emitter is off boundary, reposition it in nodes
					
					// recurse the node where the agent is in
				    //CQuadTreeNode tempNode = qtNodes.get(getArrayIndexOfNode(nodeID));
				    // traverse object from root node 
				    placeObjectInNode(CQuadTree.qtNodes.get(0), qtNodes.get(i).emitters.get(j));
				    qtNodes.get(i).emitters.remove(j);
				    
				    // if this object has left this node, don't process it below
				    continue;
				}
				
				
				// ---------------------------------- UPDATING EMITTER i TODO
				// emitters needs to affect one another too
				// get the current agent
				CWorldObject me = qtNodes.get(i).emitters.get(j);
				
				// get emitters nearby
				ArrayList<CWorldObject> ems = new ArrayList<CWorldObject>();			
				//ems = rangeQuery(me, ObjectType.emitter, me.range);	// get all emitters in range
				
				// ** pass emitters to the update of emitter j	(coder manage interaction)
				//qtNodes.get(i).emitters.get(j).update(ems); // interact with emitters
			}
			
			
			
			// ------------------------- loop through AGENTs for interaction (foreach agent j in node i)
			for(int j=0; j < qtNodes.get(i).agents.size(); j++)
			{
				// TODO -------------- AGENT DEAD FLAG
				// check to see if this agent is dead
				//**** 
				if (qtNodes.get(i).agents.get(j).isDead)
				{
					// remove this object
					qtNodes.get(i).agents.remove(j);
					
					// no need to process this agent, skip bottom code
					continue;  // jump to next item
				}
				
				// -------------------------------- AGENT HAS LEFT NODE, reposition it
				//****
				/*if (qtNodes.get(i).agents.get(j).id == 5)
				{					
					System.out.println("Agent:" + qtNodes.get(i).agents.get(j).id + 
						" pos: x" + qtNodes.get(i).agents.get(j).position.x + " y" +
						qtNodes.get(i).agents.get(j).position.y + " | top:" +						
						" " + qtNodes.get(i).top + " bottom:" + 
						qtNodes.get(i).bottom + " left:" +
						qtNodes.get(i).left + " right:" +
						qtNodes.get(i).right
						);					
				}
				*/
				if (objectOffBoundary(qtNodes.get(i).agents.get(j).position, 
						qtNodes.get(i).top, qtNodes.get(i).bottom, qtNodes.get(i).left, qtNodes.get(i).right))
				{	
					// agent is off boundary, reposition it in nodes
					//System.out.println("agent: " + qtNodes.get(i).agents.get(j).id + " left node: " + i);
					
					// recurse the node where the agent is in
				    //CQuadTreeNode tempNode = qtNodes.get(getArrayIndexOfNode(nodeID));
				    // traverse object from root node 
					/*
					int thisNodeID = CQuadTree.qtNodes.get(i).ID;
					int thisparentID = CQuadTree.qtNodes.get(i).parentID;
					int grandpa = CQuadTree.qtNodes.get(thisparentID).ID;
					*/
					
				    placeObjectInNode(CQuadTree.qtNodes.get(0), qtNodes.get(i).agents.get(j));
				    qtNodes.get(i).agents.remove(j);
				    
				    // if this object has left this node, don't process it below
				    continue;
				}
				
				// ---------------------------------- UPDATING AGENT i
				// get the current agent
				CWorldObject me = qtNodes.get(i).agents.get(j);
				
				// get emitters nearby
				//ArrayList<CWorldObject> ems = new ArrayList<CWorldObject>();			
				//ems = rangeQuery(me, ObjectType.emitter, qtNodes.get(i).emitters.get(j).range);	// get all emitters
				
				// get nearby agents and emitters (ObjectType.all)
				ArrayList<CWorldObject> ags = new ArrayList<CWorldObject>();
				ags = rangeQuery(me, ObjectType.all, me.range);		// get all agents + emitters
				
				// ** pass agents to the update of agent j	(coder manage interaction)
				qtNodes.get(i).agents.get(j).update(ags, env); // interact with agents
								
				
				/* don't want this code anymore
				// ------------------------------------ Agent j INTERACT WITH EMITTER k
				for(int k=0; k < ems.size(); k++)
				{
					
				}
					
				// ------------------------------------ Agent j INTERACT WITH AGENTS k
				// ** System.out.print("** me " + me.id + " interacts with agents: ");
				for(int k=0; k < ags.size(); k++)
				{
					if(me.id != ags.get(k).id)	// make sure agent k isn't myself
					{
						// ** System.out.print(ags.get(k).id + ", ");	
						
						
					}
					
					// -------------------------------- If agent has left node, reposition them
				}
				// ** System.out.println();
				*/
			}
		}
	}
		
 	public static boolean divideNode(CQuadTreeNode thisNode)
	{
		boolean divideCompleted = false;
		// ** System.out.println("-----------------------------------.> DIVIDING NODE " + thisNode.ID);
		
		// calculate the width and height of this node from the bounds passed in
		// note that all quadrants (child nodes) in the level has the same width and height
		int childW = thisNode.width / 2;
		int childH = thisNode.height / 2;

		// if the division is the last leaf, then divide
		  if (thisNode.nodeType == NodeType.leaf)	// these child nodes are definitely the last division
		  {    
		    thisNode.nodeType = NodeType.branch;	// change the parent node into a QT_NODE type
		    
		    // create a temporary qtnode that can be filled up for each quadrant and added to the qtNodes arraylist
		    CQuadTreeNode qtnodeNew = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
		    
		    // -------------------------- FIRST CHILD QUADRANT -. 0 NW
		    nodeCount++;		    
		    thisNode.childIndex[0] = nodeCount;		// set the child index
		    
		    qtnodeNew.parentID = 	thisNode.ID;	// parent id comes from the previous node's ID
		    qtnodeNew.ID = 	nodeCount;	// this node ID is the nodeIndex passed in		  
		    
		    qtnodeNew.width = 	childW;
		    qtnodeNew.height = 	childH;
		    
		    // receives boundary parameters for this node (quadrant 0)
		    qtnodeNew.top = 	thisNode.top;
		    qtnodeNew.bottom = 	thisNode.top + childH;
		    qtnodeNew.left = 	thisNode.left;
		    qtnodeNew.right = 	thisNode.left + childW;
		    
		    // calculate central axial position of this node (centre of quad boundary)
		    qtnodeNew.position.x =	(thisNode.left + (childW / 2));
		    qtnodeNew.position.y =	(thisNode.top + (childH / 2));
		    
		    qtnodeNew.nodeType = NodeType.leaf;	// turn this into leaf...
		    qtnodeNew.layerID = thisNode.layerID + 1;		    
		    
		   // // ** System.out.println("NW Quadrant Node 0 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		    //// ** System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + qtnodeNew.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		    //// ** System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    // -------------------------- SECOND QUADRANT -. 1 SW
		    qtnodeNew = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
		    nodeCount++;
		    thisNode.childIndex[1] = nodeCount;		// set the child index  
		    
		    qtnodeNew.parentID = 	thisNode.ID;	// parent id comes from the previous node's ID
		    qtnodeNew.ID = 	nodeCount;	// this node ID is the nodeIndex passed in		
		    
		    qtnodeNew.width = 	childW;
		    qtnodeNew.height = 	childH;
		    
		    // receives boundary parameters for this node (quadrant 0)
		    qtnodeNew.top = 	thisNode.top + childH;
		    qtnodeNew.bottom = 	thisNode.bottom;
		    qtnodeNew.left = 	thisNode.left;
		    qtnodeNew.right = 	thisNode.left + childW;
		    
		    /*
		    // ** System.out.println("thisNode.Top " + thisNode.top);
		    // ** System.out.println("ChildH " + childH);
		    // ** System.out.println("ChildW " + childW);
		    // ** System.out.println("TOP " + qtnodeNew.top);
		    // ** System.out.println("BOTTOM " + qtnodeNew.bottom);
		    */
		    
		    // calculate central axial position of this node (centre of quad boundary)
		    qtnodeNew.position.x =	(thisNode.left + (childW / 2));
		    qtnodeNew.position.y =	(thisNode.bottom - (childH / 2));
		    
		    qtnodeNew.nodeType = NodeType.leaf;	// turn this into leaf...
		    qtnodeNew.layerID = thisNode.layerID + 1;
		    
		    //// ** System.out.println("SW Quadrant Node 1 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		   // // ** System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + thisNode.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		    //// ** System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    // -------------------------- THIRD QUADRANT -. 2 NE
		    qtnodeNew = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
		    nodeCount++;
		    thisNode.childIndex[2] = nodeCount;		// set the child index
		    
		    qtnodeNew.parentID = 	thisNode.ID;	// parent id comes from the previous node's ID
		    qtnodeNew.ID = 		nodeCount;	// this node ID is the nodeIndex passed in		
		      
		    qtnodeNew.width = 	childW;
		    qtnodeNew.height = 	childH;
		    
		    // receives boundary parameters for this node (quadrant 0)
		    qtnodeNew.top = 	thisNode.top;
		    qtnodeNew.bottom = 	thisNode.top + childH;
		    qtnodeNew.left = 	thisNode.right - childW;
		    qtnodeNew.right = 	thisNode.right;
		    
		    // calculate central axial position of this node (centre of quad boundary)
		    qtnodeNew.position.x =	(thisNode.right - (childW / 2));
		    qtnodeNew.position.y =	(thisNode.top + (childH / 2));
		    
		    qtnodeNew.nodeType = NodeType.leaf;	// turn this into leaf...
		    qtnodeNew.layerID = thisNode.layerID + 1;
		    
		    //// ** System.out.println("NE Quadrant Node 2 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		    //// ** System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + qtnodeNew.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		    //// ** System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    // -------------------------- FOURTH QUADRANT -. 3 SE
		    qtnodeNew = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
		    nodeCount++;
		    thisNode.childIndex[3] = nodeCount;		// set the child index
		      
		    qtnodeNew.parentID = 	thisNode.ID;	// parent id comes from the previous node's ID
		    qtnodeNew.ID = 		nodeCount;	// this node ID is the nodeIndex passed in		
		      
		    qtnodeNew.width = 	childW;
		    qtnodeNew.height = 	childH;
		    
		    // receives boundary parameters for this node (quadrant 0)
		    qtnodeNew.top = 	thisNode.top + childH;
		    qtnodeNew.bottom = 	thisNode.bottom;
		    qtnodeNew.left = 	thisNode.right - childW;
		    qtnodeNew.right = 	thisNode.right;
		    
		    // calculate central axial position of this node (centre of quad boundary)
		    qtnodeNew.position.x =	(thisNode.right - (childW / 2));
		    qtnodeNew.position.y =	(thisNode.bottom - (childH / 2));
		    
		    qtnodeNew.nodeType = NodeType.leaf;	// turn this into leaf...
		    qtnodeNew.layerID = thisNode.layerID + 1;
		    
		    //// ** System.out.println("SE Quadrant Node 3 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		    //// ** System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + qtnodeNew.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		   // // ** System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    divideCompleted = true;
		  }
		  
		return divideCompleted;
	}
	
	public static COrganism[] getNodeIndices(int nodeID)
	{	
		CQuadTreeNode node = qtNodes.get(getArrayIndexOfNode(nodeID));
		
		COrganism[] org = new COrganism[node.agents.size()];
				
		for(int i=0; i < node.agents.size(); i++ )
		{
			org[i] = node.agents.get(i);
		}
		
		return org; 
	}
	
	public static int getArrayIndexOfNode(int nodeID)
	{
		int targetID = 0;
		
		int start = 0;
		if (nodeID > 3)
			start = nodeID;			
			
	    for(int i=start; i< qtNodes.size(); i++)
	    {
	    	if(qtNodes.get(i).ID == nodeID)
	    	{
	    		targetID = i;
	    		break;
	    	}	
	    }
	    
	    return targetID;
	}
	
	public static ArrayList<CWorldObject> rangeQuery(CWorldObject obj, ObjectType objectType, int radius)
	{
		ArrayList<CWorldObject> rangeObjs = new ArrayList<CWorldObject>();
		//if(!obj.name.equals("Herbivore"))
		//	return rangeObjs;
		
		//System.out.println("@@------------------------------------- RANGE QUERY ------->>" + obj.name);
		Random rnd = new Random();
		
		// this chunk of code collects everything from obj's own node
		if(obj.nodeID != -1)
		{	
			// depending on which types, collect it
			if(objectType == ObjectType.agent)	// only add this particular type to my query
			{
				//System.out.println("agents");
				for(int i=0; i < qtNodes.get(obj.nodeID).agents.size(); i++ )
				{
					//System.out.println("added:" + qtNodes.get(obj.nodeID).agents.get(i).id);
					rangeObjs.add(qtNodes.get(obj.nodeID).agents.get(i));			
				}
			}
			
			if(objectType == ObjectType.emitter)	// only add this particular type to my query
			{
				//System.out.println("emitters");
				for(int i=0; i < qtNodes.get(obj.nodeID).emitters.size(); i++ )
				{
					rangeObjs.add(qtNodes.get(obj.nodeID).emitters.get(i));			
				}
			}
			
			if(objectType == ObjectType.all)	// if both objects
			{
				//System.out.println("all");
				for(int i=0; i < qtNodes.get(obj.nodeID).agents.size(); i++ )
				{
					//System.out.println("added:" + qtNodes.get(obj.nodeID).agents.get(i).id);
					rangeObjs.add(qtNodes.get(obj.nodeID).agents.get(i));			
				}
				for(int i=0; i < qtNodes.get(obj.nodeID).emitters.size(); i++ )
				{
					rangeObjs.add(qtNodes.get(obj.nodeID).emitters.get(i));			
				}
			}
		}
		
		for(int i=0; i < qtNodes.size(); i++ )
		{
			if(qtNodes.get(i).ID != obj.nodeID)	// make sure my nodeID is not the same as node[i]
			{
				if(qtNodes.get(i).nodeType == NodeType.leaf)	// leaf
				{
					
					// get the distance of nodes' centre with the agent's position
					float dx = qtNodes.get(i).position.x - obj.position.x;
					float dy = qtNodes.get(i).position.y - obj.position.y;
					float dist = (float) Math.sqrt((dx*dx) + (dy*dy));
						
					// collect indices in this node if node's centre is within radius
					if(dist <= radius)
					{
						if(objectType == ObjectType.agent)	// only add this particular type to my query
						{		
							//System.out.print("agents only");
							//System.out.print("NODE[" + qtNodes.get(i).ID + "]:: ");
							// add agent indices
							for(int j=0; j<qtNodes.get(i).agents.size(); j++)
							{	
								int rIndex = 0;
								
								if(qtNodes.get(i).agents.size() >= 2)
									if(rangeObjs.size() >=1)
										rIndex = rnd.nextInt(rangeObjs.size());
								
								//System.out.println("index: " + rIndex + " size:" + rangeObjs.size());
								rangeObjs.add(rIndex, qtNodes.get(i).agents.get(j));
								//rangeObjs.add(qtNodes.get(i).agents.get(j));
								//System.out.print(qtNodes.get(i).agents.get(j).id + ", ");
							}
						}
						
						if(objectType == ObjectType.emitter)	// only add this particular type to my query
						{
							//System.out.println("emitters only");
							for(int j=0; j<qtNodes.get(i).emitters.size(); j++)
							{	
								int rIndex = 0;
								
								if(qtNodes.get(i).emitters.size() >= 2)
									if(rangeObjs.size() >=1)
										rIndex = rnd.nextInt(rangeObjs.size());
								
								//System.out.println("index: " + rIndex + " size:" + rangeObjs.size());
								rangeObjs.add(rIndex, qtNodes.get(i).emitters.get(j));
							}
						}
						
						if(objectType == ObjectType.all)	// only add this particular type to my query
						{
							//System.out.print("all");
							//System.out.print("NODE[" + qtNodes.get(i).ID + "]:: ");
							// add agent indices
							for(int j=0; j<qtNodes.get(i).agents.size(); j++)
							{	
								int rIndex = 0;
								
								if(qtNodes.get(i).agents.size() >= 2)
									if(rangeObjs.size() >=1)
										rIndex = rnd.nextInt(rangeObjs.size());
								
								//System.out.println("index: " + rIndex + " size:" + rangeObjs.size());
								rangeObjs.add(rIndex, qtNodes.get(i).agents.get(j));
							}
							
							for(int j=0; j<qtNodes.get(i).emitters.size(); j++)
							{	
								int rIndex = 0;
								
								if(qtNodes.get(i).emitters.size() >= 2)
									if(rangeObjs.size() >=1)
										rIndex = rnd.nextInt(rangeObjs.size());
								
								//System.out.println("index: " + rIndex + " size:" + rangeObjs.size());
								rangeObjs.add(rIndex, qtNodes.get(i).emitters.get(j));
							}
						}
					}
				}
			}
		}
		
		//System.out.println("@@------------------------------------- RANGE QUERY ------->>");
		
		return rangeObjs;
	}
	
	// recursive function - go in until you find the leaf
	public static int placeObjectInNode(CQuadTreeNode parentNode, CWorldObject obj)
	{
	  // ** System.out.println("----------------.>>> PLACEMENT FOR  OBJECT[" + obj.id + "] node[" + parentNode.ID + "] node.prntID[" + parentNode.parentID + "]");
	  //static int insertCount;
	  
	  int pID = parentNode.ID;//getArrayIndexOfNode(parentNode.ID);
	  
	  // check to see if this is the last layer possible
	  if(parentNode.nodeType != NodeType.leaf)	// is NOT leaf go in further
	  {
	    // ** System.out.println("[" + parentNode.ID + "]" + " FIRST IF ELSE -- NOT LEAF GO DEEPER >>");

	    // which child node of the parent is this object in?
	    nodeID = inWhichNodeBoundary(parentNode, obj.position);
	    
	    // recurse the node where the agent is in
	    CQuadTreeNode tempNode = qtNodes.get(getArrayIndexOfNode(nodeID));
	    // ** System.out.println(">>>> FIRST IF ELSE recurse placeObj:" + obj.id + " into node " + nodeID); 
	    placeObjectInNode(tempNode, obj);   
	  }
	  else	// if node is currently a leaf
	  {
	      // calculate size of agent and environment indices
	      int indexSize = parentNode.agents.size() + parentNode.emitters.size();
	      
	      // ** System.out.println("[" + parentNode.ID + "]" + "FIRST ELSE -- is leaf | index size: [" +  
	      //parentNode.agents.size() + " + " + parentNode.emitters.size() + "] = " + indexSize);
	      
	      // check if node is occupied, divideThreshold default = 1      
	      if (indexSize >= divideThreshold)	// ***** if over threshold	    	  
	      {
			// ** System.out.println("--[" + parentNode.ID + "]" + " SECOND IF ELSE -- NOT OVER THRESHOLD -- agent size: " + parentNode.agents.size() + " | env size: " + parentNode.emitters.size());		
			// ** System.out.println(">>>>>>>>>> total agent+env size: " + indexSize + " >= " + divideThreshold);
			
			if(parentNode.layerID == maxLevel)	// and is maxLevel (no more division possible)
			{
			  // ** System.out.println("----[" + parentNode.ID + "]" + " THIRD IF ELSE -- is maxLevel | layer ID: " + parentNode.layerID);
			  // ** System.out.println("---- [" + parentNode.ID + "] Objects placed in this node");
			  
			  // decide which indices to put the object index in
			  if (obj.objectType == ObjectType.agent)
			  {
				// ** System.out.println("----object is stored as AGENT TYPE");
				obj.nodeID = pID; // assign this node's id to obj
				qtNodes.get(pID).agents.add((COrganism) obj);  
				//parentNode.agents.add(objectID);	// store agentID in this node			    
			  }
			  else if (obj.objectType == ObjectType.emitter)
			  {				  
			    //parentNode.emitters.add(objectID);	// store agentID in this node
				// ** System.out.println("----object is stored as ENVIRONMENT TYPE");
				obj.nodeID = pID; // assign this node's id to obj
			    qtNodes.get(pID).emitters.add((CEmitter) obj);			    
			  }
			  
			  // ----------------- MAKE NODE VISIBLE WHEN THERE ARE AGENTS
			  qtNodes.get(pID).visible = true;
			  
			  // ** System.out.print("---- [" + parentNode.ID + "] Agent Indices: ");
			  for(int i=0; i<qtNodes.get(pID).agents.size(); i++)	  
			    // ** System.out.print(qtNodes.get(pID).agents.get(i).id + " ");	  
			  // ** System.out.println();
			  
			  // ** System.out.print("---- [" + parentNode.ID + "] Environment Indices: ");
			  //for(int i=0; i<qtNodes.get(pID).emitters.size(); i++)	  
			    // ** System.out.print(qtNodes.get(pID).emitters.get(i).id + " ");	  
			  // ** System.out.println();
			  
			  // store nodeID in agent
			  nodeID = parentNode.ID;	// return the parent ID as the last layer
			}
			else	// is leaf, over threshold, not maxLevel (can still divide)
			{
			  // ** System.out.println("---- [" + parentNode.ID + "] THIRD ELSE -- dividing " + parentNode.ID);
			  
			  // %%--------------------------------------------- divide this node
			  boolean div = divideNode(qtNodes.get(pID));
			  qtNodes.get(pID).nodeType = NodeType.branch;
			  
			  // ** System.out.println("---- divide result: " + div);
			  //// ** System.out.println((*parentNode).left);
			  
			  // which child node is this in?
			  nodeID = inWhichNodeBoundary(qtNodes.get(pID), obj.position);
			  // ** System.out.println("---- divided new nodeID: " + nodeID);
			  
			  
			  // recurse this node...
			  CQuadTreeNode tempNode = qtNodes.get(getArrayIndexOfNode(nodeID));
			  // ** System.out.println(">>>> THIRD ELSE recurse placeObj:" + obj.id + " into node " + nodeID); 
			  placeObjectInNode(tempNode, obj); 
			  //// ** System.out.println(">>>> THIRD ELSE SIMPLY PUT " + objectID + " into " + nodeID);
			  //qtNodes.get(getArrayIndexOfNode(nodeID)).agents.add(objectID);
			  
			  
			  // ------------------------ transfer all agents in this node to new node ----------------
			  // CQuadTree don't need to construct a INFONodeTransfer because all is done in house
			  
			  // ** System.out.println("-------------------------.>>>> NODE TRANSFER from " + parentNode.ID + " to " + nodeID);
			  //INFONodeTransfer info = new INFONodeTransfer();
			  //info.nodeID = nodeID;	// the nodeID is the new nodeID gotten from inWhichNodeBoundary()
			  //info.parentID = parentNode.ID;ndary
			  // ---------------- **** add the incoming object first
			  
			  // ** System.out.print("------------------------- agent size " + qtNodes.get(pID).agents.size() + "::");
			  for(int i=0; i < qtNodes.get(pID).agents.size(); i++)	// tctransfer agent indices
			  {  
				  // add to next child node of pID
				  CWorldObject wObj = qtNodes.get(pID).agents.get(i);		// get agent ID
				  nodeID = inWhichNodeBoundary(qtNodes.get(pID), wObj.position); // test agent against child of pID
				  qtNodes.get(pID).agents.get(i).nodeID = nodeID;
				  qtNodes.get(getArrayIndexOfNode(nodeID)).agents.add(qtNodes.get(pID).agents.get(i));		  
				  // ** System.out.print(qtNodes.get(pID).agents.get(i).id + ", ");				  
			  }
			  // ** System.out.println();
			  
			  // ** System.out.print("------------------------- emitter size " + qtNodes.get(pID).agents.size() + "::");
			  for(int i=0; i < qtNodes.get(pID).emitters.size(); i++)	// transfer agent indices
			  {  
				// add to next child node of pID
				  CWorldObject wObj = qtNodes.get(pID).emitters.get(i);		// get agent ID
				  nodeID = inWhichNodeBoundary(qtNodes.get(pID), wObj.position); // test agent against child of pID
				  qtNodes.get(pID).emitters.get(i).nodeID = nodeID;
				  qtNodes.get(getArrayIndexOfNode(nodeID)).emitters.add(qtNodes.get(pID).emitters.get(i));	  
				  // ** System.out.print(qtNodes.get(pID).emitters.get(i).id + ", ");				  
			  }
			  // ** System.out.println();
			  
			  // clear pID agents and emitters after transfers
			  qtNodes.get(pID).agents.clear();
			  qtNodes.get(pID).emitters.clear();
			  
			  // ----------------- MAKE NODE INVISIBLE WHEN THERE ARE AGENTS
			  qtNodes.get(pID).visible = false;
			  /*
			  for(int i=0; i < evtNodeChangeRegister.size(); i++)	// dispatch to all the listeners
			    evtNodeChangeRegister[i].eventFunction(&info);
			  */			  
			  
			 
			  
			  // ------------------------ transfer all agents in this node to new node ----------------
			  //TrophicNetwork.transferAgent(info);	// an event replacement for c++ version			  
			}
	      }
	      else	// is leaf, not over divThreshold -- STORE THE OBJECT IN THIS NODE
	      {
			// ** System.out.println("--[" + parentNode.ID + "] SECOND ELSE -- NOT OVER THRESHOLD --");
			// ** System.out.println("-- agents placed in node " + parentNode.ID);
			
			// transfer my parent's indices to me AND my siblings **********
			// depending on which quad they are in
			// add this current object to this indice			
			
			// decide which indices to put the object index in
			if (obj.objectType == ObjectType.agent)
			{
				// ** System.out.println("----object is stored as AGENT TYPE");
				obj.nodeID = pID; // assign this node's id to obj
				qtNodes.get(pID).agents.add((COrganism) obj);  
				//parentNode.agents.add(objectID);	// store agentID in this node			    
			}
			else if (obj.objectType == ObjectType.emitter)
			{				  
				//parentNode.emitters.add(objectID);	// store agentID in this node
				// ** System.out.println("----object is stored as ENVIRONMENT TYPE");
				obj.nodeID = pID; // assign this node's id to obj
				qtNodes.get(pID).emitters.add((CEmitter) obj);			    
			}
			
			// ----------------- MAKE NODE VISIBLE WHEN THERE ARE AGENTS
			qtNodes.get(pID).visible = true;
			
			/*
			for(int i=0; i<qtNodes.get(pID).agents.size(); i++)	
				qtNodes.get(pID).agents.add(qtNodes.get(pID).agents.get(i)); 
			
			for(int i=0; i<qtNodes.get(qtNodes.get(pID).agents.size()).emitters.size(); i++)	
				qtNodes.get(pID).emitters.add(qtNodes.get(pID).emitters.get(i)); 
			
			// clear parent's parent's indices
			qtNodes.get(pID).agents.clear();
			qtNodes.get(pID).emitters.clear();
			*/
			
			// decide which indices to put the object index in
			/*
			if (objectType == ObjectType.agent)
			{
				qtNodes.get(pID).agents.add(objectID);	// store agentID in this node
				// ** System.out.println("--object is AGENT TYPE");
			}
			else if (objectType == ObjectType.environment)
			{
				qtNodes.get(pID).emitters.add(objectID);	// store agentID in this node
			  // ** System.out.println("--object is ENVIRONMENT TYPE");
			}			
			*/
			
			// ** System.out.print("--[" + pID + "] Agent Indices: ");
			//for(int i=0; i<qtNodes.get(pID).agents.size(); i++)	
			  // ** System.out.print(qtNodes.get(pID).agents.get(i).id + " ");	
			// ** System.out.println();
			
			// ** System.out.print("--[" + pID + "] Environment Indices: ");
			//for(int i=0; i<qtNodes.get(pID).emitters.size(); i++)	
			  // ** System.out.print(qtNodes.get(pID).emitters.get(i).id + " ");
			// ** System.out.println("");
			
			// simply put agent here
			// store agentID in this node
			// store nodeID in agent
			
			nodeID = pID;	// return the parent ID as the last layer
	      }
	  }

	  return nodeID;
	}	
	
	public static int inWhichNodeBoundary(CQuadTreeNode parentNode, Point3D pos)
	{
	  // ** System.out.println("[" + pos.x + " " + pos.y + " " + pos.z + "]-----------------------------------.> inWhichNodeBoundary: ");
	  int _childID = -1;	// -1 means no node found
	  
	  /*
	  // ** System.out.println("childIndex 0: " + parentNode.childIndex[0]);
	  // ** System.out.println("childIndex 1: " + parentNode.childIndex[1]);
	  // ** System.out.println("childIndex 2: " + parentNode.childIndex[2]);
	  // ** System.out.println("childIndex 3: " + parentNode.childIndex[3]);
	  // ** System.out.println("parentNode bottom: " + parentNode.bottom);
	  */
	  
	  if ( // NW
	      (pos.y >= qtNodes.get(parentNode.childIndex[0]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[0]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[0]).right) &&
	      (pos.x >= qtNodes.get(parentNode.childIndex[0]).left)
	     )
	  {
	    // ** System.out.println("NW Quadrant 0 - nodeID:" + parentNode.childIndex[0]);
	    /*
	    // ** System.out.println("top: " + qtNodeArray[parentNode.childIndex[0]].top);
	    // ** System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[0]].bottom);  
	    // ** System.out.println("left: " + qtNodeArray[parentNode.childIndex[0]].left);
	    // ** System.out.println("right: " + qtNodeArray[parentNode.childIndex[0]].right);
	    */
	    _childID = parentNode.childIndex[0];    
	  } else if ( // SW
	      (pos.y > qtNodes.get(parentNode.childIndex[1]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[1]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[1]).right) &&
	      (pos.x >= qtNodes.get(parentNode.childIndex[1]).left)
	     )
	  {
	    // ** System.out.println("SW Quadrant 1 - nodeID:" + parentNode.childIndex[1]);
	    /*
	    // ** System.out.println("top: " + qtNodeArray[parentNode.childIndex[1]].top);
	    // ** System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[1]].bottom);  
	    // ** System.out.println("left: " + qtNodeArray[parentNode.childIndex[1]].left);
	    // ** System.out.println("right: " + qtNodeArray[parentNode.childIndex[1]].right);
	    */
	    _childID = parentNode.childIndex[1];    
	  } else if ( // NE
	      (pos.y >= qtNodes.get(parentNode.childIndex[2]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[2]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[2]).right) &&
	      (pos.x > qtNodes.get(parentNode.childIndex[2]).left)
	     )
	  {
	    // ** System.out.println("NE Quadrant 2 - nodeID:" + parentNode.childIndex[2]);
	    /*    
	    // ** System.out.println("top: " + qtNodeArray[parentNode.childIndex[2]).top);
	    // ** System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[2]).bottom);  
	    // ** System.out.println("left: " + qtNodeArray[parentNode.childIndex[2]).left);
	    // ** System.out.println("right: " + qtNodeArray[parentNode.childIndex[2]).right);
	    */
	    _childID = parentNode.childIndex[2];    
	  } else if (
	      (pos.y > qtNodes.get(parentNode.childIndex[3]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[3]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[3]).right) &&
	      (pos.x > qtNodes.get(parentNode.childIndex[3]).left)      
	     )
	  {  // SE
	    // ** System.out.println("SE Quadrant 3 - nodeID:" + parentNode.childIndex[3]);
	    /*
	    // ** System.out.println("top: " + qtNodeArray[parentNode.childIndex[3]].top);
	    // ** System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[3]].bottom);  
	    // ** System.out.println("left: " + qtNodeArray[parentNode.childIndex[3]].left);
	    // ** System.out.println("right: " + qtNodeArray[parentNode.childIndex[3]].right);
	    */
	    _childID = parentNode.childIndex[3];    
	  }
	  
	  // TODO
	  //if (_childID == -1)
		//  System.out.println("can't find child quad to put child: " + parentNode.ID);
	  
	  return _childID;
	}
	
	public void createQuads(CQuadTreeNode thisNode)
	{
						
		NodeType theCurrentNodeType;
		  
		// testing for leaf and assigning nodeType based on the size of the quad
	  if ((thisNode.bottom - thisNode.top) == minSizeOfQuad)
	  {	theCurrentNodeType = NodeType.leaf;	}
	  else
	  {	theCurrentNodeType = NodeType.branch;	}
		
	  
	  // --------------------------------------------------------------.> CREATE THIS NODE
	  // ** System.out.println("-----------------------------------  node[" + thisNode.ID + "] parent[" + thisNode.parentID + "]");
	  thisNode.nodeType = theCurrentNodeType;
	  
	  // calculate the width and height of this node from the bounds passed in
	    // note that all quadrants (child nodes) in the level has the same width and height
	    thisNode.width = 	thisNode.bottom - thisNode.top;
	    thisNode.height = 	thisNode.right - thisNode.left;
	    
	    // receives boundary parameters for this node
	    thisNode.top = 			thisNode.top;
	    thisNode.bottom = 	thisNode.bottom;
	    thisNode.left = 			thisNode.left;
	    thisNode.right = 		thisNode.right;
	    
	    // calculate central axial position of this node (centre of quad boundary)
	    thisNode.position.x =	((thisNode.left + thisNode.right) / 2);
	    thisNode.position.z =	((thisNode.top + thisNode.bottom) / 2);
	    
	    // ** System.out.println("nodeType:: " + thisNode.nodeType + "   | width:" + thisNode.width + " height:" + 
	    //thisNode.height + " | top:" + thisNode.top + " bottom:" + thisNode.bottom + " left:" + thisNode.left + " right:" + thisNode.right);
	    // ** System.out.println("central position: " + thisNode.position.x + " " + thisNode.position.y + " " + thisNode.position.z);
	  
	    // --------------------------------------------------------------.> CREATE CHID NODES!
	    if(theCurrentNodeType == NodeType.leaf)	// if leaf, quit
	    {
	    	// ** System.out.println("A LEAF NODE -- STOP CREATING FURTHER CHILD");
	      return;
	    }
	    else														// otherwise, create its children nodes
	    {
	    	// ** System.out.println("A NODE -- CREATE FURTHER CHILD");
	    	
	    	// setup first
	    	int childWidth = thisNode.width / 2;
	        int childHeight = thisNode.height / 2;	    	
	    	
	    //  ----------------------------------------------------------------------------------------------- QUADRANT 0 (NW)
	        //(int id, int nodeType, int parentID, int layerID, int top, int btm, int lf, int rg, int width, int height)
	        nodeIndex++; 		// go to next index
	        thisNode.childIndex[0] = nodeIndex;
	        CQuadTreeNode qtnode0 = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	        	        	        
	        // calculate boundary
	        qtnode0.top = 		thisNode.top;
	        qtnode0.bottom = 	thisNode.top + childHeight;
	        qtnode0.left = 		thisNode.left;
	        qtnode0.right = 		thisNode.left + childWidth;	        
	        
	        qtnode0.layerID = 	thisNode.layerID+1;		// the next layer now
	        
	        //// ** System.out.println("pNode.branchIndex[0]: " + nodeIndex;
	        //// ** System.out.println(" | top:" + childtop + " bottom:" + childbottom + " left:" + childleft + " right:" + childright);
	        
	        qtNodes.add(qtnode0);
	        
	        // ----------------------------------------------------------------------------------------------- QUADRANT 1 (SW)
	        nodeIndex++; 		// go to next index
	        thisNode.childIndex[1] = nodeIndex;		
	        CQuadTreeNode qtnode1 = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	       	
	        // calculate boundary
	        qtnode1.top = 		thisNode.bottom - childHeight;
	        qtnode1.bottom = 	thisNode.top + childHeight;
	        qtnode1.left = 		thisNode.left;
	        qtnode1.right = 		thisNode.left + childWidth;	        	        

	        qtnode1.layerID = 	thisNode.layerID+1;		// the next layer now

	        qtNodes.add(qtnode1);
	        	        
	        // ----------------------------------------------------------------------------------------------- QUADRANT 2 (NE)
	        nodeIndex++; 				// go to next index
	        thisNode.childIndex[2] = nodeIndex;
	        CQuadTreeNode qtnode2 = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	        //// ** System.out.println("pNode.childIndex[2]: " + nodeIndex);
	        
	        // calculate boundary
	        qtnode2.top = 		thisNode.top;
	        qtnode2.bottom = 	thisNode.top + qtnode2.height;
	        qtnode2.left = 		thisNode.right - qtnode2.width;
	        qtnode2.right = 		qtnode2.left + qtnode2.width;	        

	        qtnode2.layerID = 	thisNode.layerID+1;		// the next layer now	        

	        qtNodes.add(qtnode2);
	        
	        // ----------------------------------------------------------------------------------------------- QUADRANT 3 (SE)
	        nodeIndex++; 				// go to next index
	        thisNode.childIndex[3] = nodeIndex;
	        CQuadTreeNode qtnode3 = new CQuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	        //// ** System.out.println("pNode.childIndex[3]: " + nodeIndex);
	        
	        // calculate boundary
	        qtnode3.top = 		thisNode.bottom - qtnode3.height;
	        qtnode3.bottom = 	thisNode.top + qtnode3.height;
	        qtnode3.left = 		thisNode.right - qtnode3.width;
	        qtnode3.right = 	qtnode3.left + qtnode3.width;
	        
	        qtnode3.layerID = 	thisNode.layerID+1;		// the next layer now
	        
	        qtNodes.add(qtnode3);
	    }
	    
	}
	
	public void paint(Graphics g)
	{
		//drawQT = false;
		if(drawQT)
		{
			Font font = new Font("Arial", Font.PLAIN, 9);	    
		    g.setFont(font);
		    
			for(int i=0; i<qtNodes.size(); i++)
			{
				CQuadTreeNode node = qtNodes.get(i);
				g.setColor( Color.red );	
				g.drawLine(node.left, node.top, node.left, node.bottom);
				g.drawLine(node.left, node.top, node.right, node.top);
				g.drawLine(node.right, node.top, node.right, node.bottom);
				g.drawLine(node.left, node.bottom, node.right, node.bottom);
				g.drawString(""+node.ID, node.position.x, node.position.y);
			}
		}
		
		for(int i=0; i < qtNodes.size(); i++)
		{
			// emitters first
			for(int j=0; j < qtNodes.get(i).emitters.size(); j++)
			{
				// emitters needs to affect one another too
				qtNodes.get(i).emitters.get(j).paint(g);
			}
			
			// for each node, get agents and emitters and interact them
			for(int j=0; j < qtNodes.get(i).agents.size(); j++)
			{
				qtNodes.get(i).agents.get(j).paint(g);
			}
		}
	}
	
	public int calculateNodeSize(int level)
	{	  
	 int numNodes = 0;
	  
	  for(int i=0; i<level; i++)
	  {		
	    numNodes += Math.pow(4, i);
	    // ** System.out.println("Level [" + i + "] total nodes now is: " + numNodes);	    
	  }
	  
	  // ** System.out.println("********************* TOTAL NUMBER OF NODES: " + numNodes);	  
	  
	  return numNodes;	
	}
	
	public float calculateMinQuadSize(float width, int level)
	{
	  float size = width;
	  
	  for(int i=0; i<level-1; i++)  
	    size *= 0.5f;  
	  
	  // ** System.out.println("********************* MIN Quad Size:" + size);
	  
	  return size;
	}
	
	public void resetNodeVisibility()
	{
		for(int i = 0; i < qtNodes.size(); i++)
		{
			((CQuadTreeNode)qtNodes.get(i)).visible = false;
		}
	}
	
	public static void reportNodeBranchIndex()
	{
	  // ** System.out.println("----------------------------->> REPORTING NODE BRANCH INDICES");
	  for(int i=0; i<nodeCount+1; i++)
	  {
	    if(qtNodes.get(i).nodeType == NodeType.leaf)	// IF LEAF 
	    {
	      // ** System.out.print("    leaf ---->> qtNodes[" + i + "]");
	      
	      // check to see if there are agents or emitters
	      if (qtNodes.get(i).agents.size() >=1)		// AGENTS
	      {
			// ** System.out.print(" | indices: ");
			//for(int k=0; k<qtNodes.get(i).agents.size(); k++)
			  // ** System.out.print(qtNodes.get(i).agents.get(k).id + " ");
	      }
	      
	      if (qtNodes.get(i).emitters.size() >=1)	// EMITTERS
	      {
			// ** System.out.print(" | emitters: ");
			//for(int k=0; k<qtNodes.get(i).emitters.size(); k++)
			  // ** System.out.print(qtNodes.get(i).emitters.get(k).id + " ");
	      }
	      // ** System.out.println("");
	    }
	    else											// IF NOT EAF
	    {
	      // ** System.out.println("qtNodes[" + i + "] visible:" + qtNodes.get(i).visible);

	      for(int j=0; j<4; j++)
	      {	
    	  	// ** System.out.println("----------- child branchIndex[" + j + "]:: " + 
					//qtNodes.get(i).childIndex[j] + 
					//" visible: " + qtNodes.get(qtNodes.get(i).childIndex[j]).visible);
    	  	
    	  	// IF NOT LEAF THEN THERE IS NO AGENTS/EMITTERS... THE LINE BELOW IS NOT NEEDED
			/*
    	  	// check to see if there is child
			if (qtNodes.get(i).agents.size() >=1)	// AGENTS
			{
			  // ** System.out.print(" | indices: ");
			  for(int k=0; k<qtNodes.get(i).agents.size(); k++)
			  {
			    // ** System.out.print(qtNodes.get(i).agents.get(k).id + " ");
			  }	  
			}
			
			if (qtNodes.get(i).emitters.size() >=1)	// EMITTERS
			{
				// ** System.out.print(" | emitters: ");
				for(int k=0; k<qtNodes.get(i).emitters.size(); k++)
				  // ** System.out.print(qtNodes.get(i).emitters.get(k).id + " ");
			}
			// ** System.out.println("");
			*/
	      }	
	    }
	  }
	}
	
	public void printAgentIDs()
    {
    	// ** System.out.print("agent iDs:");
    	for (int i=0; i<qtNodes.size(); i++)
    	{
	    	for (int j=0; j<qtNodes.get(i).agents.size(); j++)
	    	{
	    		// ** System.out.print(qtNodes.get(i).agents.get(j).id + "(" + qtNodes.get(i).agents.get(j).nodeID + ") ");
	    	}
   	 	}
    	// ** System.out.print(" | ");
    	for (int i=0; i<qtNodes.size(); i++)
    	{	    
	    	for (int j=0; j<qtNodes.get(i).emitters.size(); j++)
	    	{
	    		// ** System.out.print(qtNodes.get(i).emitters.get(j).id + "(" + qtNodes.get(i).emitters.get(j).nodeID + ") ");
	    	}
    	}
    	// ** System.out.println("");
    }
	
	public int getAgentSize()
	{
		int size=0;
		for(int i=0; i<nodeCount+1; i++)
		{
			if(qtNodes.get(i).nodeType == NodeType.leaf)	// IF LEAF 
			{									
				size += qtNodes.get(i).agents.size();				
			}
		}
		return size;
	}
	
	public int getEmitterSize()
	{
		int size=0;
		for(int i=0; i<nodeCount+1; i++)
		{
			if(qtNodes.get(i).nodeType == NodeType.leaf)	// IF LEAF 
			{									
				size += qtNodes.get(i).emitters.size();				
			}
		}
		return size;
	}
	
	public int size()
	{
		return nodeSize;
	}
	
}
