package com.bobby.nodes;

import com.bobby.Component;
import processing.core.PApplet;
import processing.core.PVector;

public class ToggleSwitch extends Node {

    public int color;
    public boolean isOn = false;

    public ToggleSwitch(PApplet app, int x, int y) {
        super(app, x, y, 30, 30, 5, 0, 1);
        color = app.color(0,255,255);
    }

    public void tick(){
        this.outputs[0].sendData(this.isOn ? 1 : 0);
    }

    public void draw() {

        applet.noStroke();

        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        applet.noFill();


        if(!isOn){
            this.color = applet.color(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 100);

            applet.stroke(applet.red(this.color)- 200, applet.green(this.color)- 200, applet.blue(this.color) - 200);
        }else{
            this.color = applet.color(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 100);
            applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color));
        }
        applet.rect(this.position.x + 8, this.position.y + 8, this.size.x - 16, this.size.y - 16);
        super.drawIO();
    }

    public PVector mousePressed(Component mouse, int button){
        if(button == applet.RIGHT){
            this.isOn = !this.isOn;
        }
        return new PVector(-(mouse.position.x - this.position.x), -(mouse.position.y - this.position.y));
    }
}
