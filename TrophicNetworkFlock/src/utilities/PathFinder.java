package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
//import java.text.DecimalFormat;
import java.util.ArrayList;
import utilities.Point2D;

public class PathFinder {

	// basic properties
	private static int width, height;
	private static int row, col;
	private static int cellSize;
	
	// the cells
	public static GridCell grid[][];
	
	// colour
	public static Color lineColour;
	public static Color cellColour;
	public static Color textColour;
	
	// flags for showing graphics
	public static boolean showGrid = true;
	public static boolean showText = false;
	public static boolean showLine = false;
	
	// ----------------- PATH FINDING MANAGEMENT
	private static boolean pathFound;														// flag to make path
	private static ArrayList<GridCell> uncheckedNeighbours = new ArrayList<GridCell>();	// neighbour cells that needs checking
	public static int sourceX, sourceY, destX, destY;
	
	// for storing cells that have costs changed - for resetting
	static ArrayList<Point2D> resetList = new ArrayList<Point2D>();
	
	// ----------------- SAVE STATE
	private static FileWriter stateFW;;
	
	// Best if width,height and row,col are a power of two (then there is no remainder)
	public PathFinder(int _width, int _height, int _row, int _col, Color _lineColour, Color _cellColour)
	{
		/*
		width = _width;
		height = _height;
		row = _row;
		col = _col;
		lineColour = _lineColour;
		cellColour = _cellColour;
		textColour = Color.red;
		
		// the size of each cell is the width divided by row or columns
		cellSize = width/row;
		*/
		
		
	}
	
	public static void initGrid(int _width, int _height, int _row, int _col, Color _lineColour, Color _cellColour, int value)
	{
		width = _width;
		height = _height;
		row = _row;
		col = _col;
		lineColour = _lineColour;
		cellColour = _cellColour;
		textColour = Color.red;
		
		// the size of each cell is the width divided by row or columns
		cellSize = width/row;
		
		grid = new GridCell[row][col];
		
		for(int i=0; i<row; i++)
		{
			for(int j=0; j<col; j++)
			{
				//GridCell(int _x, int _y, int _width, int _height, boolean _visited, boolean _walkable, float _cost, Color _color)
				grid[i][j] = new GridCell(i, j, cellSize, cellSize, false, true, value, cellColour);
			}
		}
	}
	
	public static void paint(Graphics g) {
		
		if (showGrid)
        {
			// set colour and draw the plant
	        								
	        for(int i=0; i<row; i++)
			{	
	        	for(int j=0; j<col; j++)
				{	
	        		grid[i][j].paint(g);
	        		
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
		            	g.drawString(i+"", i*cellSize+2, j*cellSize+8);
		            	g.drawString(j+"", i*cellSize+8, j*cellSize + 28);
		            	//g.drawString(grid[i][j].walkable+"", i*cellSize+3, j*cellSize + 3);
	        		}
	        		
				}
			}
	        
            //g.fillRect(position.x+(int)pWidth+2, position.y+(int)pWidth+5, (int)(fitness*4), 2);
        }

	}

	// -1 = boolean, 0 = off, 1 = on
	public static void switchState(int x, int y, int state)
	{
		double cx = (x/(float)width) * row;
		double cy = (y/(float)height) * col;
		
		int fx = (int) Math.floor( cx );
		int fy = (int) Math.floor( cy );
		
		if(state == -1)
		{	
			//System.out.println(width + "," + height + " | " + row + "," + col);
			//System.out.println("x/width y/width :: " + x/(float)width + "," + y/(float)height);
			System.out.println("mouse to cell :: " + x + "," + y + " >> " + fx + "," + fy);
			
			// set on off
			if(grid[fx][fy].walkable)
			{
				grid[fx][fy].color = Color.black;
				grid[fx][fy].walkable = false;
				//System.out.println("switchstate:"+grid[fx][fy]);
				
				return;
			}
			
			if(!grid[fx][fy].walkable)
			{
				grid[fx][fy].color = cellColour;
				grid[fx][fy].walkable = true;
				//System.out.println("switchstate:"+grid[fx][fy]);
			}
		}
		else if (state == 1)
		{
			grid[fx][fy].color = Color.black;
			grid[fx][fy].walkable = false;
		}
		else
		{
			grid[fx][fy].color = cellColour;
			grid[fx][fy].walkable = true;
		}
	}
	
	public static Point2D posToCellIndex(int x, int y)
	{
		Point2D p = new Point2D(0,0);
		double cx = (x/(float)width) * row;
		double cy = (y/(float)height) * col;
		
		p.x = (int) Math.floor( cx );
		p.y = (int) Math.floor( cy );
		
		return p;
	}
	
	public static Point2D getCellCoordinate(int x, int y)
	{
		Point2D p = new Point2D(-1,-1);
		p.x = grid[x][y].centerX;
		p.y = grid[x][y].centerY;
		
		return p;
	}
	
	public static ArrayList<Point2D> getPathToRandomTargetCell(int posx, int posy, int distance)
	{
		ArrayList<Point2D> goodCellsToTarget = new ArrayList<Point2D>();
	
		// set up targets at 'distance' and convert to position
		//int min = 0;
		//int max = 360;
		// nextInt is exclusive of top value add 1 to make it inclusive
		//float angle = Utility.rnd.nextInt(max - min + 1) + min;
		
		// convert origin to cell
		Point2D p = posToCellIndex(posx, posy);
		int x = p.x;
		int y = p.y;
		
		// ---- random angle location at distance from the origin
		float angle = Utility.rnd.nextInt(360);
		int dx = (int) (posx + distance*Math.cos(angle));
		int dy = (int) (posy + distance*Math.sin(angle));
		
		// convert target to cell
		Point2D pT = posToCellIndex(dx, dy);
		int tx = pT.x;
		int ty = pT.y;
		
		// ---- check for off boundary
		if((dx > width || dx < 0) || (dy > height || dy < 0))	// if any term is true (even if one cell is off)
		{
			System.out.println("*** PROBLEMS --->> getPathToRandomTargetCell is off boundary");
			return goodCellsToTarget;
		}
		else if(!grid[tx][ty].walkable)
		{
			System.out.println("*** PROBLEMS --->> getPathToRandomTargetCell target is NOT walkable!");
			return goodCellsToTarget;
		}
		else	// if not off boundary
		{
			// ---- find the path
			goodCellsToTarget = findPath(x, y, tx, ty);
			
			return goodCellsToTarget;
		}
	}
	
	public static ArrayList<Point2D> getNextWalkableCells(int posx, int posy)
	{	
		Point2D p = posToCellIndex(posx, posy);
		int x = p.x;
		int y = p.y;
		//System.out.println("getNextWalkableCells -- cell[" + x + "," + y + "]");
		
		// convert coordinate to cell index
		ArrayList<Point2D> goodCells = new ArrayList<Point2D>();
		
		// --- CHECK OFF BOUNDARIES and WALKABLE
		if((x+1 <= row-1) && (grid[x+1][y].walkable))	// E
			goodCells.add(getCellCoordinate(x+1, y));
		
		//diag
		if((x+1 <= row-1) && (y+1 <= col-1) && (grid[x+1][y+1].walkable))	// SE
			goodCells.add(getCellCoordinate(x+1, y+1));
		
		if((x-1 >= 0) && (grid[x-1][y].walkable))	// W 
			goodCells.add(getCellCoordinate(x-1, y));
		
		//diag
		if(((x-1 >= 0) && (y-1 >= 0)) && (grid[x-1][y-1].walkable))	// NW  
			goodCells.add(getCellCoordinate(x-1, y-1));
		
		if((y+1 <= col-1) && (grid[x][y+1].walkable))	// S 
			goodCells.add(getCellCoordinate(x, y+1));
		
		//diag
		if(((y+1 <= col-1) && (x-1 >= 0))  && (grid[x-1][y+1].walkable))	// SW 
			goodCells.add(getCellCoordinate(x-1, y+1));
		
		if((y-1 >= 0) && (grid[x][y-1].walkable))	// N
			goodCells.add(getCellCoordinate(x, y-1));
		
		//diag
		if(((y-1 >= 0) && (x+1 <= row-1)) && (grid[x+1][y-1].walkable))	// NE
			goodCells.add(getCellCoordinate(x+1, y-1));
		
		//if(goodCells.size() != 0)
			//System.out.println("goodCells found: " + goodCells.size());
		
		return goodCells;
	}
	
	public static ArrayList<Point2D> getNextWalkableCellsToTarget(int posx, int posy, int targetx, int targety)
	{	
		ArrayList<Point2D> goodCells = new ArrayList<Point2D>();
		
		// convert coordinate to cell index
		Point2D p = posToCellIndex(posx, posy);
		int x = p.x;
		int y = p.y;
		Point2D t = posToCellIndex(targetx, targety);
		int tx = t.x;
		int ty = t.y;
		
		// if it so happens that x == row (e.g., 32 rather than 31 in the case where posx sent to posToCellIndex is 1024)
		// make sure it is in the right cell.
		if(x == row) x = row-1;
		if(y == col) y = col-1;
		
		System.out.println("getNextWalkableCellsToTarget::(" + posx + "," + posy + ") start:[" + x + "," + y + "] to [" + tx + "," + ty + "]" );
		
		// cost from current cell to target cell
		float cost = Math.abs(tx - x) + Math.abs(ty - y);
		
		// --- CHECK OFF BOUNDARIES and WALKABLE
		if(x+1 <= row-1)	// E
			if(grid[x+1][y].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs((tx+1) - x) + Math.abs(ty - y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x+1, y));
			}
		
		//diag
		if((x+1 <= row-1) && (y+1 <= col-1))	// SE
			if(grid[x+1][y].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs((tx+1) - x) + Math.abs((ty+1) - y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x+1, y+1));
			}
		
		if(x-1 >= 0)	// W
			if(grid[x-1][y].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs((tx-1) - x) + Math.abs(ty - y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x-1, y));
			}
		
		//diag
		if((x-1 >= 0) && (y-1 >= 0))	// NW
			if(grid[x-1][y-1].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs((tx-1) - x) + Math.abs((ty-1) - y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x-1, y-1));
			}
		
		if(y+1 <= col-1)	// S
			if(grid[x][y+1].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs(tx - x) + Math.abs((ty+1) + y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x, y+1));
			}
		
		//diag
		if((y+1 <= col-1) && (x-1 >= 0))	// SW
			if(grid[x-1][y+1].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs((tx-1) - x) + Math.abs((ty+1) + y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x-1, y+1));
			}
		
		if(y-1 >= 0)	// N
			if(grid[x][y-1].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs(tx - x) + Math.abs((ty-1) + y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x, y-1));
			}
		
		//diag
		if((y-1 >= 0) && (x+1 <= row-1))	// NE
			if(grid[x+1][y-1].walkable)
			{
				// cost from neighbour cell to target cell
				float ncost = Math.abs((tx+1) - x) + Math.abs((ty-1) + y);
				if(ncost < cost)
					goodCells.add(getCellCoordinate(x+1, y-1));
			}
		
		return goodCells;	
	}
	
	public static ArrayList<Point2D> findPath(int _startX, int _startY, int _targetX, int _targetY)
	{
		Point2D pt = getCellCoordinate(_targetX, _targetY);
		System.out.println("---------------------- FIND PATH [" + _startX + "][" + _startY + "] TO [" + _targetX + "][" + _targetY + "] " + pt.x + "," + pt.y);
		// return path
		ArrayList<Point2D> returnPath = new ArrayList<Point2D>();
		
		// store start and target cell coordinates
		sourceX = _startX;
		sourceY = _startY;
		destX = _targetX;
		destY = _targetY;
		
		// change colour
		grid[sourceX][sourceY].color = Color.green;
		grid[destX][destY].color = Color.red;
		
		//Point2D p = new Point2D(0,0);
		// calculate cost
		float cost = Math.abs(sourceX - destX) + Math.abs(sourceY - destY);
		grid[sourceX][sourceY].cost = cost;
		//resetList.add(new Point2D(sourceX, sourceY));
		
		//System.out.println("---- First Node cost [" + sourceX + "][" + sourceY + "]:" + cost);
		
		// add first node to unchecked neighbours for later checking
		uncheckedNeighbours.add(new GridCell(sourceX, sourceY, cellSize, cellSize, true, true, cost, cellColour));
		
		// ---------- START TRAVERSAL
		while(uncheckedNeighbours.size() > 0)
		{
			//try { Thread.sleep(500); } catch (Exception e) {}
			
			// get the first item (top item)
			GridCell cell = uncheckedNeighbours.remove(0); //.get(0);//
			//uncheckedNeighbours.get(0).visited = true;
			
			// have we found the target?
			if(cell.x == destX && cell.y == destY)
			{
				// found target!
				//System.out.println("******* found target: " + cell.x + "," + cell.y);
				returnPath = makePath(cell);	// create path to target (cell)
				pathFound = true;
				break;
			}
			else	// no target found yet
			{
				//cell.visited = true;
				
				//System.out.println("-- addNeighbourNode called from cell [" + (cell.x) + "][" + (cell.y) + "]");
				
				// add adjacent cells (not diagonal cells but could be added)
				// test for external boundaries
				if(cell.x+1 <= row-1) // E
				{
					if((!grid[cell.x+1][cell.y].visited)  && (grid[cell.x+1][cell.y].walkable))
						addNeighbourNode(cell, cell.x+1, cell.y, destX, destY);
					//System.out.println("add[" + (cell.x+1) + "][" + (cell.y) + "]");
					//grid[cell.x+1][cell.y].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x+1) + "][" + (cell.y) + "] not added... ");
				
				//diag
				if((cell.x+1 <= row-1) && (cell.y+1 <= col-1))	// SE
				{
					if((!grid[cell.x+1][cell.y+1].visited)  && (grid[cell.x+1][cell.y+1].walkable))
						addNeighbourNode(cell, cell.x+1, cell.y+1, destX, destY);
					//System.out.println("add[" + (cell.x+1) + "][" + (cell.y) + "]");
					//grid[cell.x+1][cell.y].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x+1) + "][" + (cell.y+1) + "] not added... ");
				
				if(cell.x-1 >= 0)	// W 
				{
					if((!grid[cell.x-1][cell.y].visited) && (grid[cell.x-1][cell.y].walkable))
						addNeighbourNode(cell, cell.x-1, cell.y, destX, destY);
					//System.out.println("add[" + (cell.x-1) + "][" + (cell.y) + "]");
					//grid[cell.x-1][cell.y].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x-1) + "][" + (cell.y) + "] not added... ");
				
				//diag
				if((cell.x-1 >= 0) && (cell.y-1 >= 0))	// NW 
				{
					if((!grid[cell.x-1][cell.y-1].visited) && (grid[cell.x-1][cell.y-1].walkable))
						addNeighbourNode(cell, cell.x-1, cell.y-1, destX, destY);
					//System.out.println("add[" + (cell.x-1) + "][" + (cell.y) + "]");
					//grid[cell.x-1][cell.y].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x-1) + "][" + (cell.y-1) + "] not added... ");
				
				if(cell.y+1 <= col-1)	// S
				{
					if((!grid[cell.x][cell.y+1].visited) && (grid[cell.x][cell.y+1].walkable))
						addNeighbourNode(cell, cell.x, cell.y+1, destX, destY);
					//System.out.println("add[" + (cell.x) + "][" + (cell.y+1) + "]");
					//grid[cell.x][cell.y+1].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x) + "][" + (cell.y+1) + "] not added... ");
				
				//diag
				if((cell.y+1 <= col-1) && (cell.x-1 >= 0))	// SW 
				{
					if((!grid[cell.x-1][cell.y+1].visited) && (grid[cell.x-1][cell.y+1].walkable))
						addNeighbourNode(cell, cell.x-1, cell.y+1, destX, destY);
					//System.out.println("add[" + (cell.x) + "][" + (cell.y+1) + "]");
					//grid[cell.x][cell.y+1].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x-1) + "][" + (cell.y-1) + "] not added... ");
				
				if(cell.y-1 >= 0)	// N
				{
					if((!grid[cell.x][cell.y-1].visited)  && (grid[cell.x][cell.y-1].walkable))
						addNeighbourNode(cell, cell.x, cell.y-1, destX, destY);
					//System.out.println("add[" + (cell.x) + "][" + (cell.y-1) + "]");
					//grid[cell.x][cell.y-1].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x) + "][" + (cell.y-1) + "] not added... ");
				
				//diag
				if((cell.y-1 >= 0) && (cell.x+1 <= row-1))	// NW
				{
					if((!grid[cell.x+1][cell.y-1].visited)  && (grid[cell.x+1][cell.y-1].walkable))
						addNeighbourNode(cell, cell.x+1, cell.y-1, destX, destY);
					//System.out.println("add[" + (cell.x) + "][" + (cell.y-1) + "]");
					//grid[cell.x][cell.y-1].color = Color.yellow;
				}
				else
					System.out.println("XX - [" + (cell.x+1) + "][" + (cell.y-1) + "] not added... ");
			}
		}
		
		// ------------------------------------ RESET GRID
		grid[sourceX][sourceY].cost = -1;
		
		for(int i=0; i<resetList.size(); i++)
		{
			grid[resetList.get(i).x][resetList.get(i).y].visited = false;
			grid[resetList.get(i).x][resetList.get(i).y].prevX = -1;
			grid[resetList.get(i).x][resetList.get(i).y].prevY = -1;
		}
		uncheckedNeighbours.clear();
		
		/*
		if(pathFound) 
		{
			System.out.println(">>>>>> PATH FOUND!");
			for(int i=0; i<returnPath.size(); i++)
			{
				System.out.println(returnPath.get(i).x + "," + returnPath.get(i).y);
			}
		}
		else
			System.out.println(">>>>>> PATH NOT FOUND!");
		*/
		
		// Convert path to coordinates
		ArrayList<Point2D> coordList = new ArrayList<Point2D>();
		
		for(int i=0; i<returnPath.size(); i++)
		{	
			coordList.add(
					new Point2D(
							grid[returnPath.get(i).x][returnPath.get(i).y].centerX, 
							grid[returnPath.get(i).x][returnPath.get(i).y].centerY)
					);
		}
		
		if(pathFound) 
		{
			System.out.println(">>>>>> PATH FOUND!");
			for(int i=0; i<coordList.size(); i++)
			{
				System.out.println(coordList.get(i).x + "," + coordList.get(i).y);
			}
		}
		else
			System.out.println(">>>>>> PATH NOT FOUND!");
		
		pathFound = false; // reset variable
		
		return coordList;
	}

	
	public static GridCell findUncheckedNeighbour(int x, int y)
	{
		GridCell gc = null;
		for(int i=0; i<uncheckedNeighbours.size(); i++)
		{
			if((x == uncheckedNeighbours.get(i).x) && (y == uncheckedNeighbours.get(i).y))
			{
				gc = uncheckedNeighbours.get(i);
				break;
			}
		}
		
		if(gc == null)
			System.out.println("gc null...");
		return gc;
	}
	
	// (the current cell, the new cell x, the new cell y, the target x and y)
	public static void addNeighbourNode(GridCell cell, int x, int y, int targetX, int targetY)
	{
		//if((grid[x][y].walkable))// && (!grid[x][y].visited))// test only if it's not a block
		
			// change color to show walkable here
			
			//if((cell.x != sourceX) &&  (cell.y != sourceY))
				//if((cell.x != destX) && (cell.y != destY))
			grid[x][y].color = Color.orange;		
			
			grid[x][y].visited = true;
			resetList.add(new Point2D(x, y));
			
			// calculate cost for the neighbour cell sent in
			float cost = Math.abs(x - targetX) + Math.abs(y - targetY);
			System.out.println("cost [" + x + "][" + y + "]:" + cost);
			
			// test this cost against
			// if the cost of the new cell is smaller than current or if its not visited yet (undefined)
			//GridCell gc = findUncheckedNeighbour(x, y);
			
			//
			//if((gc == null) || (gc.cost > cost))
			if((grid[x][y].cost > cost) || (grid[x][y].cost == -1))
			{
				//System.out.println("cell.cost:" + cell.cost + " > neighbour.cost: [" + x + "][" + y + "]:" + cost);
				//grid[x][y].visited = true;
				
				// make a new node for uncheckedNeighbours
				//GridCell(int _x, int _y, int _width, int _height, boolean _visited, boolean _walkable, float _cost, Color _color)
				GridCell newCell = new GridCell(x, y, grid[x][y].width, grid[x][y].height, false, grid[x][y].walkable, cost, grid[x][y].color);
				
				newCell.prevX = cell.x;	// record previous node (for a connected path to the target)
				newCell.prevY = cell.y;	// record previous node
				grid[x][y].prevX = cell.x;
				grid[x][y].prevY = cell.y;
				
				//grid[x][y].visited = true;
				//resetList.add(new Point2D(x, y));
				
				// loop through all uncheckedNeighbours for testing the cost against
				//System.out.println("uncheckedNeighbours.size: " + uncheckedNeighbours.size());
				
				if(uncheckedNeighbours.size() <= 1)
				{
					//System.out.println(">> added cell[" + x + "][" + y + "]: because uncheckedNeighbour.size = 0");
					uncheckedNeighbours.add(newCell);
				}
				
				for(int i=0; i<uncheckedNeighbours.size(); i++)
				{
					// test the new node's cost against the others
					// if the cost is smaller than the one being tested
					if(cost < uncheckedNeighbours.get(i).cost)
					{
						//System.out.println("insert new neighbour [" + x + "][" + y + "]: at " + i);
						// insert it before the node which has the highest cost (sorting implied in this action)
						uncheckedNeighbours.add(i, newCell);
						
						break; // break from loop so that the test below is not run
					}
					
					// if it has not been added because its cost is higher than the rest
					if(i >= uncheckedNeighbours.size())
					{
						//System.out.println("add new neighbour to end [" + x + "][" + y + "]: at " + uncheckedNeighbours.size());
						
						// add it to the end of the array
						uncheckedNeighbours.add(newCell);
					}
				}
			}
			//else
				//uncheckedNeighbours.add(grid[x][y]);
	}
	
	
	public static ArrayList<Point2D> makePath(GridCell cell)
	{
		ArrayList<Point2D> path = new ArrayList<Point2D>();
		
		while(cell.prevX != -1)
		{
			path.add(new Point2D(cell.x, cell.y));
			grid[cell.x][cell.y].color = Color.blue;	// set path colour of grid cell
			
			cell = grid[cell.prevX][cell.prevY];
		}
		
		grid[sourceX][sourceY].color = Color.green;
		grid[destX][destY].color = Color.red;
		
		return path;
	}
	
	public static Point2D getNextTreadedPath(int x, int y)
	{
		Point2D p = new Point2D(-1, -1);
		
		// if below is done, the comparison has to be more than the present position
		int tempX = x; int tempY = y;
		
		// this makes sure that as long as the path is treaded, the agent can go to that cell
		// but -1 is out of array bounds!
		//int tempX = -1; int tempY = -1;
				
			
		if(x+1 <= row-1) // if not out of boundary (East)
		{
			if(grid[x+1][y].walkable)	// if not blocked
				if(grid[x+1][y].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x+1;
					tempY = y;
				}
		}
		
		//diag SE
		if((x+1 <= row-1) && (y+1 <= col-1))
		{
			if(grid[x+1][y+1].walkable)
				if(grid[x+1][y+1].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x+1;
					tempY = y+1;
				}
				
		}
		
		if(x-1 >= 0)// W
		{
			if(grid[x-1][y].walkable)
				if(grid[x-1][y].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x-1;
					tempY = y;
				}
			
		}
		
		//diag NW
		if((x-1 >= 0) && (y-1 >= 0))  
		{
			if(grid[x-1][y-1].walkable)
				if(grid[x-1][y-1].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x-1;
					tempY = y-1;
				}
			
		}
		
		if(y+1 <= col-1)// S
		{
			if(grid[x][y+1].walkable)
				if(grid[x][y+1].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x;
					tempY = y+1;
				}
		}
		
		//diag SW
		if((y+1 <= col-1) && (x-1 >= 0))// 
		{
			if(grid[x-1][y+1].walkable)
				if(grid[x-1][y+1].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x-1;
					tempY = y+1;
				}
		}
		
		if(y-1 >= 0)// N
		{
			if(grid[x][y-1].walkable)
				if(grid[x][y-1].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x;
					tempY = y-1;
				}
			
		}
		
		//diag NE
		if(x+1 <= row-1)
		{
			if(grid[x+1][y-1].walkable)
				if(grid[x+1][y-1].treadWeight > grid[tempX][tempY].treadWeight)
				{
					tempX = x+1;
					tempY = y-1;
				}
			
		}
		
		// if tempX and Y has changed, then a treaded path is found return that path
		if((tempX != x) && (tempY != y))
		{
			p.x = tempX;
			p.y = tempY;
		}
		
		
		return p;
	}
	
	public static void treadCell(int x, int y, float weight, int colourInc)
	{
		Point2D p = posToCellIndex(x, y);
		
		if(withinBounds(p.x,p.y))
		{
			grid[p.x][p.y].treadWeight += weight;
			
			grid[p.x][p.y].conditionsRGB.get(0).r += colourInc;
			grid[p.x][p.y].conditionsRGB.get(0).g += colourInc;
			grid[p.x][p.y].conditionsRGB.get(0).b += colourInc;
			
			//grid[p.x][p.y].color = grid[p.x][p.y].color.darker();
			//System.out.println("cL"+grid[p.x][p.y].color.brighter());
		}
	}
	
	public static void untreadCell(int x, int y, float weight, int colourDecr)
	{
		Point2D p = posToCellIndex(x, y);
		
		if(withinBounds(p.x,p.y))
		{
			grid[p.x][p.y].treadWeight -= weight;
			
			grid[p.x][p.y].conditionsRGB.get(0).r += colourDecr;
			grid[p.x][p.y].conditionsRGB.get(0).g += colourDecr;
			grid[p.x][p.y].conditionsRGB.get(0).b += colourDecr;
			//grid[p.x][p.y].color = grid[p.x][p.y].color.darker();
		}
	}
	
	public static boolean withinBounds(int x, int y)
	{	
		return ( (x>=0 && y>=0) && (x<=row && y<=col) );
	}
	
	public static void loadState(String filename)
	{
		System.out.println("---------------------------------- LOAD CELLSTATES");
		try
		{
			 // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream(filename);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  //Read File Line By Line
			  int i=0;
			  while ((strLine = br.readLine()) != null)
			  {
				String parts[] = strLine.split(",");	// split csv to array
				  
				// reconstruct cell states
				for(int j=0; j < parts.length; j++)
				{	
					if(parts[j].equals("0"))
					{
						grid[i][j].walkable = true;
						System.out.print("0,");
					}
					else
					{
						grid[i][j].walkable = false;
						System.out.print("1,");
					}
				}
				System.out.println ("");
				i++;
			  }
			  
			  in.close(); //Close the input stream
			
		} catch (Exception ex) 
		{
			System.out.println("error:" + ex.toString());
		}
		
	}
	
	public static void saveState(String filename)
	{
		try
	    { 
			stateFW = new FileWriter(filename, true);
			
			System.out.println("--------------------- Saving GridCell State [" + (grid.length) + "[" + grid[grid.length-1].length + "]" + filename);
			
			for(int i=0; i < grid.length; i++)
			{
				//System.out.println("rows...");
				String line = "";
				for(int j=0; j < grid[i].length; j++)
				{	
					if(grid[i][j].walkable)
					{
						//System.out.println(line);
						line += "0,";
					}
					else
					{
						//System.out.println("1");
						line += "1,";
					}
				}
				
				System.out.println(line);
				
				stateFW.write(line + "\r\n");
			}
			
			//System.out.println("--------------------- END..." + filename);
			//stateFW.write("end---\r\n");
			stateFW.close();
	    } catch (Exception ex) 
	    {
	    	System.out.println("error:" + ex.toString());
	    }
	}
	
	public static void update() {
		
		
	}
}
