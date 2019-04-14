package ru.flametaichou.ordinaryarena.service;

import net.minecraft.entity.player.EntityPlayer;
import ru.flametaichou.ordinaryarena.model.Arena;
import ru.flametaichou.ordinaryarena.model.ArenaPlayer;
import ru.flametaichou.ordinaryarena.model.ArenasXml;

public interface IArenaService {

    void setLootListsXML(ArenasXml lootLists);
    ArenasXml getLootListsXML();
    Arena findByName(String name);
    Arena createArena(String name);
    void deleteArena(String name);
    void setPos1(Integer x, Integer y, Integer z, String name, Integer worldId);
    void setPos2(Integer x, Integer y, Integer z, String name, Integer worldId);
    void joinArena(EntityPlayer player, String name);
    void leaveArena(EntityPlayer player, Boolean withTeleport);
    boolean playerOnAnyArena(EntityPlayer player);
}
