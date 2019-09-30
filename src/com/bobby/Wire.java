package com.bobby;

import com.bobby.math.HelperFunctions;
import processing.core.PApplet;
import processing.core.PVector;

public class Wire extends Component {

    NodeIO origin, destination;
    boolean hasData = false;

    public Wire(PApplet app, NodeIO origin, NodeIO destination) {
        //All wires will physically be at 0,0 but visually represented between two nodeIOs
        super(app, 0,0);
        this.origin = origin;
        this.destination = destination;

        this.origin.addWire(this);
        this.destination.addWire(this);
    }

    @Override
    public void tick() {
        if(origin.data != 0){
            this.hasData = true;
        }else{
            this.hasData = false;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        int x = (int)origin.position.x;
        int y = (int)origin.position.y;
        int x2 = (int)destination.position.x;
        int y2 = (int)destination.position.y;
        applet.noFill();
        applet.strokeWeight(8);
        //applet.stroke(0,0,0, 100);
        //applet.bezier(x, y, x + 100, y, x2 - 100,y2,x2,y2);
        applet.strokeWeight(2);

        int dis = applet.abs(x - x2);


        if(hasData) {
            applet.stroke(0, 255, 0);
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);
        }else{
            applet.stroke(0,0,0);
            applet.bezier(x + 5, y, x + dis/2, y, x2 - dis/2,y2,x2 - 5,y2);
        }

        float halfX = (x + x2) / 2;
        float halfY = (y + y2) / 2;

        applet.fill(0, 100);
        applet.stroke(0, 160);
        applet.circle(halfX, halfY, 10);

    }

    @Override
    public void hover() {
        int x = (int)origin.position.x;
        int y = (int)origin.position.y;
        int x2 = (int)destination.position.x;
        int y2 = (int)destination.position.y;
        int dis = applet.abs(x - x2);
        applet.noFill();
        if(hasData) {
            applet.stroke(0);
            applet.strokeWeight(7);
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);
        }else{
            applet.strokeWeight(5);
            applet.stroke(255);
            applet.bezier(x + 5, y, x + dis/2, y, x2 - dis/2,y2,x2 - 5,y2);
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        float halfX = (origin.position.x + destination.position.x) / 2;
        float halfY = (origin.position.y + destination.position.y) / 2;
        float dis = applet.sqrt(applet.pow(halfX - mouseX, 2) + applet.pow(halfY - mouseY, 2));
        return dis <= 10;
    }

    @Override
    public PVector mousePressed(MouseComponent mouse, int button) {
        return null;
    }

    @Override
    public void mouseReleased(MouseComponent mouse) {

    }

    public void sendValue(int value){
        this.destination.receiveData(value);
    }

    public void destroy(){
        this.destination.removeWire(this);
        this.origin.removeWire(this);
        this.origin = null;
        this.destination = null;
    }
}
