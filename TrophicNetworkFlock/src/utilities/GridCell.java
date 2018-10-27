package utilities;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
public class GridCell {
	
	int x, y;		// indices
	//int i, j; 	// the indices
	int prevX, prevY;	// previous node indices
	int centerX, centerY;	// cell centre for navigation
	int width, height;
	
	// ------- PATH FINDING STATES
	boolean walkable = true;
	boolean visited = false;
	Color color;
	
	// for calculating cost from source to destination
	// cost will never be stored in the grid, but in a temp grid in GridObjectcells.uncheckedNeighbours
	float cost = 0;
	
	// ------- CONDITION OF THE CELL
	// condition of the cell (how much it has been trodden)
	// heavier animals tread heavier
	float treadWeight = 0;
	
	// ------- Stacked condition (for visualisation, but can be used for other things)
	// this must at least have one element, for colour to be shown.
	ArrayList<CellRGB> conditionsRGB = new ArrayList<CellRGB>();
	
	// flag for active walker cell
	boolean showTreadColor = true;
	
	public GridCell(int _x, int _y, int _width, int _height, boolean _visited, boolean _walkable, float _cost, Color _color)
	{
		conditionsRGB.add(new CellRGB(_color.getRed(), _color.getGreen(), _color.getBlue()));
		
		// x and y are the indices! not the actual spatial coordinates
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		
		prevX = -1;
		prevY = -1;
		
		// TODO calculate cell centre
		// this is for agents to record in their path
		centerX = x*width + (width/2);
		centerY = y*height + (height/2);
		
		walkable = _walkable;
		visited = _visited;
		cost = _cost;
		
		color = _color;
	}
	
	public void paint(Graphics g) 
	{
		if(showTreadColor)
		{
			// compile color
			int R = 0;
			int G = 0;
			int B = 0;
			
			for(int i=0; i<conditionsRGB.size(); i++)
			{
				R += conditionsRGB.get(i).r;
				G += conditionsRGB.get(i).g;
				B += conditionsRGB.get(i).b;
			}
			
			// make sure its within the 8 bit space
			if(R > 255) R = 255;
			if(G > 255) G = 255;
			if(B > 255) B = 255;
			
			if(R < 0) R = 0;
			if(G < 0) G = 0;
			if(B < 0) B = 0;
			
			Color color = new Color(R,G,B);	// build color
			
			if(this.walkable)
				g.setColor(color);	// the color is a local variable
			else
				g.setColor(Color.black);
		}
		else
		{
			if(this.walkable)
				g.setColor(color);	// the color here is a global variable
			else
				g.setColor(Color.black);
		}
		
		
		g.fillRect(x*width,  y*height, width, height);
		
		/*
		Font font = new Font("Arial",Font.BOLD,8);
		g.setColor( Color.red );	
	    g.setFont(font);
    	g.drawString(visited + "", x*width + 5, y*height + (height/2));
    	*/
	}
	
	public void update()
	{
		
	}
}
