package life;

import java.util.ArrayList;

import simulation.CEnvironment.Month;
import simulation.CEnvironment.Season;



public class PlantInfo {
	// Name Element in XML		
	public String filename;
	public String commonName;
	public String scientificName;

	// Info Element in XML
	public String memo;
	
	// Interaction
	public float interactionRange;
	
	// Artificial Life Variables (Tolerance)
	public float hardiness;
	public float temperaturePreferred;
	public float temperatureUpper;
	public float temperatureLower;

	public float humidityPreferred;
	public float humidityUpper;
	public float humidityLower;

	public float elevationPreferred;
	public float elevationUpper;
	public float elevationLower;

	public float sunLightPreferred;
	public float sunLightUpper;
	public float sunLightLower;

	public float spacePreferred;
	public float spaceUpper;
	public float spaceLower;

	public float nutrientPreferred;
	public float nutrientUpper;
	public float nutrientLower;

	public float CO2Preferred;
	public float CO2Upper;
	public float CO2Lower;

	public float soilPhPreferred;
	public float soilPhUpper;
	public float soilPhLower;
	
	public float soilDepthPreferred;
	public float soilDepthUpper;
	public float soilDepthLower;

	public float groundPreferred;
	public float groundUpper;
	public float groundLower;

	// Actual Values stored when touch down in ground
	public float effSoilPh;
	public float effSoilDepth;
	public float effGround;

	// File Element in XML
	public String meshDead;
	public String meshSeed;		
	public String meshSeedling;		
	public String meshYoung;
	public String meshMature;
	public String meshOld;	

	// Growth Element in XML		
	public float maxAge;
	public float currentAge;
	public String bestSoil;
	public String acceptableSoil;		
	public String type; // tree, shrub, herb, grass		
	public String leafType; // Evergreen/Deciduous		
	public float maxHeight; // REMOVE THIS LATER as in XMLManager & PlantManager		
	public float canopy; // diameter for sunlight competition and dispersal distance
	public float density; // density of leaf for sunlight penetration
	public float energy; 
	
	// Graphic Related
	public float scale; 

	// Reproduction Element in XML
	public String reproductionType;
	public float reproductionAge;
	public float dispersalDistance;
	public String dispersalType;				
	public int seedCount;
	public int averageGermination; // Maybe remove this?
	public Month pollenReleaseDateStart;
	public Month pollenReleaseDateEnd;
    public Month seedingMonth;

	// Germination Element in XML
	public int germDaysStart;
	public int germDaysEnd;
	public Month germMonthStart;		
	public Month germMonthEnd;		
	public Season germSeason;
	public float germTemperatureLower;
	public float germTemperatureUpper;
	public float germMoistureUpper;
	public float germMoistureLower;
	public String germSoil;			

}
