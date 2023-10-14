package com.bobby.serializables;

public class SerializableWire extends SerializableComponent {

    public int OriginID, OriginNode;
    public int DestinationID, DestinationNode;
    public boolean HasData;
    public int ID;

    public SerializableWire(String componentName, int ID, int x, int y, int originID, int originNode, int destinationID, int destinationNode) {
        super(componentName, ID, x, y);

        this.OriginID = originID;
        this.OriginNode = originNode;
        this.DestinationID = destinationID;
        this.DestinationNode = destinationNode;
    }
}
