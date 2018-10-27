package utilities;

import java.awt.Color;
import java.awt.Graphics;

public abstract class CRenderable extends CObject {
	public Color color;
	public boolean visible;
	
	public CRenderable (int _id)
	{
		super(_id);
	}
	
	public abstract void paint(Graphics g);
	
}
