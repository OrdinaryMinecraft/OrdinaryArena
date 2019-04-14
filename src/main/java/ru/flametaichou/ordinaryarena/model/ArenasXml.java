package ru.flametaichou.ordinaryarena.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "arenas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArenasXml {

    @XmlElement(name = "arena")
    private List<Arena> arenas = new ArrayList<Arena>();

    public List<Arena> getArenas() {
        return arenas;
    }

    public void setArenas(List<Arena> arenas) {
        this.arenas = arenas;
    }
}
