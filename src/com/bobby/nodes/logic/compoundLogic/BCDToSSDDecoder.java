package com.bobby.nodes.logic.compoundLogic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class BCDToSSDDecoder extends Node {

    int color;
    int[][] lookUp;

    public BCDToSSDDecoder(PApplet app, int x, int y) {
        super(app, x, y, 55, 120, 2, 4, 7);
        color = app.color(0,0,0, 200);
        lookUp = new int[][]{{1,1,1,1,1,1,0},{0,1,1,0,0,0,0},{1,1,0,1,1,0,1},{1,1,1,1,0,0,1},
                            {0,1,1,0,0,1,1},{1,0,1,1,0,1,1},{1,0,1,1,1,1,1},{1,1,1,0,0,0,0},
                            {1,1,1,1,1,1,1},{1,1,1,1,0,1,1},};
        this.tickDelay = 0;
    }

    public void tick(){
        boolean a = this.inputs[0].data > 0 ? true : false;
        boolean b = this.inputs[1].data > 0 ? true : false;
        boolean c = this.inputs[2].data > 0 ? true : false;
        boolean d = this.inputs[3].data > 0 ? true : false;

        String binaryString = "";
        binaryString += a ? "1":"0";
        binaryString += b ? "1":"0";
        binaryString += c ? "1":"0";
        binaryString += d ? "1":"0";
        int value = Integer.parseInt(binaryString, 2);
        if(value > 9) value = 9;

        for (int j = 0; j < lookUp[value].length; j++) {
            this.outputs[j].sendData(lookUp[value][j]);
        }


    }

    public void draw() {
        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 200);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y, this.radius);
        applet.fill(255, 255);
        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].wires.size() > 0) {
                applet.fill(0,255,0);
                applet.stroke(0);
            }else{
                applet.fill(255, 100);
                applet.stroke(0);
            }
            int stepSize = (int)this.size.y / (this.inputs.length + 1);
            applet.circle(this.position.x, this.position.y + (i + 1) * stepSize, 10);
        }

        for (int i = 0; i < this.outputs.length; i++) {
            if(this.outputs[i].wires.size() > 0) {
                applet.fill(0,255,0);
                applet.stroke(0);
            }else{
                applet.fill(255, 100);
                applet.stroke(0);
            }
            int stepSize = (int)this.size.y / (this.outputs.length + 1);
            applet.circle(this.position.x + this.size.x, this.position.y + (i + 1) * stepSize, 10);
        }
        applet.textAlign(applet.CENTER, applet.CENTER);
        applet.text("BCD\nTo\nSSD", this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
    }
}
