package com.bobby;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {


    int zoom = 1;
    PVector position;
    PApplet app;

    public Camera(PApplet applet){
        this.app = applet;
        this.position = new PVector(0,0);
    }

    public void begin(){
        this.app.translate(-this.position.x, -this.position.y);
        this.app.scale(this.zoom);
    }

    public void end(){
        this.app.resetMatrix();
    }

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void setZoom(int zoom){
        this.zoom = zoom;
    }

    public PVector screenToWorld(int x, int y){
        return new PVector(0,0);
    }

    public PVector screenToWorld(PVector p){
        return this.screenToWorld((int)p.x, (int)p.y);
    }

}
