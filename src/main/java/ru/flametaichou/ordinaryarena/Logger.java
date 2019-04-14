package ru.flametaichou.ordinaryarena;

import ru.flametaichou.ordinaryarena.model.ArenaPlayer;

public class Logger {

    private static final String prefix = "[OrdinaryArena] ";
    private static final String prefixInfo = "(INFO) ";
    private static final String prefixEror= "(ERROR) ";
    private static final String preffixDebug= "(DEBUG) ";

    public static void log(String string) {
        System.out.println(prefix + prefixInfo + string);
    }

    public static void error(String string) {
        System.out.println(prefix + prefixEror + string);
    }

    public static void debug(String string) {
        if (ConfigHelper.debugMode) System.out.println(prefix + preffixDebug + string);
    }

    public static String getCoordinatesString(int x, int y, int z) {
        return "x:" + x + " y:" + y + " z:" + z;
    }

    public static String getCoordinatesString(ArenaPlayer arenaPlayer) {
        return "x:" + arenaPlayer.getX() + " y:" + arenaPlayer.getY() + " z:" + arenaPlayer.getZ();
    }
}
