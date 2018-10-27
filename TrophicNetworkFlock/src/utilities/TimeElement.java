package utilities;

import java.util.Timer;

public class TimeElement {
	
	protected Timer timer;
    protected long startTime;    
    protected long elapsedTime; // seconds
    
    public TimeElement()
    {    	
    	// record start time of simulation
    	startTime = System.currentTimeMillis();    	
    }
    
    public void update()
    {
    	elapsedTime = (long)(System.currentTimeMillis()-startTime);
    }
    
    public long milliseconds()
    {
    	
		return elapsedTime;    	
    }
    
    public float seconds()
    {
    	return elapsedTime/1000.0f;
    }
    
}
