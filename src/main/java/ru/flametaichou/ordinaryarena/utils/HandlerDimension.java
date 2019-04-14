package ru.flametaichou.ordinaryarena.utils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.common.DimensionManager;
import ru.flametaichou.ordinaryarena.OrdinaryArenaBase;
import ru.flametaichou.ordinaryarena.model.MessageDimension;

public class HandlerDimension implements IMessageHandler<MessageDimension, IMessage> {

    @Override
    public IMessage onMessage(final MessageDimension message, MessageContext ctx) {
        OrdinaryArenaBase.proxy.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("ORDINARYARENA DEBUG: Cheching provider for dim " + message.getDim());
                if (!DimensionManager.isDimensionRegistered(message.getDim())) {
                    System.out.println("ORDINARYARENA DEBUG: Provider not exist, creating");
                    DimensionManager.registerDimension(message.getDim(), 0);
                } else {
                    System.out.println("ORDINARYARENA DEBUG: Provider exist");
                }
            }
        });
        return null;
    }
}
