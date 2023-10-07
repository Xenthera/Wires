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

    public ArrayList<Integer> IDs;

    ArrayList<Component> sceneComponents, sceneComponentsReversed, sceneComponentsUpdateOrder;
    public MouseComponent mouse;
    PApplet app;

    String[] components;
    int curComponent;
    public int logicGateInputs = 2;


    public Circuit(PApplet app){

        IDs = new ArrayList<>();

        sceneComponents = new ArrayList<>();
        sceneComponentsReversed = new ArrayList<>();
        sceneComponentsUpdateOrder = new ArrayList<>();
        this.app = app;
        components = new String[]{"ToggleSwitch", "Switch","Light","logic.Buffer", "logic.And", "logic.Or", "logic.Not","logic.Nor","logic.Nand", "logic.Xor", "logic.compoundLogic.FullAdder", "logic.compoundLogic.SSD", "logic.compoundLogic.BCDToSSDDecoder", "logic.compoundLogic.BinaryToHexSSDDecoder"};

        curComponent = 0;

    }

    public boolean RegisterID(int ID){
        if(!IDs.contains(ID)){
            System.out.println("Registering ID: " + ID);
            IDs.add(ID);
            return true;
        }
        return false;
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
            if(c.parent != null) {
                if (c instanceof Node) {
                    int snappedMouseX = Math.round((mouse.position.x - (Main.GridSize / 2)) / Main.GridSize) * Main.GridSize;
                    int snappedMouseY = Math.round((mouse.position.y - (Main.GridSize / 2)) / Main.GridSize) * Main.GridSize;
                    int snappedParentOffsetX = Math.round((c.parentOffset.x - (Main.GridSize / 2)) / Main.GridSize) * Main.GridSize;
                    int snappedParentOffsetY = Math.round((c.parentOffset.y - (Main.GridSize / 2)) / Main.GridSize) * Main.GridSize;
                    c.position.set(PVector.add(new PVector(snappedMouseX, snappedMouseY), new PVector(snappedParentOffsetX + Main.GridSize, snappedParentOffsetY + Main.GridSize)));
                }else{
                    c.position.set(PVector.add(c.parent.position, c.parentOffset));
                }
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
                if(mouse.selectedComponents.size() > 0 && c.isHovered(mouseX, mouseY)){
                    for (Component com :
                            mouse.selectedComponents) {
                        com.parent = mouse;
                        com.parentOffset = com.mousePressed(mouse, app.mouseButton);
                    }
                    hit = true;
                }
                else if (c.isHovered(mouseX, mouseY)) {
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

            if(app.keyPressed){
                System.out.println("Key was held during right click");
                if(app.keyCode == 16){
                    mouse.isSelecting = true;
                }
            }else {

                mouse.isScrolling = true;
            }
        }else if(!hit && app.mouseButton == app.RIGHT){
            if(mouse.selectedComponents.size() > 0){
                mouse.clearSelection();
            }else {

                try {
                    String className = "com.bobby.nodes." + this.components[this.curComponent];
                    Class myClass = Class.forName(className);
                    Class[] args;
                    Component c;

                    int snappedMouseX = Math.round((mouse.position.x - (Main.GridSize / 2)) / Main.GridSize) * Main.GridSize;
                    int snappedMouseY = Math.round((mouse.position.y - (Main.GridSize / 2)) / Main.GridSize) * Main.GridSize;

                    if (className.startsWith("com.bobby.nodes.logic") && !className.startsWith("com.bobby.nodes.logic.compoundLogic")) {
                        args = new Class[]{PApplet.class, int.class, int.class, int.class};
                        c = (Component) myClass.getDeclaredConstructor(args).newInstance(this.app, snappedMouseX, snappedMouseY, this.logicGateInputs >= 2 ? this.logicGateInputs : 2);
                    } else {
                        args = new Class[]{PApplet.class, int.class, int.class};
                        c = (Component) myClass.getDeclaredConstructor(args).newInstance(this.app, snappedMouseX, snappedMouseY);
                    }
                    if (c instanceof Node) {
                        //((Node) c).position.sub(((Node) c).size.x / 2, ((Node) c).size.y / 2);
                    }
                    this.addComponent(c);

                } catch (ClassNotFoundException | InstantiationException | InvocationTargetException |
                         NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
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
                }else if(mouse.selectedComponents.size() > 0){
                    for (Component comp:
                         mouse.selectedComponents) {
                        comp.mouseReleased(mouse);
                    }
                    mouse.mouseReleased(c);
                }
            }
        }

        mouse.mouseReleased(mouse);
    }

}
