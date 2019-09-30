package com.bobby.nodes.logic.compoundLogic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class FullAdder extends Node {

    int color;


    public FullAdder(PApplet app, int x, int y) {
        super(app, x, y, 55, 85, 3, 3, 2);
        color = app.color(0,100,255, 100);
    }

    public void tick(){
        boolean a = this.inputs[0].data > 0 ? true : false;
        boolean b = this.inputs[1].data > 0 ? true : false;
        boolean c = this.inputs[2].data > 0 ? true : false;
        boolean sum = c ^ (a ^ b);
        boolean carry = (a && b) || (b && c) || (a && c);
        this.outputs[0].sendData(sum ? 1 : 0);
        this.outputs[1].sendData(carry ? 1 : 0);
    }

    public void draw() {
        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 200);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y, this.radius);
        applet.fill(255, 255);
        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].wires.size() > 0) {
                applet.fill(0,255,0);
                applet.stroke(0);
            }else{
                applet.fill(255, 100);
                applet.stroke(0);
            }
            int stepSize = (int)this.size.y / (this.inputs.length + 1);
            applet.circle(this.position.x, this.position.y + (i + 1) * stepSize, 10);
        }

        for (int i = 0; i < this.outputs.length; i++) {
            if(this.outputs[i].wires.size() > 0) {
                applet.fill(0,255,0);
                applet.stroke(0);
            }else{
                applet.fill(255, 100);
                applet.stroke(0);
            }
            int stepSize = (int)this.size.y / (this.outputs.length + 1);
            applet.circle(this.position.x + this.size.x, this.position.y + (i + 1) * stepSize, 10);
        }
        applet.textAlign(applet.CENTER, applet.CENTER);
        applet.text("Full \nAdder", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
