package life;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

import simulation.CEmitter;
import simulation.CEnvironment;
import simulation.TemperatureEmitter;
import utilities.CWorldObject;
import utilities.PathFinder;
import utilities.NAgentEdge;
import utilities.NetworkInfo;
import utilities.ObjectType;
import utilities.Point3D;
import utilities.Point2D;
import utilities.Utility;
import utilities.Vector2f;


public class Human2 extends CVagile {
	
	// ----------------- CONDITIONS AND BEHAVIOUR
	float fitness;
	public AnimalInfo info;
	public enum HumanState { ROAMING, RESTING, HUNTING, FLEEING, TRAVELLING, ROAMLONGRANGE }
	HumanState humanState;

	public int NodeID;
	
	// ----------------- MOVEMENTS
	float speed;
	float angle;
	float rotAngle;
	boolean rotRight;
	boolean rotLeft;
	int xMove;
	int yMove;
	float velocityX, velocityY;
	float thrust;
	float energy;
	
	// prey
	int preyIndex;
	//int preyNodeIndex;
	int predatorIndex;
	// clan index
	int clanIndex;
	//int predatorNodeIndex;
	boolean preyFound = false;
	Vector2f visibleVec;	// visible vector
	
	// ----------------- PATH FINDING
	ArrayList<Point2D> path = new ArrayList<Point2D>();
	boolean reachedSection = false;
	boolean newPathFound = false;
	Point2D activePathPoint;
	boolean followPath = false;
	
	// ----------------- PATH ROAM
	ArrayList<Point2D> roamCells = new ArrayList<Point2D>();
	boolean newRoamCellsFound = false;
	Point2D prevRoamCells = new Point2D(-1, -1);
	int ni = 0;
	
	// ----------------- GRAPHICS
	Point3D size;
	protected Color pColor;	
	protected int screenWidth, screenHeight;
	boolean showInfo = false;
	
	
	public Human2(int _id, String _name, ObjectType _objectType, Point3D _position, int age, Point3D size, int scrWidth, int scrHeight, Color color, AnimalInfo humanInfo)
	{
		super(_id, _name, _objectType, _position, (int) humanInfo.eyeSight);
		
		screenWidth = scrWidth;
		screenHeight = scrHeight;
		pColor = color;
		this.size = size;
		info = humanInfo;
		
		xMove = _position.x;
		yMove = _position.y;
		thrust = humanInfo.thrust;
		energy = humanInfo.energy;
		
		humanState = HumanState.RESTING;
		info.speed = 1;
		info.huntingThrust = 1;
		info.thrust = 1;
		speed = 1;
		//navigateToCell(617, 271);
	}

	@Override
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env) {
		incAge.run();
		
		// if no target found, then deal with quadtree data
		// ----------------------------------- DEAL WITH QUADTREE DATA
    	// loop through only those indices
		float tempEmitter = 0.0f;
		ArrayList<Integer> clanIndices = new ArrayList<Integer>(); 	// for storing indices of friends for collective behv.
    	for (int i=0; i<worldObjects.size(); i++)
        {
    		// -------------------------- INTERACT WITH PASSED IN AGENTS
        	// decide what this object is... is it emitter or agent? what type of agent?
        	// this is the coder's job, the quadtree only manages the nodes, rangequery, etc
        	// all interaction is managed by the coder.
    		// emitter and agent are part of the architecture in enum ObjectType
    		if(worldObjects.get(i).objectType == ObjectType.emitter)
        	{
    			CEmitter em = (CEmitter) worldObjects.get(i);
    			
    			// is it a TemperatureEmitter Type?
    			if(em.name == "TemperatureEmitter")
    			{
    				// cast it to the type
    				TemperatureEmitter tEmp = (TemperatureEmitter) worldObjects.get(i);
    				
    				// calculate position and distance
    				float dX = tEmp.position.x - this.position.x;
    				float dY = tEmp.position.y - this.position.y;    				
    				float dist = (float) Math.sqrt((dX*dX) + (dY*dY));
    				
    				// increment every temperature that's within range
    				tempEmitter += 1/(Math.exp((dist/tEmp.condition))) * tEmp.condition;
    				//System.out.println("tempEm:" + tempEmitter);
    				
    				// ***** NETWORK MAPPING
    				String[] neStr = {};
    				NetworkInfo.addEdge(new NAgentEdge(this.id, tEmp.id, neStr));
    			}
        	}

    		if(worldObjects.get(i).objectType == ObjectType.agent)
        	{
    			if ((this.id != worldObjects.get(i).id))	// avoid calculating myself
    			{
	    			// ---------- here, test the objects to see what type it is...
					// here, it is up to the coder to decide the type based on the name    			
	    			
    				// ------------------------------------- IF MY FOOD
    				if(new String(worldObjects.get(i).name).equals(info.food))
	    			{	
	    				Carnivore7 food = (Carnivore7) worldObjects.get(i);
	    					    				
	    				if(Utility.distance(position.x, position.y, food.position.x, food.position.y) < info.eyeSight)
	    				{
		    				//System.out.println("Carnivore Found Food::  " + worldObjects.get(i).name);
		    				preyIndex = i;
		    				//humanState = HumanState.HUNTING;
		    				
		    				// ***** NETWORK MAPPING
		    				String[] neStr = {"true"}; // true for directed
		    				NetworkInfo.addEdge(new NAgentEdge(this.id, food.id, neStr));
		    				
		    				break;	// found a prey stick to it
	    				}
	    			}
	    			else
	    				preyIndex = -1;
	    			
	    			// ------------------------------------- IF MY PREDATOR
	    			// the natural predator (upper foodchain) must have priority, so must replace the hunt for food
	    			if(worldObjects.get(i) instanceof Carnivore2)
	    			{	
	    				//System.out.println("Carnivore2 aroudn me!" + worldObjects.get(i).name);
	    				Carnivore2 pred = (Carnivore2) worldObjects.get(i);
	    					    				
	    				if(Utility.distance(position.x, position.y, pred.position.x, pred.position.y) < info.eyeSight)
	    				{
		    				//System.out.println("Carnivore Found Food::  " + worldObjects.get(i).name);
		    				predatorIndex = i;
		    				//humanState = HumanState.FLEEING;
		    				break;	// found a prey stick to it
	    				}
	    			}
	    			else
	    				predatorIndex = -1;
	    			
	    			// ------------------------------------- IF MY TYPE (Collective Behaviour)
	    			if(worldObjects.get(i) instanceof Human)
	    			{
	    				Human friend = (Human) worldObjects.get(i);
	    				
	    				// get visibility vector
	    				Vector2f visVec = Utility.vRotate2D(this.angle, this.position.x, this.position.y, friend.position.x, friend.position.y); 
	    				float fovAngleFactor = 1; // 1 = 45 degree, <1 is less than 45 degree
	    				
	    				if ((visVec.y > info.fov) && (Math.abs(visVec.x) < Math.abs(visVec.y) * fovAngleFactor)) // within viewing angle 45 deg
							if(Utility.distance(position.x, position.y, friend.position.x, friend.position.y) < info.eyeSight)
							{	
								//System.out.println(" ******* Found my type " + worldObjects.get(i).name);
								clanIndices.add(i);
							}
	    			}
	    			
	    			
    			}
    			//System.out.println("%------ IN AGENT (AGENT!)::  " + pl.name);
        	}
        }
    	
    	// ------------------------------ for flocking
    	Point2D vel = new Point2D(0, 0);
    	Point2D alig = new Point2D(0, 0);
    	Point2D targ = new Point2D(0, 0);
    	Point2D tend = new Point2D(0, 0);
		Point2D sep = new Point2D(0, 0);
		Point2D coh = new Point2D(0, 0);
	 		
		// STATE BEGIN --------------------------------------------------------------------- STATE BEGIN
    	
		if (humanState == HumanState.RESTING)
		{
			//System.out.println("Resting");
			//energyTest(animalInfo.energyGain);
			setThrust(0);
		}
		
		if (humanState == HumanState.ROAMING)
		{
			//System.out.print("Roaming");
			
			// if roaming, make sure it doesn't hit any blocked cells
			if(newRoamCellsFound)
			{	
				roamCells = PathFinder.getNextWalkableCells(position.x, position.y);
				newRoamCellsFound = false;
				
				if(roamCells.size() > 1)
					ni = Utility.rnd.nextInt(roamCells.size()-1);
				else
					newRoamCellsFound = true;
				
			}
			
			//if((prevRoamCells.x == roamCells.get(0).x) && (prevRoamCells.y == roamCells.get(0).y))
				//roamCells.remove(0);
			
			if(roamCells.size() != 0)
			{
				//Point2D rp = roamCells.get(0);
				//prevRoamCells.x = rp.x;
				//prevRoamCells.y = rp.y;
				
				if ( (Utility.distance(position.x, position.y, roamCells.get(ni).x, roamCells.get(ni).y) >= info.feedingDistance) ||
						((Utility.distance(position.x, position.y, roamCells.get(ni).x, roamCells.get(ni).y) <= -info.feedingDistance))
						)
				{	
					System.out.println("ROAM: not arrived yet: from [" + position.x + "," + position.y + "] to [" + roamCells.get(ni).x + "," + roamCells.get(ni).y + "]");
					
					//prevRoamCells.x = roamCells.get(0).x;
					//prevRoamCells.y = roamCells.get(0).y;
					
					setThrust((int) info.huntingThrust);
					
					float fX =	roamCells.get(ni).x - position.x;	// target x
					float fY = 	roamCells.get(ni).y - position.y;	// target y
					
					angle = Math.round(Math.atan2(fY,fX)*180/Math.PI);	// angle towards target
					
					PathFinder.treadCell(position.x, position.y, 0.01f, 1);
					
					//energyTest(info.energyLoss);
				} 	
				else
					newRoamCellsFound = true;
			}
			
			// Impulse ---------------------------------------------------------------------------
			/*
			if (Utility.rnd.nextInt((int) info.impulseRange) < info.impulse)
				rotateRight(5);
			else //if (rnd.nextInt((int) info.impulseRange) > info.impulseRange-info.impulse)
				rotateLeft(5);
			
			if (Utility.rnd.nextInt((int) info.impulseRange) < info.impulse)	// thrust impulse
				setThrust((int) info.thrust);
			*/
			
			//energyTest(info.energyLoss);
			//if (thrust < info.thrustLimit) thrust = info.thrustLimit; // limit thrust			
		}
		
		if (humanState == HumanState.ROAMLONGRANGE)
		{
			//System.out.println("travelling | path size: " + path.size());
			
			if(newPathFound)
			{	
				newPathFound = false;
				if(path.size() != 0)	// if there is at least a path cell left
				{	
					activePathPoint = path.remove(path.size()-1);	// get the next path cell to move to
					//System.out.println("get new cell coordinate: [" + activePathPoint.x + "," + activePathPoint.y + "]");
					followPath = true;								// raise flag
					
				}
				else	// if no more items, get more path
				{
					System.out.println("*** target reached:" + activePathPoint.x + "," + activePathPoint.y);
					//humanState = HumanState.ROAMING;
					path = PathFinder.getPathToRandomTargetCell(position.x, position.y, 250);
					
					//if(path.size() != 0)
					{
						newPathFound = true;
						followPath = false;
					}
				}
				
			}
			
			// if it is near enough, change states.
			if(followPath)	// if there is a path
			{
				//System.out.println("followPath:");
				// put a range so that always touch the point
				if ( (Utility.distance(position.x, position.y, activePathPoint.x, activePathPoint.y) >= info.feedingDistance) ||
						((Utility.distance(position.x, position.y, activePathPoint.x, activePathPoint.y) <= -info.feedingDistance))
						)
				{	
					//System.out.println("not arrived yet: from [" + position.x + "," + position.y + "] to [" + activePathPoint.x + "," + activePathPoint.y + "]");
					setThrust((int) info.huntingThrust);
					
					float fX =	activePathPoint.x - position.x;	// target x
					float fY = 	activePathPoint.y - position.y;	// target y
					
					angle = Math.round(Math.atan2(fY,fX)*180/Math.PI);					// angle towards target
					
					PathFinder.treadCell(position.x, position.y, 0.01f, 1);
					
					//energyTest(info.energyLoss);
				} 	
				else
					newPathFound = true;
			
				/*
				visibleVec = Utility.vRotate2D((int) angle+90, position.x, position.y, activePathPoint.x, activePathPoint.y); // get visibility vector
			
				// If predator is seen with a small blind gap
				if ((visibleVec.y > 0 || visibleVec.y < 0 ) && (Math.abs(visibleVec.x) > Math.abs(visibleVec.y * 0.9f))) // within large viewing angle
				{	
					if (-visibleVec.x < -1) // if prey at left, turn left
						rotateLeft(15);
					else if (-visibleVec.x > 1) // if prey at right, turn right
						rotateRight(15);
				}
				*/
			}
		}	
		
		if (humanState == HumanState.TRAVELLING)
		{
			//System.out.println("travelling | path size: " + path.size());
			
			if(newPathFound)
			{	
				newPathFound = false;
				if(path.size() != 0)	// if there is a path
				{	
					activePathPoint = path.remove(path.size()-1);
					System.out.println("get new pathcell coordinate: [" + activePathPoint.x + "," + activePathPoint.y + "]");
					followPath = true;
				}
				else	// if no more items, switch state
				{
					//humanState = HumanState.ROAMING;
					//newRoamCellsFound = true;
					
					path = PathFinder.getPathToRandomTargetCell(position.x, position.y, 250);
					//path = PathFinder.getPathToRandomTargetCell(712, 278, 250);
					newPathFound = true;
					followPath = true;
				}
				
			}
			
			// if it is near enough, change states.
			if(followPath)	// if there is a path
			{
				//System.out.println("followPath:");
				// put a range so that always touch the point
				if ( (Utility.distance(position.x, position.y, activePathPoint.x, activePathPoint.y) >= info.feedingDistance) ||
						((Utility.distance(position.x, position.y, activePathPoint.x, activePathPoint.y) <= -info.feedingDistance))
						)
				{	
					//System.out.println("not arrived yet: from [" + position.x + "," + position.y + "] to [" + activePathPoint.x + "," + activePathPoint.y + "]");
					setThrust((int) info.huntingThrust);
					
					float fX =	activePathPoint.x - position.x;	// target x
					float fY = 	activePathPoint.y - position.y;	// target y
					
					angle = Math.round(Math.atan2(fY,fX)*180/Math.PI);					// angle towards target
					PathFinder.treadCell(position.x, position.y, 0.01f, 1);
					
					//energyTest(info.energyLoss);
				} 	
				else
					newPathFound = true;
			
				/*
				visibleVec = Utility.vRotate2D((int) angle+90, position.x, position.y, activePathPoint.x, activePathPoint.y); // get visibility vector
			
				// If predator is seen with a small blind gap
				if ((visibleVec.y > 0 || visibleVec.y < 0 ) && (Math.abs(visibleVec.x) > Math.abs(visibleVec.y * 0.9f))) // within large viewing angle
				{	
					if (-visibleVec.x < -1) // if prey at left, turn left
						rotateLeft(15);
					else if (-visibleVec.x > 1) // if prey at right, turn right
						rotateRight(15);
				}
				*/
				
				// ------------ collect flocking vectors:: separation
				/*
				int sepNum = 0, sepRatio = 1;
				for (int i=0; i<clanIndices.size(); i++)	// clanIndices was collected earlier
				{
					Human friend = (Human)worldObjects.get(i);
					sep.x -= (friend.position.x - this.position.x) / sepRatio; // proper
					sep.y -= (friend.position.y - this.position.y) / sepRatio; // proper
					sepNum++;
				}
				if(sepNum > 0)	// average out
				{
					sep.x = sep.x / sepNum;
					sep.y = sep.y / sepNum;
				}
				
				// ------------ collect flocking vectors:: separation
				int cohNum = 0, cohRatio = 50;
				for (int i=0; i<clanIndices.size(); i++)	// clanIndices was collected earlier
				{
					Human friend = (Human)worldObjects.get(i);
					coh.x += friend.position.x;
					coh.y += friend.position.y;
					
					cohNum++;
				}
				if(cohNum > 0)	// average out
				{
					coh.x = (coh.x / cohNum);
					coh.y = (coh.y / cohNum);
					coh.x = (coh.x - this.position.x) / cohRatio;
					coh.y = (coh.y - this.position.y) / cohRatio;
				}
				
				// ------------ collect flocking vectors:: alignment
				int aligNum = 0;
				for (int i=0; i<clanIndices.size(); i++)	// clanIndices was collected earlier
				{
					Human friend = (Human)worldObjects.get(i);
					alig.x += Math.cos(friend.angle*Math.PI/180); 
					alig.y += Math.sin(friend.angle*Math.PI/180);
					
					aligNum++;
				}
				if(aligNum > 0)	// average out
				{
					alig.x = (alig.x / aligNum);
					alig.y = (alig.y / aligNum);
				}
				*/
			}
		}	
		
		// COMPUTE MOVEMENT ---------------------------------------------------------------------------
		//this.velocityX += targ.x + sep.x + coh.x + alig.x;
		/*
		this.velocityY += targ.y + sep.y + coh.y + alig.y;
		
		// limit the velocity
		if (velocityX > 2) velocityX *= 0.5f;
		if (velocityY > 2) velocityY *= 0.5f;
		if (velocityX < -2) velocityX *= 0.5f;
		if (velocityY < -2) velocityY *= 0.5f;
		*/
		
		xMove += Math.cos(angle*Math.PI/180) * speed * thrust; 
		yMove += Math.sin(angle*Math.PI/180) * speed * thrust;
		thrust *= 0.9f;	// sliding effect		
		if (thrust < info.thrustLimit) thrust = info.thrustLimit;
		
		// BOUNDARY MANAGEMENT ---------------------------------------------------------------------------
		if ((xMove >= screenWidth)) {xMove = 1;}
		if ((xMove <= 0)) {xMove = screenWidth;}
		if ((yMove >= screenHeight)) {yMove = 1;}
		if ((yMove <= 0)) {yMove = screenHeight;}
		
		// ACTUAL MOVEMENT ---------------------------------------------------------------------------
		position.x = xMove;
		position.y = yMove;
	}
	
	@Override
	public void paint(Graphics g) {
		//super.paint(g);
		
		// set colour and draw the plant
        g.setColor( pColor );										
        g.fillOval( (int)(position.x-size.x/2), (int)(position.y-size.y/2), (int)size.y, (int)size.x );
        g.drawLine(position.x, position.y, (int)(position.x + Math.cos(angle*Math.PI/180)*12.0f), (int)(position.y + Math.sin(angle*Math.PI/180)*12.0f));
        
        if (showInfo == true)
        {
            //g.drawString( talk(), pLoc.x+ (int)pWidth+2, pLoc.y+(int)pLength+2 );            
            //g.drawString(""+pAge, pLoc.x+(int)pWidth+pHealth*4+4, pLoc.y+(int)pLength+12);
        	
        	String s = new DecimalFormat("0.###").format((double)fitness);
        	        	
        	new DecimalFormat("0.###").format((double)fitness);
        	g.drawString(""+s, position.x+(int)size.x+(int)(fitness*4)+4, position.y+(int)size.x+12);
            g.fillRect(position.x+(int)size.x+2, position.y+(int)size.x+5, (int)(fitness*4), 2);
        }
	}
	
	public void navigateToCell(int x, int y)
	{
		Point2D p = PathFinder.posToCellIndex(position.x,  position.y);
		Point2D pTarget = PathFinder.posToCellIndex(x, y);
		System.out.println("@@@@@@@@@@@ Navigate from:" + p.x + "," + p.y + " TO " + pTarget.x + "," + pTarget.y);
		
		path = PathFinder.findPath(p.x, p.y, pTarget.x, pTarget.y);
		humanState = HumanState.TRAVELLING;
		newPathFound = true;
	}
	
	public void energyTest(float e)
    {
		/*
    	energy += e;
    	
    	if(energy <= 1.0f)
    	{
    		humanState = HumanState.RESTING;
    		//setThrust(0);
    	}
    	
    	if (energy < info.hungerThreshold)		// if hungry 
    	{
    		humanState = HumanState.HUNTING;		   	// set hunting state
    		//if(!preyFound)	preyIndex = getPreyIndex();	// if no prey as target, look for prey    		
    	}
    	
    	if(energy >= info.hungerThreshold)	// if not hungry
    	{
    		humanState = HumanState.ROAMING;
    		setThrust(1);
    	}
    	
    	if (energy < 0) energy = 0;
    	*/
    }
	
	// Movement Controllers
	public void rotateRight(float angle)
	{
		rotAngle = angle;
		rotRight = true;
		rotLeft = false;
		this.angle += rotAngle;
	}
	
	public void rotateLeft(float angle)
	{
		rotAngle = angle;
		rotRight = false;
		rotLeft = true;
		this.angle -= rotAngle;
	}		
	
	public void setThrust(int thrust)
	{
		this.thrust = thrust;
		energy -= 0.05;
	}
}