package life;

import java.awt.Graphics;

import utilities.CRenderable;
import utilities.CWorldObject;
import utilities.ObjectType;
import utilities.Point3D;


public class CAgent extends CWorldObject {
	
    

	
	public CAgent(int _id, String _name, ObjectType _objectType, Point3D _position, int _range)
	{
		super(_id, _name, _objectType, _position, _range);		
	}


    
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
