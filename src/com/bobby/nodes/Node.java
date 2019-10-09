package com.bobby.nodes;

import com.bobby.Component;
import com.bobby.Main;
import com.bobby.MouseComponent;
import com.bobby.NodeIO;
import processing.core.PApplet;
import processing.core.PVector;

public class Node extends Component {

    public PVector size;
    protected int radius;
    public NodeIO[] inputs;
    public NodeIO[] outputs;
    public int tickDelay = 1;
    protected int tickDelayCounter;


    public Node(PApplet app, int x, int y, int width, int height, int r, int numInputs, int numOutputs){
        super(app, x, y);
        this.size = new PVector(width, height);
        this.radius = r;
        this.isGrabbable = true;
        this.updateLayer = 0;
        this.inputs = new NodeIO[numInputs];
        this.outputs = new NodeIO[numOutputs];
        int stepSize = (int)this.size.y / (this.inputs.length + 1);
        for (int i = 0; i < numInputs; i++) {
            inputs[i] = new NodeIO(app, 0, (i + 1) * stepSize, this, NodeIO.INPUT);
            ((Main)app).masterCircuit.addComponent(inputs[i]);
        }
        stepSize = (int)this.size.y / (this.outputs.length + 1);
        for (int i = 0; i < numOutputs; i++) {
            outputs[i] = new NodeIO(app, (int)this.size.x, (i + 1) * stepSize, this, NodeIO.OUTPUT);
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
        applet.stroke(0,100,255, 180);
        applet.strokeWeight(1);
        applet.fill(0,0,150, 100);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y, this.radius);

        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].wires.size() > 0) {
                applet.fill(0,255,0);
                applet.stroke(0);
            }else{
                applet.fill(255, 100);
                applet.stroke(0);
            }
            applet.circle(this.position.x, this.position.y + (int)this.size.y / 2, 10);
        }

        for (int i = 0; i < this.outputs.length; i++) {
            if(this.outputs[i].wires.size() > 0) {
                applet.fill(0,255,0);
                applet.stroke(0);
            }else{
                applet.fill(255, 100);
                applet.stroke(0);
            }
            applet.circle(this.position.x + this.size.x, this.position.y + (int)this.size.y / 2, 10);
        }
    }

    public PVector getCenter(){
        return new PVector(this.position.x + (this.size.x / 2), this.position.y + (this.size.y / 2));
    }

    public void hover(){
        applet.noFill();
        applet.strokeWeight(1);
        applet.stroke(255);
        applet.rect(this.position.x - 2, this.position.y - 2, this.size.x + 4, this.size.y + 4, this.radius);
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.position.x && mouseX <= this.position.x + this.size.x) &&
                (mouseY >= this.position.y && mouseY <= this.position.y + this.size.y);
    }

    @Override
    public PVector mousePressed(MouseComponent mouse, int button) {
        this.parent = mouse;
        return new PVector(-(mouse.position.x - this.position.x), -(mouse.position.y - this.position.y));
    }

    @Override
    public void mouseReleased(MouseComponent mouse) {
        this.parent = null;
    }

    public void scheduleTick(int delay){
        //number of ticks until the gate outputs
        this.tickDelayCounter = delay;
    }

}
