package com.bobby.nodes;

import processing.core.PApplet;

public class Light extends Node {

    public int color;
    boolean isOn = false;

    public Light(PApplet app, int x, int y) {
        super(app, x, y, 1, 1, 1, 0);
        color = app.color(0,0,0);
    }

    public void tick(){
        if(this.inputs[0].data > 0){
            this.isOn = true;
        }else{
            this.isOn = false;
        }
    }

    public void draw() {
        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 255);
        applet.strokeWeight(1);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x - 1, this.size.y - 1);
        applet.fill(255, 255);
        if(this.isOn){
            applet.fill(255);
            applet.rect(this.position.x, this.position.y, this.size.x - 1, this.size.y - 1);
        }
        super.drawIO();
    }
}
