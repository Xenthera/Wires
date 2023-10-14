package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Nor extends LogicNode {

    int color;
    boolean inValue = false;

    public Nor(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 1, 1,  numInputs, 1, "NOR", app.color(50,67,255));
    }

    public void tick(){
        this.inValue = false;
        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].data >= 1){
                this.inValue = true;
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
