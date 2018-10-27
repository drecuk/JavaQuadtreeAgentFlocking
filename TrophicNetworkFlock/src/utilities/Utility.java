package utilities;

import java.util.ArrayList;
import java.util.Random;

import life.CVagile;

public class Utility {

	// Get Distance
	public static Random rnd = new Random(); 
	
	public static float distance(int sourceX, int sourceY, int targetX, int targetY)
	{
		// get component distance from target to me
		float pX = targetX - sourceX;
		float pY= targetY - sourceY;
		
		// get distance
		return (float) Math.sqrt((pX*pX)+(pY*pY));			
	}
	
	public static Vector2f vRotate2D(float angle, int sourceX, int sourceY, int targetX, int targetY)
	{			
		// Calculate component distance
		float distX = targetX - sourceX;
		float distY = targetY - sourceY;
		
		// Define a return array for global to local x and y
		Vector2f vector = new Vector2f(0,0);

		vector.x = (distX * Math.cos(-angle*Math.PI/180) + distY * Math.sin(-angle*Math.PI/180)); // x
		vector.y = (-distX * Math.sin(-angle*Math.PI/180) + distY * Math.cos(-angle*Math.PI/180)); // y
					
		return vector;
	}
	
	// ** --------------------- BOIDS BEHAVIOUR ----------------------- **
	
		// ------------ collect flocking vectors:: COHESION
		public static Vector2f cohesion(Point3D myself, ArrayList<Integer> others, ArrayList<CWorldObject> worldObjects, float cohesionRatio)
		{
			Vector2f coh = new Vector2f(0,0);
			
			for (int i=0; i<others.size(); i++)	// clanIndices was collected earlier
			{
				// all moving objects are child of CVagile
				CVagile friend = (CVagile)worldObjects.get(others.get(i));
				coh.x += friend.position.x;
				coh.y += friend.position.y;
			}
			
			if(others.size() > 0)	// average out
			{
				// average the centre of mass
				coh.x = (coh.x / others.size());
				coh.y = (coh.y / others.size());
				
				// move towards the centre position
				coh.x = (coh.x - myself.x) / cohesionRatio;
				coh.y = (coh.y - myself.y) / cohesionRatio;
			}
			
			return coh;
		}
		
		// ------------ collect flocking vectors:: SEPARATION
		// sepDistance refers to the distance the boids comes close to each other before they separate
		// sepRatio is the 'smoothness' of the separation, the higher the smoother, the smaller, the faster they try to separate
		// sepRatio is also the 'calmness', the more panicky they are, the smaller the value.
		public static Vector2f separation(Point3D myself, ArrayList<Integer> others, ArrayList<CWorldObject> worldObjects, float sepDistance, float sepRatio)
		{
			Vector2f sep = new Vector2f(0,0);
			
			for (int i=0; i<others.size(); i++)	// clanIndices was collected earlier
			{
				// all moving objects are child of CVagile
				CVagile friend = (CVagile)worldObjects.get(others.get(i));
				if ((Utility.distance(myself.x, myself.y, friend.position.x, friend.position.y) < sepDistance))
				{
					sep.x -= (friend.position.x - myself.x) / sepRatio; // proper
					sep.y -= (friend.position.y - myself.y) / sepRatio; // proper
				}
			}
			
			return sep;
		}
		
		// ------------ collect flocking vectors:: ALIGNMENT
		public static Vector2f alignment(Point3D myself, ArrayList<Integer> others, ArrayList<CWorldObject> worldObjects)
		{
			Vector2f alig = new Vector2f(0,0);	
			
			for (int i=0; i<others.size(); i++)	// clanIndices was collected earlier
			{
				// all moving objects are child of CVagile
				CVagile friend = (CVagile)worldObjects.get(others.get(i));
				alig.x += Math.cos(friend.angle*Math.PI/180); 
				alig.y += Math.sin(friend.angle*Math.PI/180);
				
			}
			if(others.size() > 0)	// average out
			{
				alig.x = (alig.x / others.size());
				alig.y = (alig.y / others.size());
			}
		
			return alig;
		}
			
		// ------------ collect flocking vectors:: VELOCITY
		public static Vector2f velocity(Point3D myself, ArrayList<Integer> others, ArrayList<CWorldObject> worldObjects)
		{
			Vector2f vel = new Vector2f(0,0);	
			
			for (int i=0; i<others.size(); i++)	// clanIndices was collected earlier
			{
				// all moving objects are child of CVagile
				CVagile friend = (CVagile)worldObjects.get(others.get(i));
				vel.x += friend.xMove; 
				vel.y += friend.yMove;
				
			}
			if(others.size() > 0)
			{
				vel.x = ((vel.x/others.size()) - myself.x) / 8;
				vel.y = ((vel.y/others.size()) - myself.y) / 8;
			}
			
			return vel;
		}
		
		// ------------ collect flocking vectors:: TENDENCY
		public static Vector2f tendency(Point3D target)
		{
			Vector2f tend = new Vector2f(0,0);	
			
			tend.x = target.x;
			tend.y = target.y;
		
			return tend;
		}
}
