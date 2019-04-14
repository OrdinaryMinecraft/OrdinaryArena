package ru.flametaichou.ordinaryarena.utils;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import ru.flametaichou.ordinaryarena.model.MessageDimension;

public class NetworkHandler {

    public static final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel("ordinaryarena");

    public static void init() {
        channel.registerMessage(HandlerDimension.class, MessageDimension.class, 0, Side.CLIENT);
    }

}
