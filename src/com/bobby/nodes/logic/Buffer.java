package com.bobby.nodes.logic;

import com.bobby.Main;
import com.bobby.nodes.Node;
import processing.core.PApplet;

public class Buffer extends LogicNode {

    int color;
    boolean inValue = false;

    public Buffer(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 1, 1,  1, 1, "", app.color(50));
        this.tickDelay = 0;
    }

    public void tick(){
        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(this.inputs[0].data);
        }
        super.tick();
    }


}
