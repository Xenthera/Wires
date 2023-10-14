package com.bobby.nodes;

import processing.core.PApplet;

public class DisplayNode extends Node {

    public int color;
    private int value = 0;

    public DisplayNode(PApplet app, int x, int y, int width, int height) {
        super(app, x, y, width, height, 1, 0);
    }

    public void tick(){
        this.value = this.inputs[0].data;
    }

    public void draw() {
        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 200);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        applet.fill(255, 255);
        applet.text(this.value, this.position.x + this.size.x / 2 - 10, this.position.y + 5 + this.size.y / 2);
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
