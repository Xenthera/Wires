package com.bobby.nodes;

import processing.core.PApplet;

public class Light extends Node {

    public int color;
    boolean isOn = false;

    public Light(PApplet app, int x, int y) {
        super(app, x, y, 40, 40, 40, 1, 0);
        color = app.color(0,0,0, 100);
    }

    public void tick(){
        if(this.inputs[0].data > 0){
            this.isOn = true;
        }else{
            this.isOn = false;
        }
    }

    public void draw() {
        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 200);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.circle(this.position.x + this.size.x / 2, this.position.y + this.size.y / 2, 40);
        applet.fill(255, 255);
        if(this.isOn){
            applet.fill(255);
            applet.circle(this.position.x + this.size.x / 2, this.position.y + this.size.y / 2, 40);
        }
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
