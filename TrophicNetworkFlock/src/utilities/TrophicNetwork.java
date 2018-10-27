package utilities;

import java.applet.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

//Image
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import simulation.*;

import life.*;


/**
*
* @author  Eugene Ch'ng
* Adaptive QuadTree Agent-Emitter Interaction
*/

public class TrophicNetwork extends Applet implements Runnable, MouseListener, KeyListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Thread animation; // thread for VegSim
    
	// ------------------------------------------------------ STATISTICS AND NETWORK OBJECTS
	//public static NetworkInfo ninfo = new NetworkInfo(); // not necessary to declare this as the class is static
	
    // ------------------------------------------------------ OBJECT COLLECTIONS
    //Plant plant;
    //ArrayList<Plant> plants = new ArrayList<Plant>();    
    //Plants vegs = new Plants();
    //Herbivores herbis = new Herbivores();
    
    public static int AgentIDInc = 0;	// all objects get their IDs from this increment
    //public static int EmitterIDInc = 0;
    
    // ------------------------------------------------------ SCREEN AND REFRESH
    static final int REFRESH_RATE = 30;
    static int screenWidth = 1024;
    static int screenHeight = 1024;
    
    
    PlantInfo plantInfo;
    
    // ------------------------------------------------------ ENVIRONMENT
    private CEnvironment env;
    private EnvironmentInfo envInfo;
    private TimeElement theTime;
    
    // ------------------------------------------------------ GRID
  	PathFinder grid = new PathFinder(screenWidth, screenHeight, 32, 32, Color.black, Color.white);
    int sx = 10;
	int sy = 8;
	int tx = 23;
	int ty = 30;
	// grid cell size
 	int gridResX = 128;
 	int gridResY = 128;
 	
    // ------------------------------------------------------ IMAGE
    public static BufferedImage soilImage;
    Graphics offscreen; // for double buffering
    Image image; // for double buffering
    
    // Saving Images Counter
    int saveIMGCount = 0;
    int saveIMGPrev = 0;
    
    // ------------------------------------------------------ QUADTREE    
    CQuadTree QT;
    int radius = 0;	// for range query
    Point3D rangePos;
    
    boolean ctrlKeyPressed = false;

    // ------------------------------------------------------ FILE    
    String dtNow;
    double computeTime;
    String popFile; 
    String timeFile;
    Date today = new Date();
        
    FileWriter popFW;
    FileWriter timeFW;
    
    FileWriter netFW;
    String netmapFile;
    String cellStateFile;
    
    String folderName;
    
    BufferedWriter popOut;
    BufferedWriter timeOut;    
    
	
	boolean saveImage = false;
	boolean saveStats = false;
	
	//Human human;
	/*
	Human human1;
	Human human2;
	Human human3;
	Human human4;
	Human human5;
	Human human6;
	Human human7;
	Human human8;
	Human human9;
	*/
	
    public void initSimulation(){
    	QT = new CQuadTree(0, screenHeight, 0, screenWidth, 6, 4);   	
    	CQuadTreeNode rootNode = CQuadTree.qtNodes.get(0);	// get the root node    	
    	CQuadTree.divideNode(rootNode);   	
    	rangePos = new Point3D(0, 0, 0);
    	
    	// ------------------- GRID
    	// human pathfinder
//    	PathFinder.initGrid(screenWidth, screenHeight, gridResX, gridResY, Color.black, Color.gray, -1);
//    	PathFinder.loadState("../cellStateFile.txt");
//    	PathFinder.grid[sx][sy].color = Color.green;
//    	PathFinder.grid[tx][ty].color = Color.red;
    	
    	
    	/*
    	QuadTreeNode node1 = QT.qtNodes.get(1);	// get the root node
    	QT.divideNode(node1);
    	    	
    	int nID = QT.placeObjectInNode(node0, new Point3D(50, 0, 100), 0, ObjectType.agent);
    	
    	//nID = QT.placeObjectInNode(node0, new Point3D(300, 0, 300), 1, ObjectType.agent);
    	
    	QuadTreeNode node4 = QT.qtNodes.get(4);	
    	
    	nID = QT.placeObjectInNode(node4, new Point3D(600, 0, 600), 2, ObjectType.agent);
    	nID = QT.placeObjectInNode(node4, new Point3D(600, 0, 600), 3, ObjectType.agent);
    	nID = QT.placeObjectInNode(node4, new Point3D(600, 0, 600), 4, ObjectType.agent);
    	nID = QT.placeObjectInNode(node4, new Point3D(600, 0, 600), 5, ObjectType.agent);
    	nID = QT.placeObjectInNode(node4, new Point3D(600, 0, 600), 6, ObjectType.agent);
    	    	
    	QT.reportNodeBranchIndex();
    	*/
    	
        // Init vegetation here, declared global
    	        
        // load background image
        try
        {        	
        	soilImage = ImageIO.read(new File("../testmap1024.jpg"));
        	screenWidth = soilImage.getWidth();
        	screenHeight = soilImage.getHeight();
        }
        catch (IOException e) {
        	System.out.println("Soil Image Error:" + e.getMessage());
        }
        setSize(screenWidth, screenHeight);
     
        
        // loop through and create plants
        //QuadTreeNode tempNode = QT.qtNodes.get(1);	// get the root node
    	//int nID;
    	
        
        /*
        int NumPlant = 5504;	// grey
        int herb1 = 2293;		// green
        int herb2 = 0;			// blue
        int carni1 = 956;		// orange
        int carni2 = 398;		// lightgrey
        int carni3 = 166;		// white
        int carni4 = 69;		// pink
        int carni5 = 29;		// magenta
        int carni6 = 12;		// cyan
        int carni7 = 5;			// yellow
        int carni8 = 2;			// red
        */
        
        int NumPlant = 0;	// grey
        int herb1 = 0;
        int herb2 = 0;
        int carni1 = 0;
        int carni2 = 0;
        int carni3 = 0;
        int carni4 = 0;
        int carni5 = 0;
        int carni6 = 0;
        int carni7 = 0;
        int carni8 = 0;
        int humans = 1;
        int birds = 5000;
        
        /*        
		int herb1 = 10;
        int herb2 = 0;
        int carni1 = 10;
        int carni2 = 10;
        int carni3 = 10;
        int carni4 = 10;
        int carni5 = 10;
        int carni6 = 10;
        int carni7 = 10;
        int carni8 = 10;
         */
        
        //AnimalInfo humanInfo = xmlManager.fillAnimalInfo("../Carnivore.xml");
        //Point3D humanPos = new Point3D(712, 278, 0);
        //human = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        /*
        Point3D humanPos1 = new Point3D(585, 690, 0);
        human1 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos1, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos2 = new Point3D(755, 677, 0);
        human2 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos2, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos3 = new Point3D(683, 459, 0);
        human3 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos3, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos4 = new Point3D(580, 305, 0);
        human4 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos4, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos5 = new Point3D(768, 78, 0);
        human5 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos5, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos6 = new Point3D(955, 288, 0);
        human6 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos6, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos7 = new Point3D(560, 50, 0);
        human7 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos7, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos8 = new Point3D(598, 491, 0);
        human8 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos8, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        
        Point3D humanPos9 = new Point3D(827, 209, 0);
        human9 = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos9, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
        */
       
        // -------------------------------------------------------------------- UTILITY OBJECTS
        Random rnd = new Random();	// create a random generator class
        XMLManager xmlManager = new XMLManager(); // XML Handling
        
        // -------------------------------------------------------------------- CREATE LIVING THINGS       
        plantInfo = xmlManager.fillPlantInfo("../Vegetable.xml");
        for (int i=0; i<NumPlant; i++)
        {
        	// set random location
        	Point3D rLoc = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);        	
        	
        	// only if within the landscape boundary
        	// get soil value
    		int c = soilImage.getRGB(rLoc.x, rLoc.y);	// read pixel
        	float normalised = (float)(c & 0x0000ff)/255;		// normalised
        	
        	// test soil fitness
        	double soilFitness = LifeUtil.AdaptabilityRightBound(normalised, plantInfo.soilDepthPreferred, plantInfo.soilDepthUpper, 0.5f);
        	//System.out.println("soil fitness:" + soilFitness);
        	
        	// if of suitable soil type
        	if(soilFitness > 0.1d)
        	{
        		// add new plant
        		//AgentIDInc++;
        		
        		//Plant tPlant = new Plant( AgentIDInc, "Plant, 1, 10, rLoc, size, screenWidth, screenHeight, Color.getHSBColor(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat()), plantInfo, soilImage, normalised);
        		//Plant p = new Plant( TrophicNetwork.AgentIDInc++, "Plant", ObjectType.agent, 1, 10, rLoc, size, screenWidth, screenHeight, Color.getHSBColor(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat()), plantInfo, soilImage, normalised);
        		Plant p = new Plant( TrophicNetwork.AgentIDInc++, plantInfo.commonName, ObjectType.agent, 1, 10, rLoc, new Point3D(10,10,10), screenWidth, screenHeight, Color.gray, plantInfo, soilImage, normalised);
    			CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), p); // add to root node and let it cascade
        	}
        }
        Statistics.species.add(new SpeciesPopulation("Plant"));    	
        Statistics.species.get(0).population.add(1500);
        
        loadHerbivores("../Herbivore.xml", herb1, new Point3D(7,7,7), Color.green);
        Statistics.species.add(new SpeciesPopulation("Herbivore"));    	
        Statistics.species.get(1).population.add(herb1);
        
        loadHerbivores("../Herbivore2.xml", herb2, new Point3D(7,7,7), Color.blue);
        Statistics.species.add(new SpeciesPopulation("Herbivore2"));    	
        Statistics.species.get(2).population.add(herb2);
        
        //loadCarnivores("../Carnivore.xml", 50, new Point3D(8, 8, 8), Color.red);
        
        AnimalInfo animalInfo1 = xmlManager.fillAnimalInfo("../Carnivore.xml");
    	for (int i=0; i<carni1; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore tempCarni = new Carnivore( TrophicNetwork.AgentIDInc++, animalInfo1.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.orange, animalInfo1);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
        } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore"));    	
        Statistics.species.get(3).population.add(carni1);
    	
        AnimalInfo animalInfo2 = xmlManager.fillAnimalInfo("../Carnivore2.xml");
    	for (int i=0; i<carni2; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore2 tempCarni = new Carnivore2( TrophicNetwork.AgentIDInc++, animalInfo2.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.lightGray, animalInfo2);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
        } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore2"));    	
        Statistics.species.get(4).population.add(carni2);
    	
    	AnimalInfo animalInfo3 = xmlManager.fillAnimalInfo("../Carnivore3.xml");
    	//System.out.println(animalInfo3.food);
    	
    	for (int i=0; i<carni3; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	       
	        Carnivore3 tempCarni = new Carnivore3( TrophicNetwork.AgentIDInc++, animalInfo3.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.white, animalInfo3);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
        } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore3"));    	
        Statistics.species.get(5).population.add(carni3);
    	
    	AnimalInfo animalInfo4 = xmlManager.fillAnimalInfo("../Carnivore4.xml");
    	//System.out.println(animalInfo4.food);
    	
    	for (int i=0; i<carni4; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore4 tempCarni = new Carnivore4( TrophicNetwork.AgentIDInc++, animalInfo4.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(8,8,8), screenWidth, screenHeight, Color.pink, animalInfo4);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
        } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore4"));    	
        Statistics.species.get(6).population.add(carni4);
    	
    	AnimalInfo animalInfo5 = xmlManager.fillAnimalInfo("../Carnivore5.xml");
    	//System.out.println(animalInfo5.food);
    	
    	for (int i=0; i<carni5; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore5 tempCarni = new Carnivore5( TrophicNetwork.AgentIDInc++, animalInfo5.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(8,8,8), screenWidth, screenHeight, Color.magenta, animalInfo5);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
        } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore5"));    	
        Statistics.species.get(7).population.add(carni5);
    	
    	AnimalInfo animalInfo6 = xmlManager.fillAnimalInfo("../Carnivore6.xml");
    	//System.out.println(animalInfo6.food);
    	for (int i=0; i<carni6; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore6 tempCarni = new Carnivore6( TrophicNetwork.AgentIDInc++, animalInfo6.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(8,8,8), screenWidth, screenHeight, Color.cyan, animalInfo6);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
       } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore6"));    	
        Statistics.species.get(8).population.add(carni6);
    	
    	AnimalInfo animalInfo7 = xmlManager.fillAnimalInfo("../Carnivore7.xml");
    	//System.out.println(animalInfo7.food);
    	
    	for (int i=0; i<carni7; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore7 tempCarni = new Carnivore7( TrophicNetwork.AgentIDInc++, animalInfo7.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(10,10,10), screenWidth, screenHeight, Color.yellow, animalInfo7);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
        } 
    	Statistics.species.add(new SpeciesPopulation("Carnivore7"));    	
        Statistics.species.get(9).population.add(carni7);
    	
    	AnimalInfo animalInfo8 = xmlManager.fillAnimalInfo("../Carnivore8.xml");
    	//System.out.println(animalInfo8.food);
    	
    	for (int i=0; i<carni8; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Carnivore8 tempCarni = new Carnivore8( TrophicNetwork.AgentIDInc++, animalInfo8.commonName, ObjectType.agent, rLocHerb, 1, new Point3D(12,12,12), screenWidth, screenHeight, Color.red, animalInfo8);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarni);
       }
    	Statistics.species.add(new SpeciesPopulation("Carnivore8"));    	
        Statistics.species.get(10).population.add(carni8);
        
        // ----------------- HUMANS!
        AnimalInfo humanInfo = xmlManager.fillAnimalInfo("../Human.xml");
        for (int i=0; i<humans; i++)
        {
        	AgentIDInc++;
	        //Point3D humanPos = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
    		Point3D humanPos = new Point3D(712, 278, 0);
	        Human human = new Human( TrophicNetwork.AgentIDInc++, humanInfo.commonName, ObjectType.agent, humanPos, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.red, humanInfo);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), human);
        } 
    	Statistics.species.add(new SpeciesPopulation(humanInfo.commonName));    	
        Statistics.species.get(11).population.add(humans);
        
        // ----------------- BIRDS!
        AnimalInfo birdInfo = xmlManager.fillAnimalInfo("../Bird.xml");
        for (int i=0; i<birds; i++)
        {
        	AgentIDInc++;
	        //Point3D humanPos = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
        	//Point3D rLoc = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
    		Point3D birdPos = new Point3D(750 + rnd.nextInt(100), 300 + rnd.nextInt(100), 0);
	        Bird bird = new Bird( TrophicNetwork.AgentIDInc++, birdInfo.commonName, ObjectType.agent, birdPos, 1, new Point3D(7,7,7), screenWidth, screenHeight, Color.yellow, birdInfo);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), bird);
        } 
    	Statistics.species.add(new SpeciesPopulation(birdInfo.commonName));    	
        Statistics.species.get(12).population.add(birds);
        
        
        // -------------------------------------------------------------- INITIAL STATE WRITE TO FILE
        if(saveStats)
        {
	        try
		    {    
		        //popOut = new BufferedWriter(popFW);
		        //timeOut = new BufferedWriter(timeFW);
		        String statsFile = "../" + folderName + "/initialState-" + dtNow + ".txt";
		    	popFW = new FileWriter(statsFile,true); 
		        
		        //popFW.write(QT.getAgentSize() + ", \r\n");//appends the string to the file
		        popFW.write(Statistics.printSpecies() + "\r\n");
		        
		        popFW.close();
		    }
		    catch(Exception e) {}
        }
        
    	
        /*       
        loadCarnivores("../Carnivore3.xml", 20, new Point3D(5, 5, 5), Color.WHITE);
        loadCarnivores("../Carnivore4.xml", 20, new Point3D(5, 5, 5), Color.PINK);
        loadCarnivores("../Carnivore5.xml", 20, new Point3D(5, 5, 5), Color.magenta);
        loadCarnivores("../Carnivore6.xml", 20, new Point3D(5, 5, 5), Color.cyan);
        loadCarnivores("../Carnivore7.xml", 20, new Point3D(5, 5, 5), Color.yellow);
        loadCarnivores("../Carnivore8.xml", 20, new Point3D(5, 5, 5), Color.red);
        */
        
        CQuadTree.reportNodeBranchIndex();
        QT.printAgentIDs();
        
        // get the plant collection for competition
        /*
        for (int i=0; i<plants.size(); i++)
        {	
        	// create plant        	
        	plants.get(i).getPlants(plants);
        }
        */
        // create a timer for tracking age
        // int seconds = 1;
        // timer = new Timer();
        // timer.scheduleAtFixedRate(new IncrementAge(  ), 0, seconds*1000);
        
        // Create Environment
        envInfo = new EnvironmentInfo();        
        env = new CEnvironment(0, envInfo);  
        theTime = new TimeElement();        
        
    }
    
    public void loadHerbivores(String xmlFile, int population, Point3D size, Color color)
    {
    	// create a random generator class
        Random rnd = new Random(); 
        
    	// XML
        XMLManager xmlManager = new XMLManager();        
    	AnimalInfo animalInfo = xmlManager.fillAnimalInfo(xmlFile);    
    	
    	for (int i=0; i<population; i++)
        {
        	//AgentIDInc++;
	        Point3D rLocHerb = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        Herbivore tempHerb = new Herbivore( TrophicNetwork.AgentIDInc++, animalInfo.commonName, ObjectType.agent, rLocHerb, 1, size, screenWidth, screenHeight, color, animalInfo);
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempHerb);
        } 
    }
    
    public void loadCarnivores(String xmlFile, int population, Point3D size, Color color)
    {
    	// create a random generator class
        Random rnd = new Random(); 
        
    	// XML
        XMLManager xmlManager = new XMLManager();        
    	AnimalInfo animalInfo = xmlManager.fillAnimalInfo(xmlFile);    
    	
    	for (int i=0; i<population; i++)
        {
        	AgentIDInc++;
	        Point3D rLocCarni = new Point3D(rnd.nextInt(screenWidth), rnd.nextInt(screenHeight), 0);
	        //Herbivore(int _id, String _name, ObjectType _objectType, Point3D _position, int age, Point3D size, int scrWidth, int scrHeight, Color color, AnimalInfo animalInfo)
	        Carnivore tempCarn = new Carnivore( AgentIDInc, animalInfo.commonName, ObjectType.agent, rLocCarni, 1, size, screenWidth, screenHeight, color, animalInfo);
	        
	        CQuadTree.placeObjectInNode(CQuadTree.qtNodes.get(0), tempCarn);
	        	        
	        //System.out.println("random loc x:" + rLocHerb.x + " y:" + rLocHerb.y);	        
	        //tempHerb.NodeID = nID;
	        
	        //Herbivores.add(tempHerb);
        } 
    }

  	/** initialize class */
    public void init(){
        System.out.println(">> init <<");
        setBackground( Color.black );
        
        // Date Time and File        
        dtNow =  + today.getYear() + "-" + today.getMonth() + "-" + today.getDay() + "-" + today.getHours() + today.getMinutes() + today.getSeconds();
        
        if(saveStats)
        {
	        folderName = "SIM-" + dtNow;
	        boolean success = (new File("../" + folderName)).mkdirs();
	        if (!success) {
	            // Directory creation failed
	        }
	        
	        popFile= "../" + folderName + "/popFile" + dtNow + ".txt"; 
	        timeFile= "../" + folderName + "/timeFile" + dtNow + ".txt";
	        netmapFile = "../" + folderName + "/netmapFile" + dtNow + ".txt";
	        cellStateFile = "../" + folderName + "/cellStateFile" + dtNow + ".txt";
        }
        
        initSimulation();	// create vegetation
        
        
        this.addMouseListener(this);
        addMouseMotionListener(this);
        this.addKeyListener(this);
        // rendering and back buffers
        image = createImage( screenWidth, screenHeight );
        offscreen = image.getGraphics();
    }
    
    /** start thread */
    public void start() {
        System.out.println(">> start <<");
        
        // start new thread for animation
        animation = new Thread(this);
        if (animation != null){
            animation.start();
        }
    }
    
    /** Paint method */
    public void paint( Graphics g )
    {       	
    	//setIgnoreRepaint(true);
    	
    	
    	// draw soil        
        //offscreen.clearRect(0, 0, screenWidth, screenHeight);
        offscreen.drawImage(soilImage, 0, 0, this);
       
        // offscreen.fillRect( 0, 0, screenWidth, screenHeight );
        
        
        PathFinder.paint(offscreen);
        QT.paint(offscreen);
        //human.paint(offscreen);
        /*
        human1.paint(offscreen);
        human2.paint(offscreen);
        human3.paint(offscreen);
        human4.paint(offscreen);
        human5.paint(offscreen);
        human6.paint(offscreen);
        human7.paint(offscreen);
        human8.paint(offscreen);
        human9.paint(offscreen);
        */
        
        // for range query radius
        offscreen.setColor( Color.green );
        offscreen.drawOval(rangePos.x, rangePos.y, radius*2, radius*2);
        
        
        Font font = new Font("Arial", Font.BOLD, 20);	    
	    offscreen.setFont(font);
	    offscreen.setColor( Color.yellow );
	    offscreen.drawString(""+QT.getAgentSize(), 10, 20);
	    offscreen.setColor( Color.red );
	    offscreen.drawString(""+computeTime, 100, 20);
	    
	    
	    // --------------------------------------------------------------------------- SAVE IMAGE
	    saveIMGCount++;
	    if(saveImage)
	    {
		    if(saveIMGCount >= saveIMGPrev + 100)
		    {
			    BufferedImage bufferedImage = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_RGB);
		 	    Graphics bg = bufferedImage.createGraphics();
		 	    bg.drawImage( image, 0, 0, this );  
		 	    			 
				try
				{
					//System.out.println("--------------------- Saving Screenshot: " + "../" + folderName + "/c" + saveIMGCount + ".png");
					File outputfile = new File("../" + folderName + "/c" + saveIMGCount + ".png");
					ImageIO.write(bufferedImage,"png",outputfile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				bg.dispose();
				saveIMGPrev = saveIMGCount;
		    }
	    }
	    
	    
	    // draw the image
        g.drawImage( image, 0, 0, this );        
    }
    
    /** updateVegetation and let them grow */
    public void update(){
    	
    	// update time
        theTime.update();
        // update environment
        env.update(theTime.milliseconds());
        //QT.update(env);
        
        // -------------------- Calculate time before/after QT update
        // TODO
         
        long prevNanoSec = System.nanoTime();
        
        QT.update(env);
        ArrayList<CWorldObject> wo = new ArrayList<CWorldObject>();
        //human.update(wo, env);
        /*
        human1.update(wo, env);
        human2.update(wo, env);
        human3.update(wo, env);
        human4.update(wo, env);
        human5.update(wo, env);
        human6.update(wo, env);
        human7.update(wo, env);
        human8.update(wo, env);
        human9.update(wo, env);
        */
        
        long afterNanoSec = System.nanoTime();
        double elapsedTime = (afterNanoSec - prevNanoSec);
        computeTime = (double)elapsedTime / 1000000000.0;	// get seconds
        //System.out.println("nanoSec:" + computeTime);
        
        QT.getStats();
        //Statistics.printSpecies();
               
        // WRITE TO FILE	
        if(saveStats)
        {
	        try
		    {    
		        //popOut = new BufferedWriter(popFW);
		        //timeOut = new BufferedWriter(timeFW);
		        
		    	popFW = new FileWriter(popFile,true); 
		        timeFW = new FileWriter(timeFile,true); 
		        
		        //popFW.write(QT.getAgentSize() + ", \r\n");//appends the string to the file
		        popFW.write(Statistics.printToFile() + "\r\n");
		        timeFW.write(computeTime + ", \r\n");//appends the string to the file 
		        
		        popFW.close();  
		        timeFW.close(); 
		    }
		    catch(Exception e) {}
        }
        
        
        //System.out.println("env sun:"+Environment.getTemperature());
        
    	// update vegetation states
	    
        //System.out.println("elapsedTime:"+elapsedTime);
        
        //System.out.println("size:"+plants.size());
    }
    
    /** run thread */
    public void run() {
        while ( true ) {
            repaint();					// paint
            update();			// update vegetation
            try {
                Thread.sleep ( REFRESH_RATE );
            } catch ( Exception exc ) {};            
        }
    }
        
    /** update method */
    // override update so it doesn't erase screen
    public void update( Graphics g ){
       paint(g);      
    }  
      
    /** stop thread */
    public void stop() {
        System.out.println(">> stop <<");
        if (animation != null) {
            animation.stop();
            animation = null;
        }
    }
    
 // if mouseUp then setDraggable to false
       
    // this external function is used only for the old QuadTree.java class 
    public static void transferAgent(INFONodeTransfer info)
    {    	
    	System.out.print(">>> NODE[" + info.nodeID + "] :: ");    	
    	
    	for(int i=0; i < info.indices.size(); i++)
		{
    		for(int j=0; j < Herbivores.size(); j++)
    		{
    			if( ((Herbivore)Herbivores.get(j)).id == info.indices.get(i))
    			{
    				((Herbivore)Herbivores.get(j)).NodeID = info.nodeID;
    			}
    		}
		}
    	
    	//QuadTreeNode parentNode = QuadTree.qtNodes.get(QuadTree.getArrayIndexOfNode(info.parentID));
    	QuadTreeNode parentNode = QuadTree.qtNodes.get(QuadTree.getArrayIndexOfNode(info.parentID));
    	for(int i=0; i < info.indices.size(); i++)
		{
    		System.out.println("@@ Casting agentID:" + info.indices.get(i) + " from nodeID:" + info.nodeID);
    		Plant p = (Plant) Plants.getObjectFromID(info.indices.get(i));
    		int nID = QuadTree.placeObjectInNode(parentNode, p.position, p.id, ObjectType.agent);
    		((Plant) Plants.getObjectFromID(info.indices.get(i))).NodeID = info.nodeID;
    		
    		/*
    		for(int j=0; j < Plants.size(); j++)
    		{
    			if( ((Plant)Plants.get(j)).ID == info.indices.get(i))
    			{
    				((Plant)Plants.get(j)).NodeID = info.nodeID;
    				 
    			}
    		}
    		*/
		}
    	
    	
    	/*
    	for(int i=0; i < info.envIndices.size(); i++)
		{
    		for(int j=0; j < Herbivores.size(); j++)
    		{
    			if( ((Herbivore)Herbivores.get(j)).ID == info.indices.get(i))
    			{
    				((Herbivore)Herbivores.get(j)).NodeID = info.nodeID;
    			}
    		}
		}
		*/
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		// construct size and position
    	Point3D size = new Point3D(5, 5, 5);
    	Point3D loc = new Point3D(e.getX(), e.getY(), 0);
    	
    	// get root node
    	CQuadTreeNode tempNode = CQuadTree.qtNodes.get(0);
		
    	
    	
		switch(e.getModifiers()) {
	      case InputEvent.BUTTON1_MASK: {
	        //System.out.println("That's the LEFT button");   
	    	 PathFinder.switchState(e.getX(), e.getY(), -1);
	    	  
	    	/* 
	    	int c = soilImage.getRGB(e.getX(), e.getY());
	    	float normalised = (float)(c & 0x0000ff)/255;
	    	//System.out.println("mouse --->> x:" + e.getX() + " y:" + e.getY() + " color:" + normalised);
	    	
	    	Plant tPlant = new Plant( TrophicNetwork.AgentIDInc++, "Plant", ObjectType.agent, 1, 10, loc, size, screenWidth, screenHeight, Color.BLUE, plantInfo, soilImage, normalised);
			int nID = CQuadTree.placeObjectInNode(tempNode, tPlant);
	
			//System.out.println("mouse clicked - Agent In --->> got nID as: " + nID);
			
			
			// -------------------- RANGE QUERYYYYY
			radius = 100;
			rangePos = new Point3D(loc.x - radius, loc.y - radius, 0);
			CQuadTree.rangeQuery(tPlant, ObjectType.agent, radius);
			//QT.update(env);
	    	*/
	    	  
	        break;
	        }
	      case InputEvent.BUTTON2_MASK: {
	    	  //System.out.println("That's the MIDDLE button");  
	    	  //GridObjectCells.treadCell(e.getX(), e.getY(), 0.2f);
	    	
			for(int i=0; i < CQuadTree.qtNodes.size(); i++)
			{  
				for(int j=0; j < CQuadTree.qtNodes.get(i).agents.size(); j++)
				{
					if(CQuadTree.qtNodes.get(i).agents.get(j).objectType == ObjectType.agent)
					{	
						//if(new String(worldObjects.get(clanIndex).name).equals("Human"))
						if(new String(CQuadTree.qtNodes.get(i).agents.get(j).name).equals("Human"))
						{
							System.out.println("ATTENTION: HUMAN FIND PATH");
							((Human)CQuadTree.qtNodes.get(i).agents.get(j)).navigateToCell(e.getX(), e.getY());
						}
					}
				}	
			}
	    	
			//human.navigateToCell(e.getX(), e.getY());
	    	  /*
	    	  human1.navigateToCell(e.getX(), e.getY());
	    	  human2.navigateToCell(e.getX(), e.getY());
	    	  human3.navigateToCell(e.getX(), e.getY());
	    	  human4.navigateToCell(e.getX(), e.getY());
	    	  human5.navigateToCell(e.getX(), e.getY());
	    	  human6.navigateToCell(e.getX(), e.getY());
	    	  human7.navigateToCell(e.getX(), e.getY());
	    	  human8.navigateToCell(e.getX(), e.getY());
	    	  human9.navigateToCell(e.getX(), e.getY());
	    	  */
	        break;
	        }
	      case InputEvent.BUTTON3_MASK: {
	        //System.out.println("That's the RIGHT button");   
	    	  
			//TemperatureEmitter(int _id, String _name, ObjectType _objectType, Point3D _position, Color color, int width, int length, int _range)
			TemperatureEmitter tEm = new TemperatureEmitter( TrophicNetwork.AgentIDInc++, "TemperatureEmitter", ObjectType.emitter, loc, Color.GREEN, 10, 10, 50, 100, 90);
			int nID = CQuadTree.placeObjectInNode(tempNode, tEm);
			
			System.out.println("mouse clicked - Emitter In --->> got nID as: " + nID);

	        break;
	        }
	      }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// user presses left cursor key 
	    if (e.getKeyCode() == KeyEvent.VK_ENTER) 
	    {
	    	//System.out.println("ENTER keypressed..");
			CQuadTree.reportNodeBranchIndex();
			QT.printAgentIDs();
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_CONTROL)
	    	ctrlKeyPressed = true;
	    
	    // SAVE NETWORK
	    if((e.getKeyCode() == KeyEvent.VK_N))
	    {
	    	saveNetworkMap();
	    }
	    
	    // SAVE GRIDCELL
	    if((e.getKeyCode() == KeyEvent.VK_G))
	    {
	    	PathFinder.saveState(cellStateFile);
	    }
	    
	    // Navigate path
	    if((e.getKeyCode() == KeyEvent.VK_F))
	    {
	    	ArrayList<Point2D> pathPoints = PathFinder.findPath(sx, sy, tx, ty);
	    	
	    	if(pathPoints.size() >= 0)
	    		System.out.println("path found!");
	    	else
	    		System.out.println("path not found!");
	    }
	}
	
	public void saveNetworkMap()
	{
		try
	    { 
			netFW = new FileWriter(netmapFile,true);
	
			netFW.write("nodedef>name VARCHAR,label VARCHAR, speed INTEGER, color VARCHAR\r\n"); // gephi nodedef
			
			System.out.println("--------------------- Saving Network Node Map...");
			for(int i=0; i < NetworkInfo.nagents.size(); i++)
			{
				NAgentNode a = NetworkInfo.nagents.get(i);
				
				String params = "";
				for(int j=0; j < NetworkInfo.nagents.get(i).params.size(); j++)
				{
					params += "," + NetworkInfo.nagents.get(i).params.get(j).toString();
				}
				
				netFW.write(a.id + params + "\r\n");
				System.out.println(a.id + params);
			}
			
			netFW.write("edgedef>node1 VARCHAR,node2 VARCHAR,directed BOOLEAN\r\n"); // gephi edgedef
			
			System.out.println("--------------------- Saving Network Edge Map...");
			for(int i=0; i < NetworkInfo.nedges.size(); i++)
			{
				NAgentEdge e = NetworkInfo.nedges.get(i);
				
				String params = "";
				for(int j=0; j < e.params.size(); j++)
				{
					params += "," + e.params.get(j).toString();
				}
				
				netFW.write(e.agentid + "," + e.linkid + params + "\r\n");
				System.out.println(e.agentid + "," + e.linkid + params);
			}
			
			netFW.close();
	    } catch (Exception ex) {}
		
	    
		/*
		try
	    {  
	    	popFW = new FileWriter(popFile,true); 
	        timeFW = new FileWriter(timeFile,true); 
	        
	        popFW.write(Statistics.printToFile() + "\r\n");
	        timeFW.write(computeTime + ", \r\n");//appends the string to the file 
	        
	        popFW.close();  
	        timeFW.close(); 
	    }
	    catch(Exception e) {}
	    */
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
	    	ctrlKeyPressed = false;
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(ctrlKeyPressed)
		{
			//System.out.println(e.getX() + "," + e.getY());
			PathFinder.switchState(e.getX(), e.getY(), 1);
		}
	}
     

}
