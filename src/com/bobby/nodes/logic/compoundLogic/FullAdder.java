package com.bobby.nodes.logic.compoundLogic;

import com.bobby.nodes.Node;
import com.bobby.nodes.logic.LogicNode;
import processing.core.PApplet;

public class FullAdder extends LogicNode {

    int color;


    public FullAdder(PApplet app, int x, int y) {
        super(app, x, y, 2, 3,  3, 2, "Full \nAdder", app.color(0,100,255));
    }

    public void tick(){
        boolean a = this.inputs[0].data > 0 ? true : false;
        boolean b = this.inputs[1].data > 0 ? true : false;
        boolean c = this.inputs[2].data > 0 ? true : false;
        boolean sum = c ^ (a ^ b);
        boolean carry = (a && b) || (b && c) || (a && c);

        if(this.tickDelayCounter == 0) {
            this.outputs[0].sendData(sum ? 1 : 0);
            this.outputs[1].sendData(carry ? 1 : 0);
        }
        super.tick();
    }


}
