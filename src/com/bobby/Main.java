package com.bobby;

import com.bobby.nodes.Node;
import com.bobby.serializables.SerializableComponent;
import com.bobby.serializables.SerializableWire;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main extends PApplet {


    public String saveFileName = "savefile.dat";

    public MouseComponent mouseComponent;
    public Circuit masterCircuit;
    public Camera camera;
    PVector screenPos;
    PVector screenSize;
    int backgroundPatternSize = 128;
    int backgroundCornerRadius = 6;
    int backgroundGap = 1;

    ArrayList<Integer> IDs;

    PImage rPi;

    boolean debug = false;

    public boolean drawNodes = true;
    public boolean drawWires = true;

    public static void main(String[] args) {
        PApplet.main("com.bobby.Main");
    }

    public void settings(){
        size(1280, 720);
        noSmooth();
    }

    public void setup(){




        masterCircuit = new Circuit(this);
        mouseComponent = new MouseComponent(this, 0, 0);
        masterCircuit.mouse = mouseComponent;
        masterCircuit.addComponent(mouseComponent);
        this.camera = new Camera(this);
        this.camera.setZoom(1f);
        this.screenPos = new PVector(0,0);
        this.screenSize = new PVector(width - 1, height - 1);
        //thread("tickThread");
        surface.setResizable(true);
        rPi = loadImage("RPI.png");
        textSize(12);


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


        background(255);
        textAlign(LEFT, TOP);
        fill(255, 200,0);
        text("[DEBUG]", 5, 5);



        fill(255,0,0);
        image(rPi, mouseX, mouseY);



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
        masterCircuit.tick();

        this.screenSize.x = width;
        this.screenSize.y = height;
        //UPDATE AND TICK
        masterCircuit.update();

        //DRAW
        this.camera.begin();
        background(0);
        fill( 60);
        noStroke();

        background(50);
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
        fill(255,255,255);
        text("Write circuit with W, open it back up with O", 5, 85);

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

        System.out.println("Keycode: " + keyCode);
        if(keyCode == 39) {
            this.masterCircuit.logicGateInputs += 1;
        }else if(keyCode == 37){
            this.masterCircuit.logicGateInputs -= 1;
        }else if(keyCode == 68){
            this.debug = !this.debug;
        }else if(keyCode == 87){
            writeCircuitToFile(masterCircuit);
        }else if(keyCode == 78){
            drawNodes = !drawNodes;
        }else if(keyCode == 77){
            drawWires = !drawWires;
        }else if(keyCode == 79){
            Circuit c = readCircuitFromFile(saveFileName);
            if (c != null) {
                masterCircuit = c;
            }
        }
    }

    public void writeCircuitToFile(Circuit circuit){
        ArrayList<SerializableComponent> componentList = new ArrayList<>();
        for (Component c : circuit.sceneComponents) {

            if(c instanceof Wire){
                Wire w = (Wire)c;

                componentList.add(new SerializableWire(c.getClass().toString(), w.ID, (int)c.position.x, (int)c.position.y,
                        w.origin.parent.ID, indexOf(w.origin.parent.outputs, w.origin),
                        w.destination.parent.ID, indexOf(w.destination.parent.inputs, w.destination)));
            }else if(c instanceof Node){


                componentList.add(new SerializableComponent(c.getClass().toString(), c.ID, (int) c.position.x, (int) c.position.y));
            }
        }

        try {
            FileOutputStream f = new FileOutputStream(saveFileName);
            ObjectOutputStream os = new ObjectOutputStream(f);
            os.writeObject(componentList);
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> int indexOf(T[] arr, T obj){
        for (int i = 0; i < arr.length; i++){
            if(arr[i].equals(obj)){
                return i;
            }
        }
        return -1;
    }

    public Circuit readCircuitFromFile(String path) {

        Circuit cir = new Circuit(this);
        cir.mouse = mouseComponent;

        HashMap<String, Component> map = new HashMap<>();

        try {
            FileInputStream f = new FileInputStream(path);
            ObjectInputStream os = new ObjectInputStream(f);
            ArrayList<SerializableComponent> components = (ArrayList<SerializableComponent>) os.readObject();

            ArrayList<SerializableWire> wires = new ArrayList<>();

            for (SerializableComponent c: components) {
                if(c instanceof SerializableWire){
                    wires.add((SerializableWire)c);
                }
            }

            if (components != null) {
                for (SerializableComponent s : components) {

                    String className = s.name.split(" ")[1];

                    Class myClass;

                    System.out.println(className);

                    if(!className.contains("Mouse") && !className.contains("NodeIO") && !className.contains("Wire")) {

                        myClass = Class.forName(className);
                        Class[] args;
                        Component c;

                        if (className.startsWith("com.bobby.nodes.logic.compoundLogic")) {
                            args = new Class[]{PApplet.class, int.class, int.class};
                            c = (Component) myClass.getDeclaredConstructor(args).newInstance(this, s.x, s.y);
                        }
                        else if (className.startsWith("com.bobby.nodes.logic")){
                            args = new Class[]{PApplet.class, int.class, int.class, int.class};
                            c = (Component) myClass.getDeclaredConstructor(args).newInstance(this, s.x, s.y, 2);
                        }else {

                            args = new Class[]{PApplet.class, int.class, int.class};
                            c = (Component) myClass.getDeclaredConstructor(args).newInstance(this, s.x, s.y);
                        }

                        c.ID = s.ID;

                        if(c instanceof Node){
                            for (NodeIO io : ((Node)c).inputs) {
                                System.out.println("NODEIO: " + io);
                                cir.addComponent(io);
                            }
                            for (NodeIO io : ((Node)c).outputs) {
                                cir.addComponent(io);
                            }
                        }

                        if (c != null) {
                            cir.addComponent(c);
                            map.put(String.valueOf(c.ID), c);
                        }
                    }
                }
                cir.addComponent(mouseComponent);
            }
            //Now we'll add the wires last
            for (SerializableWire w : wires){
                Node origin = (Node) map.get(String.valueOf(w.OriginID));
                Node destination = (Node) map.get(String.valueOf(w.DestinationID));

                Wire wire = new Wire(this, origin.outputs[w.OriginNode], destination.inputs[w.DestinationNode]);
                wire.hasData = w.HasData;
                cir.addComponent(wire);
            }


        return cir;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
