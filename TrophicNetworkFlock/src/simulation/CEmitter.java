package simulation;

import java.awt.Graphics;

import utilities.CWorldObject;
import utilities.ObjectType;
import utilities.Point3D;

public class CEmitter extends CWorldObject {

	public float condition;
	public float gradient;
	
	public CEmitter(int _id, String _name, ObjectType _objectType, Point3D _position, int _range, float _condition, float _gradient) {
		super(_id, _name, _objectType, _position, _range);
		// TODO Auto-generated constructor stub
		
		this.condition = _condition;
		this.gradient = _gradient;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub

	}
}
