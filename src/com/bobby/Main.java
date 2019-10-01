package com.bobby;

import com.bobby.math.HelperFunctions;
import com.bobby.nodes.*;
import com.bobby.nodes.logic.*;
import com.bobby.nodes.logic.compoundLogic.FullAdder;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Main extends PApplet {

    MouseComponent mouseComponent;
    public Circuit masterCircuit;


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
    }

    public void draw(){
        //UPDATE
        masterCircuit.update();
        //TICK

        //DRAW
        background(75);

        masterCircuit.draw();


        //line(mouseX, mouseY - 5, mouseX, mouseY + 5);


    }

    @Override
    public void mousePressed() {
        masterCircuit.mousePressed();


    }

    @Override
    public void mouseReleased() {
        masterCircuit.mouseReleased();
    }

    public void mouseWheel(MouseEvent event){
        masterCircuit.mouseWheel(event);
    }

    public void keyPressed(){

    }
}
