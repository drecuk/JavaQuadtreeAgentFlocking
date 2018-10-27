package utilities;

/*
 * Point.java
 *
 * Created on 16 February 2004, 17:15
 */

/**
 *
 * @author  Eugene Ch'ng
 */
public class Point2D {
	public int x;
	public int y;
    
    /** Creates a new instance of Point */
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /** Get X */
    public int getX(){
        return x;
    }
    
    /** Get Y */
    public int getY(){
        return y;
    }

    
    /** Set x */
    public void setX( int x ){
        this.x = x;
    }
    
    /** Set Y */
    public void setY( int y ){
        this.y = y;
    }

}
