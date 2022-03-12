package com.bobby;

import com.bobby.nodes.Node;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;

public class Circuit {
    ArrayList<Component> sceneComponents, sceneComponentsReversed, sceneComponentsUpdateOrder;
    MouseComponent mouse;
    PApplet app;

    String[] components;
    int curComponent;
    public int logicGateInputs = 2;


    public Circuit(PApplet app, MouseComponent mouse){
        this.mouse = mouse;
        sceneComponents = new ArrayList<>();
        sceneComponentsReversed = new ArrayList<>();
        sceneComponentsUpdateOrder = new ArrayList<>();
        this.app = app;
        components = new String[]{"ToggleSwitch", "Switch","Light","logic.Buffer", "logic.And", "logic.Or", "logic.Not","logic.Nor","logic.Nand", "logic.Xor", "logic.compoundLogic.FullAdder", "logic.compoundLogic.SSD", "logic.compoundLogic.BCDToSSDDecoder", "logic.compoundLogic.BinaryToHexSSDDecoder", "RaspberryPi", "RaspberryPiWireless"};

        curComponent = 0;

    }

    public void addComponent(Component component){
        this.sceneComponents.add(component);
        this.sceneComponents.sort(Comparator.comparing(Component::getDrawLayer));
        this.sceneComponentsReversed = (ArrayList<Component>) this.sceneComponents.clone();
        this.sceneComponentsUpdateOrder = (ArrayList<Component>) this.sceneComponents.clone();
        this.sceneComponentsUpdateOrder.sort(Comparator.comparing(Component::getUpdateLayer));
        Collections.reverse(this.sceneComponentsReversed);
        for (Component c :
                this.sceneComponentsUpdateOrder) {
            //System.out.print(c instanceof MouseComponent ? "" : c + " ");
        }
        //System.out.println();
    }

    public void removeComponent(Component component){
        component.remove();
        this.sceneComponents.remove(component);
        this.sceneComponents.sort(Comparator.comparing(Component::getDrawLayer));
        this.sceneComponentsReversed = (ArrayList<Component>) this.sceneComponents.clone();
        Collections.reverse(this.sceneComponentsReversed);
    }

    public void tick(){
        for (Component c : sceneComponentsUpdateOrder) {
            try {
                c.tick();
            }catch (ConcurrentModificationException e){
                //Oh well keep going.
            }
        }
    }

    public void update(){
        for (Component c : sceneComponents) {
            if(c.parent != null){
                c.position.set(PVector.add(c.parent.position, c.parentOffset));
            }
            c.update();
        }
    }

    public void draw(){


        Main temp = (Main)app;
        for (Component c : sceneComponents) {
            if((c.position.x + c.getSize().x) - temp.camera.position.x >= temp.screenPos.x && (c.position.y + c.getSize().y) - temp.camera.position.y >= temp.screenPos.y) {
                if(c.position.x - temp.camera.position.x <= temp.screenPos.x + temp.screenSize.x && c.position.y - temp.camera.position.y <= temp.screenPos.y + temp.screenSize.y) {
                    c.draw();
                }
            }
        }

        for (Component c : sceneComponentsReversed) {
            if(c.isHovered((int)mouse.position.x, (int)mouse.position.y)){
                c.hover();
                break;
            }
        }

    }

    private void deleteNode(Node node){
        Node ref = node;

        for(int i = ref.inputs.length - 1; i >= 0; i--){
            NodeIO nodeio = node.inputs[i];
            if(nodeio != null) {
                for (int j = nodeio.wires.size() - 1; j >= 0; j--) {
                    Wire wire = nodeio.wires.get(j);
                    nodeio.removeWire(wire);
                    wire.destroy();
                    this.removeComponent(wire);
                }
                this.removeComponent(nodeio);
            }
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
        //mouse.mousePressed(mouse, app.mouseButton);
        boolean hit = false;
        for (Component c : sceneComponentsReversed) {
            if(app.keyPressed && app.keyCode == 16 && c instanceof Node){
                if(c.isHovered(mouseX, mouseY)) {
                    mouse.mousePressed(c, app.mouseButton);
                    break;
                }
            }else if(c.isGrabbable) {
                if (c.isHovered(mouseX, mouseY)) {
                    if(app.mouseButton == app.LEFT) {
                        mouse.attachedComponent = c;
                        c.parent = mouse;
                    }
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
                mouse.mousePressed(c, app.mouseButton);
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

        if(!hit && app.mouseButton == app.LEFT){
            try{
                String className = "com.bobby.nodes." + this.components[this.curComponent];
                Class myClass = Class.forName(className);
                Class[] args;
                Component c;
                if(className.startsWith("com.bobby.nodes.logic") && !className.startsWith("com.bobby.nodes.logic.compoundLogic")) {
                    args = new Class[]{PApplet.class, int.class, int.class, int.class};
                    c = (Component)myClass.getDeclaredConstructor(args).newInstance(this.app, (int)mouse.position.x, (int)mouse.position.y, this.logicGateInputs >= 2 ? this.logicGateInputs : 2);
                }else{
                    args = new Class[]{PApplet.class, int.class, int.class};
                    c = (Component)myClass.getDeclaredConstructor(args).newInstance(this.app, (int)mouse.position.x, (int)mouse.position.y);
                }
                if(c instanceof Node){
                    ((Node)c).position.sub(((Node)c).size.x / 2, ((Node)c).size.y / 2);
                }
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

        }else if(!hit && app.mouseButton == app.RIGHT){
            mouse.isScrolling = true;
        }

    }

    public void mouseWheel(MouseEvent event){
        this.curComponent -= event.getCount();
        this.curComponent = app.constrain(this.curComponent, 0, this.components.length - 1);
    }


    public void mouseReleased(int mouseX, int mouseY) {



        if(mouse.attachedComponent != null) {
            mouse.attachedComponent.mouseReleased(mouse);
            mouse.attachedComponent = null;
        }else{
            for (Component c : sceneComponentsReversed) {
                if(c.isHovered(mouseX, mouseY)) {
                    mouse.mouseReleased(c);
                    break;
                }
            }
        }

        mouse.mouseReleased(mouse);
    }

}
