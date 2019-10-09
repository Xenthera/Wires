package com.bobby;

import processing.core.PApplet;
import processing.core.PVector;

public class MouseComponent extends Component {

    Component attachedComponent;
    boolean isDrawingWire = false;
    boolean isScrolling = false;
    PVector wireStart;
    NodeIO origin, destination;
    PVector deltaMouse, oldMouse, newMouse;

    public MouseComponent(PApplet app, int x, int y){
        super(app, x, y);
        this.deltaMouse = new PVector(0,0);
        this.newMouse = new PVector(x,y);
        this.oldMouse = new PVector(x,y);
        this.size = new PVector(0,0);
    }

    @Override
    public PVector getSize() {
        return this.size;
    }

    @Override
    public void tick() {

    }
    @Override
    public void update(){

        Main main = (Main)applet;

        this.position.x = main.mouseX / main.camera.zoom + main.camera.position.x / main.camera.zoom;
        this.position.y = main.mouseY / main.camera.zoom + main.camera.position.y / main.camera.zoom;

        //Calculate Mouse Delta
        this.newMouse.x = main.mouseX;
        this.newMouse.y = main.mouseY;

        this.deltaMouse.x = this.newMouse.x - this.oldMouse.x;
        this.deltaMouse.y = this.newMouse.y - this.oldMouse.y;
        this.oldMouse.set(this.newMouse);

        if(isScrolling) {
            if (main.mousePressed && main.mouseButton == main.LEFT) {
                main.camera.position.x -= this.deltaMouse.x / main.camera.zoom;
                main.camera.position.y -= this.deltaMouse.y / main.camera.zoom;
            }
        }
    }

    @Override
    public void draw() {
        if(isDrawingWire){
            int x = (int)wireStart.x;
            int y = (int)wireStart.y;
            int x2 = (int)this.position.x;
            int y2 = (int)this.position.y;

            applet.strokeWeight(2);
            applet.stroke(255);
            applet.noFill();
            applet.bezier(x, y, x + 100, y, x2 - 100,y2,x2,y2);
        }
        //applet.fill(255);
        //applet.circle(this.position.x, this.position.y, 20);
    }

    @Override
    public void hover() {

    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public PVector mousePressed(MouseComponent mouse, int button) {
        return new PVector(0,0);
    }

    @Override
    public void mouseReleased(MouseComponent mouse) {
        this.isScrolling = false;
        endWireDraw(this);
    }

    public void beginWireDraw(Component c){
        this.origin = (NodeIO) c;
        this.isDrawingWire = true;
        this.wireStart = new PVector(c.position.x, c.position.y);

    }

    public void endWireDraw(Component c){
        if((c != this) && (this.isDrawingWire)){
            this.destination = (NodeIO) c;
            if(((NodeIO) c).type != this.origin.type && ((NodeIO) c).parent != this.origin.parent) {

                if(((NodeIO) c).type == NodeIO.OUTPUT) {
                    ((Main) applet).masterCircuit.addComponent(new Wire(applet, this.destination, this.origin));
                }else{
                    ((Main) applet).masterCircuit.addComponent(new Wire(applet, this.origin, this.destination));
                }
            }
            this.origin = null;
            this.destination = null;
            this.isDrawingWire = false;
            this.wireStart = null;
        }
    }
}
