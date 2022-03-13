package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Not extends Node {

    int color;
    boolean inValue = false;


    public Not(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 40, 30, 3, 1, 1);
        color = app.color(100,100,100);
        //this.tickDelay = 60;
        this.scheduleTick(this.tickDelay);
    }

    public void tick(){
        this.inValue = this.inputs[0].data > 0 ? true : false;

        int out = this.inValue ? 0 : 1;

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
        applet.text("NOT", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
