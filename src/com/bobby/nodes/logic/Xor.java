package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Xor extends Node {

    int color;
    boolean inValue = false;

    public Xor(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 45, 45, 3, 2, 1);
        color = app.color(100,255,255);
    }

    public void tick(){
        this.inValue = this.inputs[0].data > 0 ^ this.inputs[1].data > 0 ? true : false;

        int out = this.inValue ? 1 : 0;
        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(out);
        }
        super.tick();
    }

    public void draw() {
        applet.noStroke();



        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        applet.fill(255, 255);
        super.drawIO();
        applet.textAlign(applet.CENTER, applet.CENTER);
        applet.text("XOR", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
