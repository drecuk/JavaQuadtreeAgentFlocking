package utilities;

import java.awt.Color;
import java.util.ArrayList;

import life.Plant;


public class Plants {
	
	static ArrayList<Object> objects = new ArrayList<Object>();
	
	public Plants()
	{
		 
	}
	
	public static void add(Object obj)
	{
		objects.add(obj);
	}
	
	public static void remove(Object obj)
	{
		objects.remove(obj);		
	}
	
	public static void clear()
	{
		objects.clear();
	}
	
	public static int getFromID(int id)
	{
		int objIndex = 0;
		
		for(int i=0; i<objects.size(); i++)
		{
			if( ((Plant)objects.get(i)).id == id)
			{
				objIndex = i;
				break;
			}
		}

		return objIndex;
	}
	
	public static Object getObjectFromID(int id)
	{
		System.out.print("@@ Cast ID passed in:: " + id + " | compare to:: ");
		Object o = new Object();
		
		for(int i=0; i<objects.size(); i++)
		{
			System.out.print( ((Plant)objects.get(i)).id + ", ");
			if( ((Plant)objects.get(i)).id == id)
			{
				try {
					o = objects.get(i);
}
				catch (Exception e) {
					System.out.println("e:" + e.getMessage());
				}
				break;
			}
		}
		System.out.println();
		
		if((o instanceof Plant) == false)
		{
			QuadTree.reportNodeBranchIndex();
			System.out.println("*** ERROR NO OBJECT FOUND!!");
		}	
		return o;
	}
	
	
	public static Object get(int i)
	{
		Object o = new Object();
		try
		{
			o = objects.get(i);	
		}
		catch (Exception e)
		{
			
		}
		
		return o;
	}
	
	public static int size()
	{
		return objects.size();
	}
}
