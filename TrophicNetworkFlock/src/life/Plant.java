package life;

/*

 * Plant.java
 *
 * Created on 16 February 2004, 17:58
 */

import simulation.CEmitter;
import simulation.CEnvironment;
import simulation.TemperatureEmitter;
import utilities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

import utilities.Plants;


/**
 *
 * @author  Eugene Ch'ng
 */
public class Plant extends Vegetation implements Life {   
    
    public int NodeID;
    
    protected boolean draggable;
    protected boolean showInfo;
    
   // protected ArrayList<Plant> plants;
    Point3D size;

    protected Random random = new Random();
    
    protected float pEnergy;
    
    protected int screenWidth, screenHeight;

    protected double su;	// fitness of space utilisation
    public PlantInfo plantInfo;
    protected BufferedImage soilImage;
    
    protected float soilValue;
    
    
    /** Creates a new instance of Plant */
    public Plant( int _id, String _name, ObjectType _objectType, int age, int health, Point3D _position, Point3D size, int scrWidth, int scrHeight, Color _color, PlantInfo xmlInfo, BufferedImage soilImage, float currSoil) {
    	super(_id, _name, _objectType, _position, (int) xmlInfo.interactionRange);
    	
    	NodeID = -1;
    	// assign soil
    	this.soilImage = soilImage;
    	soilValue = currSoil; // the soil value
    	
    	draggable = false;
        showInfo = false;
        
        plantInfo = xmlInfo;
        
        this.age = age;
        fitness = health;
        pEnergy = 1.0f;
        pWidth = size.x;
        pLength = size.y;
        pHeight = size.getZ();
        this.color = _color;        
        this.position = _position;
        this.size = size;
        
        screenWidth = scrWidth;
        screenHeight = scrHeight;
        
        // create a timer for tracking age
        int seconds = 1;
        //timer = new Timer();
        //timer.scheduleAtFixedRate(new IncrementAge(  ), 0, seconds*1000);
        
        // create a storage for other plants 
        //plants = new ArrayList<Plant>();
        
        // ***** NETWORK MAPPING
		String strColor = "'" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "'";
		String[] nStr = {"P-"+this.id, "0", strColor};
		NetworkInfo.addAgent(new NAgentNode(this.id, nStr));
        
    }
    

        
    @Override
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env)
    {
    	incAge.run();
    	//System.out.println(id + "plant age:" + age + " f:" + fitness);
    	if(this.canReproduce)
	    	if(age >= plantInfo.reproductionAge)    	  		
				if ((random.nextInt(100) < 8) && (fitness >= 0.0025f ))
				{
					reproduce(plantInfo.seedCount);
					//System.out.println("reproducing");
				}    		
    	
    	
    	
    	// compete
    	float spaceUsed = 0.0f;
    	float tempEmitter = 0.0f;
    	// ----------------------------------- DEAL WITH QUADTREE DATA
    	// loop through only those indices
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
    			//System.out.println(em.name);
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
    				//String[] neStr = {};
    				//NetworkInfo.addEdge(new NAgentEdge(this.id, tEmp.id, neStr));
    			}
        	}
    		else if(worldObjects.get(i).objectType == ObjectType.agent)
        	{
				// once found, cast it to the coder's custom type based on the name
				
				if ((this.id != worldObjects.get(i).id))
				{
	    			// ---------- here, test the objects to see what type it is...
	        		
					//trace("plant width:"+pPlants.itemIndex[i].mySprite.width);
					//trace ("width " + mySprite.width);
					// here, it is up to the coder to decide the type based on the name    			
	    			//if(worldObjects.get(i).name == "Plant")
					if(new String(worldObjects.get(i).name).equals("Plant"))
	    			{
	    				//System.out.println(".name == plant");
    	    			Plant P = (Plant) worldObjects.get(i);
    	    			float opSize = P.pWidth;
    	    			float dX = P.position.x - this.position.x;
    	    			float dY = P.position.y - this.position.y;
    					float dist = (float)Math.sqrt(dX*dX + dY*dY);				
    					
    					// if overlap	
    					
    					if ((dist-(opSize+this.pWidth)) < 0.0)
    					{
    						//System.out.println(dist-(opSize+this.pWidth));
    						//System.out.println("dist:" + dist + "plant Width[" + i + "]:" + this.pWidth);
    						
    						//if the age is the same or I'm younger
    						//there is no competition if I'm bigger
    						if ( (age <= P.age) && (this.pWidth <= P.pWidth))
    						{
    							// if (SAME Type)
    							//if ((plantInfo.Type == pPlants.itemIndex[i].plantInfo.Type))
    							//{
    								// compete!								
    								spaceUsed += 0.2f;
    								//trace(spaceUsed);
    							//}						
    								
    								// ***** NETWORK MAPPING
    			    				//String[] neStr = {"true"}; // true for directed
    			    				//NetworkInfo.addEdge(new NAgentEdge(this.id, P.id, neStr));
    						}
    						
    						// Limit space
    						if (spaceUsed >= 1.0f) {spaceUsed = 1.0f;}
    					}
	    			}
				}	
			}
    			//System.out.println("%------ IN AGENT (AGENT!)::  " + pl.name);
        }
    	
    	//System.out.println(spaceUsed);
    	
    	/*
    	for (int i=0; i<Plants.size(); i++)
        {
    		if ((this != Plants.get(i)) && (Plants.get(i) != null))
			{
				//trace("plant width:"+pPlants.itemIndex[i].mySprite.width);
				//trace ("width " + mySprite.width);
    			float opSize = ((Plant)Plants.get(i)).pWidth;
    			float dX = ((Plant)Plants.get(i)).pos.x - this.pos.x;
    			float dY = ((Plant)Plants.get(i)).pos.y - this.pos.y;
				float dist = (float)Math.sqrt(dX*dX + dY*dY);
				
				
				// if overlap				
				if ((dist-(opSize+this.pWidth)) < 0.0)
				{
					//System.out.println("dist:" + dist + "plant Width[" + i + "]:" + this.pWidth);
					
					//if the age is the same or I'm younger
					//there is no competition if I'm bigger
					if ((pAge <= ((Plant)Plants.get(i)).pAge) && (this.pWidth <= ((Plant)Plants.get(i)).pWidth))
					{
						// if (SAME Type)
						//if ((plantInfo.Type == pPlants.itemIndex[i].plantInfo.Type))
						//{
							// compete!								
							spaceUsed += 0.1f;
							//trace(spaceUsed);
						//}							
					}
					
					// Limit space
					if (spaceUsed >= 1.0f) {spaceUsed = 1.0f;}
					
					
				}
			}
        }
    	*/
    	
    	// sunlight
    	double sunFitness = LifeUtil.Adaptability(
    			CEnvironment.getSunlight(), 
    			plantInfo.sunLightPreferred, 
    			plantInfo.sunLightLower, 
    			plantInfo.sunLightUpper, 0.5);
    	
    	// temperature
    	double temfitness = LifeUtil.Adaptability(
    			CEnvironment.getTemperature() + tempEmitter, 
    			plantInfo.temperaturePreferred, 
    			plantInfo.temperatureLower, 
    			plantInfo.temperatureUpper, 0.5);
    	
    	// humidity
    	double humFitness = LifeUtil.Adaptability(
    			CEnvironment.getHumidity(), 
    			plantInfo.humidityPreferred, 
    			plantInfo.humidityLower, 
    			plantInfo.humidityUpper, 0.5);
    	
    	// soil
    	double soilFitness = LifeUtil.Adaptability(
    			soilValue, 
    			plantInfo.soilDepthPreferred, 
    			plantInfo.soilDepthLower, 
    			plantInfo.soilDepthUpper, 0.5);
    	
    	// space
    	double spaceFitness = LifeUtil.AdaptabilityRightBound(
    			spaceUsed, 
    			plantInfo.spacePreferred,    			
    			plantInfo.spaceUpper, 
    			0.5);    	
    	
    	//fitness = (float)(spaceFitness * soilFitness * temfitness * sunFitness * humFitness);
    	fitness = (float)(spaceFitness * soilFitness * temfitness * sunFitness * humFitness);
    	//System.out.println(fitness);
    	
    	/*
    	System.out.println(
				"humidity[" + Environment.getHumidity() + "]:" 
				+ humFitness 
				+ " p:" + plantInfo.humidityPreferred  
				+ " L:" + plantInfo.humidityLower
				+ " U:" + plantInfo.humidityUpper);
    	*/
    	
    	//if(fitness < 0.001)
    	/*
    		System.out.println(
    				"spaceUsed[" + spaceUsed + "]:" 
    				+ spaceFitness 
    				+ " p:" + plantInfo.spacePreferred  
    				+ " L:" + plantInfo.spaceLower
    				+ " U:" + plantInfo.spaceUpper);
    	*/
    	
    	// die
    	//if ((age > plantInfo.maxAge) || (fitness < 0.000001f))
    	if (fitness < 0.000001f)
    	{
    		pEnergy -= 0.01f;
    		//System.out.println("plant dying...");
    		if (pEnergy <= 0.0f)	// if energy is depleted
			{	
    			// TODO -------------------- remove from quadtree? or set a flag for QT to remove.
    			this.removeMe();
			}		
    		return;
    	}
	}
    
    /** paint method */
    public void paint(Graphics g)
    {
    	/*
    	// growing in size
    	pWidth += 0.1;
    	pLength += 0.1;
    	
    	if (pWidth >= 5)
    		pWidth = pLength = 10;    	
    	*/
    	
    	Font font = new Font("Arial", Font.BOLD, 9);	    
	    g.setFont(font);
    	
    	// set colour and draw the plant
        g.setColor( Color.black );
        g.drawOval( (int)(position.x-pWidth/2), (int)(position.y-pHeight/2), (int)pWidth, (int)pLength );
        g.setColor( this.color );
        g.fillOval( (int)(position.x-pWidth/2), (int)(position.y-pHeight/2), (int)pWidth, (int)pLength );
        
        if (showInfo == true)
        {
            //g.drawString( talk(), pos.x+ (int)pWidth+2, pos.y+(int)pLength+2 );            
            //g.drawString(""+pAge, pos.x+(int)pWidth+pHealth*4+4, pos.y+(int)pLength+12);
        	
        	String s = new DecimalFormat("0.###").format((double)fitness);
        	        	
        	new DecimalFormat("0.###").format((double)fitness);
        	g.setColor( Color.yellow );
        	g.drawString(""+this.id, position.x-(int)pWidth/2, position.y+1+(int)pLength/2);
        	//g.drawString(""+ID, pos.x+(int)pWidth+(int)(fitness*4)+4, pos.y+(int)pLength+12);
            //g.fillRect(pos.x+(int)pWidth+2, pos.y+(int)pLength+5, (int)(fitness*4), 2);
        }
    }
  
    /** grow method */    
    // based on states
    public void grow()
    {    	
    	
    }
    
    public void reproduce(int offspring)
    {
    	// random angle and distance    	
    	int rndAngle = random.nextInt(360);
    	int rndDist = random.nextInt(30);
    	
    	// random position around the parent plant
    	double rX = position.x + Math.ceil(rndDist*Math.cos(rndAngle));
    	double rY = position.y + Math.ceil(rndDist*Math.sin(rndAngle));
    	
    	// within the screen boundary
    	if( (((int)rX) < screenWidth) && ((int)rY) < screenHeight)
    	{
    		if((((int)rX) > 0) && ((int)rY) > 0)
        	{    			
    			// new location and size data
    			Point3D rLoc = new Point3D((int)rX,(int)rY, 0);
    	    	//Point3D size = new Point3D(2, 2, 2);
    	    	    	    	
            	// get soil value
        		int c = soilImage.getRGB((int)rLoc.x, (int)rLoc.y);	// read pixel
            	float normalised = (float)(c & 0x0000ff)/255;		// normalised
            	
            	// test soil fitness
            	double soilFitness = LifeUtil.AdaptabilityRightBound(normalised, plantInfo.soilDepthPreferred, plantInfo.soilDepthUpper, 0.5f);
            	//System.out.println("soil fitness:" + soilFitness);
            	
            	boolean outOfWorld = false;
            	
            	if((rLoc.x >= screenWidth) || (rLoc.x <= 0))
            		outOfWorld = true;
            	if((rLoc.y >= screenHeight) || (rLoc.y <= 0))
            		outOfWorld = true;
            	
            	if(!outOfWorld)
            	{
	            	// if of suitable soil type
	            	if(soilFitness > 0.01d)
	            	{
	            		//System.out.println("reproducing...:" + rLoc.x + ", " + rLoc.y);
	            		// ----------------- create plant (ADD TO QUADTREE)
	            		//int nID = QuadTree.placeObjectInNode(QuadTree.qtNodes.get(0), rLoc, TrophicNetwork.AgentIDInc, ObjectType.agent);
	        	    	Plant p = new Plant( TrophicNetwork.AgentIDInc++, "Plant", this.objectType, 1, 10, rLoc, size, screenWidth, screenHeight, this.color, plantInfo, soilImage, normalised);
	        			CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), p); // add to root node and let it cascade            		
	            	}    	    	
            	}
        	}    		
    	}
    	
    }

    /** accessor methods: modify draggable */
    public void setDraggable(boolean b){
        draggable = b;
    }
    
    /** return draggable */
    public boolean isDraggable(){
        return draggable;
    }
    
    /** check if x,y is inside this object */
    public boolean inside(int x, int y){
        // if my x,y is <= to mouseX and mouseY
        // if my x + width >= x
        return (position.x <= x && position.y <= y && 
        (position.x + pWidth >= x) && (position.y + pHeight >= y));
}			
    
    /** translate position */
    public void translate(int x, int y){
        int locX = position.x;
        int locY = position.y;
        locX += x;
        locY += y;
        
        position.setX(locX);
        position.setY(locY);        
    }
    
    /** set showInfo to true */
    public void setShowInfo(boolean b) {
        showInfo = b;
    }
    
    /** showInfo */
    public boolean showInfo(){
     return showInfo;   
    }     
    
    public void setColor(Color c)
    {
    	this.color = c;
    }

}
