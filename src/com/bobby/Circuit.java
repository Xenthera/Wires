package com.bobby;

import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Circuit {
    ArrayList<Component> sceneComponents, sceneComponentsReversed;
    MouseComponent mouse;
    PApplet app;

    String[] components;
    int curComponent;


    public Circuit(PApplet app, MouseComponent mouse){
        this.mouse = mouse;
        sceneComponents = new ArrayList<>();
        sceneComponentsReversed = new ArrayList<>();
        this.app = app;
        components = new String[]{"ToggleSwitch", "Switch","Light","logic.Buffer", "logic.And", "logic.Or", "logic.Not","logic.Nor","logic.Nand", "logic.Xor", "logic.compoundLogic.FullAdder", "logic.compoundLogic.SSD", "logic.compoundLogic.BCDToSSDDecoder"};

        int curComponent = 0;

    }

    public void addComponent(Component component){
        this.sceneComponents.add(component);
        this.sceneComponents.sort(Comparator.comparing(Component::getLayer));
        this.sceneComponentsReversed = (ArrayList<Component>) this.sceneComponents.clone();
        Collections.reverse(this.sceneComponentsReversed);
    }

    public void removeComponent(Component component){
        this.sceneComponents.remove(component);
        this.sceneComponents.sort(Comparator.comparing(Component::getLayer));
        this.sceneComponentsReversed = (ArrayList<Component>) this.sceneComponents.clone();
        Collections.reverse(this.sceneComponentsReversed);
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

        for (Component c : sceneComponentsReversed) {
            if(c.isHovered((int)mouse.position.x, (int)mouse.position.y)){
                c.hover();
                break;
            }
        }
        Main temp = (Main)app;
        for (Component c : sceneComponents) {
            if((c.position.x + c.getSize().x) - temp.camera.position.x >= temp.screenPos.x && (c.position.y + c.getSize().y) - temp.camera.position.y >= temp.screenPos.y) {
                if(c.position.x - temp.camera.position.x <= temp.screenPos.x + temp.screenSize.x && c.position.y - temp.camera.position.y <= temp.screenPos.y + temp.screenSize.y) {
                    c.draw();
                }
            }
        }

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

    public void mousePressed(int mouseX, int mouseY) {
        mouse.mousePressed(mouse, app.mouseButton);
        boolean hit = false;
        for (Component c : sceneComponentsReversed) {
            if(c.isGrabbable) {
                if (c.isHovered(mouseX, mouseY)) {
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
            }else if(c.isHovered(mouseX, mouseY)) {
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

        }else if(!hit && app.mouseButton == app.LEFT){
            mouse.isScrolling = true;
        }

    }

    public void mouseWheel(MouseEvent event){
        this.curComponent -= event.getCount();
        this.curComponent = app.constrain(this.curComponent, 0, this.components.length - 1);
    }


    public void mouseReleased(int mouseX, int mouseY) {

        mouse.mouseReleased(mouse);

        if(mouse.attachedComponent != null) {
            mouse.attachedComponent.mouseReleased(mouse);
            mouse.attachedComponent = null;
        }else{
            for (Component c : sceneComponentsReversed) {
                if(c.isHovered(mouseX, mouseY)) {
                    c.mouseReleased(mouse);
                    break;
                }
            }
        }

    }
}
