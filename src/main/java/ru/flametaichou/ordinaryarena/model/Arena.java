package ru.flametaichou.ordinaryarena.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "arena")
public class Arena {

    private String name;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;
    private int worldId;

    private List<ArenaPlayer> players = new ArrayList<ArenaPlayer>();

    public Arena() {

    }

    public Arena(String name, int x, int y, int z, int worldId) {
        this.name = name;
        this.worldId = worldId;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "world")
    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    @XmlAttribute(name = "x1")
    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    @XmlAttribute(name = "y1")
    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    @XmlAttribute(name = "z1")
    public int getZ1() {
        return z1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    @XmlAttribute(name = "x2")
    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    @XmlAttribute(name = "y2")
    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    @XmlAttribute(name = "z2")
    public int getZ2() {
        return z2;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    @XmlElement(name = "player")
    public List<ArenaPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<ArenaPlayer> players) {
        this.players = players;
    }
}
