package com.alejandrolai.sfpark.data;


/**
 * Created by ihsan_taha on 4/30/15.
 */
public class ParkedLocation {

    private int _id;
    private float _xlocation;
    private float _ylocation;



    /**
     * Default Constructor.
     */
    public ParkedLocation() {

    }



    /**
     * Constructor.
     *
     * @param xloc
     * @param yloc
     */
    public ParkedLocation(float xloc , float yloc) {
        this._xlocation = xloc;
        this._ylocation = yloc;
    }



    /**
     * Constructor.
     *
     * @param id
     * @param xloc
     * @param yloc
     */
    public ParkedLocation(int id, float xloc , float yloc) {
        this._id  = id;
        this._xlocation = xloc;
        this._ylocation = yloc;
    }



    // Setters
    public void setID(int id) { this._id = id; }

    public void setXLocation (float xloc) { this._xlocation = xloc; }

    public void setYLocation (float yloc) { this._ylocation = yloc; }



    // Getters
    public int getID() {return this._id;}

    public float getXLocation() { return this._xlocation; }

    public float getYLocation() { return this._ylocation; }

}