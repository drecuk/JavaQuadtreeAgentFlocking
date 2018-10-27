package life;

/**
 *
 * @author  Eugene Ch'ng
 */
public class LifeUtil {
	
	public static double Adaptability(double c, double p, double L, double U, double b)
	{	
		double fitness = 1; // begin with full health return

		// Within these are full health (Lb <= c <= Ub)
		double Lb = p-b*Math.abs(p-L); // Lower Bound
		double Ub = p+b*Math.abs(p-U); // Upper Bound

		// If the current condition (c) is within the computed preferred tolerance
		if ( (Lb <= c) && (c <= Ub) )
		{
			// Assign full health
			fitness = 1.0f;
		}
		else if ( (Ub < c) && (c <= U) )
		{ 
			// if c is > the Upper percentage and <= the Upper Tolerance
			//health = (-(1/U) * Math.Abs( c+b*Math.Abs(p-U) - Ub)) + 1;
			fitness = -1/Math.abs(U-Ub) * Math.abs(c-Ub) + 1;
		} 
		else if ( (L <= c) && (c < Lb) )
		{ 
			// if c is >= the lower tolerance and < the Lower percentage
			//health = -(1/L) * Math.Abs( (c-b*Math.Abs(p-L)) - Lb ) + 1;
			fitness = -1/Math.abs(L-Lb) * Math.abs( c - Lb ) + 1;
		}
		else // outside of the tolerance is dead
		{
			fitness = 0.0d;
		}
		
		return fitness;
	}
	
	public static double AdaptabilityLeftBound(double c, double p, double L, double b)
	{
		double fitness = 1; // begin with full health return

		// Within these are full health (Lb <= c <= Ub)
		double Lb = p-b*Math.abs(p-L); // Lower Bound			

		// If the current condition (c) is within the computed preferred tolerance
		if ( (Lb <= c) && (c <= 1) )
		{
			// Assign full health
			fitness = 1.0f;
		}			
		else if ( (L <= c) && (c < Lb) )
		{ 
			// if c is >= the lower tolerance and < the Lower percentage
			//health = -(1/L) * Math.Abs( (c-b*Math.Abs(p-L)) - Lb ) + 1;
			fitness = -1/Math.abs(L-Lb) * Math.abs( c - Lb ) + 1;
		}
		else // outside of the tolerance is dead
		{
			fitness = 0.0d;
		}
    
		return fitness;
	}

	public static double AdaptabilityRightBound(double c, double p, double U, double b)
	{
		double fitness = 1.0d;
  
		// Within these are full health (Lb <= c <= Ub)			
		double Ub = p+b*Math.abs(p-U); // Upper/Right Bound

		// If the spacePts is from 0 to p computed tolerance
		if ( (c >= 0) && (c <= Ub) )
		{
			// Assign full health
			fitness = 1.0f;
		}			
		else if ( (Ub < c) && (c <= U) )
		{ 
			// if c is > the Upper percentage and <= the Upper Tolerance
			//health = -(1/U) * Math.Abs( (c+b*Math.Abs(p-U)) - Ub ) + 1;				
			fitness = -1/Math.abs(U-Ub) * Math.abs(c-Ub) + 1;
		}			
		else // outside of the above tolerance is dead
		{
			fitness = 0.0d;
		}

		// return health of space
		return fitness;
	}
}
