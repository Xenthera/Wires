package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Nand extends Node {

    int color;
    boolean inValue = false;

    public Nand(PApplet app, int x, int y) {
        super(app, x, y, 45, 45, 3, 2, 1);
        color = app.color(255,255,100, 200);
    }

    public void tick(){
        this.inValue = this.inputs[0].data > 0 && this.inputs[1].data > 0 ? true : false;

        int out = this.inValue ? 0 : 1;
        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(out);
        }
        super.tick();
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
        applet.text("NAND", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
