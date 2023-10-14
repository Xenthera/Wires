package com.bobby.nodes.logic;

import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Xor extends LogicNode {

    int color;
    boolean inValue = false;

    public Xor(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 1, 1,  2, 1,"XOR", app.color(234,50,255));
    }

    public void tick(){
        this.inValue = this.inputs[0].data > 0 ^ this.inputs[1].data > 0 ? true : false;

        int out = this.inValue ? 1 : 0;
        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(out);
        }
        super.tick();
    }
}
