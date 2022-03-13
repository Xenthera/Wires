package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class And extends Node {

    int color;
    boolean inValue = false;

    public And(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 45, 45, 3, numInputs, 1);
        color = app.color(255,255,100);
        //this.tickDelay = 60;
    }

    public void tick(){
        this.inValue = true;
        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].data < 1){
                this.inValue = false;
                break;
            }
        }
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
        applet.text("AND", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
