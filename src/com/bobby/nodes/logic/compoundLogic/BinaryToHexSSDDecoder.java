package com.bobby.nodes.logic.compoundLogic;

import com.bobby.Main;
import com.bobby.nodes.Node;
import com.bobby.nodes.logic.LogicNode;
import processing.core.PApplet;

public class BinaryToHexSSDDecoder extends LogicNode {

    int color;
    int[][] lookUp;

    public BinaryToHexSSDDecoder(PApplet app, int x, int y) {
        super(app, x, y, 2, 4,  4, 7,"Binary\nTo\nHex\nSSD", app.color(0,0,0, 200));
        lookUp = new int[][]{{1,1,1,1,1,1,0}, {0,1,1,0,0,0,0}, {1,1,0,1,1,0,1}, {1,1,1,1,0,0,1},
                             {0,1,1,0,0,1,1}, {1,0,1,1,0,1,1}, {1,0,1,1,1,1,1}, {1,1,1,0,0,0,0},
                             {1,1,1,1,1,1,1}, {1,1,1,1,0,1,1}, {1,1,1,0,1,1,1}, {0,0,1,1,1,1,1},
                             {1,0,0,1,1,1,0}, {0,1,1,1,1,0,1}, {1,0,0,1,1,1,1}, {1,0,0,0,1,1,1}};
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

        for (int j = 0; j < lookUp[value].length; j++) {
            this.outputs[j].sendData(lookUp[value][j]);
        }


    }

    public void draw() {
        applet.noStroke();

        applet.stroke(applet.red(this.color), applet.green(this.color), applet.blue(this.color), 200);
        applet.strokeWeight(2);
        applet.fill(this.color);
        applet.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        applet.fill(255, 255);
        super.drawIO();
    }
}
