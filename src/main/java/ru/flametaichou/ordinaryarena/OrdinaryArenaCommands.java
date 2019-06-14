package ru.flametaichou.ordinaryarena;

import java.util.*;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import ru.flametaichou.ordinaryarena.model.Command;

public class OrdinaryArenaCommands extends CommandBase
{
    private final List<String> aliases;
    private final static Random random = new Random();

    public OrdinaryArenaCommands()
    {
        aliases = new ArrayList<String>();
        aliases.add("arena");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }

    @Override
    public String getCommandName()
    {
        return "arena";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/arena <join {name}/leave>";
    }

    @Override
    public List<String> getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] argString) {
        World world = sender.getEntityWorld();

        if (!world.isRemote) {
            if (argString.length == 0) {
                sender.addChatMessage(new ChatComponentText("/arena <join {name}/leave>"));
                return;
            }
            if (argString[0].equals("join")) {
                if (sender instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) sender;
                    if (argString.length < 2) {
                        sender.addChatMessage(new ChatComponentTranslation("Название арены не указано!"));
                        return;
                    }

                    String name = argString[1];

                    if (Objects.isNull(OrdinaryArenaBase.arenaService.findByName(name))) {
                        sender.addChatMessage(new ChatComponentTranslation("Такой арены не существует!"));
                        return;
                    }

                    if (OrdinaryArenaBase.arenaService.playerOnAnyArena(player)) {
                        sender.addChatMessage(new ChatComponentTranslation("Вы уже находитесь на арене!"));
                        return;
                    }

                    Date timeToExecute = new Date((new Date()).getTime() + (ConfigHelper.cooldown * 1000));
                    OrdinaryArenaBase.commandsQueue.getCommandsQueue().add(
                            new Command(
                                    Command.CommandType.JOIN,
                                    player.getDisplayName(),
                                    timeToExecute,
                                    name
                            )
                    );
                    sender.addChatMessage(new ChatComponentTranslation("Команда будет выполнена через " + ConfigHelper.cooldown + " секунд."));
                }
                return;
            }

            if (argString[0].equals("leave")) {
                if (sender instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) sender;
                    Date timeToExecute = new Date((new Date()).getTime() + (ConfigHelper.cooldown * 1000));
                    OrdinaryArenaBase.commandsQueue.getCommandsQueue().add(
                            new Command(
                                    Command.CommandType.LEAVE,
                                    player.getDisplayName(),
                                    timeToExecute,
                                    null
                            )
                    );
                    sender.addChatMessage(new ChatComponentTranslation("Команда будет выполнена через " + ConfigHelper.cooldown + " секунд."));
                }
                return;
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        return true;
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2)
    {
        return false;
    }
}
