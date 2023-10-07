package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Not extends LogicNode {

    int color;
    boolean inValue = false;


    public Not(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 1, 1,  1, 1, "NOT", app.color(255,20,20));
    }

    public void tick(){
        this.inValue = this.inputs[0].data > 0 ? true : false;

        int out = this.inValue ? 0 : 1;

        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(out);
        }
        super.tick();
    }
}
