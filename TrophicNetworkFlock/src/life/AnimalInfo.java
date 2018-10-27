package life;

public class AnimalInfo {
	// Name Element in XML
		public String commonName;
		public String scientificName;

		// Info Element in XML
		public String memo;
		
		public String colour;
		public String food;
		
		public String foodCategory;
		public String predator;
		public String predatorCategory;
		
		// Dynamics
		public float speed;
		public float angle;
		public float thrust;
		public float huntingThrust;
		public float fleeingThrust;
		public float thrustLimit;
		public float eyeSight;
		public float fov;
		
		// Physique
		public float maxAge;		
		public float energy;
		public float energyLoss;
		public float energyGain;
		public float energyHuntingLoss;
		public float energyRestThreshold;
		public float energyHuntThreshold;
		public float hungerThreshold;
		public float fleshIndex;
		public float famineDeath;
		public float famineDeathRate;
		
		// Behaviour
		public float impulse;
		public float impulseRange;
		public float safeDistance;
		public float feedingDistance;
		
		
		// Artificial Life Variables (Tolerance)
		public float hardiness;
		public float temperaturePreferred;
		public float temperatureUpper;
		public float temperatureLower;

		public float humidityPreferred;
		public float humidityUpper;
		public float humidityLower;
		
		public float sunLightPreferred;
		public float sunLightUpper;
		public float sunLightLower;
		
		
		// Graphic Related
		public float scale; 

		// Reproduction Element in XML
		public String reproductionType;
		public float reproductionAge;
		public float reproductionFitness;
		public float reproductionProbability;
		public float reproductionDistance;
		public float reproductionThreshold;
		public int seedCount;		
}
