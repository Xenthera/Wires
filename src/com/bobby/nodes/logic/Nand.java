package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Nand extends LogicNode {

    int color;
    boolean inValue = false;

    public Nand(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 1, 1,  numInputs, 1, "NAND", app.color(255,255,100));
    }

    public void tick(){
        this.inValue = true;
        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].data < 1){
                this.inValue = false;
                break;
            }
        }
        int out = this.inValue ? 0 : 1;
        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(out);
        }
        super.tick();
    }
}
