package utilities;

import java.util.ArrayList;

public class Herbivores {
	
	static ArrayList<Object> objects = new ArrayList<Object>();
	
	public Herbivores ()
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
