package com.bobby.nodes;

import com.bobby.Component;
import com.bobby.Main;
import com.bobby.MouseComponent;
import com.bobby.NodeIO;
import processing.core.PApplet;
import processing.core.PVector;

public class Node extends Component {

    public PVector size;
    //protected int radius;
    public NodeIO[] inputs;
    public NodeIO[] outputs;

    public int tickDelay = 1;
    protected int tickDelayCounter;
    public Node(PApplet app, int x, int y, int gridWidth, int gridHeight, int numInputs, int numOutputs){
        super(app, x, y);
        this.size = new PVector(gridWidth * Main.GridSize, gridHeight * Main.GridSize);
        this.isGrabbable = true;
        this.updateLayer = 0;
        this.inputs = new NodeIO[numInputs];
        this.outputs = new NodeIO[numOutputs];
        int stepSize = numInputs > 0 ? (int)this.size.y / (this.inputs.length) : 0;
        for (int i = 0; i < numInputs; i++) {
            inputs[i] = new NodeIO(app, 0, (int)((i + 1) * stepSize - (this.size.y / 2) / numInputs), this, NodeIO.INPUT);
            ((Main)app).masterCircuit.addComponent(inputs[i]);
        }
        stepSize = numOutputs > 0 ? (int)this.size.y / (this.outputs.length) : 0;
        for (int i = 0; i < numOutputs; i++) {
            outputs[i] = new NodeIO(app, (int)this.size.x, (int)((i + 1) * stepSize - (this.size.y / 2) / numOutputs), this, NodeIO.OUTPUT);
            ((Main)app).masterCircuit.addComponent(outputs[i]);
        }
    }

    public PVector getSize(){
        return this.size;
    }

    @Override
    public void tick() {
        if(this.tickDelayCounter > 0){
            this.tickDelayCounter--;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {


    }

    public PVector getCenter(){
        return new PVector(this.position.x + (this.size.x / 2), this.position.y + (this.size.y / 2));
    }

    public void drawIO(){
        applet.noStroke();
        if(((Main)applet).drawNodes) {
            for (int i = 0; i < this.inputs.length; i++) {
                if (!this.inputs[i].wires.isEmpty()) {
                    applet.fill(0, 255, 0);
                    //applet.stroke(0);
                } else {
                    applet.fill(30, 255);
                    //applet.stroke(0);
                }
                int stepSize = (int) this.size.y / (this.inputs.length);
                applet.circle(this.position.x, this.position.y + (i + 1) * stepSize - (this.size.y / 2) / this.inputs.length, 10);
            }

            for (int i = 0; i < this.outputs.length; i++) {
                if (!this.outputs[i].wires.isEmpty()) {
                    applet.fill(0, 100, 255);
                    //applet.stroke(0);
                } else {
                    applet.fill(30, 255);
                    //applet.stroke(0);
                }
                int stepSize = (int) this.size.y / (this.outputs.length);
                applet.circle(this.position.x + this.size.x, this.position.y + (i + 1) * stepSize - (this.size.y / 2) / this.outputs.length, 10);
            }
        }
    }

    public void hover(){
        applet.noFill();
        applet.strokeWeight(1);
        applet.stroke(255);
        applet.rect(this.position.x - 1, this.position.y - 1, this.size.x + 1, this.size.y + 1);
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.position.x && mouseX <= this.position.x + this.size.x) &&
                (mouseY >= this.position.y && mouseY <= this.position.y + this.size.y);
    }

    @Override
    public PVector mousePressed(Component mouse, int button) {
        //this.parent = mouse;
        return new PVector(-(mouse.position.x - this.position.x), -(mouse.position.y - this.position.y));
    }

    @Override
    public void mouseReleased(Component mouse) {
        this.parent = null;
    }

    @Override
    public void remove() {

    }

    public void scheduleTick(int delay){
        //number of ticks until the gate outputs
        this.tickDelayCounter = delay;
    }

}
