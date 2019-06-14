package ru.flametaichou.ordinaryarena.model;

import java.util.Date;

public class Command {

    public CommandType type;
    public String playerName;
    public Date timeExecute;
    public String arenaName;

    public Command(CommandType type, String playerName, Date timeExecute, String arenaName) {
        this.type = type;
        this.playerName = playerName;
        this.timeExecute = timeExecute;
        this.arenaName = arenaName;
    }

    public enum CommandType {
        JOIN,
        LEAVE;
    }
}
