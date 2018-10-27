package life;

import utilities.ObjectType;
import utilities.Point3D;

public abstract class CVagile extends COrganism {

    // ---------------------------- AVOIDING HAZARDOUS EMITTERS
 	protected float tempEff = 0.0f;
 	protected float earlierHeat = 0.0f;
 	protected int pSwitchCount = 0;
	public float angle = 60;
	public float speed;
	
	// for calculating movement
	public int xMove;	
	public int yMove;
	
	public CVagile(int _id, String _name, ObjectType _objectType, Point3D _position, int _range) {
		super(_id, _name, _objectType, _position, _range);
		// TODO Auto-generated constructor stub
	}

}
