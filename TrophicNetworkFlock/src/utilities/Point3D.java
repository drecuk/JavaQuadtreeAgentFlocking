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
public class Point3D {
	public int x;
	public int y;
	public int z;
	//public int y;
    
    /** Creates a new instance of Point */
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /** Get X */
    public int getX(){
        return x;
    }
    
    /** Get Y */
    public int getY(){
        return y;
    }
    
    /** Get Z */
    public int getZ(){
        return z;
    }
    
    /** Set x */
    public void setX( int x ){
        this.x = x;
    }
    
    /** Set Y */
    public void setY( int y ){
        this.y = y;
    }
    
    /** Set Z */
    public void setZ( int z ){
        this.z = z;
    }
}
