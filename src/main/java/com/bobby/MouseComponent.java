package com.bobby;

import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class MouseComponent extends Component {

    Component attachedComponent;
    boolean isDrawingWire = false;
    boolean isDrawingMultiwire = false;
    boolean isScrolling = false;
    boolean isSelecting = false;
    private boolean _selecting = false;
    PVector wireStart;
    NodeIO origin, destination;
    Node nodeOrigin, nodeDestination;
    PVector deltaMouse, oldMouse, newMouse;

    PVector selectOrigin;

    ArrayList<Component> selectedComponents;

    public MouseComponent(PApplet app, int x, int y){
        super(app, x, y);
        this.deltaMouse = new PVector(0,0);
        this.newMouse = new PVector(x,y);
        this.oldMouse = new PVector(x,y);
        this.size = new PVector(0,0);
        selectedComponents = new ArrayList<>();
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

        if(isSelecting){
            if(_selecting == false){
                _selecting = true;
                selectBegin();
            }
        }

        if(!isSelecting){
            if(_selecting == true){
                _selecting = false;
                selectEnd();
            }
        }


    }

    public void clearSelection(){
        this.selectedComponents = new ArrayList<>();
    }

    private void selectBegin(){
        selectOrigin = new PVector(position.x, position.y);
    }

    private void selectEnd(){
        PVector p1 = new PVector(selectOrigin.x, selectOrigin.y);
        PVector p2 = new PVector(position.x, position.y);

        if(p1.x > p2.x){
            p2.x = p1.x;
            p1.x = position.x;
        }

        if(p1.y > p2.y){
            p2.y = p1.y;
            p1.y = position.y;
        }

        for (Component c : ((Main)applet).masterCircuit.sceneComponents) {
            if(c.position.x >= p1.x && c.position.x <= p2.x){
                if(c.position.y >= p1.y && c.position.y <= p2.y){
                    if(c instanceof Node)
                    selectedComponents.add(c);
                }
            }
        }
        System.out.println("Ended selecting");
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

        if(selectedComponents.size() > 0){
            for (Component c :
                    selectedComponents) {
                c.hover();
            }
        }

        if(_selecting){
            applet.stroke(100,255,220, 100);
            applet.strokeWeight(2);
            applet.fill(100,255,220,50);
            applet.rect(selectOrigin.x, selectOrigin.y, position.x - selectOrigin.x, position.y - selectOrigin.y);

            applet.fill(255,0,0);
            applet.circle(selectOrigin.x, selectOrigin.y, 20);
            applet.fill(0,255,0);
            applet.circle(position.x, position.y, 20);

            PVector p1 = new PVector(selectOrigin.x, selectOrigin.y);
            PVector p2 = new PVector(position.x, position.y);

            if(p1.x > p2.x){
                p2.x = p1.x;
                p1.x = position.x;
            }

            if(p1.y > p2.y){
                p2.y = p1.y;
                p1.y = position.y;
            }

            applet.line(p1.x, p1.y, p2.x, p2.y);

            applet.fill(255,255,0);
            applet.circle(p1.x, p1.y, 20);
            applet.fill(0,0,255);
            applet.circle(p2.x, p2.y, 20);


            for (Component c : ((Main)applet).masterCircuit.sceneComponents) {
                if(c.position.x >= p1.x && c.position.x <= p2.x){
                    if(c.position.y >= p1.y && c.position.y <= p2.y){
                        if(c instanceof Node)
                        c.hover();
                    }
                }
            }
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
        this.isSelecting = false;
        endWireDraw(c);
        endMultiWiredraw(c);
    }

    @Override
    public void remove() {

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
