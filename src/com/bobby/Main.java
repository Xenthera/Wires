package com.bobby;

import com.bobby.math.HelperFunctions;
import com.bobby.nodes.*;
import com.bobby.nodes.logic.*;
import com.bobby.nodes.logic.compoundLogic.FullAdder;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Main extends PApplet {

    MouseComponent mouseComponent;
    public Circuit masterCircuit;
    public Camera camera;
    PVector screenPos;
    PVector screenSize;

    public static void main(String[] args) {
        PApplet.main("com.bobby.Main");
    }

    public void settings(){
        size(1280, 720);
    }

    public void setup(){
        mouseComponent = new MouseComponent(this, 0, 0);
        masterCircuit = new Circuit(this, mouseComponent);
        masterCircuit.addComponent(mouseComponent);
        this.camera = new Camera(this);

        this.screenPos = new PVector(0,0);
        this.screenSize = new PVector(width - 1, height - 1);
    }

    public void draw(){
        //UPDATE AND TICK
        masterCircuit.update();

        //DRAW
        this.camera.begin();
        background(0);
        fill( 60);
        noStroke();
        for (int i = (int)(this.screenPos.x + this.camera.position.x) / 41 - 1; i <= (this.screenSize.x + this.camera.position.x) / 41 + 5; i++) {
            for (int j = (int)(this.screenPos.y + this.camera.position.y) / 41 - 1; j <= (this.screenSize.y + this.camera.position.y) / 41 + 5; j++) {
                rect(i * 41, j * 41, 40, 40, 3);
            }
        }

        masterCircuit.draw();
        this.camera.end();

        textAlign(LEFT, TOP);
        fill(255, 200,0);
        int mouseXPos = this.mouseX + (int)this.camera.position.x;
        int mouseYPos = this.mouseY + (int)this.camera.position.y;
        text("Current Component: " + this.masterCircuit.components[this.masterCircuit.curComponent], 5, 5);
        text("Mouse Position: " + mouseXPos / 41 + ", " + mouseYPos / 41, 5, 25);

        //line(mouseX, mouseY - 5, mouseX, mouseY + 5);

        noFill();
        strokeWeight(1);
        stroke(0,255,0);
        rect(this.screenPos.x, this.screenPos.y, this.screenSize.x, this.screenSize.y);
    }

    @Override
    public void mousePressed() {
        masterCircuit.mousePressed(mouseX + (int)this.camera.position.x, mouseY + (int)this.camera.position.y);


    }

    @Override
    public void mouseReleased() {
        masterCircuit.mouseReleased(mouseX + (int)this.camera.position.x, mouseY + (int)this.camera.position.y);
    }

    public void mouseWheel(MouseEvent event){
        masterCircuit.mouseWheel(event);
    }

    public void keyPressed(){

    }
}
