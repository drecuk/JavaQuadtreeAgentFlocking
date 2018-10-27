package utilities;

import java.util.ArrayList;

public class SpeciesPopulation {
	public String name;
	public ArrayList<Integer> population = new ArrayList<Integer>();
	public int tempCount;
	
	public SpeciesPopulation(String _name)
	{
		 this.name = _name;
	}
}
