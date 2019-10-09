package com.bobby;

import processing.core.PApplet;
import processing.core.PVector;

public class Camera {


    float zoom = 1;
    float inverseZoom = 1/zoom;
    PVector position;
    PApplet app;

    public Camera(PApplet applet){
        this.app = applet;
        this.position = new PVector(0,0);
    }

    public void begin(){
        //this.app.translate(app.width / 2, app.height / 2);
        this.app.scale(this.zoom);
        this.app.translate((-this.position.x), (-this.position.y));

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

    public void setZoom(float zoom){
        this.zoom = zoom;
        this.inverseZoom = 1 / zoom;
    }

}
