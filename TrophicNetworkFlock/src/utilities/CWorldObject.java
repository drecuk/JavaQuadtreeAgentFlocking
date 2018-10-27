// This class can be managed by CQUADTREE

package utilities;

import java.awt.Graphics;
import java.util.ArrayList;

import simulation.CEnvironment;

public class CWorldObject extends CRenderable implements iWorldObject {
	
	// objectType allows differentiation between agent and emitter
	public ObjectType objectType;
	
	// name allows user-defined differetiation below agent/emitter
	// this makes it very flexible within the user agent's update method
	// for casting the types created by the user
	public String name;
	
	// category allows a greater control over trophic level and other interaction/communication
	// this makes it very flexible within the user agent's update method	
	public String category;	// maybe upperCategory and lowerCategory? upper/lower foodchain
	
	// a nodeID to store which node it is in for more efficient processing
	public int nodeID;
	
	// every worldObject must have a position because they are in the world!
	public Point3D position;
	
	// every worldObject must have a range - from eyesight of creatures for range query
	public int range;
	
	// every object must have a dead flag that can be raised for for quadtree to remove it
	//****
	public boolean isDead;
		
	
	public CWorldObject(int _id, String _name, ObjectType _objectType, Point3D _point3D, int _range) {
		super(_id);
		// TODO Auto-generated constructor stub
		
		name = _name;
		objectType = _objectType;
		position = _point3D;
		isDead = false;
		range = _range;
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env) {
		// TODO Auto-generated method stub
		
	}
	
	public void removeMe()
	{
		isDead = true;
	}
}
