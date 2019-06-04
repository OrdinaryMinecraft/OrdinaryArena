package ru.flametaichou.ordinaryarena;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import ru.flametaichou.ordinaryarena.model.Arena;
import ru.flametaichou.ordinaryarena.model.Command;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CommandsQueueHandler {

    private List<Command> commands = new ArrayList<Command>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void checkingSigns(TickEvent.WorldTickEvent event) {
        if (event.world.getWorldTime() % 20 == 0) {
            for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
                for (Object playerObj : worldServer.playerEntities) {
                    EntityPlayerMP player = (EntityPlayerMP) playerObj;
                    for (Iterator<Command> iterator = commands.iterator(); iterator.hasNext(); ) {
                        Command command = iterator.next();
                        if (command.player.getDisplayName().equals(player.getDisplayName())) {
                            if (Math.abs(player.motionX) > 0 || Math.abs(player.motionY) > 0.1 || Math.abs(player.motionZ) > 0) {
                                command.player.addChatMessage(new ChatComponentTranslation("Вы сдвинулись с места. Выполнение команды отменено!"));
                                iterator.remove();
                            }
                        }
                    }
                }
            }

            for (Iterator<Command> iterator = commands.iterator(); iterator.hasNext(); ) {
                Command command = iterator.next();
                Date now = new Date();
                if (now.after(command.timeExecute)) {
                    switch (command.type) {
                        case JOIN:
                            if (OrdinaryArenaBase.arenaService.joinArena(command.player, command.arenaName)) {
                                command.player.addChatMessage(new ChatComponentTranslation("Вы вошли на арену " + command.arenaName + ". Используйте /arena leave чтобы вернуться обратно."));
                            } else {
                                command.player.addChatMessage(new ChatComponentTranslation("При выполнении команды произошла ошибка!"));
                            }
                            break;
                        case LEAVE:
                            try {
                                OrdinaryArenaBase.arenaService.leaveArena(command.player, true);
                                command.player.addChatMessage(new ChatComponentTranslation("Вы вышли с арены"));
                            } catch (Exception e) {
                                command.player.addChatMessage(new ChatComponentTranslation("Вы не находитесь на арене!"));
                            }
                            break;
                    }
                    iterator.remove();
                }
            }
        }
    }

    public List<Command> getCommandsQueue() {
        return commands;
    }
}
