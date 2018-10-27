package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

import utilities.SpeciesPopulation;

public class Statistics {

	public static ArrayList<SpeciesPopulation> species = new ArrayList<SpeciesPopulation>();
	
	//  FILE    
	static double computeTime;
	static String popFile; 
	static String timeFile;
	static Date today = new Date();
	      
	static FileWriter popFW;
	static FileWriter timeFW;
	  
	static BufferedWriter popOut;
	static BufferedWriter timeOut;

	public Statistics()
	{
		System.out.println("OBJECT CREATED----->> Statistics");
		
		// Date Time and File
        String dtNow =  + today.getYear() + "-" + today.getMonth() + "-" + today.getDay() + "-" + today.getHours() + today.getMinutes() + today.getSeconds();
        popFile= "../popFile" + dtNow + ".txt"; 
        timeFile= "../timeFile" + dtNow + ".txt";
	}
	
	public static void addNewSpecies(SpeciesPopulation sPop)
	{
		species.add(sPop);
	}
	
	public static String printSpecies()
	{
		String line = "";
		for(int i=0; i<species.size(); i++)
		{
			System.out.println("Name:" + species.get(i).name + " | population:" + species.get(i).population.get(species.get(i).population.size()-1));
			line += species.get(i).name + ": " + species.get(i).population.get(species.get(i).population.size()-1) + ", \n\r";
		}
		
		return line;
	}
	
	// prints one line of population sequence at a time
	public static String printToFile()
	{
		String line = "";	// each line contains a list of population
		for(int i=0; i<species.size(); i++)
		{
			
			//for(int j=0; j<species.get(i).population.size(); j++)
			{
				
				//System.out.println("Name:" + species.get(i).name + " | population:" + species.get(i).population.get(species.get(i).population.size()-1));
				line += species.get(i).population.get(species.get(i).population.size()-1) + ",";
			}
		}
		
		//System.out.println(line);
		
		return line;
		
		// print the sequence of species population "23, 232, 11, 5, 654, 0, "
		/*
		try
	    {    
	        //popOut = new BufferedWriter(popFW);
	        //timeOut = new BufferedWriter(timeFW);
	        
	    	popFW = new FileWriter(popFile,true); 
	        //timeFW = new FileWriter(timeFile,true); 
	        
	        //popFW.write(QT.getAgentSize() + ", \r\n");//appends the string to the file 		        
	        //timeFW.write(computeTime + ", \r\n");//appends the string to the file 
	        
	        popFW.close();  
	        //timeFW.close(); 
	    }
	    catch(Exception e){}
	    */
	}
}
