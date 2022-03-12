package com.bobby.serializables;

import java.io.Serializable;

public class SerializableComponent implements Serializable {

    private static final long serialVersionUID = -558553967080513790L;
    public String name;
    public int x,y;
    public int ID;

    public SerializableComponent(String componentName, int ID, int x, int y){
        this.name = componentName;
        this.ID = ID;
        this.x = x;
        this.y = y;
    }
}
