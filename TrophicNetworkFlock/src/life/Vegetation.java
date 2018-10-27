package life;

/*
 * Vegetation.java
 *
 * Created on 16 February 2004, 16:37
 */

import utilities.ObjectType;
import utilities.Point3D;

/**
 *
 * @author  Eugene Ch'ng
 */

public abstract class Vegetation extends COrganism {	
    protected float pWidth;
    protected float pLength;
    protected float pHeight;
    
    
    /** Creates a new instance of Vegetation */
    public Vegetation(int _id, String _name, ObjectType _objectType, Point3D _position, int _range) {       
        super(_id, _name, _objectType, _position, _range);
    }
    
    
    /** Change Image of plant */
    public void ChangeImage() {
    }
    
    /** Get age */
    public float getAge() {
        return age;
    }
    
    /** Get health */
    public float getFitness() {   
        return fitness;
    }
    
    /** get width */
    public float getWidth(){
        return pWidth;
    }
    
    /** get length */
    public float getlength(){
        return pLength;
    }
    
    /** get height */
    public float getHeight(){
        return pHeight;
    }
    
    /** Set age */
    public void setAge( int age ){
        this.age = age;
    }

    
    /** set width */
    public void setWidth( int width ) {
        pWidth = width;
    }
    
    /** set length */
    public void setLength( int length ) {
        pLength = length;
    }
    
    /** set height */
    public void setHeight( int height ) {
        pHeight = height;
    }
       
    //abstract void update();
}
