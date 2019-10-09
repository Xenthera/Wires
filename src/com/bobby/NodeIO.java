package com.bobby;

import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class NodeIO extends Component {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public int type;
    public ArrayList<Wire> wires;
    public Node parent;
    private int lastSent = -1;

    public int data;

    public NodeIO(PApplet app, int x, int y, Component parent, int type) {
        super(app, x, y, parent);
        this.layer = 1;
        this.type = type;
        this.wires = new ArrayList<>();
        this.parent = (Node)parent;
        this.size = new PVector(0,0);
    }

    public PVector getSize(){
        return this.size;
    }

    public void addWire(Wire wire){
        if(this.type == INPUT && wires.size() == 1){

        }else {
            this.wires.add(wire);
        }
    }

    public void removeWire(Wire wire){
        wire.sendValue(0);
        this.wires.remove(wire);
    }

    @Override
    public void tick() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

    @Override
    public void hover() {
        applet.noFill();
        applet.stroke(255);
        applet.strokeWeight(2);
        applet.circle(this.position.x, this.position.y, 12);
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        float dis = applet.sqrt(applet.pow(this.position.x - mouseX, 2) + applet.pow(this.position.y - mouseY, 2));
        return dis <= 15/2;
    }

    @Override
    public PVector mousePressed(Component mouse, int button) {
        return null;
    }

    @Override
    public void mouseReleased(Component mouse) {

    }

    public void sendData(int data){
        if(this.type == OUTPUT && wires.size() > 0){
            this.data = data;
            for (Wire wire : this.wires) {
                wire.sendValue(data);
            }

        }
    }

    public void receiveData(int data){
        if(this.lastSent != data) {
            this.parent.scheduleTick(this.parent.tickDelay);
            this.lastSent = data;
        }
        if(this.type == INPUT && wires.size() == 1) {
            this.data = data;
        }
    }
}
