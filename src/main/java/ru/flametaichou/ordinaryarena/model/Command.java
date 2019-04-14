package ru.flametaichou.ordinaryarena.model;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Date;

public class Command {

    public CommandType type;
    public EntityPlayer player;
    public Date timeExecute;
    public String arenaName;

    public Command(CommandType type, EntityPlayer player, Date timeExecute, String arenaName) {
        this.type = type;
        this.player = player;
        this.timeExecute = timeExecute;
        this.arenaName = arenaName;
    }

    public enum CommandType {
        JOIN,
        LEAVE;
    }
}
