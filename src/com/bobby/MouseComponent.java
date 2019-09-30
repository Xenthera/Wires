package com.bobby;

import processing.core.PApplet;
import processing.core.PVector;

public class MouseComponent extends Component {

    Component attachedComponent;
    boolean isDrawingWire = false;
    PVector wireStart;
    NodeIO origin, destination;

    public MouseComponent(PApplet app, int x, int y){
        super(app, x, y);
    }
    @Override
    public void tick() {

    }
    @Override
    public void update(){
        this.position.x = applet.mouseX;
        this.position.y = applet.mouseY;
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
