package com.alejandrolai.sfpark;

/**
 * Created by ihsan_taha on 4/30/15.
 */
public class ParkedLocation {



    // Data members

    private int _id;
    private float _xlocation;
    private float _ylocation;

    public ParkedLocation() {

    }



    // Data methods

    public ParkedLocation(int id, float xloc , float yloc) {

        this._id  = id;
        this._xlocation = xloc;
        this._ylocation = yloc;

    }



    public ParkedLocation(float xloc , float yloc) {

        this._xlocation = xloc;
        this._ylocation = yloc;

    }


    // Setters and Getters

    public void setID(int id) {
        this._id = id;
    }



    public void setXLocation (float xloc) { this._xlocation = xloc; }



    public void setYLocation (float yloc) { this._ylocation = yloc; }



    public int getID() {
        return this._id;
    }



    public float getXLocation() { return this._xlocation; }



    public float getYLocation() { return this._ylocation; }

}