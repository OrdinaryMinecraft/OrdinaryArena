package ru.flametaichou.ordinaryarena.model;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "player")
public class ArenaPlayer {

    private String name;
    private double x;
    private double y;
    private double z;
    private int worldId;

    public ArenaPlayer() {

    }

    public ArenaPlayer(String name, double x, double y, double z, int worldId) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldId = worldId;
    }

    @XmlAttribute(name = "name")

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "x")
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @XmlAttribute(name = "y")
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @XmlAttribute(name = "z")
    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @XmlAttribute(name = "world")
    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
}
