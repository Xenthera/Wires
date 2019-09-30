package com.bobby;

import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Circuit {
    ArrayList<Component> sceneComponents;
    MouseComponent mouse;
    PApplet app;

    String[] components;
    int curComponent;


    public Circuit(PApplet app, MouseComponent mouse){
        this.mouse = mouse;
        sceneComponents = new ArrayList<>();
        this.app = app;
        components = new String[]{"Switch","Light","logic.Buffer", "logic.And", "logic.Or", "logic.Not","logic.Nor","logic.Nand", "logic.Xor", "logic.compoundLogic.FullAdder", "logic.compoundLogic.SSD", "logic.compoundLogic.BCDToSSDDecoder"};

        int curComponent = 0;

    }

    public void addComponent(Component component){
        this.sceneComponents.add(component);
    }

    public void removeComponent(Component component){
        this.sceneComponents.remove(component);
    }

    public void tick(){
        for (Component c : sceneComponents) {
            c.tick();
        }
    }

    public void update(){
        ArrayList<Component> test = new ArrayList<>();

        for (Component c : sceneComponents) {

            if(c.parent != null){
                c.position.set(PVector.add(c.parent.position, c.parentOffset));
            }
            c.update();
            c.tick();
        }
    }

    public void draw(){
        for (Component c : sceneComponents) {
            if(c.isHovered((int)mouse.position.x, (int)mouse.position.y)){
                c.hover();
                break;
            }
        }
        for (Component c : sceneComponents) {
            c.draw();
        }
        app.textAlign(app.LEFT, app.TOP);
        app.fill(255, 200,0);
        app.text("Current Component: " + this.components[this.curComponent], 5, 5);


    }

    private void deleteNode(Node node){
        Node ref = node;

        for(int i = ref.inputs.length - 1; i >= 0; i--){
            NodeIO nodeio = node.inputs[i];
            for(int j = nodeio.wires.size() - 1; j >= 0; j--){
                Wire wire = nodeio.wires.get(j);
                nodeio.removeWire(wire);
                wire.destroy();
                this.removeComponent(wire);
            }
            this.removeComponent(nodeio);
        }

        for(int i = ref.outputs.length - 1; i >= 0; i--){
            NodeIO nodeio = node.outputs[i];
            for(int j = nodeio.wires.size() - 1; j >= 0; j--){
                Wire wire = nodeio.wires.get(j);
                nodeio.removeWire(wire);
                wire.destroy();
                this.removeComponent(wire);
            }
            this.removeComponent(nodeio);
        }
        this.removeComponent(node);
    }

    public void mousePressed() {
        mouse.mousePressed(mouse, app.mouseButton);
        boolean hit = false;
        for (Component c : sceneComponents) {
            if(c.isGrabbable) {
                if (c.isHovered((int) mouse.position.x, (int) mouse.position.y)) {
                    mouse.attachedComponent = c;
                    c.parentOffset = c.mousePressed(mouse, app.mouseButton);
                    hit = true;
                    if(app.mouseButton == app.CENTER){
                        if(c instanceof Node){
                            this.deleteNode((Node)c);
                        }
                    }
                    break;
                }
            }else if(c.isHovered((int) mouse.position.x, (int) mouse.position.y)) {
                c.mousePressed(mouse, app.mouseButton);
                hit = true;
                if(app.mouseButton == app.CENTER){
                    if(c instanceof Wire){
                        ((Wire) c).destroy();
                        this.removeComponent(c);
                    }
                }
                break;
            }


        }

        if(!hit && app.mouseButton == app.CENTER){
            try{
            Class myClass = Class.forName("com.bobby.nodes." + this.components[this.curComponent]);
            Class[] args = {PApplet.class, int.class, int.class};
            Component c = (Component)myClass.getDeclaredConstructor(args).newInstance(this.app, (int)mouse.position.x, (int)mouse.position.y);
            this.addComponent(c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    public void mouseWheel(MouseEvent event){
        this.curComponent -= event.getCount();
        this.curComponent = app.constrain(this.curComponent, 0, this.components.length - 1);
    }


    public void mouseReleased() {

        mouse.mouseReleased(mouse);

        if(mouse.attachedComponent != null) {
            mouse.attachedComponent.mouseReleased(mouse);
            mouse.attachedComponent = null;
        }else{
            for (Component c : sceneComponents) {
                if(c.isHovered((int) mouse.position.x, (int) mouse.position.y)) {
                    c.mouseReleased(mouse);
                    break;
                }
            }
        }

    }
}
