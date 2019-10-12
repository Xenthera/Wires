package com.bobby;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import java.awt.*;


public class Main extends PApplet {

    MouseComponent mouseComponent;
    public Circuit masterCircuit;
    public Camera camera;
    PVector screenPos;
    PVector screenSize;
    int backgroundPatternSize = 128;
    int backgroundCornerRadius = 6;
    int backgroundGap = 1;

    boolean debug = false;

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
        this.camera.setZoom(1f);
        this.screenPos = new PVector(0,0);
        this.screenSize = new PVector(width - 1, height - 1);
        thread("tickThread");
        surface.setResizable(true);
    }

    public void tickThread(){
        //Minecraft's update loop apparently :P
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while(true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                masterCircuit.tick();
                delta--;
            }
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }

    }
    public void draw_alternate(){
        float t = millis();


        background(0);
        textAlign(LEFT, TOP);
        fill(255, 200,0);
        text("[DEBUG]", 5, 5);
        noFill();

        stroke(100,0,200);
        fill(255);
        circle(mouseX, mouseY, 20);



//        if (keyPressed) {
//            if(keyCode == 49){
//                p1.set(mouseX, mouseY);
//            }else if(keyCode == 50){
//                p4.set(mouseX, mouseY);
//            }
//        }
//        noFill();
//        float r = 10000000;
//        float[] pos = bboxBezierSimple(p1, p2, p3, p4);
//        if(mouseX >= pos[0]  && mouseX <= pos[2] + pos[0]  && mouseY >= pos[1]  && mouseY <= pos[3] + pos[1]) {
//            stroke(255, 100, 100);
//            strokeWeight(1);
//            rect(pos[0], pos[1], pos[2], pos[3]);
//
//
//            stroke(255, 200, 100);
//            pos = bboxBezier(p1, p2, p3, p4);
//            if (mouseX >= pos[0] && mouseX <= pos[2] + pos[0] && mouseY >= pos[1] && mouseY <= pos[3] + pos[1]) {
//                float dis = PVector.dist(p1, p4);
//
//                r = (float) getClosestPointToCubicBezier(mouseX, mouseY, (int) (dis + 200) / 20, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
//
//                stroke(150);
//                circle(mouseX, mouseY, r * 2);
//                stroke(255, 200, 100);
//            } else {
//                stroke(100);
//            }
//        }
//        strokeWeight(1);
//        rect(pos[0],pos[1],pos[2],pos[3]);
//        stroke(100,0,255);
//        circle(p2.x, p2.y, 10);
//        circle(p3.x, p3.y, 10);
//
//
//
//
//
//
//        stroke(r < 20 ? 255 : 100);
//        strokeWeight(3);
//        bezier(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
//        circle(p1.x, p1.y, 10);
//        circle(p4.x, p4.y, 10);
//        strokeWeight(1);
//        stroke(100,0,255, 100);
//        line(p1.x, p1.y, p2.x, p2.y);
//        line(p3.x, p3.y, p4.x, p4.y);
    }





    public void draw(){

        if(debug){
            draw_alternate();
            return;
        }


        this.screenSize.x = width;
        this.screenSize.y = height;
        //UPDATE AND TICK
        masterCircuit.update();

        //DRAW
        this.camera.begin();
        background(0);
        fill( 60);
        noStroke();

        background(0);
        for (int i = (int) (this.screenPos.x + this.camera.position.x) / (backgroundPatternSize + backgroundGap) - 1; i <= (this.screenSize.x + this.camera.position.x) / (backgroundPatternSize + backgroundGap) + 1; i++) {
            for (int j = (int) (this.screenPos.y + this.camera.position.y) / (backgroundPatternSize + backgroundGap) - 1; j <= (this.screenSize.y + this.camera.position.y) / (backgroundPatternSize + backgroundGap) + 1; j++) {
                rect(i * (backgroundPatternSize + backgroundGap), j * (backgroundPatternSize + backgroundGap), (backgroundPatternSize), (backgroundPatternSize), backgroundCornerRadius);
            }
        }

        masterCircuit.draw();
        noFill();
        strokeWeight(1);
        stroke(0,255,0);

        this.camera.end();

        textAlign(LEFT, TOP);
        fill(255, 200,0);
        int mouseXPos = this.mouseX + (int)this.camera.position.x;
        int mouseYPos = this.mouseY + (int)this.camera.position.y;
        text("Current Component: " + this.masterCircuit.components[this.masterCircuit.curComponent], 5, 5);
        text("Mouse Position: " + mouseXPos / 41 + ", " + mouseYPos / 41, 5, 25);
        text(frameRate, 5, 45);
        text("Logic gate inputs: " + this.masterCircuit.logicGateInputs, 5, 65);

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
        if(keyCode == 39) {
            this.masterCircuit.logicGateInputs += 1;
        }else if(keyCode == 37){
            this.masterCircuit.logicGateInputs -= 1;
        }else if(keyCode == 68){
            this.debug = !this.debug;
        }
    }
}
