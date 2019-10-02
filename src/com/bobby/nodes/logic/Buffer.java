package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Buffer extends Node {

    int color;
    boolean inValue = false;

    public Buffer(PApplet app, int x, int y) {
        super(app, x, y, 18, 15, 3, 1, 1);
        color = app.color(30, 200);
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
        applet.fill(0, 50);
        applet.rect(this.position.x + 8, this.position.y + 8, this.size.x, this.size.y, this.radius);

        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 255);
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
