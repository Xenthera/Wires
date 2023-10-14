package com.bobby;


import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class Wire extends Component {

    NodeIO origin, destination;
    boolean hasData = false;

    public Wire(PApplet app, NodeIO origin, NodeIO destination) {
        //All wires will physically be at 0,0 but visually represented between two nodeIOs
        super(app, 0,0);
        this.origin = origin;
        this.destination = destination;
        this.drawLayer = -1;
        this.updateLayer = 3;
        this.origin.addWire(this);
        this.destination.addWire(this);
    }

    public PVector getSize(){
        return new PVector(PApplet.abs(this.origin.position.x - this.destination.position.x), PApplet.abs(this.origin.position.y - this.destination.position.y));
    }

    @Override
    public void tick() {
        if(origin != null) {
            if (origin.data != 0) {
                this.hasData = true;
            } else {
                this.hasData = false;
            }
        }
    }

    @Override
    public void update() {
        this.position.x = this.origin.position.x < this.destination.position.x ? this.origin.position.x : this.destination.position.x;
        this.position.y = this.origin.position.y < this.destination.position.y ? this.origin.position.y : this.destination.position.y;
    }

    @Override
    public void draw() {
        int x = (int)origin.position.x;
        int y = (int)origin.position.y;
        int x2 = (int)destination.position.x;
        int y2 = (int)destination.position.y;
        applet.noFill();

        //applet.stroke(0,0,0, 100);
        //applet.bezier(x, y, x + 100, y, x2 - 100,y2,x2,y2);
        applet.strokeWeight(2);

        int dis = applet.abs(x - x2);

        if(hasData) {
            applet.stroke(100, 100, 255, 60);
        }else{
            applet.stroke(0,0,0, 60);
        }


        if(((Main)applet).drawWires)
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);
        else
            applet.line(x,y, x2, y2);
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
            applet.strokeWeight(3);
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);
            //applet.line(x,y, x2, y2);
        }else{
            applet.strokeWeight(3);
            applet.stroke(255);
            applet.bezier(x, y, x + dis/2, y, x2 - dis/2,y2,x2,y2);
            //applet.line(x,y, x2, y2);
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        int x = (int)origin.position.x;
        int y = (int)origin.position.y;
        int x2 = (int)destination.position.x;
        int y2 = (int)destination.position.y;
        int dis = applet.abs(x - x2);
        PVector p1 = new PVector(x + dis/2, y);
        PVector p2 = new PVector(x2- dis/2, y2);
        float[] pos = this.bboxBezierSimple(this.origin.position, p1, p2, this.destination.position);
//        applet.noFill();
//        applet.stroke(255,255,0);
//        applet.rect(pos[0] - 5,pos[1] - 5,pos[2] + 10,pos[3] + 10);
        float r = Float.MAX_VALUE;
        if(mouseX >= pos[0] - 5 && mouseX <= pos[2] + pos[0] + 10  && mouseY >= pos[1] - 5  && mouseY <= pos[3] + pos[1] + 10) {
            pos = bboxBezier(this.origin.position, p1, p2, this.destination.position);
//            applet.stroke(255);
//            applet.rect(pos[0] - 5,pos[1] - 5,pos[2] + 10,pos[3] + 10);
            if (mouseX >= pos[0] - 5 && mouseX <= pos[2] + pos[0] + 10  && mouseY >= pos[1] - 5  && mouseY <= pos[3] + pos[1] + 10) {
                float dis2 = PVector.dist(this.origin.position, this.destination.position);
                r = (float) getClosestPointToCubicBezier(mouseX, mouseY, (int) (dis2 + 200) / 20, this.origin.position.x, this.origin.position.y, p1.x, p1.y, p2.x, p2.y, this.destination.position.x, this.destination.position.y);
            }
        }

        return r <= 10;
    }

    @Override
    public PVector mousePressed(Component mouse, int button) {
        return null;
    }

    @Override
    public void mouseReleased(Component mouse) {

    }

    public void sendValue(int value){
        this.destination.receiveData(value);
    }

    public void remove(){

    }

    public void destroy(){
        this.destination.removeWire(this);
        this.origin.removeWire(this);
        this.origin = null;
        this.destination = null;
    }

    //Math

    float[] bboxBezierSimple(PVector p0, PVector p1, PVector p2, PVector p3){
        PVector mi = new PVector(0,0);
        mi.x = applet.min(applet.min(p0.x,p1.x),applet.min(p2.x,p3.x));
        mi.y = applet.min(applet.min(p0.y,p1.y),applet.min(p2.y,p3.y));
        PVector ma = new PVector(0,0);
        ma.x = applet.max(applet.max(p0.x,p1.x),applet.max(p2.x,p3.x));
        ma.y = applet.max(applet.max(p0.y,p1.y),applet.max(p2.y,p3.y));

        return new float[]{mi.x, mi.y, ma.x - mi.x, ma.y - mi.y};
    }

    float[] bboxBezier(PVector p0, PVector p1, PVector p2, PVector p3 )
    {
        PVector mi = new PVector(applet.min(p0.x,p3.x), applet.min(p0.y, p3.y));

        PVector ma = new PVector(applet.max(p0.x,p3.x), applet.max(p0.y, p3.y));

        PVector c = PVector.add(PVector.mult(p0, -1), PVector.mult(p1, 1));
        PVector b =  PVector.add(PVector.sub(PVector.mult(p0, 1), PVector.mult(p1, 2)), PVector.mult(p2, 1));
        PVector a = PVector.add(PVector.sub(PVector.add(PVector.mult(p0, -1), PVector.mult(p1, 3)), PVector.mult(p2, 3)), PVector.mult(p3, 1));

        PVector h = new PVector(b.x*b.x - a.x*c.x, b.y*b.y - a.y*c.y);

        if( h.x > 0.0 )
        {
            h.x = applet.sqrt(h.x);
            float t = (-b.x - h.x)/a.x;
            if( t>0.0 && t<1.0 )
            {
                float s = 1.0f-t;
                float q = s*s*s*p0.x + 3.0f*s*s*t*p1.x + 3.0f*s*t*t*p2.x + t*t*t*p3.x;
                mi.x = applet.min(mi.x,q);
                ma.x = applet.max(ma.x,q);
            }
            t = (-b.x + h.x)/a.x;
            if( t>0.0 && t<1.0 )
            {
                float s = 1.0f-t;
                float q = s*s*s*p0.x + 3.0f*s*s*t*p1.x + 3.0f*s*t*t*p2.x + t*t*t*p3.x;
                mi.x = applet.min(mi.x,q);
                ma.x = applet.max(ma.x,q);
            }
        }

        if( h.y>0.0 )
        {
            h.y = applet.sqrt(h.y);
            float t = (-b.y - h.y)/a.y;
            if( t>0.0 && t<1.0 )
            {
                float s = 1.0f-t;
                float q = s*s*s*p0.y + 3.0f*s*s*t*p1.y + 3.0f*s*t*t*p2.y + t*t*t*p3.y;
                mi.y = applet.min(mi.y,q);
                ma.y = applet.max(ma.y,q);
            }
            t = (-b.y + h.y)/a.y;
            if( t>0.0 && t<1.0 )
            {
                float s = 1.0f-t;
                float q = s*s*s*p0.y + 3.0f*s*s*t*p1.y + 3.0f*s*t*t*p2.y + t*t*t*p3.y;
                mi.y = applet.min(mi.y,q);
                ma.y = applet.max(ma.y,q);
            }
        }

        return new float[]{mi.x, mi.y, ma.x - mi.x, ma.y - mi.y};
    }


    public double getClosestPointToCubicBezier(double fx, double fy, int slices, double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3)  {
        double tick = 1d / (double) slices;
        double x;
        double y;
        double t;
        double best = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        double currentDistance;

        for (int i = 0; i <= slices; i++) {
            t = i * tick;
            //B(t) = (1-t)**3 p0 + 3(1 - t)**2 t P1 + 3(1-t)t**2 P2 + t**3 P3
            x = (1 - t) * (1 - t) * (1 - t) * x0 + 3 * (1 - t) * (1 - t) * t * x1 + 3 * (1 - t) * t * t * x2 + t * t * t * x3;
            y = (1 - t) * (1 - t) * (1 - t) * y0 + 3 * (1 - t) * (1 - t) * t * y1 + 3 * (1 - t) * t * t * y2 + t * t * t * y3;

            currentDistance = Point.distance((float) x, (float) y, (float) fx, (float) fy);

            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                best = t;
            }
        }
        return bestDistance;
    }
}
