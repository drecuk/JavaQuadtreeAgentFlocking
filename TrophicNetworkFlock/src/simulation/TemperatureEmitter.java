package simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

import utilities.CWorldObject;
import utilities.NAgentNode;
import utilities.NetworkInfo;
import utilities.ObjectType;
import utilities.Point3D;

public class TemperatureEmitter extends CEmitter {

	public Color pColor;
	public int width;
	public int length;
	public boolean showInfo;	
	
	public TemperatureEmitter(int _id, String _name, ObjectType _objectType,
			Point3D _position, Color color, int width, int length, int _range, float _condition, float _gradient)
	{
		super(_id, _name, _objectType, _position, _range, _condition, _gradient);
		// TODO Auto-generated constructor stub
		
		this.pColor = color;
		showInfo = true;
		this.width = width;
		this.length = length;		
		
		// ***** NETWORK MAPPING
		String strColor = "'" + pColor.getRed() + "," + pColor.getGreen() + "," + pColor.getBlue() + "'";
		String[] nStr = {_name+this.id, "0", strColor};
		NetworkInfo.addAgent(new NAgentNode(this.id, nStr));
	}
	
	@Override
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env)
	{
		
	}
	
	@Override
	public void paint(Graphics g)
	{
		// TODO Auto-generated method stub
		Font font = new Font("Arial", Font.BOLD, 9);	    
	    g.setFont(font);
    	
    	// set colour and draw the plant
        g.setColor( this.color );										
        g.fillOval( (int)(position.x-this.width/2), (int)(position.y-this.length/2), (int)this.width, (int)this.length );
        
        if (showInfo == true)
        {
            //g.drawString( talk(), pos.x+ (int)width+2, pos.y+(int)length+2 );            
            //g.drawString(""+pAge, pos.x+(int)width+pHealth*4+4, pos.y+(int)length+12);
        	
        	g.setColor( Color.black );
        	g.drawString(""+this.id, position.x-(int)width/2, position.y+1+(int)length/2);
        	//g.drawString(""+ID, pos.x+(int)width+(int)(pFitness*4)+4, pos.y+(int)length+12);
            //g.fillRect(pos.x+(int)width+2, pos.y+(int)length+5, (int)(pFitness*4), 2);
        }
	}

}
