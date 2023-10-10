package com.bobby.nodes.logic;

import com.bobby.Main;
import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PImage;

public class LogicNode extends Node {

    protected int color;
    protected String displayText;

    protected PImage img;

    public LogicNode(PApplet app, int x, int y, int gridWidth, int gridHeight, int numInputs, int numOutputs, String text, int color) {
        super(app, x, y, gridWidth, gridHeight, numInputs, numOutputs);
        this.displayText = text;
        this.color = color;
        img = app.loadImage("Node.png");
    }


    public void draw(){
        applet.stroke(this.color);
        applet.strokeWeight(2);
        applet.noFill();
        applet.tint(this.color, 180);
        for (int i = 0; i < this.size.x / Main.GridSize; i++) {
            for (int j = 0; j < this.size.y / Main.GridSize; j++) {
                applet.image(img, this.position.x + (i * Main.GridSize), this.position.y + (j * Main.GridSize));
            }
        }
        applet.rect(this.position.x, this.position.y, this.size.x - 1, this.size.y - 1);

        super.drawIO();
        applet.fill(255);
        applet.textAlign(applet.CENTER, applet.CENTER);
        drawVerticalText(this.displayText, this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
        applet.tint(255);
    }

    void drawVerticalText(String text, float x, float y) {
        applet.pushMatrix();
        applet.translate(x, y);
        applet.rotate(applet.PI/2);
        applet.text(text, 0, 0);
        applet.popMatrix();
    }
}
