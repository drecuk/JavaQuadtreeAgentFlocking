package life;

import java.util.TimerTask;

import simulation.TemperatureEmitter;
import utilities.ObjectType;
import utilities.Point3D;
import java.util.Timer;
import java.util.TimerTask;

public class COrganism extends CAgent {
	protected float fitness;
	protected float age; //age in days
	protected Timer timer;
    protected IncrementAge incAge = new IncrementAge();

    public boolean canReproduce = false;
    protected float energyToReproduce; 
    
	public COrganism(int _id, String _name, ObjectType _objectType, Point3D _position, int _range) {
		super(_id, _name, _objectType, _position, _range);
		// TODO Auto-generated constructor stub
		
		
	}
	
    class IncrementAge extends TimerTask
    {
        public void run()
        {
        	age += 0.01f;
        	//System.out.println("age:"+pAge);
        }        
    }
}
