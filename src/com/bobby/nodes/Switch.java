package com.bobby.nodes;

import com.bobby.MouseComponent;
import processing.core.PApplet;
import processing.core.PVector;

public class Switch extends Node {

    public int color;
    public boolean isOn = false;

    public Switch(PApplet app, int x, int y) {
        super(app, x, y, 30, 30, 2, 0, 1);
        color = app.color(0,255,255, 100);
    }

    public void tick(){
        this.outputs[0].sendData(this.isOn ? 1 : 0);
    }

    public void draw() {
        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 200);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y, this.radius);
        applet.noFill();


        if(!isOn){
            this.color = applet.color(0, 200, 200, 100);
            applet.stroke(0,50,50);
        }else{
            this.color = applet.color(0, 255, 255, 100);
            applet.stroke(0,255,255);
        }
        applet.rect(this.position.x + 8, this.position.y + 8, this.size.x - 16, this.size.y - 16);
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

    public PVector mousePressed(MouseComponent mouse, int button){
        if(button == applet.LEFT) {
            this.parent = mouse;

        }else if(button == applet.RIGHT){
            this.isOn = !this.isOn;
        }
        return new PVector(-(mouse.position.x - this.position.x), -(mouse.position.y - this.position.y));
    }
}
