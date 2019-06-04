package ru.flametaichou.ordinaryarena.service;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
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


import java.text.DecimalFormat;
import java.util.*;

public class ArenaService implements IArenaService {

    private static ArenasXml arenas = new ArenasXml();

    private static Random random = new Random();

    private static DecimalFormat df = new DecimalFormat("#.00");

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
    public boolean joinArena(EntityPlayer player, String name) {
        Arena arena = findByName(name);
        if (Objects.isNull(arena)) {
            Logger.error("Arena is null!");
            return false;
        }

        if (playerOnAnyArena(player)) {
            return false;
        }

        Point3D point = null;
        try {
            point = getSpawnPoint(arena);
        } catch (Exception e) {
            Logger.error("Error on getting spawn coordinates, arena: " + arena.getName());
            return false;
        }

        addPlayerToArena(player, arena);

        WorldUtils.teleportToDimension(
                (EntityPlayerMP) player,
                arena.getWorldId(),
                point.x + 0.5,
                point.y,
                point.z + 0.5
        );

        OrdinaryArenaBase.writeArenas((long) (Math.random() * 10000L));

        return true;
    }

    @Override
    public void leaveArena(EntityPlayer player, Boolean withTeleport) throws IllegalStateException  {
        for (Iterator<Arena> iteratorArena = arenas.getArenas().iterator(); iteratorArena.hasNext(); ) {
            Arena tempArena = iteratorArena.next();

            for (Iterator<ArenaPlayer> iteratorPlayer = tempArena.getPlayers().iterator(); iteratorPlayer.hasNext(); ) {
                ArenaPlayer arenaPlayer = iteratorPlayer.next();
                if (arenaPlayer.getName().equals(player.getDisplayName())) {

                    if (withTeleport) {
                        double x = arenaPlayer.getX();
                        double y = arenaPlayer.getY();
                        double z = arenaPlayer.getZ();
                        player.addChatMessage(new ChatComponentTranslation("Вы возвращаетесь в точку x:" + df.format(x) + " y:" + df.format(y) + " z:" + df.format(z)));
                        WorldUtils.teleportToDimension(
                                (EntityPlayerMP) player,
                                arenaPlayer.getWorldId(),
                                x,
                                y,
                                z
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
        int mobRadius = 8;
        while (i < 500) {
            int x = randomBetween(Math.min(arena.getX1(), arena.getX2()), Math.max(arena.getX1(), arena.getX2()));
            int y = randomBetween(Math.min(arena.getY1(), arena.getY2()), Math.max(arena.getY1(), arena.getY2()));
            int z = randomBetween(Math.min(arena.getZ1(), arena.getZ2()), Math.max(arena.getZ1(), arena.getZ2()));

            World world = DimensionManager.getWorld(arena.getWorldId());
            if (Objects.isNull(world)) {
                String error = "World is null!";
                Logger.error(error);
                throw new IllegalStateException(error);
            }

            if (world.getBlock(x, y, z) == Blocks.air && world.getBlock(x, y + 1, z) == Blocks.air && blockNotAir(world.getBlock(x, y - 1, z))) {
                AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                        (double)(x - mobRadius),
                        (double)(y - mobRadius / 2),
                        (double)(z - mobRadius),
                        (double)(x + mobRadius),
                        (double)(y + mobRadius / 2),
                        (double)(z + mobRadius)
                );
                List list = world.selectEntitiesWithinAABB(EntityLiving.class, axisalignedbb, null);
                if (listNotContainsAngryMobs(list)) {
                    return new Point3D(x, y, z);
                }
            }
            i++;
        }
        throw new IllegalStateException("Error on getting spawn coordinates!");
    }

    private boolean listNotContainsAngryMobs(List list) {
        boolean result = true;

        for (Object o : list) {
            EntityLiving e = (EntityLiving) o;

            if (e.getClass().getName().toLowerCase().contains("body") || e instanceof EntityAnimal || e.getHealth() == 0 || e.isDead) {
                continue;
            }

            result = false;
        }

        return result;
    }

    private boolean blockNotAir(Block block) {
        if (block.getMaterial() != Material.air && block.isCollidable() && block.isNormalCube()) {
            return true;
        }
        return false;
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
                player.posX,
                player.posY,
                player.posZ,
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
