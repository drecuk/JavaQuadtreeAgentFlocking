package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
//import java.text.DecimalFormat;

public class Grid {

	int width, height;
	int row, col;
	int cellSize;
	
	Color lineColour;
	Color cellColour;
	Color textColour;
	
	double grid[][];
	boolean showGrid = true;
	boolean showText = false;
	boolean showLine = true;
	
	public Grid(int _width, int _height, int _row, int _col, Color _lineColour, Color _cellColour)
	{
		width = _width;
		height = _height;
		row = _row;
		col = _col;
		lineColour = _lineColour;
		cellColour = _cellColour;
		textColour = Color.white;
		
		cellSize = width/row;
		
		grid = new double[width][height];
		
		
	}
	
	public void initGrid(int value)
	{
		for(int i=0; i<row; i++)
		{
			for(int j=0; j<row; j++)
			{
				grid[i][j] = value;
			}
		}
	}
	
	public void paint(Graphics g) {
		
		if (showGrid)
        {
			// set colour and draw the plant
	        								
	        for(int i=0; i<row; i++)
			{
	        	for(int j=0; j<col; j++)
				{	
	        		// test for grid on/off state
	        		if(grid[i][j] == 1)
	            	{
	            		g.setColor( cellColour );	
	            		g.fillRect(i*cellSize,  j*cellSize, i*cellSize + (cellSize), j*cellSize + (cellSize));
	            	}
	        		if(grid[i][j] == 0)
	        		{
	        			g.setColor( Color.black );	
	            		g.fillRect(i*cellSize,  j*cellSize, i*cellSize + (cellSize), j*cellSize + (cellSize));
	        		}
	        		
	        		if(showLine)
	        		{
		        		g.setColor( lineColour );	
		        		g.drawLine(0, j*cellSize, width, j*cellSize ); 		// row lines
		        		g.drawLine(i*cellSize, 0, i*cellSize, height ); 	// col lines
	        		}
	        		
					//grid[i][j] = value;
	        		
	            	//new DecimalFormat("0.###").format((double)fitness);
	        		if(showText)
	        		{
		        		Font font = new Font("Arial",Font.BOLD,8);
		        		g.setColor( textColour );	
		        	    g.setFont(font);
		            	g.drawString(i+","+j, i*cellSize, j*cellSize + (cellSize/2));
	        		}
	        		
				}
			}
	        
            //g.fillRect(position.x+(int)pWidth+2, position.y+(int)pWidth+5, (int)(fitness*4), 2);
        }

	}

	public void switchState(int x, int y)
	{
		double cx = (x/(float)width) * row;
		double cy = (y/(float)height) * col;
		
		int fx = (int) Math.floor( cx );
		int fy = (int) Math.floor( cy );
		
		//System.out.println(width + "," + height + " | " + row + "," + col);
		//System.out.println("x/width y/width :: " + x/(float)width + "," + y/(float)height);
		System.out.println("mouse to cell :: " + x + "," + y + " >> " + fx + "," + fy);
		
		// set on off
		if(grid[fx][fy] == 0)
		{
			grid[fx][fy] = 1;
			//System.out.println("switchstate:"+grid[fx][fy]);
			return;
		}
		
		if(grid[fx][fy] == 1)
		{
			grid[fx][fy] = 0;
			//System.out.println("switchstate:"+grid[fx][fy]);
		}
	}
	
	public void navigateTo(int fromX, int fromY, int toX, int toY)
	{
		
	}
	
	public void update() {
		
		
	}
}
