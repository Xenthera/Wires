package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Buffer extends Node {

    int color;
    boolean inValue = false;

    public Buffer(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 18, 15, 3, 1, 1);
        color = app.color(30);
        this.tickDelay = 0;
    }

    public void tick(){
        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(this.inputs[0].data);
        }
        super.tick();
    }

    public void draw() {
        applet.noStroke();



        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        applet.fill(255, 255);
        super.drawIO();
    }
}
