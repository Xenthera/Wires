package com.bobby.nodes;

import processing.core.PApplet;

public class TestNode extends Node {

    public int color;

    public TestNode(PApplet app, int x, int y, int width, int height) {
        super(app, x, y, width, height,  1, 1);
        color = app.color(255,100,255, 200);
    }

    public void tick(){
        this.outputs[0].sendData((int)this.position.x);
    }

    public void draw() {

        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 255);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        applet.fill(255, 255);
        applet.text((int)this.position.x + ", " + (int)this.position.y, this.position.x + 20, this.position.y + 20);
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
}
