package com.bobby.nodes.logic;

import com.bobby.Main;
import com.bobby.nodes.Node;
import processing.core.PApplet;

public class And extends LogicNode {

    boolean inValue = false;

    public And(PApplet app, int x, int y, int numInputs) {
        super(app, x, y, 1, 1,  numInputs, 1, "AND", app.color(255,255,100));
        //this.tickDelay = 60;
    }

    public void tick(){
        this.inValue = true;
        for (int i = 0; i < this.inputs.length; i++) {
            if(this.inputs[i].data < 1){
                this.inValue = false;
                break;
            }
        }
        int out = this.inValue ? 1 : 0;

        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(out);
        }
        super.tick();
    }

}
