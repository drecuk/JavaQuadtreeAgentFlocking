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

public class Bird extends CVagile {
	
	public enum AnimalState
	{
		ROAMING, RESTING, HUNTING, FLEEING, FLOCKING
	}
	
	//public int ID;
	public int NodeID;
	
	float rotAngle;
	boolean rotRight;
	boolean rotLeft;
		
	float fitness;
	
	float thrust;
	float energy;
	public AnimalInfo animalInfo;
	
	AnimalState animalState;
	
	Point3D size;
	
	float velocityX = 1;
	float velocityY = 1;
	float pWidth, pHeight;
	public Color pColor;	
	protected int screenWidth, screenHeight;
	AnimalInfo info;
	
	// prey
	int preyIndex;
	int preyNodeIndex;
	int predatorIndex;
	int predatorNodeIndex;
	int clanIndex = -1;
	int humanIndex = -1;
	boolean preyFound = false;
	Vector2f visibleVec;	// visible vector
	
	boolean reproduced = false;
	
	boolean showInfo = false;
	
	ArrayList<Point3D> flockIndices = new ArrayList<Point3D>(); 	// for storing indices of friends for collective behv.
	Vector2f coh = new Vector2f(0,0);
	Vector2f tend = new Vector2f(0,0);
	Vector2f sep = new Vector2f(0,0);
	//protected ArrayList<Plant> plants;
	Random rnd = new Random(); 
	
	public Bird(int _id, String _name, ObjectType _objectType, Point3D _position, int age, Point3D size, int scrWidth, int scrHeight, Color color, AnimalInfo animalInfo)
	{		
		super(_id, _name, _objectType, _position, (int) animalInfo.eyeSight);
		
		//ID = _id;
		this.animalInfo = animalInfo;
		animalState = AnimalState.FLOCKING;
		info = animalInfo;
		pColor = color;
		speed = animalInfo.speed;
		angle = rnd.nextInt(360);//animalInfo.angle;
		thrust = animalInfo.thrust;
		energy = animalInfo.energy;
		//position = _position;
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
		String[] nStr = {"C-"+this.id, Integer.toString((int)speed), strColor};
		NetworkInfo.addAgent(new NAgentNode(this.id, nStr));
		
		/*
		String params = "";
		for(int j=0; j < nStr.length; j++)
		{
			params += nStr[j] + ",";
		}
		
		System.out.println(Integer.toString(ID) + "," + params);
		*/
		
	}
	
	@Override
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env) 
	{
		//animalState = AnimalState.ROAMING;
		
		 //incAge.run();

		// --------------------------------------------------------- ENERGY AND STATES
		 //if (energy < info.energyRestThreshold) { animalState = AnimalState.RESTING;	}
		//	if ((energy > info.energyHuntThreshold) && (preyIndex == -1)) { animalState = AnimalState.ROAMING;	}
		
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
		/*
		//System.out.println("energyToReproduce: " + energyToReproduce);
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
		*/	
		
		// reset all indices, the loop below will look for new ones
		//preyIndex = humanIndex = predatorIndex = 
		clanIndex = -1;
		
		flockIndices.clear();
		
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
    		// ------------------- PROCESS ALL EMITTERS WITHIN RANGE (passed in by CQuadTree)
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

    		// ------------------- PROCESS ALL AGENTS WITHIN RANGE (passed in by CQuadTree)
    		if(worldObjects.get(i).objectType == ObjectType.agent)
        	{
    			if ((this.id != worldObjects.get(i).id)) // if not myself
    			{
	    			// ---------- here, test the objects to see what type it is...
					// here, it is up to the coder to decide the type based on the name    			
	    			if(new String(worldObjects.get(i).name).equals(info.food))
	    			{	
	    				
	    				Herbivore herbi = (Herbivore) worldObjects.get(i);
	    					    				
	    				if(Utility.distance(position.x, position.y, herbi.position.x, herbi.position.y) < info.eyeSight)
	    				{
		    				//System.out.println("Carnivore Found Food::  " + worldObjects.get(i).name);
		    				preyIndex = i;
		    				animalState = AnimalState.HUNTING;
		    				
		    				// ***** NETWORK MAPPING
		    				String[] neStr = {"true"}; // true for directed
		    				NetworkInfo.addEdge(new NAgentEdge(this.id, herbi.id, neStr));
		    				
		    				break;	// found a prey stick to it
	    				}
	    			}
	    			else
	    				preyIndex = -1;
	    			
	    			// the natural predator (upper foodchain) must have priority, so must replace the hunt for food
	    			if(worldObjects.get(i) instanceof Carnivore2)
	    			{	
	    				//System.out.println("Carnivore around me!" + worldObjects.get(i).name);
	    				Carnivore2 pred = (Carnivore2) worldObjects.get(i);
	    					    				
	    				if(Utility.distance(position.x, position.y, pred.position.x, pred.position.y) < info.eyeSight)
	    				{
		    				//System.out.println("Carnivore Found Food::  " + worldObjects.get(i).name);
		    				predatorIndex = i;
		    				animalState = AnimalState.FLEEING;
		    				break;	// found a prey stick to it
	    				}
	    			}
	    			else
	    				predatorIndex = -1;
	    			
	    			// ------------------------------------- IF HUMAN TYPE (Collective Behaviour)
	    			// ------------------------------------- IF MY TYPE (Collective Behaviour)
	    			if(worldObjects.get(i) instanceof Human)
	    			{
	    				Human friend = (Human) worldObjects.get(i);
	    				
	    				// get visibility vector
	    				Vector2f visVec = Utility.vRotate2D(this.angle, this.position.x, this.position.y, friend.position.x, friend.position.y); 
	    				float fovAngleFactor = 1; // 1 = 45 degree, <1 is less than 45 degree
	    				
	    				if ((visVec.y > info.fov) && (Math.abs(visVec.x) < Math.abs(visVec.y) * fovAngleFactor)) // within viewing angle 45 deg
							if(Utility.distance(position.x, position.y, friend.position.x, friend.position.y) < 300)//info.eyeSight)
							{	
								//System.out.println("Human Index::  " + i + " | name:" + worldObjects.get(i).id);
								humanIndex = i;
								//pColor = Color.blue;
							}	
	    			}
	    			else
	    			{
	    				//pColor = Color.yellow;
	    				humanIndex = -1;
	    			}
	    			
	    			// ------------------------------------- IF BIRD TYPE (Collective Behaviour)
	    			if(worldObjects.get(i) instanceof Bird)
	    			{
	    				Bird friend = (Bird) worldObjects.get(i);
	    				
	    				// get visibility vector
	    				Vector2f visVec = Utility.vRotate2D(this.angle+90, this.position.x, this.position.y, friend.position.x, friend.position.y); 
	    				float fovAngleFactor = 1; // 1 = 45 degree, <1 is less than 45 degree
	    				
	    				if ((visVec.y > info.fov) && (Math.abs(visVec.x) < Math.abs(visVec.y) * fovAngleFactor)) // within viewing angle 45 deg
							if(Utility.distance(position.x, position.y, friend.position.x, friend.position.y) < info.eyeSight)
							{
								//System.out.println("Birds in sight::  " + worldObjects.get(i).id);
								clanIndices.add(i);
								flockIndices.add(worldObjects.get(i).position);
								
								//animalState = AnimalState.FLOCKING;
								//break;
							}
	    			}
	    		
    			}
    			//System.out.println("%------ IN AGENT (AGENT!)::  " + pl.name);
        	}
        }
	 		
    	/*
		if(clanIndices.size() == 0) animalState = AnimalState.ROAMING;
		// STATE BEGIN --------------------------------------------------------------------- STATE BEGIN
		
		
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
			
			//energyTest(info.energyLoss);
			//if (thrust < info.thrustLimit) thrust = info.thrustLimit; // limit thrust			
		}
		*/
    	
		/*
    	if(clanIndices.size() > 2)
			pColor = Color.red;
		else
			pColor = Color.yellow;
		*/
    	
		//if(clanIndex == -1) animalState = AnimalState.ROAMING;
		Vector2f targ = new Vector2f(0, 0);
		//System.out.println("clanIndices.size:" + clanIndices.size());
		
		// ----------------------------------------- FLOCKING VECTORS				
		//if(animalState == AnimalState.FLOCKING)
		{	
			//System.out.println(worldObjects.size() + " :: found clanIndex: " + clanIndex);
			//System.out.println("humanIndex: " + humanIndex);
			if(humanIndex != -1)
				if(new String(worldObjects.get(humanIndex).name).equals("Human"))
				{
					//System.out.println("found humanIndex" + humanIndex);
					
					setThrust((int) info.huntingThrust);
					// once found, cast it to the coder's custom type based on the name
					
					Human hum = (Human) worldObjects.get(humanIndex);
					
					targ.x = (hum.position.x - position.x) / 100;	// target x
					targ.y = (hum.position.y - position.y) / 100;	// target y
					
					angle = Math.round(Math.atan2(targ.y,targ.x)*180/Math.PI);					// angle towards prey
					
					visibleVec = Utility.vRotate2D(angle+90, this.position.x, this.position.y, hum.position.x, hum.position.y); // get visibility vector
					
					if ((visibleVec.y > 0 || visibleVec.y < 0 ) && (Math.abs(visibleVec.x) > Math.abs(visibleVec.y * 0.9f))) // within large viewing angle
					{	
						if (-visibleVec.x < -1) // if prey at left, turn left
							rotateLeft(5);
						else if (-visibleVec.x > 1) // if prey at right, turn right
							rotateRight(5);
					}
				}
		}
		/*
		else
		{
			animalState = AnimalState.ROAMING;
		}
		*/
		
		
		// STATE END --------------------------------------------------------------------- STATE END
		
		
		// COMPUTE MOVEMENT ---------------------------------------------------------------------------
		//targ.x = (600 - this.position.x) / 100;
		//targ.y = (600 - this.position.y) / 100;
		
		// ------------------------------ for flocking
    	Vector2f vel = Utility.velocity(new Point3D(this.xMove, this.yMove, 0), clanIndices, worldObjects);
    	Vector2f alig = Utility.alignment(this.position, clanIndices, worldObjects);
    	sep = Utility.separation(this.position, clanIndices, worldObjects, 10, 5f);
		coh = Utility.cohesion(this.position, clanIndices, worldObjects, 100);
		tend = Utility.tendency(new Point3D(512,512,0));
		
		this.velocityX +=  targ.x + coh.x + alig.x + sep.x;// + vel.x;// + tend.x;
		this.velocityY += targ.y + coh.y + alig.y + sep.y;// + vel.y;// + tend.y; 
		
		// the angle messes up after the collective behaviour (coh,sep,alig,etc)
		// it needs to be recalculated based on those vectors
		
		// limit the velocity
		if (velocityX > 2) velocityX *= 0.5f;
		if (velocityY > 2) velocityY *= 0.5f;
		if (velocityX < -2) velocityX *= 0.5f;
		if (velocityY < -2) velocityY *= 0.5f;
		angle = Math.round(Math.atan2(velocityY,velocityX)*180/Math.PI);
		
		
		xMove += velocityX + Math.cos(angle*Math.PI/180) * speed * thrust; 
		yMove += velocityY + Math.sin(angle*Math.PI/180) * speed * thrust;
		thrust *= 0.9f;	// sliding effect		
		if (thrust < animalInfo.thrustLimit) thrust = animalInfo.thrustLimit;
		//if (angle > 360) angle = 0;
		
		// -----------------------------------------------------------------AVOID HAZARDOUS EMITTERS
		tempEff = tempEmitter;
		// > keep going if earlier heat is larger than current heat getting colder
		if (earlierHeat < tempEff)
		{
			// Getting colder
			//System.out.println("getting hotter");
			pSwitchCount = 0;
		}
		else if (earlierHeat > tempEff)
		{
			// < Getting hotter... avoiding with rotate left/right
			
			//System.out.println("getting colder");
			pSwitchCount += 1;
			if (pSwitchCount < 10)			
				rotateRight(5);
			else
				rotateLeft(5);
		} 
		earlierHeat = tempEff; // reset earlier heat
		
		// BOUNDARY MANAGEMENT ---------------------------------------------------------------------------
		if ((xMove >= screenWidth)) {xMove = 1;}
		if ((xMove <= 0)) {xMove = screenWidth;}
		if ((yMove >= screenHeight)) {yMove = 1;}
		if ((yMove <= 0)) {yMove = screenHeight;}
		
		// ACTUAL MOVEMENT ---------------------------------------------------------------------------
		position.x = xMove;
		position.y = yMove;
		
		//viewable(); 
	 }

 
	 /** paint method */
    public void paint(Graphics g)
    {
    	// set colour and draw the plant
        g.setColor( pColor );										
        g.fillOval( (int)(position.x-pWidth/2), (int)(position.y-pHeight/2), (int)pWidth, (int)pWidth );
        g.drawLine(position.x, position.y, (int)(position.x + Math.cos(angle*Math.PI/180)*7.0f), (int)(position.y + Math.sin(angle*Math.PI/180)*7.0f));
        
        g.setColor( Color.red );
        
        /*
        for(int i=0; i<flockIndices.size();i++)
        {
        	if(Utility.distance(position.x, position.y, flockIndices.get(i).x, flockIndices.get(i).y) < 300)
        	{
	        	g.drawOval((int)(position.x + Math.cos(angle*Math.PI/180)*5.0f), (int)(position.y + Math.sin(angle*Math.PI/180)*5.0f), 4, 4);
	        	g.drawLine(position.x,  position.y, flockIndices.get(i).x, flockIndices.get(i).y);
        	}
        }
       */
        
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
