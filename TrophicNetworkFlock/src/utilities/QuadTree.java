package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class QuadTree {
	private static int nodeCount = 0;	// for divideNode use
	private static int nodeSize; // for createNode use
	private static int nodeID = 0;	// "static" is to make sure that nodeID isn't reinitialised - for placeObjectInNode()
	
	private static int divideThreshold;
	private static int maxLevel;
	public float minSizeOfQuad;
	
	public static int nodeIndex; // nodeIndex is global for maintaining value - because this is in a recursive function
	public static ArrayList<QuadTreeNode> qtNodes = new ArrayList<QuadTreeNode>();
	
	public QuadTree(int top, int bottom, int left, int right, int maxLevels, int divideThreshold)
	{		
		// calculate the number of nodes for memory allocation
		this.nodeSize = calculateNodeSize(maxLevels);
		this.divideThreshold = divideThreshold;
		this.maxLevel = maxLevels;
		
		minSizeOfQuad = calculateMinQuadSize(bottom - top, maxLevels);	// minimum size of quad at deepest level
		
		// create root node and add it to quadtreenode arraylist
		//(int id, int nodeType, int parentID, int layerID, int top, int btm, int lf, int rg, int width, int height)
		QuadTreeNode rootnode = new QuadTreeNode(0, 0, 0, top, bottom, left, right, left+right, top+bottom);		
		qtNodes.add(rootnode);
	}
	
	public static boolean divideNode(QuadTreeNode thisNode)
	{
		boolean divideCompleted = false;
		System.out.println("-----------------------------------.> DIVIDING NODE " + thisNode.ID);
		
		// calculate the width and height of this node from the bounds passed in
		// note that all quadrants (child nodes) in the level has the same width and height
		int childW = thisNode.width / 2;
		int childH = thisNode.height / 2;

		// if the division is the last leaf, then divide
		  if (thisNode.nodeType == NodeType.leaf)	// these child nodes are definitely the last division
		  {    
		    thisNode.nodeType = NodeType.branch;	// change the parent node into a QT_NODE type
		    
		    // create a temporary qtnode that can be filled up for each quadrant and added to the qtNodes arraylist
		    QuadTreeNode qtnodeNew = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
		    
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
		    
		   // System.out.println("NW Quadrant Node 0 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		    //System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + qtnodeNew.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		    //System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    // -------------------------- SECOND QUADRANT -. 1 SW
		    qtnodeNew = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
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
		    System.out.println("thisNode.Top " + thisNode.top);
		    System.out.println("ChildH " + childH);
		    System.out.println("ChildW " + childW);
		    System.out.println("TOP " + qtnodeNew.top);
		    System.out.println("BOTTOM " + qtnodeNew.bottom);
		    */
		    
		    // calculate central axial position of this node (centre of quad boundary)
		    qtnodeNew.position.x =	(thisNode.left + (childW / 2));
		    qtnodeNew.position.y =	(thisNode.bottom - (childH / 2));
		    
		    qtnodeNew.nodeType = NodeType.leaf;	// turn this into leaf...
		    qtnodeNew.layerID = thisNode.layerID + 1;
		    
		    //System.out.println("SW Quadrant Node 1 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		   // System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + thisNode.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		    //System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    // -------------------------- THIRD QUADRANT -. 2 NE
		    qtnodeNew = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
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
		    
		    //System.out.println("NE Quadrant Node 2 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		    //System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + qtnodeNew.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		    //System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    // -------------------------- FOURTH QUADRANT -. 3 SE
		    qtnodeNew = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
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
		    
		    //System.out.println("SE Quadrant Node 3 | parentID: " + qtnodeNew.parentID + " | ID: " + nodeCount);
		    //System.out.println("nodeType:: " + qtnodeNew.nodeType + "   | width:" + qtnodeNew.width + " height:" + qtnodeNew.height + " | top:" + qtnodeNew.top + " bottom:" + qtnodeNew.bottom + " left:" + qtnodeNew.left + " right:" + qtnodeNew.right);
		   // System.out.println("central position: " + qtnodeNew.position.x + " " + qtnodeNew.position.y + " " + qtnodeNew.position.z);
		    
		    qtNodes.add(qtnodeNew);
		    
		    divideCompleted = true;
		  }
		  
		return divideCompleted;
	}
	
	public static int[] getNodeIndices(int nodeID)
	{	
		QuadTreeNode node = qtNodes.get(getArrayIndexOfNode(nodeID));
		
		int[] ind = new int[node.indices.size()];
				
		for(int i=0; i < node.indices.size(); i++ )
		{
			ind[i] = node.indices.get(i);
		}
		
		return ind; 
	}
	
	public static int getArrayIndexOfNode(int nodeID)
	{
		int targetID = -1;
		//if (nodeID == -1)
			
	    for(int i=0; i< qtNodes.size(); i++)
	    {
	    	if(qtNodes.get(i).ID == nodeID)
	    	{
	    		targetID = i;
	    		break;
	    	}	
	    }
	    
	    return targetID;
	}
	
	public static ArrayList<Integer> rangeQuery(Point3D pos, float radius)
	{
		System.out.println("@@------------------------------------- RANGE QUERY ------->>");
		ArrayList<Integer> rIndices = new ArrayList<Integer>();
		
		for(int i=0; i < qtNodes.size(); i++ )
		{
			if(qtNodes.get(i).nodeType == NodeType.leaf)	// leaf
			{
				float dx = qtNodes.get(i).position.x - pos.x;
				float dy = qtNodes.get(i).position.y - pos.y;
				float dist = (float) Math.sqrt((dx*dx) + (dy*dy));
				
				// collect indices in this node if within radius
				if(dist <= radius)
				{
					System.out.print("NODE[" + qtNodes.get(i).ID + "]:: ");
					// add agent indices
					for(int j=0; j<qtNodes.get(i).indices.size(); j++)
					{
						rIndices.add(qtNodes.get(i).indices.get(j));
						System.out.print(qtNodes.get(i).indices.get(j) + ", ");
					}
					System.out.println();
					// add env indices
					
				}
			}
		}
		
		System.out.println("@@------------------------------------- RANGE QUERY ------->>");
		
		return rIndices;
	}
	
	// recursive function - go in until you find the leaf
	public static int placeObjectInNode(QuadTreeNode parentNode, Point3D pos, int objectID, ObjectType objectType)
	{  
	  System.out.println("----------------.>>> PLACEMENT FOR  OBJECT[" + objectID + "] node[" + parentNode.ID + "] node.prntID[" + parentNode.parentID + "]");
	  
	  int pID = getArrayIndexOfNode(parentNode.ID);
	  
	  // check to see if this is the last layer possible
	  if(parentNode.nodeType != NodeType.leaf)	// is NOT leaf go in further
	  {
	    System.out.println("[" + parentNode.ID + "]" + " FIRST IF ELSE -- NOT LEAF GO DEEPER >>");

	    // which child node of the parent is this object in?
	    nodeID = inWhichNodeBoundary(parentNode, pos);
	    
	    // recurse the node where the agent is in
	    QuadTreeNode tempNode = qtNodes.get(getArrayIndexOfNode(nodeID));
	    System.out.println(">>>> FIRST IF ELSE recurse placeObj:" + objectID + " into node " + nodeID); 
	    placeObjectInNode(tempNode, pos, objectID, objectType);   
	  }
	  else	// if node is currently a leaf
	  {
	      // calculate size of agent and environment indices
	      int indexSize = parentNode.indices.size() + parentNode.envIndices.size();
	      
	      System.out.println("[" + parentNode.ID + "]" + "FIRST ELSE -- is leaf | index size: [" +  
	      parentNode.indices.size() + " + " + parentNode.envIndices.size() + "] = " + indexSize);
	      
	      // check if node is occupied, divideThreshold default = 1      
	      if (indexSize >= divideThreshold)	// ***** if over threshold	    	  
	      {
			System.out.println("--[" + parentNode.ID + "]" + " SECOND IF ELSE -- NOT OVER THRESHOLD -- agent size: " + parentNode.indices.size() + " | env size: " + parentNode.envIndices.size());		
			System.out.println(">>>>>>>>>> total agent+env size: " + indexSize + " >= " + divideThreshold);
			
			if(parentNode.layerID == maxLevel)	// and is maxLevel (no more division possible)
			{
			  System.out.println("----[" + parentNode.ID + "]" + " THIRD IF ELSE -- is maxLevel | layer ID: " + parentNode.layerID);
			  System.out.println("---- [" + parentNode.ID + "] Objects placed in this node");
			  
			  // decide which indices to put the object index in
			  if (objectType == ObjectType.agent)
			  {
				System.out.println("----object is stored as AGENT TYPE");
				qtNodes.get(pID).indices.add(objectID);  
				//parentNode.indices.add(objectID);	// store agentID in this node			    
			  }
			  else if (objectType == ObjectType.emitter)
			  {				  
			    //parentNode.envIndices.add(objectID);	// store agentID in this node
				System.out.println("----object is stored as ENVIRONMENT TYPE");
			    qtNodes.get(pID).envIndices.add(objectID);			    
			  }
			  
			  // ----------------- MAKE NODE VISIBLE WHEN THERE ARE AGENTS
			  qtNodes.get(pID).visible = true;
			  
			  System.out.print("---- [" + parentNode.ID + "] Agent Indices: ");
			  for(int i=0; i<qtNodes.get(pID).indices.size(); i++)	  
			    System.out.print(qtNodes.get(pID).indices.get(i) + " ");	  
			  System.out.println();
			  
			  System.out.print("---- [" + parentNode.ID + "] Environment Indices: ");
			  for(int i=0; i<qtNodes.get(pID).envIndices.size(); i++)	  
			    System.out.print(qtNodes.get(pID).envIndices.get(i)+ " ");	  
			  System.out.println();
			  
			  // store nodeID in agent
			  nodeID = parentNode.ID;	// return the parent ID as the last layer
			}
			else	// is leaf, over threshold, not maxLevel (can still divide)
			{
			  System.out.println("---- [" + parentNode.ID + "] THIRD ELSE -- dividing " + parentNode.ID);
			  
			  // %%--------------------------------------------- divide this node
			  boolean div = divideNode(qtNodes.get(pID));
			  qtNodes.get(pID).nodeType = NodeType.branch;
			  
			  System.out.println("---- divide result: " + div);
			  //System.out.println((*parentNode).left);
			  
			  // which child node is this in?
			  nodeID = inWhichNodeBoundary(qtNodes.get(pID), pos);
			  System.out.println("---- divided new nodeID: " + nodeID);
			  
			  
			  // recurse this node...
			  QuadTreeNode tempNode = qtNodes.get(getArrayIndexOfNode(nodeID));
			  System.out.println(">>>> THIRD ELSE recurse placeObj:" + objectID + " into node " + nodeID); 
			  placeObjectInNode(tempNode, pos, objectID, objectType); 
			  //System.out.println(">>>> THIRD ELSE SIMPLY PUT " + objectID + " into " + nodeID);
			  //qtNodes.get(getArrayIndexOfNode(nodeID)).indices.add(objectID);
			  
			  
			  // ------------------------ transfer all agents in this node to new node ----------------
			  System.out.println("-------------------------.>>>> NODE TRANSFER from " + parentNode.ID + " to " + nodeID);
			  INFONodeTransfer info = new INFONodeTransfer();
			  info.nodeID = nodeID;	// the nodeID is the new nodeID gotten from inWhichNodeBoundary()
			  info.parentID = parentNode.ID;
			  // ---------------- **** add the incoming object first
			  /*
			  if (objectType == ObjectType.agent)
			  {
				System.out.println("----object is stored as AGENT TYPE");
				qtNodes.get(nodeID).indices.add(objectID);
				info.indices.add(objectID);
			  }
			  else if (objectType == ObjectType.environment)
			  {	
				System.out.println("----object is stored as ENVIRONMENT TYPE");
				qtNodes.get(nodeID).envIndices.add(objectID);
				info.envIndices.add(objectID);			    
			  }
			  */
			  // qtNodes.get(pID).indices.add(i);	// add to new node
			  
			  System.out.print("------------------------- size " + qtNodes.get(pID).indices.size() + "::");
			  for(int i=0; i < qtNodes.get(pID).indices.size(); i++)	// transfer agent indices
			  {  
				  info.indices.add(qtNodes.get(pID).indices.get(i));	// for changing agent.nodeID			  
				  System.out.print(qtNodes.get(pID).indices.get(i)+ ", ");				  
			  }
			  System.out.println();
			  
			  for(int i=0; i < qtNodes.get(pID).envIndices.size(); i++)	// transfer environment indices
			    info.envIndices.add(qtNodes.get(pID).envIndices.get(i));	  
			  
			  // clear parent after transfers
			  qtNodes.get(pID).indices.clear();
			  qtNodes.get(pID).envIndices.clear();
			  
			  // ----------------- MAKE NODE VISIBLE WHEN THERE ARE AGENTS
			  qtNodes.get(pID).visible = false;
			  /*
			  for(int i=0; i < evtNodeChangeRegister.size(); i++)	// dispatch to all the listeners
			    evtNodeChangeRegister[i].eventFunction(&info);
			  */			  
			  
			 
			  
			  // ------------------------ transfer all agents in this node to new node ----------------
			  TrophicNetwork.transferAgent(info);	// an event replacement for c++ version			  
			}
	      }
	      else	// is leaf, not over divThreshold -- STORE THE OBJECT IN THIS NODE
	      {
			System.out.println("--[" + parentNode.ID + "] SECOND ELSE -- NOT OVER THRESHOLD --");
			System.out.println("-- agents placed in node " + parentNode.ID);
			
			// transfer my parent's indices to me AND my siblings **********
			// depending on which quad they are in
			// add this current object to this indice
			qtNodes.get(pID).indices.add(objectID);
			
			// ----------------- MAKE NODE VISIBLE WHEN THERE ARE AGENTS
			qtNodes.get(pID).visible = true;
			
			/*
			for(int i=0; i<qtNodes.get(pID).indices.size(); i++)	
				qtNodes.get(pID).indices.add(qtNodes.get(pID).indices.get(i)); 
			
			for(int i=0; i<qtNodes.get(qtNodes.get(pID).indices.size()).envIndices.size(); i++)	
				qtNodes.get(pID).envIndices.add(qtNodes.get(pID).envIndices.get(i)); 
			
			// clear parent's parent's indices
			qtNodes.get(pID).indices.clear();
			qtNodes.get(pID).envIndices.clear();
			*/
			
			// decide which indices to put the object index in
			/*
			if (objectType == ObjectType.agent)
			{
				qtNodes.get(pID).indices.add(objectID);	// store agentID in this node
				System.out.println("--object is AGENT TYPE");
			}
			else if (objectType == ObjectType.environment)
			{
				qtNodes.get(pID).envIndices.add(objectID);	// store agentID in this node
			  System.out.println("--object is ENVIRONMENT TYPE");
			}			
			*/
			
			System.out.print("--[" + pID + "] Agent Indices: ");
			for(int i=0; i<qtNodes.get(pID).indices.size(); i++)	
			  System.out.print(qtNodes.get(pID).indices.get(i) + " ");	
			System.out.println();
			
			System.out.print("--[" + pID + "] Environment Indices: ");
			for(int i=0; i<qtNodes.get(pID).envIndices.size(); i++)	
			  System.out.print(qtNodes.get(pID).envIndices.get(i)+ " ");
			System.out.println("");
			
			// simply put agent here
			// store agentID in this node
			// store nodeID in agent
			
			nodeID = pID;	// return the parent ID as the last layer
	      }
	  }

	  return nodeID;
	}	
	
	public static int inWhichNodeBoundary(QuadTreeNode parentNode, Point3D pos)
	{
	  System.out.println("[" + pos.x + " " + pos.y + " " + pos.z + "]-----------------------------------.> inWhichNodeBoundary: ");
	  int _childID = -1;	// -1 means no node found
	  
	  /*
	  System.out.println("childIndex 0: " + parentNode.childIndex[0]);
	  System.out.println("childIndex 1: " + parentNode.childIndex[1]);
	  System.out.println("childIndex 2: " + parentNode.childIndex[2]);
	  System.out.println("childIndex 3: " + parentNode.childIndex[3]);
	  System.out.println("parentNode bottom: " + parentNode.bottom);
	  */
	  
	  if ( // NW
	      (pos.y >= qtNodes.get(parentNode.childIndex[0]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[0]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[0]).right) &&
	      (pos.x >= qtNodes.get(parentNode.childIndex[0]).left)
	     )
	  {
	    System.out.println("NW Quadrant 0 - nodeID:" + parentNode.childIndex[0]);
	    /*
	    System.out.println("top: " + qtNodeArray[parentNode.childIndex[0]].top);
	    System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[0]].bottom);  
	    System.out.println("left: " + qtNodeArray[parentNode.childIndex[0]].left);
	    System.out.println("right: " + qtNodeArray[parentNode.childIndex[0]].right);
	    */
	    _childID = parentNode.childIndex[0];    
	  } else if ( // SW
	      (pos.y > qtNodes.get(parentNode.childIndex[1]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[1]).bottom) &&      
	      (pos.x < qtNodes.get(parentNode.childIndex[1]).right) &&
	      (pos.x >= qtNodes.get(parentNode.childIndex[1]).left)
	     )
	  {
	    System.out.println("SW Quadrant 1 - nodeID:" + parentNode.childIndex[1]);
	    /*
	    System.out.println("top: " + qtNodeArray[parentNode.childIndex[1]].top);
	    System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[1]].bottom);  
	    System.out.println("left: " + qtNodeArray[parentNode.childIndex[1]].left);
	    System.out.println("right: " + qtNodeArray[parentNode.childIndex[1]].right);
	    */
	    _childID = parentNode.childIndex[1];    
	  } else if ( // NE
	      (pos.y >= qtNodes.get(parentNode.childIndex[2]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[2]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[2]).right) &&
	      (pos.x >= qtNodes.get(parentNode.childIndex[2]).left)
	     )
	  {
	    System.out.println("NE Quadrant 2 - nodeID:" + parentNode.childIndex[2]);
	    /*    
	    System.out.println("top: " + qtNodeArray[parentNode.childIndex[2]).top);
	    System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[2]).bottom);  
	    System.out.println("left: " + qtNodeArray[parentNode.childIndex[2]).left);
	    System.out.println("right: " + qtNodeArray[parentNode.childIndex[2]).right);
	    */
	    _childID = parentNode.childIndex[2];    
	  } else if (
	      (pos.y >= qtNodes.get(parentNode.childIndex[3]).top) &&
	      (pos.y <= qtNodes.get(parentNode.childIndex[3]).bottom) &&      
	      (pos.x <= qtNodes.get(parentNode.childIndex[3]).right) &&
	      (pos.x > qtNodes.get(parentNode.childIndex[3]).left)      
	     )
	  {  // SE
	    System.out.println("SE Quadrant 3 - nodeID:" + parentNode.childIndex[3]);
	    /*
	    System.out.println("top: " + qtNodeArray[parentNode.childIndex[3]].top);
	    System.out.println("bottom: " + qtNodeArray[parentNode.childIndex[3]].bottom);  
	    System.out.println("left: " + qtNodeArray[parentNode.childIndex[3]].left);
	    System.out.println("right: " + qtNodeArray[parentNode.childIndex[3]].right);
	    */
	    _childID = parentNode.childIndex[3];    
	  }
	  
	  return _childID;
	}
	
	public void createQuads(QuadTreeNode thisNode)
	{
						
		NodeType theCurrentNodeType;
		  
		// testing for leaf and assigning nodeType based on the size of the quad
	  if ((thisNode.bottom - thisNode.top) == minSizeOfQuad)
	  {	theCurrentNodeType = NodeType.leaf;	}
	  else
	  {	theCurrentNodeType = NodeType.branch;	}
		
	  
	  // --------------------------------------------------------------.> CREATE THIS NODE
	  System.out.println("-----------------------------------  node[" + thisNode.ID + "] parent[" + thisNode.parentID + "]");
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
	    
	    System.out.println("nodeType:: " + thisNode.nodeType + "   | width:" + thisNode.width + " height:" + 
	    thisNode.height + " | top:" + thisNode.top + " bottom:" + thisNode.bottom + " left:" + thisNode.left + " right:" + thisNode.right);
	    System.out.println("central position: " + thisNode.position.x + " " + thisNode.position.y + " " + thisNode.position.z);
	  
	    // --------------------------------------------------------------.> CREATE CHID NODES!
	    if(theCurrentNodeType == NodeType.leaf)	// if leaf, quit
	    {
	    	System.out.println("A LEAF NODE -- STOP CREATING FURTHER CHILD");
	      return;
	    }
	    else														// otherwise, create its children nodes
	    {
	    	System.out.println("A NODE -- CREATE FURTHER CHILD");
	    	
	    	// setup first
	    	int childWidth = thisNode.width / 2;
	        int childHeight = thisNode.height / 2;	    	
	    	
	    //  ----------------------------------------------------------------------------------------------- QUADRANT 0 (NW)
	        //(int id, int nodeType, int parentID, int layerID, int top, int btm, int lf, int rg, int width, int height)
	        nodeIndex++; 		// go to next index
	        thisNode.childIndex[0] = nodeIndex;
	        QuadTreeNode qtnode0 = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	        	        	        
	        // calculate boundary
	        qtnode0.top = 		thisNode.top;
	        qtnode0.bottom = 	thisNode.top + childHeight;
	        qtnode0.left = 		thisNode.left;
	        qtnode0.right = 		thisNode.left + childWidth;	        
	        
	        qtnode0.layerID = 	thisNode.layerID+1;		// the next layer now
	        
	        //System.out.println("pNode.branchIndex[0]: " + nodeIndex;
	        //System.out.println(" | top:" + childtop + " bottom:" + childbottom + " left:" + childleft + " right:" + childright);
	        
	        qtNodes.add(qtnode0);
	        
	        // ----------------------------------------------------------------------------------------------- QUADRANT 1 (SW)
	        nodeIndex++; 		// go to next index
	        thisNode.childIndex[1] = nodeIndex;		
	        QuadTreeNode qtnode1 = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	       	
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
	        QuadTreeNode qtnode2 = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	        //System.out.println("pNode.childIndex[2]: " + nodeIndex);
	        
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
	        QuadTreeNode qtnode3 = new QuadTreeNode(nodeIndex, thisNode.ID, 0, 0, 0, 0, 0, 0, 0);
	        //System.out.println("pNode.childIndex[3]: " + nodeIndex);
	        
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
		Font font = new Font("Arial", Font.PLAIN, 9);	    
	    g.setFont(font);
	    
		for(int i=0; i<qtNodes.size(); i++)
		{
			QuadTreeNode node = qtNodes.get(i);
			g.setColor( Color.red );	
			g.drawLine(node.left, node.top, node.left, node.bottom);
			g.drawLine(node.left, node.top, node.right, node.top);
			g.drawLine(node.right, node.top, node.right, node.bottom);
			g.drawLine(node.left, node.bottom, node.right, node.bottom);
			g.drawString(""+node.ID, node.position.x, node.position.y);
		}
	}
	
	public int calculateNodeSize(int level)
	{	  
	 int numNodes = 0;
	  
	  for(int i=0; i<level; i++)
	  {		
	    numNodes += Math.pow(4, i);
	    System.out.println("Level [" + i + "] total nodes now is: " + numNodes);	    
	  }
	  
	  System.out.println("********************* TOTAL NUMBER OF NODES: " + numNodes);	  
	  
	  return numNodes;	
	}
	
	public float calculateMinQuadSize(float width, int level)
	{
	  float size = width;
	  
	  for(int i=0; i<level-1; i++)  
	    size *= 0.5f;  
	  
	  System.out.println("********************* MIN Quad Size:" + size);
	  
	  return size;
	}
	
	public void resetNodeVisibility()
	{
		for(int i = 0; i < qtNodes.size(); i++)
		{
			((QuadTreeNode)qtNodes.get(i)).visible = false;
		}
	}
	
	public static void reportNodeBranchIndex()
	{
	  System.out.println("----------------------------->> REPORTING NODE BRANCH INDICES");
	  for(int i=0; i<nodeCount+1; i++)
	  {
	    if(qtNodes.get(i).nodeType == NodeType.leaf)	 
	    {
	      System.out.print("    leaf ---->> qtNodes[" + i + "]");
	      
	      // check to see if there is child
	      if (qtNodes.get(i).indices.size() >=1)
	      {
			System.out.print(" | indices: ");
			for(int k=0; k<qtNodes.get(i).indices.size(); k++)
			{
			  System.out.print(qtNodes.get(i).indices.get(k) + " ");
			}	  
			 
	      }
	      System.out.println("");
	    }
	    else
	    {
	      System.out.println("qtNodes[" + i + "] visible:" + qtNodes.get(i).visible);

	      for(int j=0; j<4; j++)
	      {	
    	  	System.out.print("----------- child branchIndex[" + j + "]:: " + 
					qtNodes.get(i).childIndex[j] + 
					" visible: " + qtNodes.get(qtNodes.get(i).childIndex[j]).visible);

			// check to see if there is child
			if (qtNodes.get(i).indices.size() >=1)
			{
			  System.out.print(" | indices: ");
			  for(int k=0; k<qtNodes.get(i).indices.size(); k++)
			  {
			    System.out.print(qtNodes.get(i).indices.get(k) + " ");
			  }	  
			}
			System.out.println("");
	      }	
	    }

	  }
	}
	
	public int size()
	{
		return nodeSize;
	}
	
}
