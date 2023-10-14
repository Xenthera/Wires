package com.bobby.nodes.logic.compoundLogic;

import com.bobby.Main;
import com.bobby.nodes.Node;
import com.bobby.nodes.logic.LogicNode;
import processing.core.PApplet;

public class SSD extends LogicNode {

    int color;
    boolean[] state;

    public SSD(PApplet app, int x, int y) {
        super(app, x, y, 3, 4,  8, 0, "", app.color(0,0,0, 200));
        state = new boolean[8];
    }

    public void tick(){
        for (int i = 0; i < this.inputs.length; i++) {
            this.state[i] = this.inputs[i].data > 0 ? true : false;
        }
    }

    private void drawDisplay(){
        int on = applet.color(255,50,50);
        int off = applet.color(70);
        applet.strokeCap(applet.ROUND);
        applet.strokeWeight(8);
        applet.stroke(state[0] ? on : off);
        applet.line(this.position.x + 30, this.position.y + 15, this.position.x + this.size.x - 30, this.position.y + 15);
        applet.stroke(state[1] ? on : off);
        applet.line(this.position.x + this.size.x - 20, this.position.y + 23, this.position.x + this.size.x - 20, this.position.y + this.size.y / 2 - 6);
        applet.stroke(state[2] ? on : off);
        applet.line(this.position.x + this.size.x - 20, this.position.y + this.size.y / 2 + 6, this.position.x + this.size.x - 20, this.position.y + this.size.y - 23);
        applet.stroke(state[3] ? on : off);
        applet.line(this.position.x + 30, this.position.y + this.size.y - 15, this.position.x + this.size.x - 30, this.position.y + this.size.y - 15);
        applet.stroke(state[4] ? on : off);
        applet.line(this.position.x + 20, this.position.y + this.size.y / 2 + 6, this.position.x + 20, this.position.y + this.size.y - 23);
        applet.stroke(state[5] ? on : off);
        applet.line(this.position.x + 20, this.position.y + 23, this.position.x + 20, this.position.y + this.size.y / 2 - 6);
        applet.stroke(state[6] ? on : off);
        applet.line(this.position.x + 30, this.position.y + this.size.y / 2, this.position.x + this.size.x - 30, this.position.y + this.size.y / 2);
        applet.fill(state[7] ? on : off);
        applet.noStroke();
        applet.circle(this.position.x + this.size.x - 10, this.position.y + this.size.y - 10, 8);

    }

    public void draw() {


        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 255);
        applet.strokeWeight(1);
        applet.fill(this.color, 180);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);

        drawDisplay();
        applet.strokeWeight(2);
        applet.fill(255, 255);
        super.drawIO();


    }
}
