//package com.bobby.nodes;
//
//import com.bobby.Component;
//import com.bobby.Main;
//import com.pi4j.io.gpio.*;
//import processing.core.PApplet;
//import processing.core.PImage;
//import processing.core.PVector;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//
//public class RaspberryPi extends Node {
//
//    private PImage piImage;
//    private boolean isPi;
//
//    private Pin[] pinout = {
//        RaspiPin.GPIO_29, null,
//        RaspiPin.GPIO_28, RaspiPin.GPIO_25,
//        RaspiPin.GPIO_27, RaspiPin.GPIO_24,
//        null,             RaspiPin.GPIO_23,
//        RaspiPin.GPIO_26, RaspiPin.GPIO_22,
//        null,             RaspiPin.GPIO_21,
//        null,             null,
//        RaspiPin.GPIO_11, null,
//        RaspiPin.GPIO_10, RaspiPin.GPIO_14,
//        RaspiPin.GPIO_06, RaspiPin.GPIO_13,
//        null,             RaspiPin.GPIO_12,
//        RaspiPin.GPIO_05, null,
//        RaspiPin.GPIO_04, RaspiPin.GPIO_03,
//        null,             RaspiPin.GPIO_02,
//        RaspiPin.GPIO_01, RaspiPin.GPIO_00,
//        RaspiPin.GPIO_16, null,
//        RaspiPin.GPIO_15, null,
//        null,             RaspiPin.GPIO_09,
//        null,             RaspiPin.GPIO_08,
//        null,             null
//    };
//
//
//    GpioPinDigitalOutput[] outputs;
//    GpioController gpio;
//
//    public RaspberryPi(PApplet app, int x, int y) {
//        super(app, x, y, 312, 473, 15, 26, 0); //40 pins, 26 actual
//
//        piImage = app.loadImage("RPI.png");
//        outputs = new GpioPinDigitalOutput[26];
//
//        if(isRaspberryPi()) {
//            isPi = true;
//            int c = 0;
//            for (int i = 0; i < 40; i++) {
//                if (this.pinout[i] != null) {
//                    inputs[c].parentOffset.x = (i * 14) % 28 + 13;
//                    inputs[c].parentOffset.y = (i / 2) * 14.25f + 167;
//                    c += 1;
//                }
//            }
//
//            gpio = GpioFactory.getInstance();
////            c = 0;
////            for (int i = 0; i < pinout.length; i++) {
////                if(pinout[i] != null) {
////                    outputs[c] = gpio.provisionDigitalOutputPin(pinout[i], String.valueOf(c), PinState.LOW);
////                    outputs[c].setShutdownOptions(true, PinState.LOW);
////                    c += 1;
////                }
////            }
//
//            outputs[0] = gpio.provisionDigitalOutputPin(pinout[0], "Test 0", PinState.LOW);
//
//        }else{
//            isPi = false;
//            for (int i = 0; i < this.inputs.length; i++) {
//                ((Main)app).masterCircuit.removeComponent(this.inputs[i]);
//                this.inputs[i] = null;
//            }
//        }
//    }
//
//    public void destroy(){
//        if(isPi) {
//            gpio.shutdown();
//        }
//    }
//
//    public void tick(){
//        if(isPi) {
////            for (int i = 0; i < this.inputs.length; i++) {
////                //this.outputs[i].setState(this.inputs[i].data > 0 ? PinState.HIGH : PinState.LOW);
////                if(this.inputs[i].data > 0){
////                    this.outputs[i].high();
////                }else{
////                    this.outputs[i].low();
////                }
////            }
//            this.outputs[0].setState(this.inputs[0].data > 0 ? PinState.HIGH : PinState.LOW);
//        }
//    }
//
//    public void hover(){
//        applet.noFill();
//        applet.strokeWeight(2);
//        applet.stroke(255);
//        applet.rect(this.position.x - 2, this.position.y + 8, this.size.x + 4, this.size.y + 4, this.radius);
//    }
//
//    public void draw() {
//        applet.noStroke();
//        applet.fill(0, 50);
//        applet.rect(this.position.x + 16, this.position.y + 16 + 10, this.size.x, this.size.y, this.radius);
//        this.applet.image(this.piImage, this.position.x, this.position.y);
//
//
//        applet.strokeWeight(2);
//        int c = 0;
//        if(isPi) {
//            for (int i = 0; i < 40; i++) {
//                if (this.pinout[i] != null) {
//                    if (this.inputs[c].wires.size() > 0) {
//                        applet.fill(0, 255, 0);
//                        applet.stroke(0);
//                    } else {
//                        applet.fill(100,50,200, 200);
//                        applet.stroke(0);
//                    }
//                    applet.circle(this.position.x + (i * 14) % 28 + 13, (this.position.y + (i / 2) * 14.25f) + 167, 10); //Magic numbers to align with the PNG. (Terrible but oh well, i'll fix it later)
//                    c += 1;
//                }
//            }
//        }
//
//        if(!isPi){
//            applet.fill(0, 200);
//            applet.rect(this.position.x, this.position.y + 10, this.size.x, this.size.y, this.radius);
//            applet.fill(255);
//            applet.textAlign(applet.CENTER, applet.CENTER);
//            applet.textSize(25);
//            applet.text("Not running on a PI", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
//            applet.textSize(12);
//        }
//
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
//
//    private boolean isRaspberryPi(){
//        boolean isLinux = false;
//        final String os = System.getProperty("os.name").toLowerCase();
//        if (os.indexOf("linux") >= 0) {
//            isLinux = true;
//        }
//        if (isLinux) {
//            final File file = new File("/etc", "os-release");
//            try (FileInputStream fis = new FileInputStream(file);
//                 BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
//                String string;
//                while ((string = br.readLine()) != null) {
//                    if (string.toLowerCase().contains("raspbian")) {
//                        if (string.toLowerCase().contains("name")) {
//                            return true; // a pi.
//                        }
//                    }
//                }
//            } catch (final Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return false; // not a pi.
//    }
//}
