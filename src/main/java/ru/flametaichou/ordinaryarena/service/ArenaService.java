package ru.flametaichou.ordinaryarena.service;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import ru.flametaichou.ordinaryarena.OrdinaryArenaBase;
import ru.flametaichou.ordinaryarena.Logger;
import ru.flametaichou.ordinaryarena.model.Arena;
import ru.flametaichou.ordinaryarena.model.ArenaPlayer;
import ru.flametaichou.ordinaryarena.model.ArenasXml;
import ru.flametaichou.ordinaryarena.model.Point3D;
import ru.flametaichou.ordinaryarena.utils.WorldUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class ArenaService implements IArenaService {

    private static ArenasXml arenas = new ArenasXml();

    private static Random random = new Random();

    public ArenaService() {
        arenas.setArenas(new ArrayList<Arena>());
    }

    @Override
    public void setLootListsXML(ArenasXml lootLists) {
        arenas = lootLists;
    }

    @Override
    public ArenasXml getLootListsXML() {
        return arenas;
    }

    @Override
    public Arena findByName(String name) {
        for (Iterator<Arena> iterator = arenas.getArenas().iterator(); iterator.hasNext(); ) {
            Arena arena = iterator.next();
            if (arena.getName().equals(name)) {
                return arena;
            }
        }
        //Logger.error("can't find Arena by name: " + name + "!");
        return null;
    }

    @Override
    public Arena createArena(String name) {
        Arena arena = new Arena();
        arena.setName(name);
        addArena(arena);
        OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));
        return arena;
    }

    @Override
    public void deleteArena(String name) {
        Arena arena = findByName(name);
        if (Objects.nonNull(arena)) {
            removeArena(arena);
        } else {
            Logger.error("Arena is null!");
        }
        OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));
    }

    @Override
    public void setPos1(Integer x, Integer y, Integer z, String name, Integer worldId) {
        Arena arena = findByName(name);
        if (Objects.isNull(arena)) {
            Logger.error("Arena is null!");
            return;
        }
        arena.setX1(x);
        arena.setY1(y);
        arena.setZ1(z);
        arena.setWorldId(worldId);
        OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));
    }

    @Override
    public void setPos2(Integer x, Integer y, Integer z, String name, Integer worldId) {
        Arena arena = findByName(name);
        if (Objects.isNull(arena)) {
            Logger.error("Arena is null!");
            return;
        }
        arena.setX2(x);
        arena.setY2(y);
        arena.setZ2(z);
        arena.setWorldId(worldId);
        OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));
    }

    @Override
    public void joinArena(EntityPlayer player, String name) {
        Arena arena = findByName(name);
        if (Objects.isNull(arena)) {
            Logger.error("Arena is null!");
            return;
        }

        if (playerOnAnyArena(player)) {
            return;
        }

        Point3D point = null;
        try {
            point = getSpawnPoint(arena);
        } catch (Exception e) {
            Logger.error("Error on getting spawn coordinates, arena: " + arena.getName());
            return;
        }

        addPlayerToArena(player, arena);

        WorldUtils.teleportToDimension(
                (EntityPlayerMP) player,
                arena.getWorldId(),
                point.x,
                point.y,
                point.z
        );

        OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));
    }

    @Override
    public void leaveArena(EntityPlayer player, Boolean withTeleport) throws IllegalStateException  {
        for (Iterator<Arena> iteratorArena = arenas.getArenas().iterator(); iteratorArena.hasNext(); ) {
            Arena tempArena = iteratorArena.next();

            for (Iterator<ArenaPlayer> iteratorPlayer = tempArena.getPlayers().iterator(); iteratorPlayer.hasNext(); ) {
                ArenaPlayer arenaPlayer = iteratorPlayer.next();
                if (arenaPlayer.getName().equals(player.getDisplayName())) {

                    if (withTeleport) {
                        WorldUtils.teleportToDimension(
                                (EntityPlayerMP) player,
                                arenaPlayer.getWorldId(),
                                arenaPlayer.getX(),
                                arenaPlayer.getY(),
                                arenaPlayer.getZ()
                        );
                    }

                    iteratorPlayer.remove();

                    OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));
                    return;
                }
            }
        }
        throw new IllegalStateException("Player is not on arena!");
    }

    @Override
    public boolean playerOnAnyArena(EntityPlayer player) {
        for (Iterator<Arena> iteratorArena = arenas.getArenas().iterator(); iteratorArena.hasNext(); ) {
            Arena tempArena = iteratorArena.next();

            for (Iterator<ArenaPlayer> iteratorPlayer = tempArena.getPlayers().iterator(); iteratorPlayer.hasNext(); ) {
                ArenaPlayer arenaPlayer = iteratorPlayer.next();
                if (arenaPlayer.getName().equals(player.getDisplayName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean playerOnArena(EntityPlayer player, Arena arena) {
        for (Iterator<ArenaPlayer> iteratorPlayer = arena.getPlayers().iterator(); iteratorPlayer.hasNext(); ) {
            ArenaPlayer arenaPlayer = iteratorPlayer.next();
            if (arenaPlayer.getName().equals(player.getDisplayName())) {
                return true;
            }
        }
        return false;
    }

    private Point3D getSpawnPoint(Arena arena) throws IllegalStateException {
        int i = 0;
        while (i < 100) {
            int x = randomBetween(Math.min(arena.getX1(), arena.getX2()), Math.max(arena.getX1(), arena.getX2()));
            int y = randomBetween(Math.min(arena.getY1(), arena.getY2()), Math.max(arena.getY1(), arena.getY2()));
            int z = randomBetween(Math.min(arena.getZ1(), arena.getZ2()), Math.max(arena.getZ1(), arena.getZ2()));

            World world = DimensionManager.getWorld(arena.getWorldId());
            if (Objects.isNull(world)) {
                String error = "World is null!";
                Logger.error(error);
                throw new IllegalStateException(error);
            }

            if (world.getBlock(x, y ,z) == Blocks.air && world.getBlock(x, y+1 ,z) == Blocks.air && world.getBlock(x, y-1 ,z) != Blocks.air) {
                return new Point3D(x, y, z);
            }
            i++;
        }
        throw new IllegalStateException("Error on getting spawn coordinates!");
    }

    public static int randomBetween(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private void addArena(Arena arena) {
        arenas.getArenas().add(arena);
    }

    private void removeArena(Arena arena) {
        for (Iterator<Arena> iterator = arenas.getArenas().iterator(); iterator.hasNext(); ) {
            Arena tempArena = iterator.next();
            if (tempArena.getName().equals(arena.getName())) {
                iterator.remove();
                return;
            }
        }
        Logger.error("can't find Arena by name: " + arena.getName() + "!");
    }

    private void addPlayerToArena(EntityPlayer player, Arena arena) {
        arena.getPlayers().add(new ArenaPlayer(
                player.getDisplayName(),
                (int) player.posX,
                (int) player.posY,
                (int) player.posZ,
                player.worldObj.provider.dimensionId
        ));
    }

    private void removePlayerFromArena(EntityPlayer player, Arena arena) {
        for (Iterator<ArenaPlayer> iterator = arena.getPlayers().iterator(); iterator.hasNext(); ) {
            ArenaPlayer arenaPlayer = iterator.next();
            if (arenaPlayer.getName().equals(player.getDisplayName())) {
                iterator.remove();
                return;
            }
        }
    }
}
