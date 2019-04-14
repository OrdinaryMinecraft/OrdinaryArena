package ru.flametaichou.ordinaryarena;

import java.util.*;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class OrdinaryArenaAdminCommands extends CommandBase
{
    private final List<String> aliases;

    public OrdinaryArenaAdminCommands()
    {
        aliases = new ArrayList<String>();
        aliases.add("arenaadm");
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
        return "arenaadm";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/arenaadm <create {name}/delete {name}/pos1/pos2>";
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
                sender.addChatMessage(new ChatComponentText("/arenaadm <create {name}/delete {name}/pos1/pos2>"));
                return;
            }
            if (argString[0].equals("create")) {
                if (sender instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) sender;
                    if (argString.length < 2) {
                        sender.addChatMessage(new ChatComponentTranslation("Название арены не указано!"));
                        return;
                    }

                    String name = argString[1];

                    if (Objects.nonNull(OrdinaryArenaBase.arenaService.findByName(name))) {
                        sender.addChatMessage(new ChatComponentTranslation("Уже заведена арена с таким названием!"));
                        return;
                    }

                    OrdinaryArenaBase.arenaService.createArena(name);
                    sender.addChatMessage(new ChatComponentTranslation("Создана арена " + name + "."));
                }
                return;
            }

            if (argString[0].equals("delete")) {
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

                    OrdinaryArenaBase.arenaService.deleteArena(name);
                    sender.addChatMessage(new ChatComponentTranslation("Удалена арена " + name + "."));
                }
                return;
            }

            if (argString[0].equals("pos1")) {
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

                    OrdinaryArenaBase.arenaService.setPos1(
                            (int) player.posX,
                            (int) player.posY,
                            (int) player.posZ,
                            name,
                            player.worldObj.provider.dimensionId
                    );
                    sender.addChatMessage(new ChatComponentTranslation("Установлена точка 1 для арены " + name + "."));
                }
                return;
            }

            if (argString[0].equals("pos2")) {
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

                    OrdinaryArenaBase.arenaService.setPos2(
                            (int) player.posX,
                            (int) player.posY,
                            (int) player.posZ,
                            name,
                            player.worldObj.provider.dimensionId
                    );
                    sender.addChatMessage(new ChatComponentTranslation("Установлена точка 2 для арены " + name + "."));
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
