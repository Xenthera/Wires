package com.bobby;

import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PVector;

public class MouseComponent extends Component {

    Component attachedComponent;
    boolean isDrawingWire = false;
    boolean isDrawingMultiwire = false;
    boolean isScrolling = false;
    PVector wireStart;
    NodeIO origin, destination;
    Node nodeOrigin, nodeDestination;
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
            main.camera.position.x -= this.deltaMouse.x;
            main.camera.position.y -= this.deltaMouse.y;
        }
    }

    @Override
    public void draw() {
        if(isDrawingWire){
            int x = (int)wireStart.x;
            int y = (int)wireStart.y;
            int x2 = (int)this.position.x;
            int y2 = (int)this.position.y;
            int dis = applet.abs(x - x2);

            applet.strokeWeight(5);
            applet.stroke(0, 0, 0, 255);
            applet.noFill();
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);

            applet.strokeWeight(2);
            applet.stroke(0, 255, 100, 255);
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);

        }else if(isDrawingMultiwire){

            int x = (int)wireStart.x;
            int y = (int)wireStart.y;
            int x2 = (int)this.position.x;
            int y2 = (int)this.position.y;
            int dis = applet.abs(x - x2);

            applet.strokeWeight(12);
            applet.stroke(0, 0, 0, 255);
            applet.noFill();
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);

            applet.strokeWeight(2);
            applet.stroke(255, 0, 100, 255);
            applet.bezier(x, y-3, x + dis/2, y-3, x2 - dis/2,y2-3,x2,y2-3);
            applet.stroke(0, 255, 100, 255);
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);
            applet.stroke(0, 100, 255, 255);
            applet.bezier(x, y + 3, x + dis/2, y + 3, x2 - dis/2,y2 + 3,x2,y2 + 3);

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
    public PVector mousePressed(Component c, int button) {
        if(c != this) {
            c.mousePressed(this, button);
        }
        if(c instanceof NodeIO) {
            this.beginWireDraw(c);
        }
        if(c instanceof Node){
            this.beginMultiwireDraw(c);
        }

        return new PVector(0,0);
    }

    @Override
    public void mouseReleased(Component c) {
        if(c != this) {
            c.mouseReleased(this);
        }else{

        }
        this.isScrolling = false;
        endWireDraw(c);
        endMultiWiredraw(c);
    }

    public void beginMultiwireDraw(Component c){
        if(((Node) c).outputs.length  > 0) {
            this.nodeOrigin = (Node) c;
            this.isDrawingMultiwire = true;
            this.wireStart = new PVector(this.nodeOrigin.position.x + this.nodeOrigin.size.x / 2, this.nodeOrigin.position.y + this.nodeOrigin.size.y / 2);
        }
    }

    public void endMultiWiredraw(Component c){
        if(c instanceof Node && this.isDrawingMultiwire){
            Node n = (Node)c;
            int maxConnections = applet.min(this.nodeOrigin.outputs.length, n.inputs.length);
            for (int i = 0; i < maxConnections; i++) {
                ((Main) applet).masterCircuit.addComponent(new Wire(applet, this.nodeOrigin.outputs[i], n.inputs[i]));
            }
        }

        this.isDrawingMultiwire = false;

    }

    public void beginWireDraw(Component c){
        this.origin = (NodeIO) c;
        this.isDrawingWire = true;
        this.wireStart = new PVector(c.position.x, c.position.y);
    }

    public void endWireDraw(Component c){
        if((c != this) && (this.isDrawingWire) && (c instanceof NodeIO)){
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
            //this.isDrawingWire = false;
            this.wireStart = null;
        }
        this.isDrawingWire = false;
    }
}
