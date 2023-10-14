//package com.bobby.nodes;
//
//import com.bobby.Component;
//import com.bobby.Main;
//import com.bobby.nodes.Node;
//
//import processing.core.PApplet;
//import processing.core.PImage;
//import processing.core.PVector;
//
//
//
//public class RaspberryPiWireless extends Node {
//
//    private PImage piImage;
//    private boolean isConnected;
//
//    private int[] pinout = {
//        29, -1,
//        28, 25,
//        27, 24,
//        -1, 23,
//        26, 22,
//        -1, 21,
//        -1, -1,
//        11, -1,
//        10, 14,
//         6, 13,
//        -1, 12,
//         5, -1,
//         4,  3,
//        -1,  2,
//         1,  0,
//        16, -1,
//        15, -1,
//        -1,  9,
//        -1,  8,
//        -1, -1
//    };
//
//
//    public RaspberryPiWireless(PApplet app, int x, int y) {
//        super(app, x, y, 312, 473,  26, 0); //40 pins, 26 actual
//
//        piImage = app.loadImage("RPI.png");
//
//
//
//            int c = 0;
//            for (int i = 0; i < 40; i++) {
//                if (this.pinout[i] != -1) {
//                    inputs[c].parentOffset.x = (i * 14) % 28 + 13;
//                    inputs[c].parentOffset.y = (i / 2) * 14.25f + 167;
//                    c += 1;
//                }
//            }
//
//
////            c = 0;
////            for (int i = 0; i < pinout.length; i++) {
////                if(pinout[i] != null) {
////                    outputs[c] = gpio.provisionDigitalOutputPin(pinout[i], String.valueOf(c), PinState.LOW);
////                    outputs[c].setShutdownOptions(true, PinState.LOW);
////                    c += 1;
////                }
////            }
//
//
//
//
//    }
//
//    public void destroy(){
//
//    }
//
//    public void tick(){
//
////            for (int i = 0; i < this.inputs.length; i++) {
////                //this.outputs[i].setState(this.inputs[i].data > 0 ? PinState.HIGH : PinState.LOW);
////                if(this.inputs[i].data > 0){
////                    this.outputs[i].high();
////                }else{
////                    this.outputs[i].low();
////                }
////            }
//
//    }
//
//    public void hover(){
//        applet.noFill();
//        applet.strokeWeight(2);
//        applet.stroke(255);
//        applet.rect(this.position.x - 2, this.position.y + 8, this.size.x + 4, this.size.y + 4);
//    }
//
//    public void draw() {
//        applet.noStroke();
//        applet.fill(0, 50);
//        applet.rect(this.position.x + 16, this.position.y + 16 + 10, this.size.x, this.size.y);
//        this.applet.image(this.piImage, this.position.x, this.position.y);
//
//        applet.fill(0, 100);
//        applet.stroke(0);
//        applet.strokeWeight(1);
//        for (int i = 0; i < 12; i++) {
//            applet.rect(this.position.x + this.size.x / 4 + i * 12, this.position.y + this.size.y / 2, 10,20);
//        }
//
//
//        applet.strokeWeight(2);
//        int c = 0;
//
//        for (int i = 0; i < 40; i++) {
//            if (this.pinout[i] != -1) {
//                if (this.inputs[c].wires.size() > 0) {
//                    applet.fill(0, 255, 0);
//                    applet.stroke(0);
//                } else {
//                    applet.fill(100,50,200, 200);
//                    applet.stroke(0);
//                }
//                applet.circle(this.position.x + (i * 14) % 28 + 13, (this.position.y + (i / 2) * 14.25f) + 167, 10); //Magic numbers to align with the PNG. (Terrible but oh well, i'll fix it later)
//                c += 1;
//            }
//        }
//
//    }
//
//    @Override
//    public PVector mousePressed(Component mouse, int button) {
//
//        System.out.println((mouse.position.x - this.position.x) + " " + (mouse.position.y - this.position.y));
//
//        return super.mousePressed(mouse, button);
//    }
//}
