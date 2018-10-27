package utilities;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import life.AnimalInfo;
import life.PlantInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLManager {
	
	public XMLManager()
    {    	
    	
    }
	
	public AnimalInfo fillAnimalInfo(String fileName)
	{
		AnimalInfo animalInfo = new AnimalInfo();
		
		try
        {
	        File file = new File(fileName);
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(file);
	        doc.getDocumentElement().normalize();

	        // the plant tag
	        //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
	        
	        // get the plant tag
	        NodeList nodeLst = doc.getElementsByTagName("Predator");
	        //System.out.println("Information of all elements");
	        
	        Node animalTag = nodeLst.item(0);				        // first node <Plant>
	        Element firstElement = (Element) animalTag;				// <Plant>

	        // Name
	        NodeList nlNameTag = firstElement.getElementsByTagName("Name"); // get something inside <Plant>
	        Element eName = (Element) nlNameTag.item(0);
	        
	        NodeList nlNameVal = eName.getChildNodes();
	        //System.out.println("Name: "  + ((Node) nlNameVal.item(0)).getNodeValue());
	        animalInfo.commonName = ((Node) nlNameVal.item(0)).getNodeValue();
	        
	        // Colour
	        NodeList nlColourTag = firstElement.getElementsByTagName("Colour"); // get something inside <Plant>
	        Element eColour = (Element) nlColourTag.item(0);
	        
	        NodeList nlColourVal = eColour.getChildNodes();
	        //System.out.println("Colour: "  + ((Node) nlColourVal.item(0)).getNodeValue());
	        animalInfo.colour = ((Node) nlColourVal.item(0)).getNodeValue();
	        
	        // ---------------------------------------------------------- TROPHIC NETWORK
	        // Food
	        NodeList nlFoodTag = firstElement.getElementsByTagName("Food"); // get something inside <Plant>
	        Element eFood = (Element) nlFoodTag.item(0);
	        
	        NodeList nlFoodVal = eFood.getChildNodes();
	        //System.out.println("Food: "  + ((Node) nlFoodVal.item(0)).getNodeValue());
	        animalInfo.food = ((Node) nlFoodVal.item(0)).getNodeValue();	        
	        //animalInfo.food.replace("\r", "");
	        //animalInfo.food.replace("\t", "");
	        //animalInfo.food.replace("\n", "");
	        
	        // FoodCategory
	        NodeList nlFoodCategoryTag = firstElement.getElementsByTagName("FoodCategory"); // get something inside <Plant>
	        Element eFoodCategory = (Element) nlFoodCategoryTag.item(0);
	        
	        NodeList nlFoodCategoryVal = eFoodCategory.getChildNodes();
	        //System.out.println("FoodCategory: "  + ((Node) nlFoodCategoryVal.item(0)).getNodeValue());
	        animalInfo.foodCategory = ((Node) nlFoodCategoryVal.item(0)).getNodeValue();
	        	        
	        // Predator
	        NodeList nlPredatorTag = firstElement.getElementsByTagName("Predator"); // get something inside <Plant>
	        Element ePredator = (Element) nlPredatorTag.item(0);
	        
	        NodeList nlPredatorVal = ePredator.getChildNodes();
	        //System.out.println("Predator: "  + ((Node) nlPredatorVal.item(0)).getNodeValue());
	        animalInfo.predator = ((Node) nlPredatorVal.item(0)).getNodeValue();	
	        
	        // PredatorCategory
	        NodeList nlPredatorCatTag = firstElement.getElementsByTagName("PredatorCategory"); // get something inside <Plant>
	        Element ePredatorCat = (Element) nlPredatorCatTag.item(0);
	        
	        NodeList nlPredatorCatVal = ePredatorCat.getChildNodes();
	        //System.out.println("PredatorCat: "  + ((Node) nlPredatorCatVal.item(0)).getNodeValue());
	        animalInfo.predatorCategory = ((Node) nlPredatorCatVal.item(0)).getNodeValue();	
	        // ---------------------------------------------------------- TROPHIC NETWORK ENDS
	        
	        // ---------------------------------------------------------- DYNAMICS
	        // Speed
	        NodeList nlSpeedTag = firstElement.getElementsByTagName("Speed"); // get something inside <Plant>
	        Element eSpeed = (Element) nlSpeedTag.item(0);
	        
	        NodeList nlSpeedVal = eSpeed.getChildNodes();
	        //System.out.println("Speed: "  + ((Node) nlSpeedVal.item(0)).getNodeValue());	
	        animalInfo.speed = Float.valueOf(((Node) nlSpeedVal.item(0)).getNodeValue());
	        
	        // Thrust
	        NodeList nlThrustTag = firstElement.getElementsByTagName("Thrust"); // get something inside <Plant>
	        Element eThrust = (Element) nlThrustTag.item(0);
	        
	        NodeList nlThrustVal = eThrust.getChildNodes();
	        //System.out.println("Thrust: "  + ((Node) nlThrustVal.item(0)).getNodeValue());	
	        animalInfo.thrust = Float.valueOf(((Node) nlThrustVal.item(0)).getNodeValue());
	        
	        // huntingThrust
	        NodeList nlhuntingThrustTag = firstElement.getElementsByTagName("HuntingThrust"); // get something inside <Plant>
	        Element ehuntingThrust = (Element) nlhuntingThrustTag.item(0);
	        
	        NodeList nlhuntingThrustVal = ehuntingThrust.getChildNodes();
	        //System.out.println("huntingThrust: "  + ((Node) nlhuntingThrustVal.item(0)).getNodeValue());	
	        animalInfo.huntingThrust = Float.valueOf(((Node) nlhuntingThrustVal.item(0)).getNodeValue());
	        
	     // fleeingThrust
	        NodeList nlfleeingThrustTag = firstElement.getElementsByTagName("FleeingThrust"); // get something inside <Plant>
	        Element efleeingThrust = (Element) nlfleeingThrustTag.item(0);
	        
	        NodeList nlfleeingThrustVal = efleeingThrust.getChildNodes();
	        //System.out.println("fleeingThrust: "  + ((Node) nlfleeingThrustVal.item(0)).getNodeValue());	
	        animalInfo.fleeingThrust = Float.valueOf(((Node) nlfleeingThrustVal.item(0)).getNodeValue());
	        
	     // thrustLimit
	        NodeList nlthrustLimitTag = firstElement.getElementsByTagName("ThrustLimit"); // get something inside <Plant>
	        Element ethrustLimit = (Element) nlthrustLimitTag.item(0);
	        
	        NodeList nlthrustLimitVal = ethrustLimit.getChildNodes();
	        //System.out.println("thrustLimit: "  + ((Node) nlthrustLimitVal.item(0)).getNodeValue());	
	        animalInfo.thrustLimit = Float.valueOf(((Node) nlthrustLimitVal.item(0)).getNodeValue());

	        // ---------------------------------------------------------- DYNAMICS ENDS
	        
	        
	     // eyeSight
	        NodeList nleyeSightTag = firstElement.getElementsByTagName("Eyesight"); // get something inside <Plant>
	        Element eeyeSight = (Element) nleyeSightTag.item(0);
	        
	        NodeList nleyeSightVal = eeyeSight.getChildNodes();
	        //System.out.println("eyeSight: "  + ((Node) nleyeSightVal.item(0)).getNodeValue());	
	        animalInfo.eyeSight = Float.valueOf(((Node) nleyeSightVal.item(0)).getNodeValue());
	        
	     // fov
	        NodeList nlfovTag = firstElement.getElementsByTagName("FOV"); // get something inside <Plant>
	        Element efov = (Element) nlfovTag.item(0);
	        
	        NodeList nlfovVal = efov.getChildNodes();
	        //System.out.println("fov: "  + ((Node) nlfovVal.item(0)).getNodeValue());	
	        animalInfo.fov = Float.valueOf(((Node) nlfovVal.item(0)).getNodeValue());
	        
	        // maxAge
	        NodeList nlmaxAgeTag = firstElement.getElementsByTagName("MaxAge"); // get something inside <Plant>
	        Element emaxAge = (Element) nlmaxAgeTag.item(0);
	        
	        NodeList nlmaxAgeVal = emaxAge.getChildNodes();
	        //System.out.println("maxAge: "  + ((Node) nlmaxAgeVal.item(0)).getNodeValue());	
	        animalInfo.maxAge = Float.valueOf(((Node) nlmaxAgeVal.item(0)).getNodeValue());
	        
	        // energy
	        NodeList nlenergyTag = firstElement.getElementsByTagName("Energy"); // get something inside <Plant>
	        Element eenergy = (Element) nlenergyTag.item(0);
	        
	        NodeList nlenergyVal = eenergy.getChildNodes();
	        //System.out.println("energy: "  + ((Node) nlenergyVal.item(0)).getNodeValue());	
	        animalInfo.energy = Float.valueOf(((Node) nlenergyVal.item(0)).getNodeValue());
	        
	        // energyLoss
	        NodeList nlenergyLossTag = firstElement.getElementsByTagName("EnergyLoss"); // get something inside <Plant>
	        Element eenergyLoss = (Element) nlenergyLossTag.item(0);
	        
	        NodeList nlenergyLossVal = eenergyLoss.getChildNodes();
	        //System.out.println("energyLoss: "  + ((Node) nlenergyLossVal.item(0)).getNodeValue());	
	        animalInfo.energyLoss = Float.valueOf(((Node) nlenergyLossVal.item(0)).getNodeValue());
	        
	        // energyGain
	        NodeList nlenergyGainTag = firstElement.getElementsByTagName("EnergyGain"); // get something inside <Plant>
	        Element eenergyGain = (Element) nlenergyGainTag.item(0);
	        
	        NodeList nlenergyGainVal = eenergyGain.getChildNodes();
	        //System.out.println("energyGain: "  + ((Node) nlenergyGainVal.item(0)).getNodeValue());	
	        animalInfo.energyGain = Float.valueOf(((Node) nlenergyGainVal.item(0)).getNodeValue());
	        
	        // energyHuntingLoss
	        NodeList nlenergyHuntLossTag = firstElement.getElementsByTagName("EnergyHuntingLoss"); // get something inside <Plant>
	        Element eenergyHuntLoss = (Element) nlenergyHuntLossTag.item(0);
	        
	        NodeList nlenergyHuntLossVal = eenergyHuntLoss.getChildNodes();
	        //System.out.println("energyHuntLoss: "  + ((Node) nlenergyHuntLossVal.item(0)).getNodeValue());	
	        animalInfo.energyHuntingLoss = Float.valueOf(((Node) nlenergyHuntLossVal.item(0)).getNodeValue());
	     
	        // energyRestThreshold
	        NodeList nlenergyRestThresholdTag = firstElement.getElementsByTagName("EnergyRestThreshold"); // get something inside <Plant>
	        Element eenergyRestThreshold = (Element) nlenergyRestThresholdTag.item(0);
	        
	        NodeList nlenergyRestThresholdVal = eenergyRestThreshold.getChildNodes();
	        //System.out.println("energyRestThreshold: "  + ((Node) nlenergyRestThresholdVal.item(0)).getNodeValue());	
	        animalInfo.energyRestThreshold = Float.valueOf(((Node) nlenergyRestThresholdVal.item(0)).getNodeValue());
	        
	     // energyHuntThreshold
	        NodeList nlenergyHuntThresholdTag = firstElement.getElementsByTagName("EnergyHuntThreshold"); // get something inside <Plant>
	        Element eenergyHuntThreshold = (Element) nlenergyHuntThresholdTag.item(0);
	        
	        NodeList nlenergyHuntThresholdVal = eenergyHuntThreshold.getChildNodes();
	        //System.out.println("energyHuntThreshold: "  + ((Node) nlenergyHuntThresholdVal.item(0)).getNodeValue());	
	        animalInfo.energyHuntThreshold = Float.valueOf(((Node) nlenergyHuntThresholdVal.item(0)).getNodeValue());
	        
	     // hungerThreshold
	        NodeList nlhungerThresholdTag = firstElement.getElementsByTagName("HungerThreshold"); // get something inside <Plant>
	        Element ehungerThreshold = (Element) nlhungerThresholdTag.item(0);
	        
	        NodeList nlhungerThresholdVal = ehungerThreshold.getChildNodes();
	        //System.out.println("hungerThreshold: "  + ((Node) nlhungerThresholdVal.item(0)).getNodeValue());	
	        animalInfo.hungerThreshold = Float.valueOf(((Node) nlhungerThresholdVal.item(0)).getNodeValue());
	        
	     // fleshIndex
	        NodeList nlfleshIndexTag = firstElement.getElementsByTagName("FleshIndex"); // get something inside <Plant>
	        Element efleshIndex = (Element) nlfleshIndexTag.item(0);
	        
	        NodeList nlfleshIndexVal = efleshIndex.getChildNodes();
	        //System.out.println("fleshIndex: "  + ((Node) nlfleshIndexVal.item(0)).getNodeValue());	
	        animalInfo.fleshIndex = Float.valueOf(((Node) nlfleshIndexVal.item(0)).getNodeValue());
	        
	     // famineDeath
	        NodeList nlfamineDeathTag = firstElement.getElementsByTagName("FamineDeath"); // get something inside <Plant>
	        Element efamineDeath = (Element) nlfamineDeathTag.item(0);
	        
	        NodeList nlfamineDeathVal = efamineDeath.getChildNodes();
	        //System.out.println("famineDeath: "  + ((Node) nlfamineDeathVal.item(0)).getNodeValue());	
	        animalInfo.famineDeath = Float.valueOf(((Node) nlfamineDeathVal.item(0)).getNodeValue());
	        
	        
	     // famineDeathRateRate
	        NodeList nlfamineDeathRateTag = firstElement.getElementsByTagName("FamineDeathRate"); // get something inside <Plant>
	        Element efamineDeathRate = (Element) nlfamineDeathRateTag.item(0);
	        
	        NodeList nlfamineDeathRateVal = efamineDeathRate.getChildNodes();
	        //System.out.println("famineDeathRate: "  + ((Node) nlfamineDeathRateVal.item(0)).getNodeValue());	
	        animalInfo.famineDeathRate = Float.valueOf(((Node) nlfamineDeathRateVal.item(0)).getNodeValue());
	        
	        
	        
	     // impulse
	        NodeList nlimpulseTag = firstElement.getElementsByTagName("Impulse"); // get something inside <Plant>
	        Element eimpulse = (Element) nlimpulseTag.item(0);
	        
	        NodeList nlimpulseVal = eimpulse.getChildNodes();
	        //System.out.println("impulse: "  + ((Node) nlimpulseVal.item(0)).getNodeValue());	
	        animalInfo.impulse = Float.valueOf(((Node) nlimpulseVal.item(0)).getNodeValue());
	        
	        
	        // impulseRange
	        NodeList nlimpulseRangeTag = firstElement.getElementsByTagName("ImpulseRange"); // get something inside <Plant>
	        Element eimpulseRange = (Element) nlimpulseRangeTag.item(0);
	        
	        NodeList nlimpulseRangeVal = eimpulseRange.getChildNodes();
	        //System.out.println("impulseRange: "  + ((Node) nlimpulseRangeVal.item(0)).getNodeValue());	
	        animalInfo.impulseRange = Float.valueOf(((Node) nlimpulseRangeVal.item(0)).getNodeValue());
	        
	        // safeDistance
	        NodeList nlsafeDistanceTag = firstElement.getElementsByTagName("SafeDistance"); // get something inside <Plant>
	        Element esafeDistance = (Element) nlsafeDistanceTag.item(0);
	        
	        NodeList nlsafeDistanceVal = esafeDistance.getChildNodes();
	        //System.out.println("safeDistance: "  + ((Node) nlsafeDistanceVal.item(0)).getNodeValue());	
	        animalInfo.safeDistance = Float.valueOf(((Node) nlsafeDistanceVal.item(0)).getNodeValue());
	        
	        // feedingDistance
	        NodeList nlfeedingDistanceTag = firstElement.getElementsByTagName("FeedingDistance"); // get something inside <Plant>
	        Element efeedingDistance = (Element) nlfeedingDistanceTag.item(0);
	        
	        NodeList nlfeedingDistanceVal = efeedingDistance.getChildNodes();
	        //System.out.println("feedingDistance: "  + ((Node) nlfeedingDistanceVal.item(0)).getNodeValue());	
	        animalInfo.feedingDistance = Float.valueOf(((Node) nlfeedingDistanceVal.item(0)).getNodeValue());
	        
	        // RepAge
	        NodeList nlRepAgeTag = firstElement.getElementsByTagName("ReproductionAge"); // get something inside <Plant>
	        Element eRepAge = (Element) nlRepAgeTag.item(0);	        
	        NodeList nlRepAgeVal = eRepAge.getChildNodes();
	        //System.out.println("RepAge: "  + ((Node) nlRepAgeVal.item(0)).getNodeValue());	
	        animalInfo.reproductionAge = Float.valueOf(((Node) nlRepAgeVal.item(0)).getNodeValue());
	        
	     // reproductionFitness
	        NodeList nlRepFitnessTag = firstElement.getElementsByTagName("ReproductionFitness"); // get something inside <Plant>
	        Element eRepFitness = (Element) nlRepFitnessTag.item(0);	        
	        NodeList nlRepFitnessVal = eRepFitness.getChildNodes();
	        //System.out.println("RepFitness: "  + ((Node) nlRepFitnessVal.item(0)).getNodeValue());	
	        animalInfo.reproductionFitness = Float.valueOf(((Node) nlRepFitnessVal.item(0)).getNodeValue());
	        
	        // reproductionProbability
	        NodeList nlRepProbTag = firstElement.getElementsByTagName("ReproductionProbability"); // get something inside <Plant>
	        Element eRepProb = (Element) nlRepProbTag.item(0);	        
	        NodeList nlRepProbVal = eRepProb.getChildNodes();
	        //System.out.println("RepProb: "  + ((Node) nlRepProbVal.item(0)).getNodeValue());	
	        animalInfo.reproductionProbability = Float.valueOf(((Node) nlRepProbVal.item(0)).getNodeValue());
	        
	     // ReproductionThreshold
	        NodeList nlRepThresholdTag = firstElement.getElementsByTagName("ReproductionThreshold"); // get something inside <Plant>
	        Element eRepThreshold = (Element) nlRepThresholdTag.item(0);	        
	        NodeList nlRepThresholdVal = eRepThreshold.getChildNodes();
	        //System.out.println("RepThreshold: "  + ((Node) nlRepThresholdVal.item(0)).getNodeValue());	
	        animalInfo.reproductionThreshold = Float.valueOf(((Node) nlRepThresholdVal.item(0)).getNodeValue());
	        
	        // SeedCount
	        NodeList nlSeedCountTag = firstElement.getElementsByTagName("SeedCount"); // get something inside <Plant>
	        Element eSeedCount = (Element) nlSeedCountTag.item(0);	        
	        NodeList nlSeedCountVal = eSeedCount.getChildNodes();
	        //System.out.println("SeedCount: "  + ((Node) nlSeedCountVal.item(0)).getNodeValue());
	        animalInfo.seedCount = Integer.parseInt(((Node) nlSeedCountVal.item(0)).getNodeValue());
	        
	        /*
	        // Scale
	        NodeList nlScaleTag = firstElement.getElementsByTagName("Scale"); // get something inside <Plant>
	        Element eScale = (Element) nlScaleTag.item(0);
	        
	        NodeList nlScaleVal = eScale.getChildNodes();
	        //System.out.println("Scale: "  + ((Node) nlScaleVal.item(0)).getNodeValue());
	        animalInfo.scale = Float.valueOf(((Node) nlScaleVal.item(0)).getNodeValue());

	        
	        // MaxAge
	        NodeList nlMaxAgeTag = firstElement.getElementsByTagName("MaxAge"); // get something inside <Plant>
	        Element eMaxAge = (Element) nlMaxAgeTag.item(0);
	        
	        NodeList nlMaxAgeVal = eMaxAge.getChildNodes();
	        //System.out.println("MaxAge: "  + ((Node) nlMaxAgeVal.item(0)).getNodeValue());	    
	        animalInfo.maxAge = Float.valueOf(((Node) nlMaxAgeVal.item(0)).getNodeValue());
	        
	        // CurrentAge
	        NodeList nlCurrentAgeTag = firstElement.getElementsByTagName("CurrentAge"); // get something inside <Plant>
	        Element eCurrentAge = (Element) nlCurrentAgeTag.item(0);	        
	        NodeList nlCurrentAgeVal = eCurrentAge.getChildNodes();
	        //System.out.println("CurrentAge: "  + ((Node) nlCurrentAgeVal.item(0)).getNodeValue());
	        animalInfo.currentAge = Float.valueOf(((Node) nlCurrentAgeVal.item(0)).getNodeValue());
	        
	       
	        
	        // Dispersal Distance
	        NodeList nlDDistTag = firstElement.getElementsByTagName("DispersalDistance"); // get something inside <Plant>
	        Element eDDist = (Element) nlDDistTag.item(0);	        
	        NodeList nlDDistVal = eDDist.getChildNodes();
	        //System.out.println("DDist: "  + ((Node) nlDDistVal.item(0)).getNodeValue());	
	        animalInfo.dispersalDistance = Float.valueOf(((Node) nlDDistVal.item(0)).getNodeValue());
	        
	        
	        */
	        
	        // Temperature
	        NodeList nlTempTag = firstElement.getElementsByTagName("Temperature"); // get something inside <Plant>
	        Element eTemp = (Element) nlTempTag.item(0);	        
	        NodeList nlTempVal = eTemp.getChildNodes();
	        //System.out.println("Temperature: "  + ((Node) nlTempVal.item(0)).getNodeValue());	        
	        //System.out.println("Temperature U: "  + eTemp.getAttribute("U"));
	        //System.out.println("Temperature L: "  + eTemp.getAttribute("L"));
	        animalInfo.temperaturePreferred = Float.valueOf(((Node) nlTempVal.item(0)).getNodeValue());
	        animalInfo.temperatureUpper = Float.valueOf(eTemp.getAttribute("U"));
	        animalInfo.temperatureLower = Float.valueOf(eTemp.getAttribute("L"));
	        
	        
	        // Sunlight
	        NodeList nlSunlightTag = firstElement.getElementsByTagName("Sunlight"); // get something inside <Plant>
	        Element eSunlight = (Element) nlSunlightTag.item(0);
	        
	        NodeList nlSunlightVal = eTemp.getChildNodes();
	        //System.out.println("Sunlight: "  + ((Node) nlSunlightVal.item(0)).getNodeValue());	        
	        //System.out.println("Sunlight U: "  + eSunlight.getAttribute("U"));
	        //System.out.println("Sunlight L: "  + eSunlight.getAttribute("L"));
	        animalInfo.sunLightPreferred = Float.valueOf(((Node) nlSunlightVal.item(0)).getNodeValue());
	        animalInfo.sunLightUpper = Float.valueOf(eSunlight.getAttribute("U"));
	        animalInfo.sunLightLower = Float.valueOf(eSunlight.getAttribute("L"));

	         
	        // Humidity
	        NodeList nlHumidityTag = firstElement.getElementsByTagName("Humidity"); // get something inside <Plant>
	        Element eHumidity = (Element) nlHumidityTag.item(0);	        
	        NodeList nlHumidityVal = eHumidity.getChildNodes();
	        //System.out.println("Humidity: "  + ((Node) nlHumidityVal.item(0)).getNodeValue());	        
	        //System.out.println("Humidity U: "  + eHumidity.getAttribute("U"));
	        //System.out.println("Humidity L: "  + eHumidity.getAttribute("L"));
	        animalInfo.humidityPreferred = Float.valueOf(((Node) nlHumidityVal.item(0)).getNodeValue());
	        animalInfo.humidityUpper = Float.valueOf(eHumidity.getAttribute("U"));
	        animalInfo.humidityLower = Float.valueOf(eHumidity.getAttribute("L"));
	        	        
	        
	       
        }
        catch (Exception e)
        {            e.printStackTrace();        }
		
		return animalInfo;		
	}
	
	public PlantInfo fillPlantInfo(String fileName)
	{
		PlantInfo plantInfo = new PlantInfo();
		
		try
        {
	        File file = new File(fileName);
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(file);
	        doc.getDocumentElement().normalize();

	        // the plant tag
	        //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
	        
	        // get the plant tag
	        NodeList nodeLst = doc.getElementsByTagName("Plant");
	        //System.out.println("Information of all elements");
	        
	        Node plantTag = nodeLst.item(0);				        // first node <Plant>
	        Element firstElement = (Element) plantTag;				// <Plant>

	        // Name
	        NodeList nlNameTag = firstElement.getElementsByTagName("Name"); // get something inside <Plant>
	        Element eName = (Element) nlNameTag.item(0);
	        
	        NodeList nlNameVal = eName.getChildNodes();
	        //System.out.println("Name: "  + ((Node) nlNameVal.item(0)).getNodeValue());
	        plantInfo.commonName = ((Node) nlNameVal.item(0)).getNodeValue();
	        
	        // Name
	        NodeList nlRangeTag = firstElement.getElementsByTagName("InteractionRange"); // get something inside <Plant>
	        Element eRange = (Element) nlRangeTag.item(0);
	        
	        NodeList nlRangeVal = eRange.getChildNodes();
	        //System.out.println("Range: "  + ((Node) nlRangeVal.item(0)).getNodeValue());
	        plantInfo.interactionRange = Integer.valueOf(((Node) nlRangeVal.item(0)).getNodeValue());
	        
	        // Energy
	        NodeList nlEnergyTag = firstElement.getElementsByTagName("Energy"); // get something inside <Plant>
	        Element eEnergy = (Element) nlEnergyTag.item(0);
	        
	        NodeList nlEnergyVal = eEnergy.getChildNodes();
	        //System.out.println("Energy: "  + ((Node) nlEnergyVal.item(0)).getNodeValue());	
	        plantInfo.energy = Float.valueOf(((Node) nlEnergyVal.item(0)).getNodeValue());
	        
	        // Scale
	        NodeList nlScaleTag = firstElement.getElementsByTagName("Scale"); // get something inside <Plant>
	        Element eScale = (Element) nlScaleTag.item(0);
	        
	        NodeList nlScaleVal = eScale.getChildNodes();
	        //System.out.println("Scale: "  + ((Node) nlScaleVal.item(0)).getNodeValue());
	        plantInfo.scale = Float.valueOf(((Node) nlScaleVal.item(0)).getNodeValue());

	        
	        // MaxAge
	        NodeList nlMaxAgeTag = firstElement.getElementsByTagName("MaxAge"); // get something inside <Plant>
	        Element eMaxAge = (Element) nlMaxAgeTag.item(0);
	        
	        NodeList nlMaxAgeVal = eMaxAge.getChildNodes();
	        //System.out.println("MaxAge: "  + ((Node) nlMaxAgeVal.item(0)).getNodeValue());	    
	        plantInfo.maxAge = Float.valueOf(((Node) nlMaxAgeVal.item(0)).getNodeValue());
	        
	        // CurrentAge
	        NodeList nlCurrentAgeTag = firstElement.getElementsByTagName("CurrentAge"); // get something inside <Plant>
	        Element eCurrentAge = (Element) nlCurrentAgeTag.item(0);	        
	        NodeList nlCurrentAgeVal = eCurrentAge.getChildNodes();
	        //System.out.println("CurrentAge: "  + ((Node) nlCurrentAgeVal.item(0)).getNodeValue());
	        plantInfo.currentAge = Float.valueOf(((Node) nlCurrentAgeVal.item(0)).getNodeValue());
	        
	        // RepAge
	        NodeList nlRepAgeTag = firstElement.getElementsByTagName("ReproductionAge"); // get something inside <Plant>
	        Element eRepAge = (Element) nlRepAgeTag.item(0);	        
	        NodeList nlRepAgeVal = eRepAge.getChildNodes();
	        //System.out.println("RepAge: "  + ((Node) nlRepAgeVal.item(0)).getNodeValue());	
	        plantInfo.reproductionAge = Float.valueOf(((Node) nlRepAgeVal.item(0)).getNodeValue());
	        
	        // Dispersal Distance
	        NodeList nlDDistTag = firstElement.getElementsByTagName("DispersalDistance"); // get something inside <Plant>
	        Element eDDist = (Element) nlDDistTag.item(0);	        
	        NodeList nlDDistVal = eDDist.getChildNodes();
	        //System.out.println("DDist: "  + ((Node) nlDDistVal.item(0)).getNodeValue());	
	        plantInfo.dispersalDistance = Float.valueOf(((Node) nlDDistVal.item(0)).getNodeValue());
	        
	        // SeedCount
	        NodeList nlSeedCountTag = firstElement.getElementsByTagName("SeedCount"); // get something inside <Plant>
	        Element eSeedCount = (Element) nlSeedCountTag.item(0);	        
	        NodeList nlSeedCountVal = eSeedCount.getChildNodes();
	        //System.out.println("SeedCount: "  + ((Node) nlSeedCountVal.item(0)).getNodeValue());
	        plantInfo.seedCount = Integer.parseInt(((Node) nlSeedCountVal.item(0)).getNodeValue());
	        
	        
	        // Temperature
	        NodeList nlTempTag = firstElement.getElementsByTagName("Temperature"); // get something inside <Plant>
	        Element eTemp = (Element) nlTempTag.item(0);	        
	        NodeList nlTempVal = eTemp.getChildNodes();
	        //System.out.println("Temperature: "  + ((Node) nlTempVal.item(0)).getNodeValue());	        
	        //System.out.println("Temperature U: "  + eTemp.getAttribute("U"));
	        //System.out.println("Temperature L: "  + eTemp.getAttribute("L"));
	        plantInfo.temperaturePreferred = Float.valueOf(((Node) nlTempVal.item(0)).getNodeValue());
	        plantInfo.temperatureUpper = Float.valueOf(eTemp.getAttribute("U"));
	        plantInfo.temperatureLower = Float.valueOf(eTemp.getAttribute("L"));
	        
	        
	        // Sunlight
	        NodeList nlSunlightTag = firstElement.getElementsByTagName("Sunlight"); // get something inside <Plant>
	        Element eSunlight = (Element) nlSunlightTag.item(0);
	        
	        NodeList nlSunlightVal = eTemp.getChildNodes();
	        //System.out.println("Sunlight: "  + ((Node) nlSunlightVal.item(0)).getNodeValue());	        
	        //System.out.println("Sunlight U: "  + eSunlight.getAttribute("U"));
	        //System.out.println("Sunlight L: "  + eSunlight.getAttribute("L"));
	        plantInfo.sunLightPreferred = Float.valueOf(((Node) nlSunlightVal.item(0)).getNodeValue());
	        plantInfo.sunLightUpper = Float.valueOf(eSunlight.getAttribute("U"));
	        plantInfo.sunLightLower = Float.valueOf(eSunlight.getAttribute("L"));
	        
	        // Soil
	        NodeList nlSoilTag = firstElement.getElementsByTagName("Soil"); // get something inside <Plant>
	        Element eSoil = (Element) nlSoilTag.item(0);	        
	        NodeList nlSoilVal = eSoil.getChildNodes();
	        //System.out.println("Soil: "  + ((Node) nlSoilVal.item(0)).getNodeValue());	        
	        //System.out.println("Soil U: "  + eSoil.getAttribute("U"));
	        //System.out.println("Soil L: "  + eSoil.getAttribute("L"));
	        plantInfo.soilDepthPreferred = Float.valueOf(((Node) nlSoilVal.item(0)).getNodeValue());
	        plantInfo.soilDepthUpper = Float.valueOf(eSoil.getAttribute("U"));
	        plantInfo.soilDepthLower = Float.valueOf(eSoil.getAttribute("L"));
	        
	        // PH
	        NodeList nlPHTag = firstElement.getElementsByTagName("PH"); // get something inside <Plant>
	        Element ePH = (Element) nlPHTag.item(0);	        
	        NodeList nlPHVal = ePH.getChildNodes();
	        //System.out.println("PH: "  + ((Node) nlPHVal.item(0)).getNodeValue());	        
	        //System.out.println("PH U: "  + ePH.getAttribute("U"));
	        //System.out.println("PH L: "  + ePH.getAttribute("L"));
	        plantInfo.soilPhPreferred = Float.valueOf(((Node) nlPHVal.item(0)).getNodeValue());
	        plantInfo.soilPhUpper = Float.valueOf(ePH.getAttribute("U"));
	        plantInfo.soilPhLower = Float.valueOf(ePH.getAttribute("L"));
	 	            
	        // Space
	        NodeList nlSpaceTag = firstElement.getElementsByTagName("Space"); // get something inside <Plant>
	        Element eSpace = (Element) nlSpaceTag.item(0);	        
	        NodeList nlSpaceVal = eSpace.getChildNodes();
	        //System.out.println("Space: "  + ((Node) nlSpaceVal.item(0)).getNodeValue());	        
	        //System.out.println("Space U: "  + eSpace.getAttribute("U"));
	        //System.out.println("Space L: "  + eSpace.getAttribute("L"));
	        plantInfo.spacePreferred = Float.valueOf(((Node) nlSpaceVal.item(0)).getNodeValue());
	        plantInfo.spaceUpper = Float.valueOf(eSpace.getAttribute("U"));
	        plantInfo.spaceLower = Float.valueOf(eSpace.getAttribute("L"));
	         
	        // Humidity
	        NodeList nlHumidityTag = firstElement.getElementsByTagName("Humidity"); // get something inside <Plant>
	        Element eHumidity = (Element) nlHumidityTag.item(0);	        
	        NodeList nlHumidityVal = eHumidity.getChildNodes();
	        //System.out.println("Humidity: "  + ((Node) nlHumidityVal.item(0)).getNodeValue());	        
	        //System.out.println("Humidity U: "  + eHumidity.getAttribute("U"));
	        //System.out.println("Humidity L: "  + eHumidity.getAttribute("L"));
	        plantInfo.humidityPreferred = Float.valueOf(((Node) nlHumidityVal.item(0)).getNodeValue());
	        plantInfo.humidityUpper = Float.valueOf(eHumidity.getAttribute("U"));
	        plantInfo.humidityLower = Float.valueOf(eHumidity.getAttribute("L"));
	        	        
	        
	       
        }
        catch (Exception e)
        {            e.printStackTrace();        }
		
		return plantInfo;		
	}
}
