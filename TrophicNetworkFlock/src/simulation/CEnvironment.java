package simulation;

import java.awt.Color;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import simulation.CEnvironment.*;
import utilities.CObject;
import utilities.PathFinder;

public class CEnvironment extends CObject {

	public enum Month
	{
		January, February, March, April, May, June, July, August, September, October, November, December
	}

	public enum Season
	{
		Spring, Summer, Autumn, Winter
	}
	
	// Ecosystem Factors
	private static float pTemperature;    
	private static float pSunlight;
	private static float pHumidity = 0.45f;	
	private static Month pMonth;
	private static Season pSeason;

	// Increments
	//private static float pWaterLevelIncrement;
	//private static float pMoistureIncrement;
	//private static float pTemperatureIncrement;
	//private static float pElevationIncrement;
	//private static float pSoilPHIncrement;
	//private static float pSunlightIncrement;

	// Terrain Size
	//private static float terrainWidth;
	//private static float terrainLength;
	//private static float terrainHeight;

	// Ground Info
	//private static Bitmap pBMPSoilPh;
	//private static Bitmap pBMPSoilDepth;
	//private static Bitmap pBMPGround;

	// Time Variables
	private static float pAge;
	private static float pAgeEnd;
	private static float pPrevTime = 0.0f;
	private static float pTimeScale;
	private static int pDayCount;
	private static float pMonthCount;

	private static Set<Month> monthSet;
	private static Iterator<Month> monthIterator;
	
	// Elevation Temperature Ratio
	private static float pElevTempRatio = -0.6f;
	
	
	public CEnvironment(int _id, EnvironmentInfo envInfo) {
		super(_id);
		// TODO Auto-generated constructor stub
		
		pAge = envInfo.age;
		pAgeEnd = envInfo.ageEnd;		
		pSunlight = envInfo.sunlight;
		pTemperature = envInfo.temperature;		
		
		// Timer Info
		monthSet = EnumSet.allOf(Month.class);  
		monthIterator = monthSet.iterator();
		
		pMonth = Month.January;
		pSeason = envInfo.season;		
		pDayCount = 0;
		pTimeScale = 1.0f;
		
	}
	
	public void update(long elapsedTime)
	{
		
		pAge += 1; // increase age
		
		switch(pMonth)
		{
			case January:					
				pSeason = Season.Winter;
				pTemperature = 0.0f + TemperatureVariation();
				pSunlight = SunVariation(0.45f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.55f);
				break;
			case February:
				pSeason = Season.Winter;
				pTemperature = 3.0f + TemperatureVariation();
				pSunlight = SunVariation(0.45f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.55f);
				break;
			case March:
				pSeason = Season.Spring;
				pTemperature = 8.0f + TemperatureVariation();
				pSunlight = SunVariation(0.53f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
				break;
			case April:
				pSeason = Season.Spring;
				pTemperature = 15.0f + TemperatureVariation();
				pSunlight = SunVariation(0.53f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
				break;
			case May:
				pSeason = Season.Summer;
				pTemperature = 25.0f + TemperatureVariation();
				pSunlight = SunVariation(0.8f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.35f);
				break;
			case June:
				pSeason = Season.Summer;
				pTemperature = 27.0f + TemperatureVariation();
				pSunlight =	SunVariation(0.9f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.3f);
				break;
			case July:
				pSeason = Season.Summer;
				pTemperature = 28.0f + TemperatureVariation();
				pSunlight = SunVariation(0.9f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.3f);
				break;
			case August:
				pSeason = Season.Summer;
				pTemperature = 25.0f + TemperatureVariation();
				pSunlight = SunVariation(0.8f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.35f);
				break;
			case September:
				pSeason = Season.Autumn;
				pTemperature = 18.0f + TemperatureVariation();
				pSunlight = SunVariation(0.7f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
				break;
			case October:
				pSeason = Season.Autumn;
				pTemperature = 15.0f + TemperatureVariation();
				pSunlight = SunVariation(0.7f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
				break;
			case November:
				pSeason = Season.Winter;
				pTemperature = 8.0f + TemperatureVariation();
				pSunlight = SunVariation(0.6f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.5f);
				break;
			case December:
				pSeason = Season.Winter;
				pTemperature = 3.0f + TemperatureVariation();
				pSunlight = SunVariation(0.45f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.53f);
				break;
			default:
				pSeason = Season.Winter;	
				pTemperature = 0.0f + TemperatureVariation();
				pSunlight = SunVariation(0.45f);
				pHumidity = MoistureHalfVariation(pHumidity + 0.55f);
				break; 
		}
	
		if (pSunlight > 1.0f)
			pSunlight = 1.0f;
		if (pSunlight < 0.3f)
			pSunlight = 0.3f;
		
		
	}		
	
	/*
	public void update(long elapsedTime)
	{
		// Set increment of month
		if (elapsedTime >= pPrevTime + pTimeScale)
		{	
			float pt = pPrevTime * pTimeScale;
			//System.out.println("Day:" + pDayCount + " | env pMonth:"+pMonth + " | prevTime:" + pPrevTime + " | temp:" + pTemperature);
			
			// Iterate through month
			
			  
			//while (monthIterator.hasNext()) {  
				//pMonth = monthIterator.next();  
			//}  
			
			pDayCount++;			
			if(pDayCount > 30) // 30 days in a month
			{
				pDayCount = 0;
				//pMonth = monthIterator.next();		
			}
			
			if (pMonth == Month.December) // if reached December
			{
				pMonth = Month.January; // go January
				pAge += 1; // increase age
			}

			pPrevTime = elapsedTime; // Store current time			
			
			switch(pMonth)
			{
				case January:					
					pSeason = Season.Winter;
					pTemperature = 0.0f + TemperatureVariation();
					pSunlight = SunVariation(0.45f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.55f);
					break;
				case February:
					pSeason = Season.Winter;
					pTemperature = 3.0f + TemperatureVariation();
					pSunlight = SunVariation(0.45f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.55f);
					break;
				case March:
					pSeason = Season.Spring;
					pTemperature = 8.0f + TemperatureVariation();
					pSunlight = SunVariation(0.53f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
					break;
				case April:
					pSeason = Season.Spring;
					pTemperature = 15.0f + TemperatureVariation();
					pSunlight = SunVariation(0.53f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
					break;
				case May:
					pSeason = Season.Summer;
					pTemperature = 25.0f + TemperatureVariation();
					pSunlight = SunVariation(0.8f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.35f);
					break;
				case June:
					pSeason = Season.Summer;
					pTemperature = 27.0f + TemperatureVariation();
					pSunlight =	SunVariation(0.9f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.3f);
					break;
				case July:
					pSeason = Season.Summer;
					pTemperature = 28.0f + TemperatureVariation();
					pSunlight = SunVariation(0.9f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.3f);
					break;
				case August:
					pSeason = Season.Summer;
					pTemperature = 25.0f + TemperatureVariation();
					pSunlight = SunVariation(0.8f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.35f);
					break;
				case September:
					pSeason = Season.Autumn;
					pTemperature = 18.0f + TemperatureVariation();
					pSunlight = SunVariation(0.7f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
					break;
				case October:
					pSeason = Season.Autumn;
					pTemperature = 15.0f + TemperatureVariation();
					pSunlight = SunVariation(0.7f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.47f);
					break;
				case November:
					pSeason = Season.Winter;
					pTemperature = 8.0f + TemperatureVariation();
					pSunlight = SunVariation(0.6f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.5f);
					break;
				case December:
					pSeason = Season.Winter;
					pTemperature = 3.0f + TemperatureVariation();
					pSunlight = SunVariation(0.45f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.53f);
					break;
				default:
					pSeason = Season.Winter;	
					pTemperature = 0.0f + TemperatureVariation();
					pSunlight = SunVariation(0.45f);
					pHumidity = MoistureHalfVariation(pHumidity + 0.55f);
					break; 
			}

			if (pSunlight > 1.0f)
						pSunlight = 1.0f;
					if (pSunlight < 0.3f)
						pSunlight = 0.3f;
				}		
	}
	*/
	private static float TemperatureVariation()
	{			
		// Variation of Temperature
		if (Math.random()*10 == 8)			
			return (float)Math.random() * 2.0f;			
		else
			return -(float)Math.random() * 2.0f;
	}
	
	private static float MoistureHalfVariation(float h)
	{			
		// Variation of Moisture
		

		if (Math.random()*10 <= 5)
			h += (float)Math.random() * 0.05f;			
		else
			h -= (float)Math.random() * 0.05f;

		// Make sure it doesn't go over limit
		if (h > 0.5f)
			h = 0.5f;			
		if (h < 0.15f)
			h = 0.15f;

		return h;
	}

	private static float SunVariation(float s)
	{			
		// Variation of Sunlight
		if (Math.random()*2 == 1)			
			s += (float)Math.random() * 0.05f;			
		else 			
			s += -(float)Math.random() * 0.05f;

		// Make sure the sunlight doesn't go below the limit
		if (s < 0.35f)			
			return 0.35f;
		else
			return s;

	}
	
	public static float getAge()
	{
		return pAge;		
	}
	public static void setAge(float value)
	{
		pAge = value;
	}
	
	public static float getTemperature()
	{
		return pTemperature;
	}
	public static void setTemperature(float value)
	{
		pTemperature = value;
	}
	
	public static float getSunlight()
	{
		return pSunlight;
	}
	public static void setSunlight(float value)
	{
		pSunlight = value;
	}
	
	public static float getHumidity()
	{
		return pHumidity;
	}
	public static void setHumidity(float value)
	{
		pHumidity = value;
	}
	
	public static Month getMonth()
	{
		return pMonth;
	}
	public static void setMonth(Month value)
	{
		pMonth = value;
	}
	
	public static Season getSeason()
	{
		return pSeason;
	}
	public static void setSeason(Season value)
	{
		pSeason = value;
	}	
}
