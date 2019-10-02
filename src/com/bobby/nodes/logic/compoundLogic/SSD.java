package com.bobby.nodes.logic.compoundLogic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class SSD extends Node {

    int color;
    boolean[] state;

    public SSD(PApplet app, int x, int y) {
        super(app, x, y, 70, 110, 3, 8, 0);
        color = app.color(0,0,0, 200);
        state = new boolean[8];
    }

    public void tick(){
        for (int i = 0; i < this.inputs.length; i++) {
            this.state[i] = this.inputs[i].data > 0 ? true : false;
        }
    }

    private void drawDisplay(){
        int on = applet.color(255,50,50);
        int off = applet.color(100);
        applet.strokeCap(applet.ROUND);
        applet.strokeWeight(8);
        applet.stroke(state[0] ? on : off);
        applet.line(this.position.x + 24, this.position.y + 15, this.position.x + this.size.x - 24, this.position.y + 15);
        applet.stroke(state[1] ? on : off);
        applet.line(this.position.x + this.size.x - 15, this.position.y + 23, this.position.x + this.size.x - 15, this.position.y + this.size.y / 2 - 6);
        applet.stroke(state[2] ? on : off);
        applet.line(this.position.x + this.size.x - 15, this.position.y + this.size.y / 2 + 6, this.position.x + this.size.x - 15, this.position.y + this.size.y - 23);
        applet.stroke(state[3] ? on : off);
        applet.line(this.position.x + 24, this.position.y + this.size.y - 15, this.position.x + this.size.x - 24, this.position.y + this.size.y - 15);
        applet.stroke(state[4] ? on : off);
        applet.line(this.position.x + 15, this.position.y + this.size.y / 2 + 6, this.position.x + 15, this.position.y + this.size.y - 23);
        applet.stroke(state[5] ? on : off);
        applet.line(this.position.x + 15, this.position.y + 23, this.position.x + 15, this.position.y + this.size.y / 2 - 6);
        applet.stroke(state[6] ? on : off);
        applet.line(this.position.x + 24, this.position.y + this.size.y / 2, this.position.x + this.size.x - 24, this.position.y + this.size.y / 2);
        applet.fill(state[7] ? on : off);
        applet.noStroke();
        applet.circle(this.position.x + this.size.x - 10, this.position.y + this.size.y - 10, 8);

    }

    public void draw() {
        applet.noStroke();
        applet.fill(0, 90);
        applet.rect(this.position.x + 8, this.position.y + 8, this.size.x, this.size.y, this.radius);

        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 255);
        applet.strokeWeight(1);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y, this.radius);

        drawDisplay();
        applet.strokeWeight(2);
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


    }
}
