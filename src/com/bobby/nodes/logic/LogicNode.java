package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class LogicNode extends Node {

    protected int color;
    protected String displayText;

    public LogicNode(PApplet app, int x, int y, int gridWidth, int gridHeight, int numInputs, int numOutputs, String text, int color) {
        super(app, x, y, gridWidth, gridHeight, numInputs, numOutputs);
        this.displayText = text;
        this.color = color;
    }


    public void draw(){
        applet.stroke(this.color);
        applet.strokeWeight(1);
        applet.fill(this.color, 180);
        applet.rect(this.position.x, this.position.y, this.size.x - 1, this.size.y - 1);
        super.drawIO();
        applet.fill(255);
        applet.textAlign(applet.CENTER, applet.CENTER);
        drawVerticalText(this.displayText, this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }

    void drawVerticalText(String text, float x, float y) {
        applet.pushMatrix();
        applet.translate(x, y);
        applet.rotate(applet.PI/2);
        applet.text(text, 0, 0);
        applet.popMatrix();
    }
}
