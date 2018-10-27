package utilities;

import java.util.ArrayList;

import simulation.CEnvironment;


public interface iWorldObject
{
	public void update(ArrayList<CWorldObject> worldObjects, CEnvironment env);
}
