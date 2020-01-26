package com.bobby;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Component {

    public PVector position;
    public PVector size;
    public PVector parentOffset;
    public Component parent;
    protected PApplet applet;
    protected int drawLayer = 0;



    protected int updateLayer = 0;
    public boolean isGrabbable = false;

    public Component(PApplet app, int x, int y){
        this.applet = app;
        this.position = new PVector(x, y);
        this.size = new PVector(0,0);
    }
    public int getDrawLayer(){
        return this.drawLayer;
    }

    public int getUpdateLayer() {
        return updateLayer;
    }

//    public Component(PApplet app, int x, int y, Component parent){
//        this.applet = app;
//        this.position = new PVector(x, y);
//        this.parent = parent;
//    }
    public Component(PApplet app, int localX, int localY, Component parent){
        this.applet = app;
        this.parentOffset = new PVector(localX, localY);
        this.position = new PVector(parent.position.x + localX, parent.position.y + localY);
        this.parent = parent;
    }

    public abstract PVector getSize();

    //Logic update (not tied to framerate)
    public abstract void tick();
    //General update (positions, parent positions, etc)
    public abstract void update();
    //Drawing
    public abstract void draw();
    //Drawing when hovered (can also be used as something else)
    public abstract void hover();
    //Returns true if the mouse is currently hovering over the object (you must define what is considered hovering here)
    public abstract boolean isHovered(int mouseX, int mouseY);
    //Called when mouse is clicked on this object, should return the offset of the mouse, and the object position
    public abstract PVector mousePressed(Component c, int button);
    //Called when the mouse is released and detaches the object from the mouse
    public abstract void mouseReleased(Component c);

    public abstract void remove();

}
