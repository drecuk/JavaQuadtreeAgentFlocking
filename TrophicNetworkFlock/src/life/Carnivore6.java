package life;
import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import life.Carnivore.AnimalState;


import simulation.CEmitter;
import simulation.CEnvironment;
import simulation.TemperatureEmitter;
import utilities.*;

public class Carnivore6 extends CVagile {
	
	public enum AnimalState
	{
		ROAMING, RESTING, HUNTING, FLEEING
	}
	
	public int ID;
	public int NodeID;
	
	float speed;
	float angle;
	float rotAngle;
	boolean rotRight;
	boolean rotLeft;
		
	float fitness;
	
	float thrust;
	float energy;
	public AnimalInfo animalInfo;
	
	AnimalState animalState;
	
	Point3D size;
	int xMove;
	int yMove;
	float pWidth, pHeight;
	protected Color pColor;	
	protected int screenWidth, screenHeight;
	AnimalInfo info;
	
	// prey
	int preyIndex;
	int preyNodeIndex;
	int predatorIndex;
	int predatorNodeIndex;
	boolean preyFound = false;
	Point3D visibleVec;	// visible vector
	
	boolean showInfo = false;
	
	boolean reproduced = false;
	
	//protected ArrayList<Plant> plants;
	Random rnd = new Random(); 
	
	public Carnivore6(int _id, String _name, ObjectType _objectType, Point3D _position, int age, Point3D size, int scrWidth, int scrHeight, Color color, AnimalInfo animalInfo)
	{		
		super(_id, _name, _objectType, _position, (int) animalInfo.eyeSight);
		
		ID = _id;
		this.animalInfo = animalInfo;
		animalState = AnimalState.ROAMING;
		info = animalInfo;
		pColor = color;
		speed = animalInfo.speed;
		angle = animalInfo.angle;
		thrust = animalInfo.thrust;
		energy = animalInfo.energy;
		position = _position;
		xMove = _position.x;
		yMove = _position.y;
		pWidth = pHeight = size.x;
		screenWidth = scrWidth;
		screenHeight = scrHeight;
		preyIndex = -1;
		predatorIndex = -1;
		this.size = size;
		
		// ***** NETWORK MAPPING
				String strColor = "'" + pColor.getRed() + "," + pColor.getGreen() + "," + pColor.getBlue() + "'";
				String[] nStr = {"C6-"+this.id, Integer.toString((int)speed), strColor};
				NetworkInfo.addAgent(new NAgentNode(this.id, nStr));
	}
	
	 @Override
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env) 
	 {
		 incAge.run();

			// --------------------------------------------------------- ENERGY AND STATES
		 if (energy < info.energyRestThreshold) { animalState = AnimalState.RESTING;	}
			if ((energy > info.energyHuntThreshold) && (preyIndex == -1)) { animalState = AnimalState.ROAMING;	}
			
			// --------------------------------------------------------- FITNESS		
			double sunFitness = LifeUtil.Adaptability(	// sunlight
					CEnvironment.getSunlight(), 
					info.sunLightPreferred, 
					info.sunLightLower, 
					info.sunLightUpper, 0.5);    	
			double temfitness = LifeUtil.Adaptability(	// temperature
					CEnvironment.getTemperature(), 
					info.temperaturePreferred, 
					info.temperatureLower, 
					info.temperatureUpper, 0.5);		
			fitness = (float)(temfitness * sunFitness);
		    	
			// --------------------------------------------------------- REPRODUCTION
			if(this.canReproduce)
				if(energyToReproduce > info.reproductionThreshold)
					if(age >= info.reproductionAge)
					{	
						if (fitness >= info.reproductionFitness )
						{
							reproduce(info.seedCount);
							energyToReproduce = 0.0f;
						}
					}
			
			// --------------------------------------------------------- NO FEED = DEATH
			energyToReproduce -= info.famineDeathRate;
			//System.out.println(energyToReproduce);
			if (energyToReproduce < -info.famineDeath)
			{
				//System.out.println("I am dead" + energyToReproduce);
				this.removeMe();			
				return;
			}
			
			
		 if(preyIndex == -1)
			animalState = AnimalState.ROAMING;	
			
		 
		// if no target found, then deal with quadtree data
		// ----------------------------------- DEAL WITH QUADTREE DATA
    	// loop through only those indices
		 float tempEmitter = 0.0f;
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
    			if ((this.id != worldObjects.get(i).id))
    			{
	    			// ---------- here, test the objects to see what type it is...
					// here, it is up to the coder to decide the type based on the name    			
	    			if(new String(worldObjects.get(i).name).equals(info.food))
	    			{	
	    				Carnivore5 food = (Carnivore5) worldObjects.get(i);
	    					    				
	    				if(getDistance(food.position, position) < info.eyeSight)
	    				{
		    				//System.out.println("Carnivore Found Food::  " + worldObjects.get(i).name);
		    				preyIndex = i;
		    				animalState = AnimalState.HUNTING;
		    				
		    				// ***** NETWORK MAPPING
		    				String[] neStr = {"true"}; // true for directed
		    				NetworkInfo.addEdge(new NAgentEdge(this.id, food.id, neStr));
		    				
		    				break;	// found a prey stick to it
	    				}
	    			}
	    			else
	    				preyIndex = -1;
	    			
	    			// the natural predator (upper foodchain) must have priority, so must replace the hunt for food
	    			if(worldObjects.get(i) instanceof Carnivore7)
	    			{	
	    				//System.out.println("Carnivore2 aroudn me!" + worldObjects.get(i).name);
	    				Carnivore7 pred = (Carnivore7) worldObjects.get(i);
	    					    				
	    				if(getDistance(pred.position, position) < info.eyeSight)
	    				{
		    				//System.out.println("Carnivore Found Food::  " + worldObjects.get(i).name);
		    				predatorIndex = i;
		    				animalState = AnimalState.FLEEING;
		    				
		    				
		    				break;	// found a prey stick to it
	    				}
	    			}
	    			else
	    				predatorIndex = -1;
    			}
    			//System.out.println("%------ IN AGENT (AGENT!)::  " + pl.name);
        	}
        }
	 		
		// STATE BEGIN --------------------------------------------------------------------- STATE BEGIN
	
		if (animalState == AnimalState.RESTING)
		{
			energyTest(animalInfo.energyGain);
			setThrust(0);
		}
		
		if (animalState == AnimalState.ROAMING)
		{			
			//System.out.println("roam: irange:" + rnd.nextInt((int) info.impulseRange));
			// Impulse ---------------------------------------------------------------------------
			if (rnd.nextInt((int) info.impulseRange) < info.impulse)
				rotateRight(5);
			else //if (rnd.nextInt((int) info.impulseRange) > info.impulseRange-info.impulse)
				rotateLeft(5);
			
			if (rnd.nextInt((int) info.impulseRange) < info.impulse)	// thrust impulse
				setThrust((int) info.thrust);
			
			energyTest(info.energyLoss);
			//if (thrust < info.thrustLimit) thrust = info.thrustLimit; // limit thrust			
		}
		
		if (animalState == AnimalState.FLEEING)
		{
			if(predatorIndex != -1)
			{
				// the natural predator (upper foodchain) must have priority, so must replace the hunt for food
    			if(worldObjects.get(predatorIndex) instanceof Carnivore7)
    			{
    				Carnivore7 pred = (Carnivore7) worldObjects.get(predatorIndex);
    				
    				setThrust((int) info.fleeingThrust);
    				energyTest(info.energyLoss);
    				    				
    				float fX =	pred.position.x - position.x;	// target x
    				float fY = 	pred.position.y - position.y;	// target y
    				angle = -Math.round(Math.atan2(fY,fX)*180/Math.PI); // angle towards prey
    				
    				visibleVec = vRotate2D((int) angle+90, pred.position.x, pred.position.y); // get visibility vector
    				
    				// if it is near enough, change states.
    				if(getDistance(pred.position, position) < info.safeDistance)
    				{	
    					// If predator is seen with a small blind gap
        				if ((visibleVec.y > 0 || visibleVec.y < 0 ) && (Math.abs(visibleVec.x) > Math.abs(visibleVec.y * 0.9f))) // within large viewing angle
        				{	
        					// this is the reverse of follow
        					if (-visibleVec.x < -1) // if prey at left, turn left
        						rotateRight(5);
        					else if (-visibleVec.x > 1) // if prey at right, turn right
        						rotateLeft(5);
        				}
    				}
    			}
				
			}
		}
		
		if (animalState == AnimalState.HUNTING)
		{				
			if(preyIndex != -1)
			{
				//System.out.println("found preyIndex");
				
				if(new String(worldObjects.get(preyIndex).name).equals(info.food))
    			{		
					setThrust((int) info.huntingThrust);
    				// once found, cast it to the coder's custom type based on the name
    									
					Carnivore5 hbv = (Carnivore5) worldObjects.get(preyIndex);
    				
    				float fX =	hbv.position.x - position.x;	// target x
    				float fY = 	hbv.position.y - position.y;	// target y
    				
    				angle = Math.round(Math.atan2(fY,fX)*180/Math.PI);					// angle towards prey
    				
    				visibleVec = vRotate2D((int) angle+90, hbv.position.x, hbv.position.y); // get visibility vector
    				
    				// if it is near enough, change states.
    				if(getDistance(hbv.position, position) < info.feedingDistance)
    				{
    					preyFound = false;
    					energyTest(hbv.animalInfo.energy);
    					energyToReproduce += hbv.animalInfo.energy;
    					
    					try	// just to make sure that the prey hasn't died itself...
    					{
    						preyNodeIndex = CQuadTree.getArrayIndexOfNode(worldObjects.get(preyIndex).nodeID);
    						CQuadTree.qtNodes.get(preyNodeIndex).agents.remove(worldObjects.get(preyIndex));
    						//System.out.println("prey died: " + worldObjects.get(preyIndex).id + ":" + worldObjects.get(preyIndex).name);
    					} catch (Exception ex)
    					{
    					
    					}
    				
    					preyIndex = -1;
    				}    				
    				
    				// If predator is seen with a small blind gap
    				if ((visibleVec.y > 0 || visibleVec.y < 0 ) && (Math.abs(visibleVec.x) > Math.abs(visibleVec.y * 0.9f))) // within large viewing angle
    				{	
    					if (-visibleVec.x < -1) // if prey at left, turn left
    						rotateLeft(5);
    					else if (-visibleVec.x > 1) // if prey at right, turn right
    						rotateRight(5);
    				}
    				
    				energyTest(info.energyLoss);
    			}				
			}
			else
			{
				animalState = AnimalState.ROAMING;
				preyIndex = getPreyIndex();
			}
		}
		// STATE END --------------------------------------------------------------------- STATE END
		
		// -----------------------------------------------------------------AVOID HAZARDOUS EMITTERS
		tempEff = tempEmitter;
		// Heat Seeker
		if (earlierHeat > tempEff)
		{
			// Getting colder
			//System.out.println("getting hotter");
			pSwitchCount = 0;
		}
		else if (earlierHeat < tempEff)
		{
			// Getting hotter... avoiding with rotate left/right
			
			//System.out.println("getting colder");
			pSwitchCount += 1;
			if (pSwitchCount < 10)			
				rotateRight(10);
			else
				rotateLeft(10);
		} 
		earlierHeat = tempEff; // reset earlier heat
		
		// COMPUTE MOVEMENT ---------------------------------------------------------------------------
		xMove += Math.cos(angle*Math.PI/180) * speed * thrust; 
		yMove += Math.sin(angle*Math.PI/180) * speed * thrust;
		thrust *= 0.9f;	// sliding effect		
		if (thrust < animalInfo.thrustLimit) thrust = animalInfo.thrustLimit;
		
		// BOUNDARY MANAGEMENT ---------------------------------------------------------------------------
		if ((xMove >= screenWidth)) {xMove = 1;}
		if ((xMove <= 0)) {xMove = screenWidth;}
		if ((yMove >= screenHeight)) {yMove = 1;}
		if ((yMove <= 0)) {yMove = screenHeight;}
		
		// ACTUAL MOVEMENT ---------------------------------------------------------------------------
		position.x = xMove;
		position.y = yMove;
		//viewable();
		//System.out.println("[" + animalState + "]e:" + energy + "::(" + preyIndex + ")::" + position.x + "," + position.y + " | angle:"+ rotAngle + " | thrust:" + thrust);
		 
	 }

	 
	 public void reproduce(int offspring)
	    {
	    	// random angle and distance
		 	Random random = new Random();
	    	int rndAngle = random.nextInt(360);
	    	int rndDist = random.nextInt(30);
	    	
	    	// random position around the parent plant
	    	double rX = position.x + rndDist*Math.cos(rndAngle);
	    	double rY = position.y + rndDist*Math.sin(rndAngle);
	    	
	    	// within the screen boundary
	    	if(((rX) < screenWidth) && (rY) < screenHeight)
	    	{
	    		if((((int)rX) > 0) && ((int)rY) > 0)
	        	{      			
	    			// new location and size data
	    	    	Point3D rLoc = new Point3D((int)rX, (int)rY, 0);
	    	    	//Point3D size = new Point3D(2, 2, 2);
	    	    	
	    	    	boolean outOfWorld = false;
	            	
	            	if((rLoc.x >= screenWidth) || (rLoc.x <= 0))
	            		outOfWorld = true;
	            	if((rLoc.y >= screenHeight) || (rLoc.y <= 0))
	            		outOfWorld = true;
	            	
	    	    	
	            	if(!outOfWorld)
	            	{
		            		//System.out.println("reproducing...:" + rLoc.x + ", " + rLoc.y);
		            		// ----------------- create plant (ADD TO QUADTREE)
		            		//int nID = QuadTree.placeObjectInNode(QuadTree.qtNodes.get(0), rLoc, TrophicNetwork.AgentIDInc, ObjectType.agent);
		            		Carnivore6 p = new Carnivore6( TrophicNetwork.AgentIDInc++, info.commonName, objectType, rLoc, 1, size, screenWidth, screenHeight, pColor, info);
		            		CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), p); // add to root node and let it cascade            		
		            }
	        	}    		
	    	}
	    	
	    }
	 
	 /** paint method */
    public void paint(Graphics g)
    {
    	// set colour and draw the plant
        g.setColor( pColor );										
        g.fillOval( (int)(position.x-pWidth/2), (int)(position.y-pHeight/2), (int)pWidth, (int)pWidth );
        g.drawLine(position.x, position.y, (int)(position.x + Math.cos(angle*Math.PI/180)*6.0f), (int)(position.y + Math.sin(angle*Math.PI/180)*6.0f));
        
        if (showInfo == true)
        {
            //g.drawString( talk(), pLoc.x+ (int)pWidth+2, pLoc.y+(int)pLength+2 );            
            //g.drawString(""+pAge, pLoc.x+(int)pWidth+pHealth*4+4, pLoc.y+(int)pLength+12);
        	
        	String s = new DecimalFormat("0.###").format((double)fitness);
        	        	
        	new DecimalFormat("0.###").format((double)fitness);
        	g.drawString(""+s, position.x+(int)pWidth+(int)(fitness*4)+4, position.y+(int)pWidth+12);
            g.fillRect(position.x+(int)pWidth+2, position.y+(int)pWidth+5, (int)(fitness*4), 2);
        }
    }
	
    public void energyTest(float e)
    {
    	energy += e;
    	
    	if(energy <= 1.0f)
    	{
    		animalState = AnimalState.RESTING;
    		//setThrust(0);
    	}
    	
    	if (energy < info.hungerThreshold)		// if hungry 
    	{
    		animalState = AnimalState.HUNTING;		   	// set hunting state
    		if(!preyFound)	preyIndex = getPreyIndex();	// if no prey as target, look for prey    		
    	}
    	
    	if(energy >= info.hungerThreshold)	// if not hungry
    	{
    		animalState = AnimalState.ROAMING;
    		setThrust(1);
    	}
    	
    	if (energy < 0) energy = 0;
    }
    	
	public int getPreyIndex()
	{
		int pIndex = -1;
	
		// -- THE LOOP --
		for (int i=0; i<Plants.size(); i++)
        {
    		if (((Plant)Plants.get(i)) != null)
			{
    			//float dX = ((Plant)Collections.get(i)).pLoc.x - position.x;
    			//float dY = ((Plant)Collections.get(i)).pLoc.y - position.y;
				float dist = getDistance(((Plant)Plants.get(i)).position, position);// (float)Math.sqrt(dX*dX + dY*dY);
				
				if(dist < info.eyeSight)	// distance within eyesight
				{
					visibleVec = vRotate2D((int) angle+90, ((Plant)Plants.get(i)).position.x, ((Plant)Plants.get(i)).position.y);
					//System.out.print("visibleVec x:" + visibleVec.x + " y:" + visibleVec.y + " ");
					
					if (visibleVec.y > info.fov) // within viewing angle (2PI)
					{
						//pPlants.itemIndex[i].gotoAndStop(2);
						pIndex = i;
						animalState = AnimalState.HUNTING; // hunting mode now
						preyFound = true;						
						break;
					}
					else // outside of viewing angle
					{
						//animalState = AnimalState.ROAMING; // hunting mode now						
					}
				}
			}
        }
		
		return pIndex;		
	}
	
	public void viewable()
	{
		// -- THE LOOP --
		for (int i=0; i<Plants.size(); i++)
        {
    		if (((Plant)Plants.get(i)) != null)
			{
    			//float dX = ((Plant)Collections.get(i)).pLoc.x - position.x;
    			//float dY = ((Plant)Collections.get(i)).pLoc.y - position.y;
				float dist = getDistance(((Plant)Plants.get(i)).position, position);// (float)Math.sqrt(dX*dX + dY*dY);
				
				//System.out.println("dist:" + dist + " | eyesight:" + info.eyeSight);
				
				if(dist < info.eyeSight)	// distance within eyesight
				{
					visibleVec = vRotate2D((int) angle+90, ((Plant)Plants.get(i)).position.x, ((Plant)Plants.get(i)).position.y);
					//System.out.print("visibleVec x:" + visibleVec.x + " y:" + visibleVec.y + " ");
					
					if (visibleVec.y > info.fov) // within viewing angle (2PI)
					{	
						((Plant)Plants.get(i)).setColor(Color.RED);					
					}
					else // outside of viewing angle
					{					
						((Plant)Plants.get(i)).setColor(Color.GREEN);
					}
				}
			}
        }
	}
	
	// Get Distance
	public float getDistance(Point3D targetX, Point3D targetY)
	{
		// get component distance from target to me
		float pX = targetX.x - position.x;
		float pY= targetY.y - position.y;
		
		// get distance
		return (float) Math.sqrt((pX*pX)+(pY*pY));			
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
	
	public Point3D vRotate2D(int angle, int targetX, int targetY)
	{			
		// Calculate component distance
		float distX = targetX - position.x;
		float distY = targetY - position.y;
		
		// Define a return array for global to local x and y
		Point3D vector2D = new Point3D(0,0,0);

		vector2D.x = (int) (distX * Math.cos(-angle*Math.PI/180) + distY * Math.sin(-angle*Math.PI/180)); // x
		vector2D.y = (int) (-distX * Math.sin(-angle*Math.PI/180) + distY * Math.cos(-angle*Math.PI/180)); // y
					
		return vector2D;
	}

	/*
    public void getPlants(ArrayList<Plant> thePlants)
    {   
    	plants = thePlants;
    } 
    */ 
}
