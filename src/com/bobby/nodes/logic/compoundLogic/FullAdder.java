package com.bobby.nodes.logic.compoundLogic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class FullAdder extends Node {

    int color;


    public FullAdder(PApplet app, int x, int y) {
        super(app, x, y, 55, 85, 3, 3, 2);
        color = app.color(0,100,2550);
    }

    public void tick(){
        boolean a = this.inputs[0].data > 0 ? true : false;
        boolean b = this.inputs[1].data > 0 ? true : false;
        boolean c = this.inputs[2].data > 0 ? true : false;
        boolean sum = c ^ (a ^ b);
        boolean carry = (a && b) || (b && c) || (a && c);

        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(sum ? 1 : 0);
            this.outputs[1].sendData(carry ? 1 : 0);
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
        applet.text("Full \nAdder", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
